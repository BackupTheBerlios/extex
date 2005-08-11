/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.scanner.stream;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.util.Locator;

/**
 * This interface describes the features of a stream capable of delivering
 * {@link de.dante.extex.scanner.type.token.Token Token}s. In fact it is a push-back
 * stream since Tokens already read can be pushed back onto the stream for
 * further reading.
 * <p>
 * Nevertheless you should be aware that characters once coined into tokens are
 * not changed &ndash; even if the tokenizer might produce another result in the
 * meantime.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public interface TokenStream {

    /**
     * Close this stream if it is a file stream.
     *
     * @return <code>true</code> if the closing was successful
     */
    boolean closeFileStream();

    /**
     * Get the next token from the token stream.
     * If tokens are on the push-back stack then those are delivered otherwise
     * new tokens might be extracted utilizing the token factory and the
     * tokenizer.
     *
     * @param factory the token factory
     * @param tokenizer the tokenizer
     *
     * @return the next Token or <code>null</code> if no more tokens are
     * available
     *
     * @throws ScannerException in case of an error
     */
    Token get(TokenFactory factory, Tokenizer tokenizer)
            throws ScannerException;

    /**
     * Getter for the locator.
     * The locator describes the place the tokens have been read from in terms
     * of the user. This information is meant for the end user to track down
     * problems.
     *
     * @return the locator
     */
    Locator getLocator();

    /**
     * Check to see if a further token can be acquired from the token stream.
     *
     * @return <code>true</code> if the stream is at its end
     *
     * @throws ScannerException in case that an error has been encountered.
     *  Especially if an IO exceptions occurs it is delivered as chained
     *  exception in a ScannerException.
     */
    boolean isEof() throws ScannerException;

    /**
     * Check whether the current stream is associated with a file to read from.
     *
     * @return <code>true</code> if the stream is a file stream
     */
    boolean isFileStream();

    /**
     * Push back a token into the stream.
     * If the token is <code>null</code> then nothing happens:
     * a <code>null</code> token is not pushed!
     * <p>
     * Note that it is up to the implementation to accept tokens not produced
     * with the token factory for push back. In general the behaviour in such a
     * case is not defined and should be avoided.
     * </p>
     *
     * @param token the token to push back
     * @see "<logo>TeX</logo> &ndash; The Program [325]"
     */
    void put(Token token);

}