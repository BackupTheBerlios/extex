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
 * Number.
 *
 * <p>Operand Encoding</p>
 * <table border="1">
 *   <thead>
 *     <tr><td><b>Size</b></td><td><b>b0 range</b></td>
 *         <td><b>Value range</b></td><td><b>Value calculation</b></td>
 *     </tr>
 *   </thead>
 *   <tr><td>1</td><td>32 - 246</td><td>-107  - +107</td><td>
 *      b0 139</td></tr>
 *   <tr><td>2</td><td>247 - 250</td><td>+108 - +1131</td><td>
 *     (b0 247)*256+b1+108</td></tr>
 *   <tr><td>2</td>251 - 254</td><td>-1131 - -108</td>
 *     (b0 251)*256 b1 108</td></tr>
 *   <tr><td>3</td><td>28</td><td>-32768  -+32767</td><td>
 *     b1<<8|b2</td></tr>
 *   <tr><td>5</td><td>29</td><td>-(2^31) - +(2^31-1)</td><td>
 *     b1<<24|b2<<16|b3<<8|b4</td></tr>
 * </table>
 *
 * A real number operand is provided in addition to integer operands.
 * This operand begins with a byte value of 30 followed by
 * a variable-length sequence of bytes. Each byte is composed
 * of two 4-bit nibbles. The first nibble of a pair is stored
 * in the most significant 4 bits of a byte and the second
 * nibble of a pair is stored in the least significant 4 bits of
 * a byte.
 *
 * <p>Nibble Definitions</p>
 * <table border="0">
 *   <thead>
 *     <tr><td><b>Nibble</b></td><td><b>Represents</b></td></tr>
 *   </thead>
 *   <tr><td>0 - 9</td><td>0 - 9</td></tr>
 *   <tr><td>a</td><td>. (decimal point)</td></tr>
 *   <tr><td>b</td><td>E</td></tr>
 *   <tr><td>c</td><td>-E</td></tr>
 *   <tr><td>d</td><td>reserved</td></tr>
 *   <tr><td>e</td><td>- (minus)</td></tr>
 *   <tr><td>f</td><td>end of number</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public abstract class T2Number extends T2CharString {

    /**
     * Create a new object.
     */
    protected T2Number() {

        super();
    }

    /**
     * Create a new instance.
     *
     * @param rar       the input
     * @return Returns the new T2Number object.
     * @throws IOException if an IO-error occurs.
     */
    public static T2Number newInstance(final RandomAccessR rar)
            throws IOException {

        int b0 = rar.readUnsignedByte();
        return newInstance(rar, b0);
    }

    /**
     * Create a new instance.
     *
     * @param rar       the input
     * @param b0        the first byte
     * @return Returns the new T2Number object.
     * @throws IOException if an IO-error occurs.
     */
    public static T2Number newInstance(final RandomAccessR rar, final int b0)
            throws IOException {

        if ((b0 >= T2Integer.R1 && b0 <= T2Integer.R6)
                || (b0 >= T2Integer.R7 && b0 <= T2Double.ID)) {
            if (b0 == T2Double.ID) {
                return new T2Double(rar, b0);
            }
            return new T2Integer(rar, b0);

        }
        throw new T2NotANumberException();
    }

    /**
     * Returns the value as int.
     * @return Returns the value as int.
     */
    public abstract int getInteger();

    /**
     * Returns the value as double.
     * @return Returns the value as double.
     */
    public abstract double getDouble();

}