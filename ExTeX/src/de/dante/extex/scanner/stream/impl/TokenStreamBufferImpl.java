/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.main.MainIOException;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.GeneralException;
import de.dante.util.Locator;

/*
 * ...
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de"> Gerd Neugebauer </a>
 * 
 * @version $Revision: 1.8 $
 */
/**
 * This class contains an implementation of a token stream which is fed from a
 * buffer. The basic functionality of stacking prefabricated tokens and
 * delivering them is inherited from the base class TokenStreamBaseImpl.
 * <p>
 * In addition this class provides an engine to tokenize the input stream and
 * refill the buffer at its end.
 * </p>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.8 $
 */
public class TokenStreamBufferImpl extends TokenStreamBaseImpl implements
        TokenStream, CatcodeVisitor {
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
     * The index in the buffer of the next character to consider.
     */
    protected int pointer = -1;

    /**
     * The buffer to read from. The line is empty
     */
    private CharBuffer buffer;

    /**
     * Creates a new object. The input buffer is initially empty.
     */
    public TokenStreamBufferImpl() {
        super();
    }

    /**
     * Creates a new object. The encoding used is ISO-8859-1.
     * 
     * @param line a string to read from
     * 
     * @throws CharacterCodingException in case of an error
     */
    public TokenStreamBufferImpl(String line) throws CharacterCodingException {
        this(line, "ISO-8859-1");
    }

    /**
     * Creates a new object.
     * 
     * @param line a string to read from
     * @param encoding the encoding to use
     * 
     * @throws CharacterCodingException in case of an error
     */
    public TokenStreamBufferImpl(String line, String encoding)
            throws CharacterCodingException {
        super();
        buffer = Charset.forName(encoding).newDecoder().decode(
                ByteBuffer.wrap(line.getBytes()));
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
    public Token getNext(TokenFactory factory, Tokenizer tokenizer)
            throws GeneralException {
        Token t;
        int c;

        if (buffer == null) {
            return null;
        }

        do {

            if ((c = getChar()) < 0) {
                return null;
            }

            t = (Token) tokenizer.getCatcode((char) c).visit(this, factory,
                    tokenizer);
        } while (t == null);

        return t;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object,java.lang.Object)
     */
    public Object visitActive(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        Token t = ((TokenFactory) oFactory).newInstance(Catcode.ACTIVE, buffer
                .get(pointer));
        return t;
    }

    /**
     * Comment
     * 
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object,java.lang.Object)
     */
    public Object visitComment(Object oFactory, Object oTokenizer)
            throws GeneralException {
        endLine();
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,java.lang.Object)
     */
    public Object visitCr(Object oFactory, Object oTokenizer)
            throws GeneralException {
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
    public Object visitEscape(Object oFactory, Object oTokenizer)
            throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        int c = getChar();

        if (c < 0) {
            // TODO error handling
            return null;
        } else if (tokenizer.getCatcode((char) c) == Catcode.LETTER) {
            StringBuffer sb = new StringBuffer((char) c);

            while ((c = getChar()) >= 0
                   && tokenizer.getCatcode((char) c) == Catcode.LETTER) {
                sb.append((char) c);
            }
            state = SKIP_BLANKS;

            return factory.newInstance(Catcode.ESCAPE, sb.toString());
        } else {
            state = MID_LINE;
            return factory.newInstance(Catcode.ESCAPE, Character
                    .toString((char) c));
        }
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,java.lang.Object)
     */
    public Object visitIgnore(Object oFactory, Object oTokenizer)
            throws GeneralException {
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,java.lang.Object)
     */
    public Object visitInvalid(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        throw new GeneralHelpingException("TTP.InvalidChar");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object,java.lang.Object)
     */
    public Object visitLeftBrace(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.LEFTBRACE, buffer
                .get(pointer));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,java.lang.Object)
     */
    public Object visitLetter(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.LETTER, buffer
                .get(pointer));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object,java.lang.Object)
     */
    public Object visitMacroParam(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.MACPARAM, buffer
                .get(pointer));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object,java.lang.Object)
     */
    public Object visitMathShift(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.MATHSHIFT, buffer
                .get(pointer));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,java.lang.Object)
     */
    public Object visitOther(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.OTHER, buffer
                .get(pointer));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRigthBrace(java.lang.Object,java.lang.Object)
     */
    public Object visitRightBrace(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.RIGTHBRACE, buffer
                .get(pointer));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,java.lang.Object)
     * @see "The TeXbook [Chapter 8, page 47]"
     */
    public Object visitSpace(Object oFactory, Object oTokenizer)
            throws GeneralException {
        TokenFactory factory = (TokenFactory) oFactory;

        if (state == MID_LINE) {
            state = SKIP_BLANKS;
            return factory.newInstance(Catcode.SPACE, " ");
        }

        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSubMark(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.SUBMARK, buffer
                .get(pointer));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSupMark(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        TokenFactory factory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        char c = buffer.get(pointer);
        int next = getChar();

        if (next < 0) {
            return factory.newInstance(Catcode.SUPMARK, c);
        } else if (next != c) {
            pointer--;//gene: dirty
            return factory.newInstance(Catcode.SUPMARK, c);
        }

        // ^^ notation
        if ((next = getChar()) < 0) {
            // ^^ at end: save one and return one
            Token t = factory.newInstance(Catcode.SUPMARK, c);
            put(t);
            return t;
        }

        c = (char) next;
        int hc1 = hex2int(c);

        if (hc1 < 0 || (next = getChar()) < 0) {
            c = (char) (c < '\100' ? c + 64 : c - 64);

            ungetChar(c);
            return tokenizer.getCatcode((char) getChar()).visit(this, oFactory,
                    oTokenizer);
        }

        c = (char) next;
        int hc2 = hex2int(c);

        if (hc2 < 0) {
            put(factory.newInstance(tokenizer.getCatcode(c), c));

            c = (char) (c < '\100' ? c + 64 : c - 64);

            ungetChar(c);
            return tokenizer.getCatcode((char) getChar()).visit(this, oFactory,
                    oTokenizer);
        }

        ungetChar((char) (hc1 * 16 + hc2));
        return tokenizer.getCatcode((char) getChar()).visit(this, oFactory,
                oTokenizer);

        //TODO: use ^^^^ notation
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,java.lang.Object)
     */
    public Object visitTabMark(Object oFactory, Object oTokenizer)
            throws GeneralException {
        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.TABMARK, buffer
                .get(pointer));
    }

    /**
     * Set the buffer to a new character array.
     * 
     * @param cs the new buffer
     */
    protected void setBuffer(CharBuffer cs) {
        buffer = cs;
    }

    /**
     * Get a new line of characters to consider.
     * 
     * @return <code>false</code> if no further input is available.
     */
    protected boolean refill() throws IOException {
        return false;
    }

    /**
     * Return the next character to process. The pointer is advanced and points
     * to the character returned.
     * <p>
     * This operation might involve that an additional bunch of characters is
     * read in (with {@link #refill() refill()}).
     * </p>
     * 
     * @return the character code or <code>-1</code> if no more character is
     *         available
     * @throws MainIOException in the rare case that an IOException has
     *             occurred.
     */
    private int getChar() throws MainIOException {

        while (++pointer >= buffer.length()) {
            try {
                if (!refill()) {
                    return -1;
                }
            } catch (IOException e) {
                throw new MainIOException(e);
            }

            pointer = -1;
            state = NEW_LINE;
        }

        char c = buffer.get(pointer);
        return c;
    }

    private void ungetChar(char c) {
        buffer.put(pointer--,c);
    }

    /**
     * End the current line
     */
    private void endLine() {
        pointer = buffer.length();
    }

    /**
     * Analyze a character and return its hex value, i.e. '0' to '9' are mapped
     * to 0 to 9 and 'a' to 'f' (case sensitive) are mapped to 10 to 15.
     * 
     * @param c the character to analyze
     * 
     * @return the integer value of a hex digit or -1 if no hex digit is given
     */
    private int hex2int(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 10;
            //        } else if ('A' <= c && c <= 'F') {
            //            return c - 'A' + 10;
        } else {
            return -1;
        }
    }
}
