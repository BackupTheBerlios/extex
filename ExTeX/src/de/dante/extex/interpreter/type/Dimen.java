/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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

import de.dante.extex.interpreter.CountConvertable;
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
 * @version $Revision: 1.16 $
 */
public class Dimen extends GlueComponent implements Serializable, CountConvertable {

     /**
      * The constant <tt>ZERO_PT</tt> contains the ...
      */
     public static final Dimen ZERO_PT = new Dimen(0);

     /**
      * The constant <tt>ONE_PT</tt> contains the ...
      */
     public static final Dimen ONE_PT = new Dimen(1 << 16);

     /**
      * The constant <tt>ONE</tt> contains the internal representation for 1pt.
      *
      * @see "TeX -- The Program [101]"
      */
     public static final long ONE = 1 << 16;

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
        set(source, context, false);
    }

    /**
     * Add the value of the argument to the current value.
     * This modifies this instance.
     *
     * @param d the Dimen to add to
     *
     * @return ...
     */
    public Dimen add(final Dimen d) {
        setValue(getValue() + d.getValue());
        return new Dimen(getValue());
    }

    /**
     * Subtract the value of the argument from the current value.
     * This modifies this instance.
     *
     * @param d the Dimen to add to
     *
     * @return ...
     */
    public Dimen subtract(final Dimen d) {
        setValue(getValue() - d.getValue());
        return new Dimen(getValue());
    }

    /**
     * ...
     *
     * @param d ...
     *
     * @return ...
     */
    public boolean lt(final Dimen d) {
        return (getValue() < d.getValue());
    }

    /**
     * ...
     *
     * @param d ...
     *
     * @return ...
     */
    public boolean le(final Dimen d) {
        return (getValue() <= d.getValue());
    }

    /**
     * ...
     *
     * @param d ...
     *
     * @return ...
     */
    public void max(final Dimen d) {

        long val = d.getValue();
        if (val > getValue()) {
            setValue(val);
        }
    }

     /**
      * ...
      *
      * @return ...
      */
     public String toString() {
          return Long.toString(getValue()) + "sp";
     }

     /**
      * ...
      *
      * @param sb the output string buffer
      */
     public void toString(final StringBuffer sb) {
          sb.append(Long.toString(getValue()));
          sb.append("sp");
     }

     /**
      * Return a String with the Dimen value in pt
      *
      * @return a String with the Dimen value in pt
      */
     public String toPT() {
          return String.valueOf(round((double)getValue() / ONE)) + "pt";
     }

     /**
      * Rounds a floating-point number to nearest whole number.
      * It uses exactly the same algorithm as web2c implementation of TeX.
      *
      * @param d number to be rounded
      *
      * @return rounded value
      */
     private long round(double d) {

         return (long) ((d >= 0.0) ? d + 0.5 : d - 0.5);
     }

     /**
     * ...
     *
     * @param factory the token factory to get the required tokens from
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     *
     * @see "TeX -- The Program [103]"
     */
    public Tokens toToks(final TokenFactory factory) throws GeneralException {

        Tokens toks = new Tokens();
        String s = Long.toString(getValue() / ONE);

        for (int i = 0; i < s.length(); i++) {
            toks.add(factory.newInstance(Catcode.OTHER, s.substring(i, i + 1)));
        }

        //TODO: decimal places and rounding
        toks.add(factory.newInstance(Catcode.LETTER, "p"));
        toks.add(factory.newInstance(Catcode.LETTER, "t"));

        return toks;
    }

    /**
     * Return the <code>Dimen</code>-value in bp
     * @return	the value in bp
     */
    public double toBP() {
        return ((double) getValue() * 7200) / (7227 << 16);
    }

    /**
     * @see de.dante.extex.interpreter.CountConvertable#convertCount(de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */
    public long convertCount(final Context context, final TokenSource source)
            throws GeneralException {

        return getValue();
    }
}
