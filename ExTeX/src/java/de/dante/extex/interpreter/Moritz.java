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
package de.dante.extex.interpreter;

import java.util.Stack;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.ActiveCharacterToken;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.NotObservableException;
import de.dante.util.Observable;
import de.dante.util.Observer;
import de.dante.util.ObserverList;
import de.dante.util.configuration.Configuration;

/**
 * This class provides the layer above the input streams and the tokenizer. It
 * has additional methods for parsing. The details of the token streams are
 * mostly hidden.
 * 
 * <p>
 * This class is the companion to Max. (The name is a joke for friends of
 * Wilhelm Busch)
 * </p>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class Moritz implements TokenSource, Observable {

	/** ... */
	private static final long MAX_CHAR_CODE = Integer.MAX_VALUE; 
	//TODO: find a good value 
	
	/**
	 * The interpreter context. well, the two of them are more closely linked
	 * than I like it.
	 */
	private Context context;

	/** ... */
	private ObserverList observersCloseStream = new ObserverList();

	/**
	 * This observer list is used for the observers which are registered to
	 * receive a notification when all streams are at their end. The argument
	 * is always <code>null</code>.
	 */
	private ObserverList observersEOF = new ObserverList();

	/**
	 * This observer list is used for the observers which are registered to
	 * receive a notification when a new token is about to be delivered. The
	 * argument is the token to be delivered.
	 */
	private ObserverList observersPop = new ObserverList();

	/**
	 * This observer list is used for the observers which are registered to
	 * receive a notification when a new token is pushed back to the input
	 * stream. The argument is the token to be pushed.
	 */
	private ObserverList observersPush = new ObserverList();

	/**
	 * This is the stack of streams to read from except of the current one
	 * which is stored in the variable <code>stream</code>.
	 */
	private Stack streamStack = new Stack();

	/**
	 * The current stream to read tokens from. For efficiency this stream is
	 * kept in a variable instead of accessing the streamStack each time it is
	 * needed.
	 */
	private TokenStream stream = null;

	/** The factory for new token streams */
	private TokenStreamFactory tokenStreamFactory = null;

	/**
	 * Creates a new object.
	 */
	public Moritz(Configuration config) {
		super();
	}

	/**
	 * @see de.dante.extex.interpreter.TokenSource#getLocator()
	 */
	public Locator getLocator() {
		return (stream == null ? null : stream.getLocator());
	}

	/**
	 * Get the next token which has not the catcode SPACE.
	 * 
	 * @return the next non-space token or <code>null</code> at EOF
	 */
	public Token getNextNonSpace() throws GeneralException {
		for (Token t = getNextToken(); t != null; t = getNextToken()) {
			if (!(t.isa(Catcode.SPACE))) {
				return t;
			}
		}

		return null;
	}

	/**
	 * Get the next token form the input streams. If the current input stream
	 * is at its end then the next one on the streamStack is used until a token
	 * could be read. If all stream are at the end then <code>null</code> is
	 * returned.
	 * 
	 * @return the next token or <code>null</code>
	 */
	public Token getNextToken() throws GeneralException {
		TokenFactory factory = context.getTokenFactory();
		Tokenizer tokenizer = context.getTokenizer();
		Token t;

		while (stream != null) {
			t = stream.get(factory, tokenizer);

			if (t != null) {
				observersPop.update(this, t);
				return t;
			}

			observersCloseStream.update(this, stream);

			stream = (TokenStream) streamStack.pop();
		}

		observersEOF.update(this, null);
		return null;
	}

	/**
	 * Setter for the token stream factory.
	 * 
	 * @param factory
	 *                 the token stream factory
	 */
	public void setTokenStreamFactory(TokenStreamFactory factory) {
		tokenStreamFactory = factory;
	}

	/**
	 * Getter for the token stream factory.
	 * 
	 * @return the token stream factory
	 */
	public TokenStreamFactory getTokenStreamFactory() {
		return tokenStreamFactory;
	}

	/**
	 * Put a given stream on top of the stream stack. The reading occurs on
	 * this new stream before resorting to the previous streams.
	 * 
	 * @param stream
	 *                 the new stream to read from
	 */
	public void addStream(TokenStream stream) {
		streamStack.push(this.stream);
		this.stream = stream;
	}

	/**
	 * @see de.dante.extex.interpreter.TokenSource#closeAllStreams()
	 */
	public void closeAllStreams() {
		streamStack = new Stack();
		stream = null;
	}

	/**
	 * @see de.dante.extex.interpreter.TokenSource#closeNextFileStream()
	 */
	public void closeNextFileStream() {
		// TODO unimplemented; needed for \endinput
	}

	/**
	 * Push back a token onto the input stream for subsequent reading.
	 * 
	 * @param token
	 *                 the token to push
	 */
	public void push(Token token) {
		observersPush.update(this, token);
		stream.put(token);
	}

	/**
	 * Push back a list of tokens onto the input stream for subsequent reading.
	 * 
	 * @param tokens
	 *                 the tokens to push
	 */
	public void push(Token[] tokens) {
		for (int i = 0; i < tokens.length; i++) {
			observersPush.update(this, tokens[i]);
			stream.put(tokens[i]);
		}
	}

	/**
	 * Push back a list of tokens onto the input stream for subsequent reading.
	 * 
	 * @param tokens
	 *                 the tokens to push
	 */
	public void push(Tokens tokens) {
		for (int i = 0; i < tokens.length(); i++) {
			observersPush.update(this, tokens.get(i));
			stream.put(tokens.get(i));
		}
	}

	/**
	 * This method can be used to register observers for certain events.
	 * 
	 * The following events are currently supported: <table>
	 * <tr>
	 * <th>Name</th>
	 * <th>Description</th>
	 * </tr>
	 * <tr>
	 * <td>pop</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>push</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>EOF</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>close</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * 
	 * @see de.dante.util.Observable#registerObserver(java.lang.String,
	 *         de.dante.util.Observer)
	 */
	public void registerObserver(String name, Observer observer) throws NotObservableException {
		if ("push".equals(name)) {
			observersPush.add(observer);
		} else if ("pop".equals(name)) {
			observersPop.add(observer);
		} else if ("EOF".equals(name)) {
			observersEOF.add(observer);
		} else if ("close".equals(name)) {
			observersCloseStream.add(observer);
		} else {
			throw new NotObservableException(name);
		}
	}

	/**
	 * @see de.dante.extex.interpreter.TokenSource#scanCharacterCode()
	 */
	public long scanCharacterCode() throws GeneralException {
		long cc = scanNumber();

		if (cc < 0 || cc > MAX_CHAR_CODE) {
			throw new GeneralHelpingException("TTP.BadCharCode", Long.toString(cc), "0", Long.toString(MAX_CHAR_CODE));
		}

		return cc;
	}

	/**
	 * @see de.dante.extex.interpreter.TokenSource#scanFloat()
	 */
	public long scanFloat() throws GeneralException {
		long value = 0;
		boolean neg = false;
		int post = 0;
		Token t = scanNextNonSpace();

		if (t == null) {
		} else if (t.equals(Catcode.OTHER, "-")) {
			neg = true;
			t = scanNextNonSpace();
		} else if (t.equals(Catcode.OTHER, "+")) {
			t = scanNextNonSpace();
		}

		if (t != null && !t.equals(Catcode.OTHER, ".") && !t.equals(Catcode.OTHER, ",")) {
			value = scanNumber(t);
			t = getNextToken();
		}

		if (t != null && (t.equals(Catcode.OTHER, ".") || t.equals(Catcode.OTHER, ","))) {
			// @see "TeX -- The Program [102]"
			int[] dig = new int[17];
			int k = 0;

			for (t = getNextToken(); t != null && t.isa(Catcode.OTHER) && t.getValue().matches("[0-9]"); t = scanNextToken()) {
				if (k < 17) {
					dig[k++] = t.getValue().charAt(0) - '0';
				}
			}

			if (k < 17) {
				k = 17;
			}

			post = 0;

			while (k-- > 0) {
				post = (post + dig[k] * (1 << 17)) / 10;
			}

			post = (post + 1) / 2;
		}

		push(t);

		value = value << 16 | post;

		return (neg ? -value : value);
	}

	/**
	 * Scan the input stream for tokens making up an integer, this is a number
	 * optionally preceeded by a sign (+ or -). The number can be preceeded by
	 * optional whitespace. Whitespace is also ignored between the sign and the
	 * number. All non-whitespace characters must have the catcode OTHER.
	 * 
	 * @return the value of the integer scanned
	 * 
	 * @throws CodeNumberFormatException
	 *                 in case that no number is found
	 * @throws CodeEOFException
	 *                 in case that the end of file has been reached before an
	 *                 integer could be acquired
	 */
	public long scanInteger() throws GeneralException {
		Token t = scanNextNonSpace();

		if (t == null) {
			throw new GeneralHelpingException("TTP.MissingNumber");
		} else if (t.equals(Catcode.OTHER, "-")) {
			return -scanNumber();
		} else if (t.equals(Catcode.OTHER, "+")) {
			return scanNumber();
		}

		stream.put(t);
		return scanNumber();
	}

	/**
	 * Scan the expanded token stream for a sequence of letter tokens. If all
	 * tokens are found then they are removed from the input stream and <code>true</code>
	 * is returned. Otherwise all tokens are left in the input stream and
	 * <code>false</code> is returned.
	 * 
	 * @param s
	 *                 the tokens to scan
	 * 
	 * @return <code>true</code> iff the tokens could have been successfully
	 *            removed from the input stream
	 */
	public boolean scanKeyword(String s) throws GeneralException {
		return scanKeyword(s, 0);
	}

	/**
	 * Scan the input for the next token which has not the catcode SPACE.
	 * 
	 * @return the next non-space token or <code>null</code> at EOF
	 */
	public Token scanNextNonSpace() throws GeneralException {
		for (Token t = scanNextToken(); t != null; t = scanNextToken()) {
			if (!(t.isa(Catcode.SPACE))) {
				return t;
			}
		}

		return null;
	}

	/**
	 * Scan the input for the next token which has not the catcode SPACE.
	 * 
	 * @return the next non-space token or <code>null</code> at EOF
	 */
	public Token scanNextNonSpace(Token t) throws GeneralException {
		for (; t != null; t = scanNextToken()) {
			if (!(t.isa(Catcode.SPACE))) {
				return t;
			}
		}

		return null;
	}

	/**
	 * Expand tokens from the input stream until an unexpandable token is
	 * found. This unexpandable token is returned.
	 * 
	 * @return the next unexpandable token
	 * 
	 * @throws GeneralException
	 *                 ...
	 */
	public Token scanNextToken() throws GeneralException {
		Token t = getNextToken();

		//TODO: incomplete
		if (t == null) {
		} else if (t instanceof ControlSequenceToken) {
		} else if (t instanceof ActiveCharacterToken) {
		} else {
			return t;
		}

		return t;
	}

	/**
	 * Scan the input stream for tokens making up a number, this means a
	 * sequence of digits with catcode OTHER. Alternative notations for a
	 * number may exist. The number can be preceeded by optional whitespace.
	 * 
	 * @return the value of the integer scanned
	 * 
	 * @throws CodeNumberFormatException
	 *                 in case that no number is found
	 * @throws CodeEOFException
	 *                 in case that the end of file has been reached before an
	 *                 integer could be acquired
	 */
	public long scanNumber() throws GeneralException {
		return scanNumber(scanNextNonSpace());
	}

	/**
	 * Scan a number with a given first token.
	 * 
	 * @param t
	 *                 the first token to consider
	 * 
	 * @return the value of the integer scanned
	 * 
	 * @throws CodeNumberFormatException
	 *                 in case that no number is found
	 * @throws CodeEOFException
	 *                 in case that the end of file has been reached before an
	 *                 integer could be acquired
	 */
	public long scanNumber(Token t) throws GeneralException {
		long n = 0;

		if (t == null) {
			throw new GeneralHelpingException("TTP.MissingNumber");
		} else if (t.isa(Catcode.OTHER) && t.getValue().matches("[0-9]")) {
			do {
				n = n * 10 + t.getValue().charAt(0) - '0';
				t = scanNextToken();
			} while (t != null && t.isa(Catcode.OTHER) && t.getValue().matches("[0-9]"));

			stream.put(t);
		} else if (t.isa(Catcode.OTHER) && t.getValue().equals("'")) {
			while (t != null && t.isa(Catcode.OTHER) && t.getValue().matches("[0-7]")) {
				n = n * 8 + t.getValue().charAt(0) - '0';
				t = scanNextToken();
			}

			stream.put(t);
		} else if (t.isa(Catcode.OTHER) && t.getValue().equals("`")) {
			t = getNextToken();

			if (t == null) {
				throw new GeneralHelpingException("TTP.MissingNumber");
			}

			n = t.getValue().charAt(0);
		} else if (t.isa(Catcode.OTHER) && t.getValue().equals("\"")) {
			//TODO parse hex numbers
		} else {
			stream.put(t);
			throw new GeneralHelpingException("TTP.MissingNumber");
		}

		return n;
	}

	/**
	 * Skip spaces and if the next non-space character is an equal sign skip it
	 * as well and all spaces afterwards.
	 */
	public void scanOptionalEquals() throws GeneralException {
		Token t = scanNextNonSpace();

		if (t == null) {
			throw new GeneralHelpingException("TTP.MissingNumber");
		} else if (t.equals(Catcode.OTHER, "=")) {
			stream.put(scanNextNonSpace());
		} else {
			stream.put(t);
		}
	}

	/**
	 * Setter for the context.
	 * 
	 * @param context
	 *                 the context to use
	 */
	protected void setContext(Context context) {
		this.context = context;
	}

	/**
	 * ...
	 * 
	 * @param s
	 *                 the string to use as reference
	 * @param i
	 *                 the index in s to start working at
	 * 
	 * @return <code>true</code> iff the keyword has been found
	 * 
	 * @throws GeneralException
	 *                 in case of an error
	 */
	private boolean scanKeyword(String s, int i) throws GeneralException {
		if (i < s.length()) {
			Token t = scanNextToken();

			if (t == null) {
				return false;
			} else if (!(t.equals(Catcode.LETTER, s.charAt(i)) || t.equals(Catcode.OTHER, s.charAt(i))) || !scanKeyword(s, i + 1)) {
				stream.put(t);
				return false;
			}
		}

		return true;
	}
}
