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
package de.dante.extex.interpreter.type;

import java.io.Serializable;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.TokenFactory;
import de.dante.util.GeneralException;

/**
 * This class implements the dimen value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.25 $
 */
public class Dimen extends GlueComponent implements Serializable {

    /**
     * The constant <tt>ONE</tt> contains the internal representation for 1pt.
     *
     * @see "TeX -- The Program [101]"
     */
    public static final long ONE = 1 << 16;

    /**
      * The constant <tt>ZERO_PT</tt> contains the immutable dimen register
      * representing the length of 0pt.
      */
     public static final Dimen ZERO_PT = new ImmutableDimen(0);

     /**
      * The constant <tt>ONE_PT</tt> contains the immutable dimen register
      * representing the length of 1pt.
      */
     public static final Dimen ONE_PT = new ImmutableDimen(ONE);

     /**
      * The constant <tt>ONE_INCH</tt> contains the immutable dimen register
      * representing the length of 1in.
      */
     public static final Dimen ONE_INCH = new ImmutableDimen(ONE * 7227 / 100);

     /**
      * Creates a new object.
      * The length stored in it is initialized to 0pt.
      */
     public Dimen() {

         super();
     }

     /**
      * Creates a new object.
      * This method makes a new instance of the class with the given value.
      *
      * @param value the value to set
      */
     public Dimen(final long value) {

         super(value);
     }

     /**
      * Creates a new object.
      * This method makes a new instance of the class with the given value.
      *
      * @param value the value to set
      */
     public Dimen(final int value) {

         super((long) value);
     }

    /**
     * Creates a new object.
     * This method makes a new instance of the class with the same value as
     * the given instance. I.e. it acts like clone().
     *
     * @param value the value to imitate
     */
    public Dimen(final Dimen value) {

        super(value == null ? 0 : value.getValue());
    }

    /**
     * Creates a new object.
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     *
     * @throws GeneralException in case of an error
     */
    public Dimen(final Context context, final TokenSource source)
        throws GeneralException {

        super(source, context, false);
    }

    /**
     * @see de.dante.extex.interpreter.type.GlueComponent#set(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void set(final Context context, final TokenSource source)
        throws GeneralException {

        set(context, source, false);
    }

    /**
     * Add the value of the argument to the current value.
     * This operation modifies the instance.
     *
     * <p>
     * |<i>this</i>| := |<i>this</i>| + |<i>d</i>|
     * </p>
     *
     * @param d the Dimen to add
     */
    public void add(final Dimen d) {

        setValue(getValue() + d.getValue());
    }

    /**
     * Subtract the value of the argument from the current value.
     * This operation modifies the instance.
     *
     * <p>
     * |<i>this</i>| := |<i>this</i>| - |<i>d</i>|
     * </p>
     *
     * @param d the Dimen to subtract
     */
    public void subtract(final Dimen d) {

        setValue(getValue() - d.getValue());
    }

    /**
     * Multiply the current value with a given number.
     * This operation modifies this instance.
     *
     * <p>
     * |<i>this</i>| := |<i>this</i>| * <i>factor</i>
     * </p>
     *
     * @param factor the factor to multiply with
     */
    public void multiply(final long factor) {

        setValue(getValue() * factor);
    }

    /**
     * Divide the current value with a given number.
     * This operation modifies this instance.
     *
     * <p>
     * |<i>this</i>| := |<i>this</i>| / <i>denom</i>
     * </p>
     *
     * @param denom denominator to divide by
     *
     * @throws GeneralHelpingException in case of a division by 0, i.e. if
     * denom is 0.
     */
    public void divide(final long denom) throws GeneralHelpingException {

        if (denom == 0) {
            throw new GeneralHelpingException("TTP.ArithOverflow");
        }
        setValue(getValue() / denom);
    }

    /**
     * Compares the current instance with another Dimen for equality of the
     * values.
     *
     * @param d the other dimen to compare to
     *
     * @return <code>true</code> iff <i>|this| == |d|</i>
     *
     * @throws NullPointerException in case that the argument is
     * <code>null</code>.
     */
    public boolean eq(final Dimen d) {
        return (getValue() == d.getValue());
    }

    /**
     * Compares the current instance with another Dimen.
     *
     * @param d the other dimen to compare to
     *
     * @return <code>true</code> iff <i>|this| &lt; |d|</i>
     *
     * @throws NullPointerException in case that the argument is
     * <code>null</code>.
     */
    public boolean lt(final Dimen d) {
        return (getValue() < d.getValue());
    }

