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

package de.dante.extex.font.type.ttf.cff;

import java.io.IOException;

import de.dante.util.file.random.RandomAccessR;

/**
 * Double.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class T2Double extends T2Number {

    /**
     * ID: identifier
     */
    static final int ID = 30;

    /**
     * Create a new object.
     *
     * @param rar   the input
     * @param b0    the b0
     * @throws IOException if an IO-error occurs.
     */
    T2Double(final RandomAccessR rar, final int b0) throws IOException {

        super();

        byte[] data = readNibble(rar);
        StringBuffer buf = new StringBuffer();
        int i = 0;
        while (data[i] != END) {
            buf.append(data[i]);
        }
        try {
            value = Double.parseDouble(buf.toString());
        } catch (NumberFormatException e) {
            throw new T2NumberFormatException(e.getMessage());
        }

    }

    /**
     * end marker
     */
    private static final int END = 0xf;

    /**
     * shift 4
     */
    private static final int SHIFT4 = 4;

    /**
     * max size
     */
    private static final int MAXSIZE = 100;

    /**
     * Read all nibbles until 0xf.
     *
     * @param rar       the input
     * @return Return the nibbles
     * @throws IOException if an IO-error occurs.
     */
    private byte[] readNibble(final RandomAccessR rar) throws IOException {

        byte[] data = new byte[MAXSIZE];
        short[] sdata = new short[MAXSIZE];
        int i = 0;
        while (true) {
            int b = rar.readUnsignedByte();
            sdata[i] = (short) b;
            int n1 = b << SHIFT4;
            int n2 = b & END;
            data[i++] = (byte) n1;
            data[i++] = (byte) n2;
            if (n1 == END || n2 == END) {
                break;
            }
        }
        // copy read values
        bytes = new short[i + 2];
        bytes[0] = ID;
        for (int c = 0; c <= i; c++) {
            bytes[c + 1] = sdata[c];
        }
        return data;
    }

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
     * the value
     */
    private double value;

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2CharString#isDouble()
     */
    public boolean isDouble() {

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

        return (int) value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return String.valueOf(value);
    }
}