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

package de.dante.extex.interpreter.type.dimen;

import java.io.Serializable;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.util.GeneralException;

/**
 * This class implements the dimen value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class Dimen extends GlueComponent implements Serializable, FixedDimen {

    /**
     * The constant <tt>ONE_INCH</tt> contains the immutable dimen register
     * representing the length of 1&nbsp;in.
     */
    public static final Dimen ONE_INCH = new ImmutableDimen(ONE * 7227 / 100);

    /**
     * The constant <tt>ONE_PT</tt> contains the immutable dimen register
     * representing the length of 1&nbsp;pt.
     */
    public static final Dimen ONE_PT = new ImmutableDimen(ONE);

    /**
     * The constant <tt>ZERO_PT</tt> contains the immutable dimen register
     * representing the length of 0&nbsp;pt.
     */
    public static final Dimen ZERO_PT = new ImmutableDimen(0);

    /**
     * Creates a new object.
     * The length stored in it is initialized to 0&nbsp;pt.
     */
    public Dimen() {

        super();
    }

    /**
     * Creates a new object from a token stream.
     *
     * <doc type="syntax" name="dimen">
     * This method parses the following syntactic entity:
     * <pre class="syntax">
     *   &lang;dimen&rang; </pre>
     * ...
     * </doc>
     *
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     *
     * @throws GeneralException in case of an error
     */
    public Dimen(final Context context, final TokenSource source)
            throws GeneralException {

        super(context, source, false);
    }

    /**
     * Creates a new object.
     * This method makes a new instance of the class with the same value as
     * the given instance. I.e. it acts like clone().
     *
     * @param value the value to imitate
     */
    public Dimen(final FixedDimen value) {

        super(value == null ? 0 : value.getValue());
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
     * This method makes a new instance of the class with the given value.
     *
     * @param value the value to set
     */
    public Dimen(final long value) {

        super(value);
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
    public void add(final FixedDimen d) {

        setValue(getValue() + d.getValue());
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
     * @throws HelpingException in case of a division by 0, i.e. if
     * denom is 0.
     */
    public void divide(final long denom) throws HelpingException {

        if (denom == 0) {
            throw new HelpingException("TTP.ArithOverflow");
        }
        setValue(getValue() / denom);
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
     *  <code>null</code>.
     */
    public void max(final FixedDimen d) {

        long val = d.getValue();
        if (val > getValue()) {
            setValue(val);
        }
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
     * Negate the value of the argument to the current value.
     * This operation modifies the instance.
     *
     * <p>
     * |<i>this</i>| :=  - |<i>this</i>|
     * </p>
     */
    public void negate() {

        setValue(-getValue());
    }

    /**
     * Set the value from the data gathered by parsing a token source.
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.interpreter.type.glue.GlueComponent#set(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void set(final Context context, final TokenSource source)
            throws GeneralException {

        set(context, source, true);
    }

    /**
     * Setter for the value.
     * The order of the arument is ignored.
     *
     * @param d the new value
     */
    public void set(final FixedGlueComponent d) {

        set(d.getValue());
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
    public void subtract(final FixedDimen d) {

        setValue(getValue() - d.getValue());
    }

}