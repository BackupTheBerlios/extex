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

package de.dante.extex.interpreter.type.real;

import java.io.Serializable;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * Real (with a double value)
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class Real implements Serializable {

    /**
     * ZERO-Real
     */
    public static final Real ZERO = new ImmutableReal(0);

    /**
     * max-Real
     */
    public static final Real MAX_VALUE = new ImmutableReal(Double.MAX_VALUE);

    /**
     * The value
     */
    private double value = 0.0d;

    /**
     * Creates a new object.
     *
     * @param val    init with double-value
     */
    public Real(final double val) {

        super();
        value = val;
    }

    /**
     * Creates a new object.
     *
     * Scan the <code>TokenSource</code> for a <code>Real</code>.
     *
     * @param context ...
     * @param source the token source
     * @throws GeneralException ...
     */
    public Real(final Context context, final TokenSource source)
            throws GeneralException {

        super();
        value = scanReal(context, source);
    }

    /**
     * Scan the input stream for tokens making up a <code>Real</code>.
     *
     * @param context ...
     * @param source ...
     * @return the <code>Real</code>-value
     * @throws GeneralException in case of an error
     */
    private double scanReal(final Context context, final TokenSource source)
            throws GeneralException {

        long val = 0;
        boolean neg = false;

        // get number
        Token t = source.scanNonSpace(context);
        if (t == null) {
            throw new HelpingException("TTP.MissingNumber");
        } else if (t.equals(Catcode.OTHER, "-")) {
            neg = true;
            t = source.scanNonSpace(context);
        } else if (t.equals(Catcode.OTHER, "+")) {
            t = source.scanNonSpace(context);
        } else if (t instanceof ControlSequenceToken) {
            Code code = context.getCode((ControlSequenceToken) t);
            if (code instanceof RealConvertible) {
                return (((RealConvertible) code).convertReal(context, source))
                        .getValue();
            } else if (code instanceof CountConvertible) {
                return (new Real(((CountConvertible) code).convertCount(
                        context, source, null))).getValue();
            } else if (code instanceof DimenConvertible) {
                return (new Real(((DimenConvertible) code).convertDimen(
                        context, source, null))).getValue();
            }
        }

        StringBuffer sb = new StringBuffer();
        if (neg) {
            sb.append('-');
        }

        if (t != null && !t.equals(Catcode.OTHER, ".")
                && !t.equals(Catcode.OTHER, ",")) {
            val = source.scanNumber(context, t);
            t = source.getToken(context);
        }

        sb.append(Long.toString(val));
        sb.append('.');
        val = 0;

        if (t != null
                && (t.equals(Catcode.OTHER, ".") || t
                        .equals(Catcode.OTHER, ","))) {
            val = source.scanNumber(context);
        } else {
            source.push(t);
        }
        sb.append(Long.toString(val));

        return (new Real(sb.toString())).getValue();
    }

    /**
     * Creates a new object.
     * @param val   the value
     */
    public Real(final Real val) {

        value = val.getValue();
    }

    /**
     * Creates a new object.
     * @param val   tha value as float
     */
    public Real(final float val) {

        value = val;
    }

    /**
     * Creates a new object.
     * @param l the value as long
     */
    public Real(final long l) {

        value = l;
    }

    /**
     * Creates a new object.
     * @param   i   the value as int
     */
    public Real(final int i) {

        value = i;
    }

    /**
     * Creates a new object.<p>
     * If the string equlas <code>null</code> or empty, the value is set to zero
     * @param s     the value as String
     * @throws GeneralException if a NumberFormatException is throws
     */
    public Real(final String s) throws GeneralException {

        if (s == null || s.trim().length() == 0) {
            value = 0.0d;
        } else {

            try {
                value = Double.valueOf(s).doubleValue();
            } catch (NumberFormatException e) {
                throw new HelpingException("TTP.NumberFormatError", s);
            }
        }
    }

    /**
     * Setter for the value.
     *
     * @param d the new value
     */
    public void setValue(final double d) {

        value = d;
    }

    /**
     * Getter for the value
     *
     * @return the value
     */
    public double getValue() {

        return value;
    }

    /**
     * add
     *
     * @param val the value to add
     */
    public void add(final double val) {

        value += val;
    }

    /**
     * add
     *
     * @param real the value to add
     */
    public void add(final Real real) {

        value += real.getValue();
    }

    /**
     * divide
     *
     * @param val the value to divide
     *
     * @throws GeneralException in case of a division by zero
     */
    public void divide(final double val) throws GeneralException {

        if (val == 0.0d) {
            throw new HelpingException("TTP.ArithOverflow");
        }

        value /= val;
    }

    /**
     * divide
     *
     * @param val the value to divide
     *
     * @throws GeneralException in case of a division by zero
     */
    public void divide(final Real val) throws GeneralException {

        divide(val.getValue());
    }

    /**
     * multiply
     *
     * @param val the value to multiply
     */
    public void multiply(final double val) {

        value *= val;
    }

    /**
     * multiply
     *
     * @param val the value to multiply
     */
    public void multiply(final Real val) {

        value *= val.getValue();
    }

    /**
     * Return the value as long.
     * @return the value as long
     */
    public long getLong() {

        return (long) value;
    }

    /**
     * Return the value as <code>String</code>
     * @return the value as <code>String</code>
     */
    public String toString() {

        return Double.toString(value);
    }
}