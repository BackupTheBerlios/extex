/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
package de.dante.extex.scanner.stream.impl;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.GeneralException;
import de.dante.util.Locator;

/**
 * This is the base implementation of a token stream. It has an internal stack
 * of tokens which can be enlarged with push() or reduced with pop().
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class TokenStreamBaseImpl implements TokenStream {

    /**
     * The field <tt>stack</tt> contains the Token stack for the pushback
     * operation.
     */
    private List stack = new ArrayList();

    /**
     * The field <tt>fileStream</tt> contains the indicator whether or not this
     * TokenStream is attached to a file.
     */
    private boolean fileStream = false;

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
     * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
     */
    public Locator getLocator() {

        return new Locator(null, 0, null, 0);
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
     */
    public boolean closeFileStream() {

        stack = new ArrayList();
        return false;
    }

    /**
     * @see de.dante.extex.token.TokenStream#get()
     */
    public Token get(final TokenFactory factory, final Tokenizer tokenizer)
            throws GeneralException {

        return (stack.size() == 0
                ? getNext(factory, tokenizer)
                : (Token) stack.remove(stack.size() - 1));
    }

    /**
     * @see de.dante.extex.token.TokenStream#put(Token)
     */
    public void put(final Token token) {
        if (token != null) {
            stack.add(token);
        }
    }

    /**
     * @see de.dante.extex.token.TokenStream#put(Tokens)
     */
    public void put(final Tokens toks) {
        if (toks != null) {
            stack.add(toks);
        }
    }

    /**
     * Get the next token when the stack is empty.
     * This method is meant to be overloaded by derived classes.
     *
     * @param factory the factory for new tokens
     * @param tokenizer the classifies for characters
     *
     * @return the next Token or <code>null</code>
     *
     * @throws GeneralException in case of an error
     */
    protected Token getNext(final TokenFactory factory,
            final Tokenizer tokenizer)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
     */
    public boolean isFileStream() {

        return fileStream;
    }

}
