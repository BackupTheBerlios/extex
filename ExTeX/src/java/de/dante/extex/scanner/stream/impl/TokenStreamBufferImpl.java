/*
 * Copyright (C) 2003 Gerd Neugebauer, Michael Niedermair
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *  
 */
package de.dante.extex.scanner.stream.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.main.MainIOException;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.GeneralException;
import de.dante.util.Locator;

/**
 * This class contains an implementation of a token stream which is fed from a
 * buffer. The basic functionality of stacking prefabricated tokens and
 * delivering them is inherited from the base class TokenStreamBaseimpl.
 * 
 * In addition this class provides an engine to tokenize the input stream and
 * refill the buffer at its end.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class TokenStreamBufferImpl extends TokenStreamBaseImpl implements TokenStream, CatcodeVisitor {

	/**
	 * The buffer to read from
	 */
	private CharBuffer buffer;

	/**
	 * the index of the next character to consider
	 */
	protected int pointer = 0;

	/**
	 * new line
	 */
	public static final byte NEW_LINE = 0;

	/**
	 * mid line
	 */
	public static final byte MID_LINE = 1;

	/**
	 * skip blanks
	 */
	public static final byte SKIP_BLANKS = 2;

	/**
	 * state of a line
	 */
	protected byte state = NEW_LINE;

	/**
	 * Creates a new object.
	 */
	public TokenStreamBufferImpl() {
		super();
	}

	/**
	 * Creates a new object. The encoding used is ISO-8859-1.
	 * 
	 * @param line
	 *                 a string to read from
	 * 
	 * @throws CharacterCodingException
	 *                 in case of an error
	 */
	public TokenStreamBufferImpl(String line) throws CharacterCodingException {
		this(line, "ISO-8859-1");
	}

	/**
	 * Creates a new object.
	 * 
	 * @param line
	 *                 a string to read from
	 * @param encoding
	 *                 the encoding to use
	 * 
	 * @throws CharacterCodingException
	 *                 in case of an error
	 */
	public TokenStreamBufferImpl(String line, String encoding) throws CharacterCodingException {
		super();
		buffer = Charset.forName(encoding).newDecoder().decode(ByteBuffer.wrap(line.getBytes()));
	}

	/**
	 * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
	 */
	public Locator getLocator() {
		return new Locator("<buffer>", 1);
	}

	/**
	 * Get the next token when the stack is empty.
	 * 
	 * @return the next Token or <code>null</code>
	 */
	public Token getNext(TokenFactory factory, Tokenizer tokenizer) throws GeneralException {
		Token t;

		do {
			if (buffer == null) {
				return null;
			}

			if (pointer >= buffer.length() && advancePointer()) {
				return null;
			}

			t = (Token) tokenizer.getCatcode(buffer.get(pointer)).visit(this, factory, tokenizer);
			if (t != null) {
				System.err.println("tok : " + t); // MGN wieder
				// raus!!!
			}
		} while (t == null);

		return t;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object,java.lang.Object)
	 */
	public Object visitActive(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token t = ((TokenFactory) oFactory).newInstance(Catcode.ACTIVE, buffer.get(pointer));
		advancePointer();
		return t;
	}

	/**
	 * Comment
	 * 
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object,java.lang.Object)
	 */
	public Object visitComment(Object oFactory, Object oTokenizer) throws GeneralException {
		endLine();
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,java.lang.Object)
	 */
	public Object visitCr(Object oFactory, Object oTokenizer) throws GeneralException {

		TokenFactory factory = (TokenFactory) oFactory;
		Token t = null;

		if (state == MID_LINE) {
			t = factory.newInstance(Catcode.SPACE, " ");
		} else if (state == NEW_LINE) {
			t = factory.newInstance(Catcode.ESCAPE, "par");
		}
		endLine();
		return t;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object,java.lang.Object)
	 */
	public Object visitEscape(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		TokenFactory factory = (TokenFactory) oFactory;
		Tokenizer tokenizer = (Tokenizer) oTokenizer;

		if (advancePointer()) {
			// TODO error handling
			return null;
		} else if (tokenizer.getCatcode(buffer.get(pointer)) == Catcode.LETTER) {
			StringBuffer sb = new StringBuffer();
			sb.append(buffer.get(pointer));

			while (!advancePointer() && tokenizer.getCatcode(buffer.get(pointer)) == Catcode.LETTER) {
				sb.append(buffer.get(pointer));
			}

			return factory.newInstance(Catcode.ESCAPE, sb.toString());
		} else {
			return factory.newInstance(Catcode.ESCAPE, Character.toString(buffer.get(pointer++)));
		}
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,java.lang.Object)
	 */
	public Object visitIgnore(Object oFactory, Object oTokenizer) throws GeneralException {
		advancePointer();
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,java.lang.Object)
	 */
	public Object visitInvalid(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		//TODO error handling
		advancePointer();
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object,java.lang.Object)
	 */
	public Object visitLeftBrace(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token token = ((TokenFactory) oFactory).newInstance(Catcode.LEFTBRACE, buffer.get(pointer));
		advancePointer();
		return token;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,java.lang.Object)
	 */
	public Object visitLetter(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token token = ((TokenFactory) oFactory).newInstance(Catcode.LETTER, buffer.get(pointer));
		advancePointer();
		return token;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object,java.lang.Object)
	 */
	public Object visitMacroParam(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token token = ((TokenFactory) oFactory).newInstance(Catcode.MACPARAM, buffer.get(pointer));
		advancePointer();
		return token;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object,java.lang.Object)
	 */
	public Object visitMathShift(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token token = ((TokenFactory) oFactory).newInstance(Catcode.MATHSHIFT, buffer.get(pointer));
		advancePointer();
		return token;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,java.lang.Object)
	 */
	public Object visitOther(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token token = ((TokenFactory) oFactory).newInstance(Catcode.OTHER, buffer.get(pointer));
		advancePointer();
		return token;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitRigthBrace(java.lang.Object,java.lang.Object)
	 */
	public Object visitRightBrace(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token token = ((TokenFactory) oFactory).newInstance(Catcode.RIGTHBRACE, buffer.get(pointer));
		advancePointer();
		return token;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,java.lang.Object)
	 */
	public Object visitSpace(Object oFactory, Object oTokenizer) throws GeneralException {
		TokenFactory factory = (TokenFactory) oFactory;
		advancePointer();
		if (state == MID_LINE) {
			state = SKIP_BLANKS;
			return factory.newInstance(Catcode.SPACE, " ");
		}
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,java.lang.Object)
	 */
	public Object visitSubMark(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token token = ((TokenFactory) oFactory).newInstance(Catcode.SUBMARK, buffer.get(pointer));
		advancePointer();
		return token;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,java.lang.Object)
	 */
	public Object visitSupMark(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		TokenFactory factory = (TokenFactory) oFactory;
		Tokenizer tokenizer = (Tokenizer) oTokenizer;
		char c = buffer.get(pointer);

		if (!advancePointer() || buffer.get(pointer) != c) {
			return factory.newInstance(Catcode.SUPMARK, c);
		}

		// ^^ notation
		if (!advancePointer()) {
			Token t = factory.newInstance(Catcode.SUPMARK, c);
			put(t);
			return t;
		}

		char c1 = buffer.get(pointer);
		int hc1 = hex2int(c1);

		if (hc1 < 0 || !advancePointer()) {
			if (c < '\100') {
				hc1 = c + 64;
			} else {
				hc1 = c - 64;
			}

			buffer.put(pointer, (char) hc1);
			return tokenizer.getCatcode((char) hc1).visit(this, oFactory, oTokenizer);
		}

		char c2 = buffer.get(pointer);
		int hc2 = hex2int(c1);
		advancePointer();

		if (hc2 < 0) {
			put(factory.newInstance(tokenizer.getCatcode(c2), c2));

			if (c < '\100') {
				hc1 = c + 64;
			} else {
				hc1 = c - 64;
			}

			buffer.put(pointer, (char) hc1);
			return tokenizer.getCatcode((char) hc1).visit(this, oFactory, oTokenizer);
		}

		buffer.put(pointer, (char) (hc1 * 16 + hc2));
		return tokenizer.getCatcode(buffer.get(pointer)).visit(this, oFactory, oTokenizer);

		//TODO: use ^^^^ notation
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,java.lang.Object)
	 */
	public Object visitTabMark(Object oFactory, Object oTokenizer) throws GeneralException {
		state = MID_LINE;
		Token token = ((TokenFactory) oFactory).newInstance(Catcode.TABMARK, buffer.get(pointer));
		advancePointer();
		return token;
	}

	/**
	 * Set the buffer to a new character array.
	 * 
	 * @param cs
	 *                 the new buffer
	 */
	protected void setBuffer(CharBuffer cs) {
		buffer = cs;
	}

	/**
	 * Get a new buffer of characters to consider.
	 * 
	 * @return <code>false</code> if no further input is available.
	 */
	protected boolean refill() throws IOException {
		state = NEW_LINE;
		pointer = 0;
		return false;
	}

	/**
	 * Set the pointer to the next character to process. This operation might
	 * involve that an additional bunch of characters is read in (with
	 * {@link refill() refill()}).
	 * 
	 * @return <code>true</code> iff no more character is available
	 */
	private boolean advancePointer() throws MainIOException {
		while (++pointer >= buffer.length()) {
			try {
				if (!refill()) {
					return true;
				}
			} catch (IOException e) {
				throw new MainIOException(e);
			}

			pointer = 0;
		}

		return false;
	}

	/**
	 * End the current line
	 * 
	 * @return <code>false</code> if no further input is available.
	 */
	private boolean endLine() throws MainIOException {
		pointer = buffer.length();
		try {
			return refill();
		} catch (IOException e) {
			throw new MainIOException(e);
		}
	}

	/**
	 * Analyze a character and return its hex value, i.e. '0' to '9' are mapped
	 * to 0 to 9 and 'a' to 'f' (not case sensitive) are mapped to 10 to 15.
	 * 
	 * @param c
	 *                 the character to analyze
	 * 
	 * @return the integer value of a hex digit or -1 if no hex digit is given
	 */
	private int hex2int(char c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		} else if ('a' <= c && c <= 'f') {
			return c - 'a' + 10;
		} else if ('A' <= c && c <= 'F') {
			return c - 'A' + 10;
		} else {
			return -1;
		}
	}

	/**
	 * Getter for the internal buffer. This method is meant to be used for the
	 * look-ahead to determine the format to be used. For this purpose this
	 * method has to be used immediately after the constructor.
	 * <p>
	 * Since the buffer is returned directly the contents may change when the
	 * the tokens are acquired.
	 * </p>
	 * 
	 * @return the buffer of this TokenStream
	 */
	public CharBuffer getBuffer() {
		return buffer;
	}
}