    /**
     * Compares the current instance with another Dimen.
     *
     * @param d the other dimen to compare to
     *
     * @return <code>true</code> iff <i>|this| &lt;= |d|</i>
     *
     * @throws NullPointerException in case that the argument is
     * <code>null</code>.
     */
    public boolean le(final Dimen d) {
        return (getValue() <= d.getValue());
    }

    /**
     * Sets the value of the dimen to the maximum of the value already stored
     * and a given argument.
     *
     * <i>|this| = max(|this|, |d|)</i>
     *
     * @param d the other dimen
     *
     * @throws NullPointerException in case that the argument is
     * <code>null</code>.
     */
    public void max(final Dimen d) {

        long val = d.getValue();
        if (val > getValue()) {
            setValue(val);
        }
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactely the string which would be produced by
     * TeX to print the Dimen. This means the result is expressed in pt and
     * properly rounded to be read back in again without loss of information.
     *
     * @return the printable representation
     *
     * @see #toString(StringBuffer)
     * @see #toToks(TokenFactory)
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Determine the printable representation of the object and append it to
     * the given StringBuffer.
     * The value returned is exactely the string which would be produced by
     * TeX to print the Dimen. This means the result is expressed in pt and
     * properly rounded to be read back in again without loss of information.
     *
     * @param sb the output string buffer
     *
     * @see #toString()
     * @see #toToks(TokenFactory)
     */
    public void toString(final StringBuffer sb) {
        long val = getValue();
        
        if (val < 0) {
            sb.append('-');
            val = -val;
        }

        long v = val / ONE;
        if (v == 0) {
            sb.append('0');
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                sb.append((char) ('0' + (v / m)));
                v = v % m;
                m /= 10;
            }
        }

        sb.append('.');

        val = 10 * (val % ONE) + 5;
        long delta = 10;
        do {
            if (delta > ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / ONE);
            sb.append((char) ('0' + i));
            val = 10 * (val % ONE);
            delta *= 10;
        } while (val > delta);

        sb.append('p');
        sb.append('t');
    }

    /**
     * Determine the printable representation of the object and return it as a
     * löist of Tokens.
     * The value returned is exactely the string which would be produced by
     * TeX to print the Dimen. This means the result is expressed in pt and
     * properly rounded to be read back in again without loss of information.
     *
     * @param factory the token factory to get the required tokens from
     *
     * @return the printable representation
     *
     * @throws GeneralException in case of an error
     *
     * @see "TeX -- The Program [103]"
     * @see #toString()
     * @see #toString(StringBuffer)
     */
    public Tokens toToks(final TokenFactory factory) throws GeneralException {

        Tokens toks = new Tokens();
        long val = getValue();

        if (val < 0) {
            toks.add(factory.newInstance(Catcode.OTHER, '-'));
            val = -val;
        }

        long v = val / ONE;
        if (v == 0) {
            toks.add(factory.newInstance(Catcode.OTHER, '0'));
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                toks.add(factory.newInstance(Catcode.OTHER,
                                             (char) ('0' + (v / m))));
                v = v % m;
                m /= 10;
            }
        }

        toks.add(factory.newInstance(Catcode.OTHER, '.'));

        val = 10 * (val % ONE) + 5;
        long delta = 10;
        do {
            if (delta > ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / ONE);
            toks.add(factory.newInstance(Catcode.OTHER, (char) ('0' + i)));
            val = 10 * (val % ONE);
            delta *= 10;
        } while (val > delta);

        toks.add(factory.newInstance(Catcode.LETTER, "p"));
        toks.add(factory.newInstance(Catcode.LETTER, "t"));

        return toks;
    }


    /**
     * Return a String with the Dimen value in pt
     *
     * @return a String with the Dimen value in pt
     * @deprecated this method produces incorrectly rounded values; use toString() instead
     */
    public String toPT() {
         return String.valueOf(round((double)getValue() / ONE)) + "pt";
    }

    /**
     * Return the <code>Dimen</code>-value in bp
     *
     * @return the value in bp
     * @deprecated use dimen arithmetic instead of incorrect rounding to a double
     */
    public double toBP() {
        return ((double) getValue() * 7200) / (7227 * ONE);
    }

    /**
     * Rounds a floating-point number to nearest whole number.
     * It uses exactly the same algorithm as web2c implementation of TeX.
     *
     * @param d number to be rounded
     *
     * @return rounded value
     */
    private long round(final double d) {

        return (long) ((d >= 0.0) ? d + 0.5 : d - 0.5);
    }

}
