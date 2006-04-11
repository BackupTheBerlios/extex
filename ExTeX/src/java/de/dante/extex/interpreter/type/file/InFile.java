/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import java.io.Serializable;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;

/**
 * This class holds an input file from which tokens can be read.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public class InFile implements Serializable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060411L;

    /**
     * The field <tt>standardStream</tt> contains the indicator that the current
     * stream is the standard stream.
     */
    private boolean standardStream;

    /**
     * The field <tt>stream</tt> contains the stream.
     */
    private TokenStream stream = null;

    /**
     * Creates a new object.
     *
     * @param inStream the token stream to read from
     */
    public InFile(final TokenStream inStream, boolean isStandard) {

        super();
        this.stream = inStream;
        this.standardStream = isStandard;
    }

    /**
     * Close the current stream. No reading operation is possible afterwards.
     */
    public void close() {

        stream = null;
    }

    /**
     * Checks whether this InFile is at end of file.
     *
     * @return <code>true</code> iff no further token can be read.
     *
     * @throws InterpreterException in case of an error
     */
    public boolean isEof() throws InterpreterException {

        try {
            return (stream == null || stream.isEof());
        } catch (ScannerException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
     */
    public boolean isFileStream() {

        return stream != null && this.stream.isFileStream();
    }

    /**
     * Check whether this InFile is currently opened for reading.
     *
     * @return <tt>true</tt> iff the input stream has still a stream assigned
     * to it.
     */
    public boolean isOpen() {

        return (stream != null);
    }

    /**
     * Getter for standardStream.
     *
     * @return the standardStream
     */
    public boolean isStandardStream() {

        return this.standardStream;
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

        try {
            for (;;) {
                t = stream.get(factory, tokenizer);
                if (t == null) {
                    return (toks.length() > 0 ? toks : null);
                } else if (stream.isEol()) {
                    return toks;
                }
                toks.add(t);
            }
        } catch (ScannerException e) {
            throw new InterpreterException(e);
        }
    }

}
