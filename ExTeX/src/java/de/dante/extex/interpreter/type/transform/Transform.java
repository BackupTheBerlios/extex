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

package de.dante.extex.interpreter.type.transform;

import java.io.Serializable;
import java.util.StringTokenizer;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.real.Real;
import de.dante.util.GeneralException;

/**
 * Transform (transformation with six values)
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class Transform implements Serializable {

    /**
     * max values
     */
    private static final int MAXVAL = 6;

    /**
     * T1
     */
    private static final int T1 = 0;

    /**
     * T2
     */
    private static final int T2 = 1;

    /**
     * T3
     */
    private static final int T3 = 2;

    /**
     * T4
     */
    private static final int T4 = 3;

    /**
     * T5
     */
    private static final int T5 = 4;

    /**
     * T6
     */
    private static final int T6 = 5;

    /**
     * The value
     */
    private Real[] val = new Real[MAXVAL];

    /**
     * Creates a new object.
     */
    public Transform() {

        super();
        for (int i = 0; i < MAXVAL; i++) {
            val[i] = new Real(0);
        }
    }

    /**
     * Creates a new object.
     *
     * @param v1    the value 1
     * @param v2    the value 2
     * @param v3    the value 3
     * @param v4    the value 4
     * @param v5    the value 5
     * @param v6    the value 6
     */
    public Transform(final Real v1, final Real v2, final Real v3,
            final Real v4, final Real v5, final Real v6) {

        super();
        val[T1] = v1;
        val[T2] = v2;
        val[T3] = v3;
        val[T4] = v4;
        val[T5] = v5;
        val[T6] = v6;
    }

    /**
     * Creates a new object.
     *
     * @param v1    the value 1
     * @param v2    the value 2
     * @param v3    the value 3
     * @param v4    the value 4
     * @param v5    the value 5
     * @param v6    the value 6
     */
    public Transform(final double v1, final double v2, final double v3,
            final double v4, final double v5, final double v6) {

        super();
        val[T1] = new Real(v1);
        val[T2] = new Real(v2);
        val[T3] = new Real(v3);
        val[T4] = new Real(v4);
        val[T5] = new Real(v5);
        val[T6] = new Real(v6);
    }

    /**
     * Creates a new object.
     * Scan the <code>TokenSource</code> for a <code>Transform</code>.
     *
     * @param context   the context
     * @param source    the token source
     * @throws GeneralException ...
     */
    public Transform(final Context context, final TokenSource source)
            throws GeneralException {

        super();
        for (int i = 0; i < MAXVAL; i++) {
            val[i] = new Real(context, source);
        }
    }

    /**
     * Creates a new object.<p>
     * If the string equlas <code>null</code> or empty, the value is set to zero
     * @param s     the value as String
     * @throws GeneralException if a NumberFormatException is throws
     */
    public Transform(final String s) throws GeneralException {

        if (s == null || s.trim().length() == 0) {
            for (int i = 0; i < MAXVAL; i++) {
                val[i] = new Real(0);
            }
        } else {

            try {
                StringTokenizer st = new StringTokenizer(s);
                for (int i = 0; i < MAXVAL; i++) {
                    if (st.hasMoreTokens()) {
                        val[i] = new Real(st.nextToken());
                    } else {
                        throw new HelpingException(
                                "TTP.NumberFormatError", s);
                    }
                }
            } catch (NumberFormatException e) {
                throw new HelpingException("TTP.NumberFormatError", s);
            }
        }
    }

    /**
     * Setter for the value.
     * @param value the new value
     */
    public void set(final Transform value) {

        for (int i = 0; i < MAXVAL; i++) {
            val[i] = value.get(i);
        }
    }

    /**
     * Setter for the value with index idx
     * @param idx   the index
     * @param value the value with index
     */
    public void set(final int idx, final Real value) {

        if (idx >= 0 && idx < MAXVAL) {
            val[idx] = value;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Return the idx-value for the transfomation
     * @param idx   the number of the value
     * @return  the value on index idx
     */
    public Real get(final int idx) {

        if (idx >= 0 && idx < MAXVAL) {
            return val[idx];
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Return the value as <code>String</code>
     * @return the value as <code>String</code>
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < MAXVAL; i++) {
            sb.append(val[i].toString());
            if (i < MAXVAL - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
}