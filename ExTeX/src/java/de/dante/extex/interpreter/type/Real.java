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
package de.dante.extex.interpreter.type;

import java.io.Serializable;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.CountConvertable;
import de.dante.extex.interpreter.DimenConvertable;
import de.dante.extex.interpreter.RealConvertable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * Real (with a double value)
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class Real implements Serializable {

    /**
     * ZERO-Real
     */
    public static final Real ZERO = new Real(0);

    /**
     * max-Real
     */
    public static final Real MAX_VALUE = new Real(Double.MAX_VALUE);

    /**
     * The value
     */
    private double value = 0.0d;

    /**
     * Creates a new object.
     *
     * @param value    init with double-value
     */
    public Real(final double value) {
        super();
        this.value = value;
    }

    /**
     * Creates a new object.
     *
     * Scan the <code>TokenSource</code> for a <code>Real</code>.
     *
     * @param context ...
     * @param source the token source
     */
    public Real(final Context context, final TokenSource source)
            throws GeneralException {

        super();
        Real r = scanReal(context, source);
        //gene: this should be integrated
        value = r.getValue();
    }

    /**
     * Scan the input stream for tokens making up a <code>Real</code>.
     *
     * @param context ...
     * @param source ...
     *
     * @return    the <code>Real</code>-value
     *
     * @throws GeneralException in case of an error
     */
    private Real scanReal(final Context context, final TokenSource source)
            throws GeneralException {

        long value = 0;
        boolean neg = false;

        // get number
        Token t = source.scanNonSpace();
        if (t == null) {
            throw new GeneralHelpingException("TTP.MissingNumber");
        } else if (t.equals(Catcode.OTHER, "-")) {
            neg = true;
            t = source.scanNonSpace();
        } else if (t.equals(Catcode.OTHER, "+")) {
            t = source.scanNonSpace();
        } else if (t instanceof ControlSequenceToken) {
            Code code = context.getMacro(t.getValue());
            if (code != null && code instanceof CountConvertable) {
                return new Real(((CountConvertable) code).convertCount(context,
                                                                       source));
            } else if (code != null && code instanceof DimenConvertable) {
                return new Real(((DimenConvertable) code).convertDimen(context,
                                                                       source));
            } else if (code != null && code instanceof RealConvertable) {
                return ((RealConvertable) code).convertReal(context, source);
            }
        }

        StringBuffer sb = new StringBuffer(32);
        if (neg) {
            sb.append('-');
        }

        if (t != null && !t.equals(Catcode.OTHER, ".")
            && !t.equals(Catcode.OTHER, ",")) {
            value = source.scanNumber(t);
            t = source.getToken();
        }

        sb.append(Long.toString(value));
        sb.append('.');
        value = 0;

        if (t != null
            && (t.equals(Catcode.OTHER, ".") || t.equals(Catcode.OTHER, ","))) {
            value = source.scanNumber();
        }
        sb.append(Long.toString(value));

        return new Real(sb.toString());
    }

    /**
     * Creates a new object.
     */
    public Real(final Real val) {
        value = val.getValue();
    }

    /**
     * Creates a new object.
     */
    public Real(final float val) {
        value = val;
    }

    /**
     * Creates a new object.
     */
    public Real(final long l) {
        if (l > MAX_VALUE.getLong()) {
            value = MAX_VALUE.getLong();
        } else {
            value = l;
        }
    }

    /**
     * Creates a new object.
     */
    public Real(final int i) {
        value = i;
    }

    /**
     * Creates a new object.<p>
     * If a error is throws, the value is set to zero
     */
    public Real(final String s) {
        try {
            value = Double.valueOf(s).doubleValue();
        } catch (NumberFormatException e) {
            //gene: bad idea to ignore a number format exception:-(
            // set to zero
            value = 0.0d;
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
     * ...
     *
     * @param val ...
     */
    public void add(final double val) {
        value += val;
    }

    /**
     * ...
     *
     * @param real ...
     */
    public void add(final Real real) {
        value += real.getValue();
    }

    /**
     * ...
     *
     * @param val ...
     *
     * @throws GeneralException in case of a division by zero
     */
    public void divide(final double val) throws GeneralException {
        if (val == 0.0d) {
            throw new GeneralHelpingException("TTP.ArithOverflow");
        }

        value /= val;
    }

    /**
     * ...
     *
     * @param val ...
     *
     * @throws GeneralException in case of a division by zero
     */
    public void divide(final Real val) throws GeneralException {
        divide(val.getValue());
    }

    /**
     * ...
     *
     * @param val ...
     */
    public void multiply(final double val) {
        value *= val;
    }

    /**
     * ...
     *
     * @param val ...
     */
    public void multiply(final Real val) {
        value *= val.getValue();
    }

    /**
     * Return the value as long.
     *
     * @return ...
     */
    public long getLong() {
        return (long) value;
    }

    /**
     * Return the value as <code>String</code>
     *
     * @return the value as <code>String</code>
     */
    public String toString() {
        return Double.toString(value);
    }
}
