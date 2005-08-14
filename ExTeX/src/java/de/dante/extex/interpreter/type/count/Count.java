/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.count;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class represents a long integer value.
 * It is used for instance as count register.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.22 $
 */
public class Count implements Serializable, FixedCount {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant <tt>ONE</tt> contains the count register with the value 1.
     * This count register is in fact immutable.
     */
    public static final Count ONE = new ImmutableCount(1);

    /**
     * The constant <tt>THOUSAND</tt> contains the count register with the
     * value 1000.
     * This count register is in fact immutable.
     */
    public static final Count THOUSAND = new ImmutableCount(1000);

    /**
     * The constant <tt>ZERO</tt> contains the count register with the value 0.
     * This count register is in fact immutable.
     */
    public static final Count ZERO = new ImmutableCount(0);

    /**
     * parse a token stream for a count value.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return a new Count instance with the value acquired
     *
     * @throws InterpreterException in case of an error
     */
    public static Count parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Count(scanCount(context, source, typesetter));
    }

    /**
     * Scan the input stream for a count value.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter to use for conversion
     *
     * @return the value of the count
     *
     * @throws InterpreterException in case of an error
     */
    public static long scanCount(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Token t = source.getNonSpace(context);

        if (t == null) {
            throw new EofException(null);
        }

        if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code != null && code instanceof CountConvertible) {
                return ((CountConvertible) code).convertCount(context, source,
                        typesetter);
            }
        }
        source.push(t);

        return source.scanInteger(context, typesetter);
    }

    /**
     * The field <tt>value</tt> contains the value of the count register.
     */
    private long value = 0;

    /**
     * Creates a new object.
     *
     * @param count the reference to be copied
     */
    public Count(final FixedCount count) {

        super();
        this.value = count.getValue();
    }

    /**
     * Creates a new object.
     *
     * @param value the value
     */
    public Count(final long value) {

        super();
        this.value = value;
    }

    /**
     * Add a long to the value.
     * This operation modifies the value.
     *
     * @param val the value to add to
     */
    public void add(final long val) {

        value += val;
    }

    /**
     * Divide the value by a long.
     * This operation modifies the value.
     *
     * @param denom the denominator to divide by
     *
     * @throws GeneralException in case of a division by zero
     */
    public void divide(final long denom) throws GeneralException {

        if (denom == 0) {
            throw new ArithmeticOverflowException("");
        }

        value /= denom;
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#eq(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean eq(final FixedCount count) {

        return count.getValue() == value;
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#ge(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean ge(final FixedCount count) {

        return value >= count.getValue();
    }

    /**
     * Getter for the localizer.
     * The localizer is initialized from the name of the Count class.
     *
     * @return the localizer
     */
    protected Localizer getLocalizer() {

        return LocalizerFactory.getLocalizer(Count.class.getName());
    }

    /**
     * Getter for the value
     *
     * @return the value
     */
    public long getValue() {

        return value;
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#gt(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean gt(final FixedCount count) {

        return value > count.getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#le(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean le(final FixedCount count) {

        return value <= count.getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#lt(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean lt(final FixedCount count) {

        return value < count.getValue();
    }

    /**
     * Multiply the value with a factor.
     * This operation modifies the value.
     *
     * @param factor the factor to multiply with
     */
    public void multiply(final long factor) {

        value *= factor;
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#ne(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean ne(final FixedCount count) {

        return value != count.getValue();
    }

    /**
     * Setter for the value.
     *
     * @param l the new value
     *
     * @see #setValue(long)
     */
    public void set(final long l) {

        value = l;
    }

    /**
     * Setter for the value.
     *
     * @param l the new value
     *
     * @see #set(long)
     */
    public void setValue(final long l) {

        value = l;
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactely the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @return the printable representation
     *
     * @see #toString(StringBuffer)
     */
    public String toString() {

        return Long.toString(value);
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactely the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @param sb the target string buffer
     *
     * @see #toString()
     */
    public void toString(final StringBuffer sb) {

        sb.append(value);
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactely the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @param context the interpreter context
     *
     * @return the Tokens representing this instance
     *
     * @throws InterpreterException in case of an error
     */
    public Tokens toToks(final Context context) throws InterpreterException {

        return new Tokens(context, Long.toString(value));
    }

}