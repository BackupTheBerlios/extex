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

package de.dante.extex.font.type.tfm;

/**
 * TFM-FixWord
 * <p>
 * The dimensions are represented in the same way as in tfm files. Higher
 * 12 bits is the whole part and lower 20 bits is the fractional part.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TFMFixWord {

    /**
     * NULL
     */
    public static final TFMFixWord NULL = null;

    /**
     * ZERO
     */
    public static final TFMFixWord ZERO = new TFMFixWord(0);

    /**
     * UNITY
     */
    public static final TFMFixWord UNITY = new TFMFixWord(1);

    /**
     * TEN
     */
    public static final TFMFixWord TEN = new TFMFixWord(10);

    /**
     * POINT-SHIFT
     */
    private static final int POINTSHIFT = 20;

    /**
     * Create a new object
     *
     * @param val the values as int
     */
    public TFMFixWord(final int val) {

        value = val << POINTSHIFT;
    }

    /**
     * Create new object
     *
     * @param num   the num
     * @param den   the den
     */
    public TFMFixWord(final int num, final int den) {

        value = ((long) num << POINTSHIFT) / den;
    }

    /**
     * the internal value
     */
    private long value;

    /**
     * Return the internal value
     *
     * @return the internal value
     */
    public long getValue() {

        return value;
    }

    /**
     * less than
     *
     * @param num the value to compare
     * @return <code>true</code>, if the internal values is lesser,
     * otherwise <code>false</code>
     */
    public boolean lessThan(final int num) {

        return (value < (num << POINTSHIFT));
    }

    /**
     * more than
     *
     * @param num the value to compare
     * @return <code>true</code>, if the internal values are more,
     * otherwise <code>false</code>
     */
    public boolean moreThan(final int num) {

        return (value > (num << POINTSHIFT));
    }

    /**
     * Return the value as String in units. <p>It devide the value by
     * 1000.
     *
     * @return the value as String in units
     */
    public String toStringUnits() {

        if (value > 0) {
            return String.valueOf((value * 1000) >>> POINTSHIFT);
        }
        return String.valueOf(-((-value * 1000) >>> POINTSHIFT));
    }

    /**
     * Return the values as String
     *
     * @return the values as String
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        long v = value;
        int unity = 1 << POINTSHIFT;
        int mask = unity - 1;
        if (v < 0) {
            buf.append('-');
            v = -v;
        }
        buf.append(v >>> POINTSHIFT);
        buf.append('.');
        v = 10 * (v & mask) + 5;
        int delta = 10;
        do {
            if (delta > unity) {
                v += unity / 2 - delta / 2;
            }
            buf.append(Character.forDigit((int) (v >>> POINTSHIFT), 10));
            v = 10 * (v & mask);
        } while (v > (delta *= 10));
        return buf.toString();
    }

}