/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.util;

/**
 * Class for a fixed point format.
 * <p>
 * The Fixed point format consists of a signed,
 * 2 s complement mantissa and an
 * unsigned fraction. To compute the actual value,
 * take the mantissa and add the fraction.
 * <p>
 * <p>Examples:</p>
 * <table>
 *   <thead><tr>td>Decimal Value</td><td>Hex Value</td>
 *          <td>Mantissa</td><td>Fraction</td></tr>
 *   </thead>
 *   <tr><td>1.999939 </td><td>0x7fff</td><td>1  </td><td>16383/16384</td></tr>
 *   <tr><td>1.75     </td><td>0x7000</td><td>1  </td><td>0/16384</td></tr>
 *   <tr><td>0.000061 </td><td>0x0001</td><td>0  </td><td>1/16384</td></tr>
 *   <tr><td>0.0      </td><td>0x0000</td><td>0  </td><td>0/16384</td></tr>
 *   <tr><td>-0.000061</td><td>0xffff</td><td>-1 </td><td>16383/16384</td></tr>
 *   <tr><td>-2.0     </td><td>0x8000</td><td>-2 </td><td>0/16384</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FixedPoint {

    /**
     * the value
     */
    private long fp;

    /**
     * Create a new object.
     *
     * @param value the fixed point value
     */
    public FixedPoint(final long value) {

        fp = value;
    }

    /**
     * Create a new object.
     *
     * @param value the value as double
     */
    public FixedPoint(final double value) {

        fp = convert(value);
    }

    /**
     * Convert a double in a fixed point format
     *
     * @param dvalue    the doube value
     * @return Returns the fixed point format
     */
    private long convert(final double dvalue) {

        if (dvalue >= RANGE || dvalue < -RANGE) {
            throw new ArithmeticException("range error");
        }
        long n;
        double ff;
        if (dvalue < 0) {
            ff = Math.floor(dvalue);
            n = CONST4 + (long) ff;
            n = (n << SHIFT14);
            ff = dvalue - ff;
        } else {
            ff = Math.floor(dvalue);
            n = ((long) ff) << SHIFT14;
            ff = dvalue - ((long) ff) + ROUND; // rounding may be wrong
        }
        return n | (long) (ff * MASK4);
    }

    /**
     * shift 14
     */
    private static final int SHIFT14 = 14;

    /**
     * mask 0x4000
     */
    private static final int MASK4 = 0x4000;

    /**
     * mask 0x3fff
     */
    private static final int MASK3 = 0x3fff;

    /**
     * mask 0x8000
     */
    private static final int MASK8 = 0x8000;

    /**
     * const 4
     */
    private static final int CONST4 = 4;

    /**
     * round
     */
    private static final double ROUND = .0000005;

    /**
     * range
     */
    private static final double RANGE = 2.0;

    /**
     * Convert a fiex point value in a double
     *
     * @param fpvalue   the fixed point value
     * @return Returns the double value
     */
    private double convert(final long fpvalue) {

        long m = (fpvalue >> SHIFT14);

        if ((fpvalue & MASK8) != 0) {
            return ((double) (m - CONST4) + ((double) (fpvalue & MASK3))
                    / MASK4);
        } else {
            return (double) m + ((double) (fpvalue & MASK3)) / MASK4;

        }
    }

    /**
     * Returns the fixed point value.
     * @return Returns the fixed point value.
     */
    public long getFixedPoint() {

        return fp;
    }

    /**
     * Returns the double vlaue.
     * @return Returns the double vlaue.
     */
    public double getDoubleValue() {

        return convert(fp);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("0x").append(Long.toHexString(fp)).append(" = ");
        buf.append(convert(fp));
        return buf.toString();
    }
}