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

package de.dante.extex.scanner.stream.impl;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.SpaceToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.util.Locator;

/**
 * This is the base implementation of a token stream. It has an internal stack
 * of tokens which can be enlarged with push() or reduced with pop().
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.20 $
 */
public class TokenStreamBaseImpl implements TokenStream {

    /**
     * The field <tt>fileStream</tt> contains the indicator whether or not this
     * TokenStream is attached to a file.
     */
    private boolean fileStream = false;

    /**
     * The field <tt>skipSpaces</tt> contains the indicator that spaces should
     * be ignored before the next token is delivered.
     */
    private boolean skipSpaces = false;

    /**
     * The field <tt>stack</tt> contains the Token stack for the pushback
     * operation.
     */
    private Tokens stack = new Tokens();

    /**
     * Creates a new object.
     *
     * @param isFile indicator whether or not the token stream is assigned to
     * a file
     */
    public TokenStreamBaseImpl(final boolean isFile) {

        super();
        this.fileStream = isFile;
    }

    /**
     * Creates a new object.
     *
     * @param isFile indicator whether or not the token stream is assigned to
     * a file
     * @param tokens the tokens to push to the stream initially
     */
    public TokenStreamBaseImpl(final boolean isFile, final Tokens tokens) {

        super();
        this.fileStream = isFile;

        for (int i = tokens.length() - 1; i > 0; i--) {
            stack.add(tokens.get(i));
        }
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
     */
    public boolean closeFileStream() {

        stack = new Tokens();
        return false;
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#get(
     *      de.dante.extex.scanner.TokenFactory,
     *      de.dante.extex.interpreter.Tokenizer)
     */
    public Token get(final TokenFactory factory, final Tokenizer tokenizer)
            throws ScannerException {

        if (!skipSpaces) {
            return (stack.length() > 0 ? //
                    stack.removeLast() : //
                    getNext(factory, tokenizer));
        }

        Token t;
        while (stack.length() > 0) {
            t = stack.removeLast();
            if (!(t instanceof SpaceToken)) {
                return t;
            }
        }

        do {
            t = getNext(factory, tokenizer);
        } while(t instanceof SpaceToken);

        return t;
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
     */
    public Locator getLocator() {

        return new Locator(null, 0, null, 0);
    }

    /**
     * Get the next token when the stack is empty.
     * This method is meant to be overloaded by derived classes.
     *
     * @param factory the factory for new tokens
     * @param tokenizer the classifies for characters
     *
     * @return the next Token or <code>null</code>
     * @throws ScannerException in case of an error
     */
    protected Token getNext(final TokenFactory factory,
            final Tokenizer tokenizer) throws ScannerException {

        return null;
    }

    /**
     * Test for end of file.
     *
     * @return <code>true</code> iff the stream is at its end
     */
    public boolean isEof() throws ScannerException {

        return (stack.length() == 0);
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
     */
    public boolean isFileStream() {

        return fileStream;
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#put(
     *      de.dante.extex.scanner.Token)
     */
    public void put(final Token token) {

        if (token != null) {
            stack.add(token);
        }
    }

    /**
     * Setter for skipSpaces.
     */
    public void skipSpaces() {

        this.skipSpaces = true;
    }

}