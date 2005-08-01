/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.documentWriter.postscript.util;

import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;

/**
 * This class contains some utility methods.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class PsUnit {

    /**
     * The constant <tt>FLOAT_DIGITS</tt> contains the number of digits to
     * consider when producing a string representation of this type.
     *
     * Attention: Do not change this value unless you have read and understood
     * <logo>TeX</logo> the program!
     */
    private static final int FLOAT_DIGITS = 17;

    /**
     * Get the next character from a character sequence.
     *
     * @param seq the sequence to read from
     * @param index the index of the next character to read
     *
     * @return the code of the next character or a negative number if none
     *  could be read
     */
    public static int getChar(final CharSequence seq, final Count index) {

        int i = (int) index.getValue();
        if (i >= seq.length()) {
            return -1;
        }
        index.add(1);
        return seq.charAt(i);
    }

    /**
     * Get the next non-space character from a character sequence.
     *
     * @param seq the sequence to read from
     * @param index the index of the next character to read
     *
     * @return the code of the next character or a negative number if none
     *  could be read
     */
    private static int getNonSpace(final CharSequence seq, final Count index) {

        int i = (int) index.getValue();
        int length = seq.length();
        while (i < length) {
            char c = seq.charAt(i++);
            if (!Character.isWhitespace(c)) {
                index.set(i);
                return c;
            }
        }
        index.set(i);
        return -1;
    }

    /**
     * Scan a floating point number from a character sequence.
     *
     * @param seq the sequence to read from
     * @param index the index of the next character to read
     *
     * @return the fixed point number in multiples of 2^16
     */
    public static long getFloat(final CharSequence seq, final Count index) {

        boolean neg = false;
        long val = 0;
        int post = 0;
        int t = getChar(seq, index);

        while (t >= 0) {
            if (t == '-') {
                neg = !neg;
            } else if (t != '+') {
                break;
            }
            t = getNonSpace(seq, index);
        }
        if (t >= 0 && t != '.' && t != ',') {
            for (val = 0; t >= '0' && t <= '9'; t = getChar(seq, index)) {
                val = val * 10 + t - '0';
            }
            t = getChar(seq, index);
        }
        if (t >= 0 && (t == '.' || t == ',')) {
            // @see "TeX -- The Program [102]"
            int[] dig = new int[FLOAT_DIGITS];
            int k = 0;
            for (t = getChar(seq, index); t >= '0' && t <= '9'; t = getChar(
                    seq, index)) {
                if (k < FLOAT_DIGITS) {
                    dig[k++] = t - '0';
                }
            }
            if (k < FLOAT_DIGITS) {
                k = FLOAT_DIGITS;
            }
            post = 0;
            while (k-- > 0) {
                post = (post + dig[k] * (1 << FLOAT_DIGITS)) / 10;
            }
            post = (post + 1) / 2;
        }
        index.add(-1);
        val = val << 16 | post;
        return (neg ? -val : val);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param seq the sequence to read from
     * @param index the index of the next character to read
     *
     * @return
     */
    public static long getLength(final CharSequence seq, final Count index)
            throws NumberFormatException {

        long length = getFloat(seq, index);

        int c = getChar(seq, index);
        switch (c) {
            case 'b':
                c = getChar(seq, index);
                if (c == 'p') {
                    return (length * 7227 / 7200);
                }
                break;
            case 'c':
                c = getChar(seq, index);
                if (c == 'm') {
                    return (length * 7227 / 254);
                } else if (c == 'c') {
                    return (length * 14856 / 1157);
                }
                break;
            case 'd':
                c = getChar(seq, index);
                if (c == 'd') {
                    return (length * 1238 / 1157);
                }
                break;
            case 'i':
                c = getChar(seq, index);
                if (c == 'n') {
                    return (length * 7227 / 100);
                }
                break;
            case 'm':
                c = getChar(seq, index);
                if (c == 'm') {
                    return (length * 7227 / 2540);
                }
                break;
            case 'p':
                c = getChar(seq, index);
                if (c == 't') {
                    return (length);
                } else if (c == 'c') {
                    return (length * 12);
                }
                break;
            case 's':
                c = getChar(seq, index);
                if (c == 'p') {
                    return (length >> 16);
                }
                break;
            default:
        // fall through to exception
        }
        throw new NumberFormatException();
    }

    /**
     * This method produces a printable representation of a length in terms of
     * PostScript points. This means that 72 PostScript points are 1 inch.
     *
     * @param d the dimen to convert
     * @param out the target buffer
     * @param strip indicator whether rounding to the next higher integral
     *  number is desirable
     */
    public static void toPoint(final Dimen d, final StringBuffer out,
            final boolean strip) {

        long val = d.getValue() * 7200 / 7227;

        if (val < 0) {
            out.append('-');
            val = -val;
        }

        if (strip) {
            val += Dimen.ONE - 1;
        } else {
            val += 1; // gene: rounding
        }

        long v = val / Dimen.ONE;
        if (v == 0) {
            //            if (val == 0) {
            out.append('0');
            //            }
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                out.append((char) ('0' + (v / m)));
                v = v % m;
                m /= 10;
            }
        }

        if (strip) {
            return;
        }

        if (val == 0) {
            return;
        }

        out.append('.');

        val = 10 * (val % Dimen.ONE) + 5;
        long delta = 10;
        delta = 10000; //gene: precision
        do {
            if (delta > Dimen.ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / Dimen.ONE);
            out.append((char) ('0' + i));
            val = 10 * (val % Dimen.ONE);
            delta *= 10;
        } while (val > delta);

    }

    /**
     * Creating a new object is impossible.
     */
    private PsUnit() {

    }

}
