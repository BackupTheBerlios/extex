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
package de.dante.extex.interpreter;

import de.dante.extex.interpreter.type.Real;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;

import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.NotObservableException;
import de.dante.util.UnicodeChar;

/**
 * This interface describes a class to acquire
 * {@link de.dante.extex.scanner.Token Token}s from.
 * Beside the pure getter for the next token some higher-level parsing methods
 * are provided here as well.
 *
 * <p>There are two classes of methods for reading something from a token
 * stream. The methods starting with <tt>get</tt> perform the raw reading,
 * whereas the methods starting with <tt>scan</tt> perform expansion as well.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.15 $
 */
public interface TokenSource {
    /**
     * Getter for the locator.
     *
     * @return the current locator
     */
    Locator getLocator();

    /**
     * Get the next token which has not the catcode SPACE.
     *
     * @return the next non-space token or <code>null</code> at EOF
     *
     * @throws GeneralException in case of an error
     */
    Token getNonSpace() throws GeneralException;

    /**
     * Get the next token form the input streams. If the current input stream
     * is at its end then the next one on the streamStack is used until a token
     * could be read. If all stream are at the end then <code>null</code> is
     * returned.
     *
     * @return the next token or <code>null</code>
     *
     * @throws GeneralException in case of an IOException
     *
     * @see "TeX -- The Program [332]"
     */
    Token getToken() throws GeneralException;

    /**
     * Get the next tokens form the input streams between <code>{</code> and
     * <code>}</code>. If the current input stream is at its end then the
     * next one on the streamStack is used until a token could be read. If all
     * streams are at the end then <code>null</code> is returned.
     *
     * @return the next tokens or <code>null</code>
     *
     * @throws GeneralException in case of an error
     */
    Tokens getTokens() throws GeneralException;

    /**
     * ...
     * 
     * @return
     *
     * @throws GeneralException
     */
    Token getControlSequence() throws GeneralException;
    
    /**
     * Getter for the token stream factory.
     *
     * @return the token stream factory
     */
    TokenStreamFactory getTokenStreamFactory();

    /**
     * Put a given stream on top of the stream stack. The reading occurs on
     * this new stream before resorting to the previous streams.
     *
     * @param stream the new stream to read from
     */
    void addStream(TokenStream stream);

    /**
     * All input streams are closed and not further Token is available for
     * processing. This normally means that the interpreter is forced to
     * terminate more or less gracefully.
     */
    void closeAllStreams();

    /**
     * Close all streams on the stack until a file stream is found. This file
     * stream is closed as last one. The other streams are left unchanged.
     * If no file stream is found the all streams are closed and none is left.
     */
    void closeNextFileStream();

    /**
     * Push back a token onto the input stream for subsequent reading.
     * 
     * @param token the token to push
     */
    void push(Token token);

    /**
     * Push back a list of tokens onto the input stream for subsequent reading.
     *
     * @param tokens the tokens to push
     */
    void push(Token[] tokens);

    /**
     * Push back a list of tokens onto the input stream for subsequent reading.
     *
     * @param tokens the tokens to push
     */
    void push(Tokens tokens);

    /**
     * Scan the input stream for tokens making up a character code, this is a
     * sequence of digits with catcode <tt>OTHER</tt>. The number can be
     * preceeded by optional whitespace. Alternate representations for an
     * character code exist.
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException in case that no number is found or the end of
     *             file has been reached before an integer could be acquired
     */
    UnicodeChar scanCharacterCode() throws GeneralException;

    /**
     * ...
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    long scanFloat() throws GeneralException;

    /**
     * Scan a real-value
     *
     * @return the real value
     *
     * @throws GeneralException in case of an error
     */
    Real scanReal() throws GeneralException;
    
    /**
     * Scan the input stream for tokens making up an integer, this is a number
     * optionally preceeded by a sign (+ or -). The number can be preceeded by
     * optional whitespace. Whitespace is also ignored between the sign and the
     * number. All non-whitespace characters must have the catcode OTHER.
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException in case of an error
     */
    long scanInteger() throws GeneralException;

    /**
     * Scan the expanded token stream for a sequence of letter tokens. If all
     * tokens are found then they are removed from the input stream and <code>true</code>
     * is returned. Otherwise all tokens are left in the input stream and
     * <code>false</code> is returned.
     *
     * @param s the tokens to scan
     *
     * @return <code>true</code> iff the tokens could have been successfully
     *         removed from the input stream
     *
     * @throws GeneralException in case of an error
     */
    boolean scanKeyword(String s) throws GeneralException;

    /**
     * Scan the input for the next token which has not the catcode SPACE.
     *
     * @return the next non-space token or <code>null</code> at EOF
     *
     * @throws GeneralException in case of an error
     */
    Token scanNonSpace() throws GeneralException;

    /**
     * Scan the input for the next token which has not the catcode SPACE.
     *
     * @param token the first token to consider
     *
     * @return the next non-space token or <code>null</code> at EOF
     *
     * @throws GeneralException in case of an error
     */
    Token scanNonSpace(Token token) throws GeneralException;

    /**
     * Get the next expanded token form the input streams. If the current input
     * stream is at its end then the next one on the streamStack is used until
     * a token could be read. If all stream are at the end then
     * <code>null</code> is returned.
     *
     * @return the next token or <code>null</code>
     *
     * @throws GeneralException in case of an error
     */
    Token scanToken() throws GeneralException;

    /**
     * Get the next expanded token form the input streams between <code>{</code>
     * and <code>}</code>. If the current inputstream is at its end then the
     * next one on the streamStack is used until a token could be read. If all
     * stream are at the end then <code>null</code> is returned.
     *
     * @return the next tokens or <code>null</code>
     *
     * @throws GeneralException in case of an error
     */
    Tokens scanTokens() throws GeneralException;

    /**
     * Get the next expanded token form the input streams between <code>{</code>
     * and <code>}</code> an convert it to a <code>String</code>. If the
     * current inputstream is at its end then the next one on the streamStack
     * is used until a token could be read. If all stream are at the end then
     * <code>null</code> is returned.
     *
     * @return the next tokens as <code>String</code> or <code>null</code>
     *
     * @throws GeneralException in case of an error
     */
    String scanTokensAsString() throws GeneralException;

    /**
     * Scan the input stream for tokens making up a number, this is a sequence
     * of digits with catcode <tt>OTHER</tt>. The number can be preceeded by
     * optional whitespace. Alternate representations for an integer exist.
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException in case of an error
     */
    long scanNumber() throws GeneralException;

    /**
     * Scan the input stream for tokens making up a number, i.e. a sequence of
     * digits with catcode OTHER. The number can be preceeded by optional
     * whitespace.
     *
     * @param t the first token to consider
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException in case of an error
     */
    long scanNumber(Token t) throws GeneralException;

    /**
     * Skip spaces and if the next non-space character is an equal sign skip it
     * as well and all spaces afterwards.
     *
     * @throws GeneralException in case of an error
     */
    //gene: this method is subject to be eliminated in favor of getKeyword().
    void scanOptionalEquals() throws GeneralException;

    /**
     * Send the string to the named observer. The observer must be capable to
     * deal with a string argument.
     *
     * @param name name of the observer
     * @param text the text to send to the observer
     *
     * @throws NotObservableException in case that the named observer is not
     *             accessible
     */
    void update(String name, String text) throws NotObservableException;

}
