/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.type;

import java.io.File;
import java.io.Serializable;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class InFile implements Serializable {

    /**
     * The constant <tt>MAX_FILE_NO</tt> contains the ...
     */
    public static int MAX_FILE_NO = 15;

    /**
     * The field <tt>filename</tt> contains the ...
     */
    private File file = null;

    /**
     * The field <tt>stream</tt> contains the ...
     */
    private TokenStream stream = null;

    /**
     * The field <tt>factory</tt> contains the ...
     */
    private TokenFactory factory = null;

    /**
     * The field <tt>tokenizer</tt> contains the ...
     */
    private Tokenizer tokenizer = null;

    /**
     * The field <tt>lookahead</tt> contains the ...
     */
    private Token lookahead = null;

    /**
     * Creates a new object.
     *
     * @param file
     */
    public InFile(final File file) {
        super();
        this.file = file;
    }

    /**
     * ...
     *
     * @return ...
     */
    public boolean ifEof() {
        return (lookahead == null);
    }

    /**
     * ...
     *
     * @return ...
     *
     * @throws GeneralException ...
     */
    public boolean open() throws GeneralException {
        return false; // TODO
    }

    /**
     * ...
     *
     */
    public void close() {
        //TODO
        stream = null;
        file = null;
    }

    /**
     * ...
     *
     * @return ...
     *
     * @throws GeneralException ...
     */
    public Tokens read() throws GeneralException {

        if (stream == null) {
            if (file == null) {
                //TODO: error
            } else if (!open()) {
                //TODO: error
            }
        }
        Token t = lookahead;
        lookahead = stream.get(factory, tokenizer);
        //TODO incorrect and incomplete
        return new Tokens(t);
    }

}
