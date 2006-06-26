/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.count.util;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.InitializableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.arithmetic.Advanceable;
import de.dante.extex.interpreter.type.arithmetic.Divideable;
import de.dante.extex.interpreter.type.arithmetic.Multiplyable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an object which acts like a count register.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class IntegerCode extends AbstractAssignment
        implements
            CountConvertible,
            Theable,
            Multiplyable,
            Divideable,
            Advanceable,
            ExpandableCode,
            InitializableCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060607L;

    /**
     * The field <tt>value</tt> contains the value stored in this object.
     */
    private long value;

    /**
     * Creates a new object.
     *
     * @param name the first name of the primitive
     */
    public IntegerCode(final String name) {

        super(name);
        this.value = 0;
    }

    /**
     * Creates a new object.
     *
     * @param name the first name of the primitive
     * @param value the initial value
     */
    public IntegerCode(final String name, final long value) {

        super(name);
        this.value = value;
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Advanceable#advance(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void advance(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.getKeyword(context, "by");
        Count v = Count.parse(context, source, typesetter);
        value += v.getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.getOptionalEquals(context);
        Count v = Count.parse(context, source, typesetter);
        value = v.getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return value;
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Divideable#divide(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void divide(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.getKeyword(context, "by");
        Count v = Count.parse(context, source, typesetter);
        if (v.eq(Count.ZERO)) {
            throw new ArithmeticOverflowException(
                    printableControlSequence(context));
        }
        value /= v.getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.push(new Tokens(context, value));
    }

    /**
     * Getter for value.
     *
     * @return the value
     */
    public long getValue() {

        return this.value;
    }

    /**
     * @see de.dante.extex.interpreter.type.InitializableCode#init(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void init(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        value = Count.scanInteger(context, source, typesetter);
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Multiplyable#multiply(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void multiply(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.getKeyword(context, "by");
        Count v = Count.parse(context, source, typesetter);
        value *= v.getValue();
    }

    /**
     * Setter for value.
     *
     * @param value the value to set
     */
    public void setValue(long value) {

        this.value = value;
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, value);
    }

}
