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
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.DimenConvertable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * This class provides a means to store floating numbers with an order.
 *
 * <p>Examples</p>
 * <pre>
 * 123 pt
 * -123 pt
 * 123.456 pt
 * 123.pt
 * .465 pt
 * -.456pt
 * +456pt
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.12 $
 */
public class GlueComponent implements Serializable {
    /**
     * The constant <tt>ONE</tt> contains the internal representation for 1pt.
     * @see "TeX -- The Program [101]"
     */
    public static final long ONE = 1 << 16;

    /**
     * The field <tt>order</tt> contains the order of infinity.
     * In case of an order 0 the value holds the absolute value; otherwise
     * value holde the factor of the order.
     */
    private int order = 0;

    /**
     * The field <tt>value</tt> contains the integer representatation of the
     * dimen register in sp if the order is 0.
     * If the order is not 0 then the value holds the factor to the order in
     * units of 2<sup>16</sup>.
     */
    private long value = 0;

    /**
     * Creates a new object.
     */
    public GlueComponent() {
        super();
    }

    /**
     * Creates a new object with a fixed width.
     *
     * @param value the fixed value
     */
    public GlueComponent(final long value) {
        super();
        this.value = value;
    }

    /**
     * Creates a new object with a width with a possibly higher order.
     *
     * @param value the fixed width or the factor
     * @param order the order
     */
    public GlueComponent(final long value, final int order) {
        super();
        this.value = value;
        this.order = order;
    }

    /**
     * Creates a new object from a TokenStream.
     *
     * @param source the source for new tokens
     * @param context the interpreter context
     *
     * @throws GeneralException in case of an error
     */
    public GlueComponent(final TokenSource source, final Context context)
                  throws GeneralException {
        this(source, context, false);
    }

    /**
     * Creates a new object.
     *
     * @param source the source for the tokens to be read
     * @param context the interpreter context
     * @param fixed if <code>true</code> then no glue order is allowed
     *
     * @throws GeneralException in case of an error
     */
    public GlueComponent(final TokenSource source, final Context context,
        final boolean fixed) throws GeneralException {
        super();
        set(context, source, fixed);
    }

    /**
     * Getter for the value in scaled points (sp).
     *
     * @return the value in internal units of scaled points (sp)
     */
    public long getValue() {
        return value;
    }

    /**
     * Setter for the value.
     *
     * @param val the new value
     */
    public void setValue(final long val) {
        value = val;
    }

    /**
     * ...
     *
     * @return ...
     */
    public GlueComponent copy() {
        return new GlueComponent(value, order);
    }

    /**
     * Setter for the value in terms of the internal representation.
     *
     * @param l the new value
     */
    public void set(final long l) {

        value = l;
    }

    /**
     * Setter for the value.
     *
     * @param d the new value
     */
    public void set(final Dimen d) {

        value = d.getValue();
        order = 0;
    }

    /**
     * ...
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     *
     * @throws GeneralException in case of an error
     */
    public void set(final Context context, final TokenSource source)
             throws GeneralException {

        set(context, source, true);
    }

    /**
     * ...
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     * @param fixed this argument indicates that no fil parts of the object
     * should be filled. This means that the component is in fact a fixed
     * dimen value.
     *
     * @throws GeneralException in case of an error
     */
    protected void set(final Context context, final TokenSource source,
            final boolean fixed) throws GeneralException {

        Token t = source.scanNonSpace();
        if (t == null) {
            throw new GeneralHelpingException("Glue: unit not found!"); //TODO
            // incomplete
        }

        value = scanFloat(source, t);

        t = source.scanNonSpace();
        if (t == null) {
            throw new GeneralHelpingException("Glue: unit not found!"); //TODO
            // incomplete
        }
        source.push(t);
        long mag = 1000;
        if (source.scanKeyword("true")) { // cf. TTP[453], TTP[457]
            mag = context.getMagnification();
            source.push(source.scanNonSpace());
        }
        // cf. TTP[458]
        if (source.scanKeyword("sp")) {
            value = value / ONE;
        } else if (source.scanKeyword("pt")) {
            // nothing to do
        } else if (source.scanKeyword("mm")) {
            value = value * 7227 / 2540;
        } else if (source.scanKeyword("cm")) {
            value = value * 7227 / 254;
        } else if (source.scanKeyword("in")) {
            value = value * 7227 / 100;
        } else if (source.scanKeyword("pc")) {
            value = value * 12;
        } else if (source.scanKeyword("bp")) {
            value = value * 7227 / 7200;
        } else if (source.scanKeyword("dd")) {
            value = value * 1238 / 1157;
        } else if (source.scanKeyword("cc")) {
            value = value * 14856 / 1157;
        } else if (source.scanKeyword("ex")) {
            Dimen ex = context.getTypesettingContext().getFont().getEm();
            value = value * ex.getValue() / ONE;
        } else if (source.scanKeyword("em")) {
            Dimen em = context.getTypesettingContext().getFont().getEm();
            value = value * em.getValue() / ONE;
        } else if (fixed && source.scanKeyword("fil")) {
            order = 1;
            for (t = source.getToken(); //
                    (t != null && (t.equals('l') || t.equals('L'))); //
                    t = source.getToken()) {
                order++;
            }
            source.push(t);
        } else if ((t = source.getToken()) != null) {
            if (t instanceof ControlSequenceToken) {
                Code code = context.getMacro(t.getValue());
                if (code instanceof DimenConvertable) {
                    value = value
                            * ((DimenConvertable) code).convertDimen(context,
                                                                     source)
                            / ONE;
                } else {
                    throw new GeneralHelpingException("TTP.IllegalUnit");
                }
            } else {
                throw new GeneralHelpingException("TTP.IllegalUnit");
            }
        } else { // cf. TTP [459]
            throw new GeneralHelpingException("TTP.IllegalUnit");
        }

        if (mag != 1000) {
            value = value * mag / 1000;
        }
    }

    /**
     * ...
     *
     * @param source ...
     * @param start ...
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    public long scanFloat(final TokenSource source, final Token start)
            throws GeneralException {

        long val = 0;
        boolean neg = false;
        int post = 0;
        Token t = start;
        if (t == null) {
            return 0;
        } else if (t.equals(Catcode.OTHER, "-")) {
            neg = true;
            t = source.scanNonSpace();
        } else if (t.equals(Catcode.OTHER, "+")) {
            t = source.scanNonSpace();
        }
        if (t != null && !t.equals(Catcode.OTHER, ".")
            && !t.equals(Catcode.OTHER, ",")) {
            val = source.scanNumber(t);
            t = source.getToken();
        }
        if (t != null
            && (t.equals(Catcode.OTHER, ".") || t.equals(Catcode.OTHER, ","))) {
            // @see "TeX -- The Program [102]"
            int[] dig = new int[17];
            int k = 0;
            for (t = source.getToken(); t != null && t.isa(Catcode.OTHER)
                                        && t.getValue().matches("[0-9]"); t = source
                    .scanToken()) {
                if (k < 17) {
                    dig[k++] = t.getValue().charAt(0) - '0';
                }
            }
            if (k < 17) {
                k = 17;
            }
            post = 0;
            while (k-- > 0) {
                post = (post + dig[k] * (1 << 17)) / 10;
            }
            post = (post + 1) / 2;
        }
        source.push(t);
        val = val << 16 | post;
        return (neg ? -val : val);
    }

}
