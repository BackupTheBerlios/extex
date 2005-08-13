/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.xtf.cff;

import java.io.IOException;
import java.util.List;

/**
 * Abstract class for all boolean-values.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public abstract class T2TDOBoolean extends T2TopDICTOperator {

    /**
     * Create a new object.
     *
     * @param stack the stack
     * @param id    the operator-id for the value
     * @throws IOException if an IO.error occurs.
     */
    protected T2TDOBoolean(final List stack, final short[] id)
            throws IOException {

        super();

        if (stack.size() < 1) {
            throw new T2MissingNumberException();
        }
        int v = ((T2Number) stack.get(0)).getInteger();
        bytes = convertStackaddID(stack, id);

        if (v == 0) {
            value = false;
        } else {
            value = true;
        }
    }

    /**
     * bytes
     */
    private short[] bytes;

    /**
     * Check, if the objekt is a boolean.
     * @return Returns <code>true</code>, if the object is a boolean.
     */
    public boolean isBoolean() {

        return true;
    }

    /**
     * value
     */
    private boolean value;

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2CharString#getBytes()
     */
    public short[] getBytes() {

        return bytes;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return String.valueOf(value);
    }

    /**
     * Returns the value.
     * @return Returns the value.
     */
    public boolean isValue() {

        return value;
    }
}