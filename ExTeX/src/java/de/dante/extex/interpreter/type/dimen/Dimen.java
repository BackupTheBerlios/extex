/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.dimen;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.type.dimen.parser.LengthParser;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class implements the dimen value. This is a length with fixed point
 * arithmetic.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.36 $
 */
public class Dimen extends GlueComponent implements Serializable, FixedDimen {

    /**
     * The constant <tt>ONE_INCH</tt> contains the immutable dimen register
     * representing the length of 1&nbsp;in.
     */
    public static final ImmutableDimen ONE_INCH = new ImmutableDimen(
            ONE * 7227 / 100);

    /**
     * The constant <tt>ONE_PT</tt> contains the immutable dimen register
     * representing the length of 1&nbsp;pt.
     */
    public static final ImmutableDimen ONE_PT = new ImmutableDimen(ONE);

    /**
     * The constant <tt>ONE_SP</tt> contains the immutable dimen register
     * representing the length of 1&nbsp;scaled point.
     */
    public static final ImmutableDimen ONE_SP = new ImmutableDimen(1);

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060513L;

    /**
     * The constant <tt>ZERO_PT</tt> contains the immutable dimen register
     * representing the length of 0&nbsp;pt.
     */
    public static final ImmutableDimen ZERO_PT = new ImmutableDimen(0);

    /**
     * Creates a new object from a token stream.
     *
     * <doc type="syntax" name="dimen">
     * This method parses the following syntactic entity:
     *
     *  <pre class="syntax">
     *    &lang;dimen&rang;
     *      &rarr; &lang;float&rang; &lang;dimen unit&rang;
     *       |  &lang;float&rang; <tt>true</tt> &lang;dimen unit&rang;
     *       |  &lang;dimen variable&rang;
     *
     *    &lang;float&rang;
     *      &rarr; [+-]? [0-9]+
     *      |  [+-]? [0-9]+[.][0-9]*
     *      |  [+-]? [.][0-9]+
     *
     *    &lang;dimen unit&rang;
     *      &rarr; pt | in | sp
     *      |  mm | cm | dm | km
     *      |  dd | cc | bp  </pre>
     * </doc>
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     * @param typesetter the typesetter
     *
     * @return a new instance with the value acquired
     *
     * @throws InterpreterException in case of an error
     */
    public static Dimen parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return LengthParser.parse(context, source, typesetter);
    }

    /**
     * Creates a new object.
     * The length stored in it is initialized to 0&nbsp;pt.
     */
    public Dimen() {

        super();
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
    public Dimen(final long value) {

        super(value);
    }

    /**
     * Set the value of this dimen to the absolute value of a given value.
     *
     * @param d the given value
     */
    public void abs(final FixedDimen d) {

        long v = d.getValue();
        setValue(v < 0 ? -v : v);
    }

    /**
     * Add the value of the argument to the current value.
     * This operation modifies the instance.
     *
     * <p>
     * |<i>this</i>| &rarr; |<i>this</i>| + |<i>d</i>|
     * </p>
     *
     * @param d the Dimen to add
     */
    public void add(final FixedDimen d) {

        setValue(getValue() + d.getValue());
    }

    /**
     * Add the value of the argument to the current value.
     * This operation modifies the instance.
     *
     * <p>
     * |<i>this</i>| &rarr; |<i>this</i>| + |<i>d</i>|
     * </p>
     *
     * @param d the value to add in sp
     */
    public void add(final long d) {

        if (d != 0) {
            setValue(getValue() + d);
        }
    }

    /**
     * Divide the current value with a given number.
     * This operation modifies this instance.
     *
     * <p>
     * |<i>this</i>| &rarr; |<i>this</i>| / <i>denom</i>
     * </p>
     *
     * @param denom denominator to divide by
     *
     * @throws ArithmeticOverflowException in case of a division by 0, i.e. if
     *  denom is 0.
     */
    public void divide(final long denom) throws ArithmeticOverflowException {

        if (denom == 0) {
            throw new ArithmeticOverflowException("");
        }
        setValue(getValue() / denom);
    }

    /**
     * Getter for the localizer.
     * The localizer is initialized from the name of the Dimen class.
     *
     * @return the localizer
     */
    protected Localizer getLocalizer() {

        return LocalizerFactory.getLocalizer(Dimen.class);
    }

    /**
     * Test for a value of zero.
     *
     * @return <code>true</code> iff the value is 0
     */
    public boolean isZero() {

        return getValue() == 0;
    }

    /**
     * Sets the value of the dimen to the maximum of the value already stored
     * and a given argument.
     *
     * <i>|this| = max(|this|, |d|)</i>
     *
     * @param d the other dimen
     */
    public void max(final FixedDimen d) {

        long val = d.getValue();
        if (val > getValue()) {
            setValue(val);
        }
    }

    /**
     * Sets the value of the dimen to the minimum of the value already stored
     * and a given argument.
     *
     * <i>|this| = min(|this|, |d|)</i>
     *
     * @param d the other dimen
     */
    public void min(final FixedDimen d) {

        long val = d.getValue();
        if (val < getValue()) {
            setValue(val);
        }
    }

    /**
     * Multiply the current value with a given number.
     * This operation modifies this instance.
     *
     * <p>
     * |<i>this</i>| &rarr; |<i>this</i>| * <i>factor</i>
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
     * |<i>this</i>| &rarr;  - |<i>this</i>|
     * </p>
     */
    public void negate() {

        setValue(-getValue());
    }

    /**
     * Setter for the value.
     * The order of the argument is ignored.
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
     * |<i>this</i>| &rarr; |<i>this</i>| - |<i>d</i>|
     * </p>
     *
     * @param d the Dimen to subtract
     */
    public void subtract(final FixedDimen d) {

        setValue(getValue() - d.getValue());
    }

}