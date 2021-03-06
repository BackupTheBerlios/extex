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
 * Integer.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class T2Integer extends T2Number {

    /**
     * range 32
     */
    static final int R1 = 32;

    /**
     * range 246
     */
    static final int R2 = 246;

    /**
     * range 247
     */
    static final int R3 = 247;

    /**
     * range 250
     */
    static final int R4 = 250;

    /**
     * range 251
     */
    static final int R5 = 251;

    /**
     * range 254
     */
    static final int R6 = 254;

    /**
     * range 28
     */
    static final int R7 = 28;

    /**
     * range 29
     */
    static final int R8 = 29;

    /**
     * Create a new object.
     *
     * @param rar   the input
     * @param b0    the b0
     * @throws IOException if an IO-error occurs.
     */
    T2Integer(final RandomAccessR rar, final int b0) throws IOException {

        super();

        if (b0 >= R1 && b0 <= R2) {
            value = b0 - 139;
            bytes = new short[1];
            bytes[0] = (short) b0;
        } else if (b0 >= R3 && b0 <= R4) {
            int b1 = rar.readUnsignedByte();
            value = (b0 - 247) * 256 + b1 + 108;
        } else if (b0 >= R5 && b0 <= R6) {
            int b1 = rar.readUnsignedByte();
            value = -(b0 - 251) * 256 - b1 - 108;
            bytes = new short[2];
            bytes[0] = (short) b0;
            bytes[1] = (short) b1;
        } else if (b0 == R7) {
            int b1 = rar.readUnsignedByte();
            int b2 = rar.readUnsignedByte();
            value = b1 << 8 | b2;
            bytes = new short[3];
            bytes[0] = (short) b0;
            bytes[1] = (short) b1;
            bytes[2] = (short) b2;

        } else if (b0 == R8) {
            int b1 = rar.readUnsignedByte();
            int b2 = rar.readUnsignedByte();
            int b3 = rar.readUnsignedByte();
            int b4 = rar.readUnsignedByte();
            value = b1 << XtfConstants.SHIFT24 | b2 << XtfConstants.SHIFT16
                    | b3 << XtfConstants.SHIFT8 | b4;
            bytes = new short[5];
            bytes[0] = (short) b0;
            bytes[1] = (short) b1;
            bytes[2] = (short) b2;
            bytes[3] = (short) b3;
            bytes[4] = (short) b4;
        }
    }

    /**
     * the value
     */
    private int value;

    /**
     * the bytes as short-array
     */
    private short[] bytes;

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2CharString#getBytes()
     */
    public short[] getBytes() {

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