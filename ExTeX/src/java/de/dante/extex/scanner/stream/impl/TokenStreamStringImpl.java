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
import de.dante.util.UnicodeChar;

/**
 * This class contains an implementation of a token stream which is fed from a
 * String.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class TokenStreamStringImpl extends AbstractTokenStreamImpl
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
    public TokenStreamStringImpl(final String l) {

        super();
        if (l != null) {
            line = l;
        } else {
            this.line = "";
        }
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.AbstractTokenStreamImpl#bufferLength()
     */
    protected int bufferLength() {

        return line.length();
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.AbstractTokenStreamImpl#getSingleChar()
     */
    protected UnicodeChar getSingleChar() {

        return new UnicodeChar(line, pointer);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return line;
    }
}
