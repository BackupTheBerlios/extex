/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.file;

import java.io.File;
import java.io.Serializable;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.token.CrToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;

/**
 * This class holds an input file from which tokens can be read.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class InFile implements Serializable {

    /**
     * The field <tt>file</tt> contains name of the underlying file.
     */
    private File file;

    /**
     * The field <tt>stream</tt> contains the stream.
     */
    private TokenStream stream = null;

    /**
     * Creates a new object.
     *
     * @param inStream the token stream to read from
     */
    public InFile(final TokenStream inStream) {

        super();
        this.file = null;
        this.stream = inStream;
    }

    /**
     * Checks whether this InFile is at end of file.
     *
     * @return <code>true</code> iff no further token can be read.
     */
    public boolean isEof() throws InterpreterException {

        try {
            return (stream == null || stream.isEof());
        } catch (ScannerException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * Check whether this InFile is curretly opened for reading.
     *
     * @return <tt>true</tt> iff the input stream has still a stream assigned
     * to it.
     */
    public boolean isOpen() {

        return (stream != null);
    }

    /**
     * Close the current stream. No reading operation is possible afterwards.
     */
    public void close() {

        stream = null;
        file = null;
    }

    /**
     * Read a line of characters into a tokens list.
     *
     * @param factory the factory to request new tokens from
     * @param tokenizer the tokenizer to use
     *
     * @return the tokens read or <code>null</code> in case of eof
     *
     * @throws InterpreterException in case of an error
     */
    public Tokens read(final TokenFactory factory, final Tokenizer tokenizer)
            throws InterpreterException {

        if (stream == null) {
            return null;
        }

        Tokens toks = new Tokens();
        Token t;

        for (;;) {
            try {
                t = stream.get(factory, tokenizer);
            } catch (ScannerException e) {
                throw new InterpreterException(e);
            }
            if (t == null) {
                return (toks.length() > 0 ? toks : null);
            } else if (t instanceof CrToken) { //TODO gene: correct?
                return toks;
            }
            toks.add(t);
        }
    }
}