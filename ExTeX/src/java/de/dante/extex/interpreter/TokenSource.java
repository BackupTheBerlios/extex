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

import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.util.GeneralException;
import de.dante.util.Locator;

/**
 * This interface describes a class to acquire {@link Token Token}s from.
 * Beside the pure getter for the next token some higher parsing methods are
 * provided here as well.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public interface TokenSource {

	/**
	 * Scan the input stream for tokens making up an integer, i.e. a number
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
	public abstract long scanInteger() throws GeneralException;

	/**
	 * Getter for the locator.
	 * 
	 * @return the current locator
	 */
	public abstract Locator getLocator();

	/**
	 * Get the next token form the input streams. If the current input stream
	 * is at its end then the next one on the streamStack is used until a token
	 * could be read. If all stream are at the end then <code>null</code> is
	 * returned.
	 * 
	 * @return the next token or <code>null</code>
	 * @see "TeX -- The Program [332]"
	 */
	public abstract Token getNextToken() throws GeneralException;

	/**
	 * Get the next expanded token form the input streams. If the current input
	 * stream is at its end then the next one on the streamStack is used until
	 * a token could be read. If all stream are at the end then <code>null</code>
	 * is returned.
	 * 
	 * @return the next token or <code>null</code>
	 */
	public abstract Token scanNextToken() throws GeneralException;

	/**
	 * Get the next expanded token form the input streams between <code>{</code>
	 * and <code>}</code>. If the current inputstream is at its end then the
	 * next one on the streamStack is used until a token could be read. If all
	 * stream are at the end then <code>null</code> is returned.
	 * 
	 * @return the next token or <code>null</code>
	 */
	public abstract Tokens scanNextTokens() throws GeneralException;

	/**
	 * Scan the input stream for tokens making up a number, this is a sequence
	 * of digits with catcode <tt>OTHER</tt>. The number can be preceeded by
	 * optional whitespace. Alternate representations for an integer exist.
	 * 
	 * @return the value of the integer scanned
	 * 
	 * @throws CodeNumberFormatException
	 *                 in case that no number is found
	 * @throws CodeEOFException
	 *                 in case that the end of file has been reached before an
	 *                 integer could be acquired
	 */
	public abstract long scanNumber() throws GeneralException;

	/**
	 * Scan the input stream for tokens making up a character code, this is a
	 * sequence of digits with catcode <tt>OTHER</tt>. The number can be
	 * preceeded by optional whitespace. Alternate representations for an
	 * character code exist.
	 * 
	 * @return the value of the integer scanned
	 * 
	 * @throws CodeNumberFormatException
	 *                 in case that no number is found
	 * @throws CodeEOFException
	 *                 in case that the end of file has been reached before an
	 *                 integer could be acquired
	 */
	public abstract long scanCharacterCode() throws GeneralException;

	/**
	 * Scan the input stream for tokens making up a number, i.e. a sequence of
	 * digits with catcode OTHER. The number can be preceeded by optional
	 * whitespace.
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
	public abstract long scanNumber(Token t) throws GeneralException;

	/**
	 * ...
	 * 
	 * @return ...
	 */
	public abstract TokenStreamFactory getTokenStreamFactory();

	public abstract long scanFloat() throws GeneralException;

	/**
	 * Put a given stream on top of the stream stack. The reading occurs on
	 * this new stream before resorting to the previous streams.
	 * 
	 * @param stream
	 *                 the new stream to read from
	 */
	public abstract void addStream(TokenStream stream);

	/**
	 * All input streams are closed and not further Token is available for
	 * processing. This normally means that the interpreter is forced to
	 * terminate more or less gracefully.
	 */
	public abstract void closeAllStreams();

	/**
	 * ...
	 */
	public abstract void closeNextFileStream();

	/**
	 * Push back a token onto the input stream for subsequent reading.
	 * 
	 * @param token
	 *                 the token to push
	 */
	public abstract void push(Token token);

	/**
	 * Push back a list of tokens onto the input stream for subsequent reading.
	 * 
	 * @param tokens
	 *                 the tokens to push
	 */
	public abstract void push(Token[] tokens);

	/**
	 * Push back a list of tokens onto the input stream for subsequent reading.
	 * 
	 * @param tokens
	 *                 the tokens to push
	 */
	public abstract void push(Tokens tokens);

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
	public abstract boolean scanKeyword(String s) throws GeneralException;

	/**
	 * Skip spaces and if the next non-space character is an equal sign skip it
	 * as well and all spaces afterwards.
	 */
	public void scanOptionalEquals() throws GeneralException;

	/**
	 * Scan the input for the next token which has not the catcode SPACE.
	 * 
	 * @return the next non-space token or <code>null</code> at EOF
	 */
	public abstract Token scanNextNonSpace() throws GeneralException;

	/**
	 * Get the next token which has not the catcode SPACE.
	 * 
	 * @return the next non-space token or <code>null</code> at EOF
	 */
	public abstract Token getNextNonSpace() throws GeneralException;

	/**
	 * Scan the input for the next token which has not the catcode SPACE.
	 * 
	 * @param t
	 *                 the first token to consider
	 * 
	 * @return the next non-space token or <code>null</code> at EOF
	 */
	public abstract Token scanNextNonSpace(Token t) throws GeneralException;
}
