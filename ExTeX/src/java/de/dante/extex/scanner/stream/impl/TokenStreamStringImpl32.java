/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.stream.TokenStream;

/**
 * This class contains an implementation of a token stream which is fed from a
 * String. It use 32 bit characters.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TokenStreamStringImpl32 extends TokenStreamBaseImpl32
        implements
            TokenStream,
            CatcodeVisitor {

    /**
     * the line
     */
    private String line;

    /**
     * Creates a new object.
     * @param   l    the line for the tokenizer
     */
    public TokenStreamStringImpl32(final String l) {

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
}
