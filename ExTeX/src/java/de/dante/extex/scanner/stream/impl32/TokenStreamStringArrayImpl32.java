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

package de.dante.extex.scanner.stream.impl32;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;

import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.stream.TokenStream;

/**
 * This class contains an implementation of a token stream which is fed from a
 * StringArray. It use 32 bit characters.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class TokenStreamStringArrayImpl32 extends TokenStreamBaseImpl32
        implements
            TokenStream,
            CatcodeVisitor {

    /**
     * the line
     */
    private String line;

    /**
     * Creates a new object.
     * @param   l the line for the tokenizer
     */
    public TokenStreamStringArrayImpl32(final String l) {

        super();
        if (l != null) {
            line = l;
        } else {
            line = "";
        }
        setBuffer(line);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return line;
    }

    /**
     * The field <tt>nextLine</tt> ...
     */
    private int nextLine = 1;

    /**
     * The field <tt>lines</tt> ...
     */
    private String[] lines = null;

    /**
     * Creates a new object.
     *
     * @param ls the array of lines to consider
     * @throws CharacterCodingException in cas of an error
     */
    public TokenStreamStringArrayImpl32(final String[] ls)
            throws CharacterCodingException {

        line = ls[0];
        this.lines = ls;
        setBuffer(line);
    }

    /**
     * @see de.dante.extex.scanner.stream.impl32.TokenStreamBaseImpl32#refill()
     */
    protected boolean refill() throws IOException {

        super.refill();
        if (lines == null || nextLine >= lines.length) {
            return false;
        }
        line = lines[nextLine++];
        setBuffer(line);
        return true;
    }
}
