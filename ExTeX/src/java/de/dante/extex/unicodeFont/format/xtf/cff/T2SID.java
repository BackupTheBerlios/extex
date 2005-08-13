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

package de.dante.extex.unicodeFont.format.xtf.cff;

import java.io.IOException;

import de.dante.extex.unicodeFont.format.xtf.XtfConstants;
import de.dante.util.file.random.RandomAccessR;

/**
 * SID.
 *
 * <p>
 * SID  (0-64999) 2-byte string identifier
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class T2SID extends T2Number {

    /**
     * Create a new object.
     *
     * @param rar   the input
     * @throws IOException if an IO-error occurs.
     */
    T2SID(final RandomAccessR rar) throws IOException {

        super();

        value = rar.readUnsignedShort();
    }

    /**
     * the value
     */
    private int value;

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2CharString#getBytes()
     */
    public short[] getBytes() {

        short[] bytes = new short[2];
        bytes[0] = (short) (value >> XtfConstants.SHIFT8);
        bytes[1] = (short) (value & XtfConstants.CONSTXFF);
        return bytes;
    }

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2CharString#isInteger()
     */
    public boolean isInteger() {

        return true;
    }

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2Number#getDouble()
     */
    public double getDouble() {

        return value;
    }

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2Number#getInteger()
     */
    public int getInteger() {

        return value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return String.valueOf(value);
    }

}