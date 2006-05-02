/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides objects of type
 * {@link de.dante.extex.interpreter.type.dimen.Dimen Dimen} where all
 * assignment methods are redefined to produce a run-time exception.
 * Thus the object is in fact immutable.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class ImmutableDimen extends Dimen implements Serializable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060502L;

    /**
     * Creates a new object.
     *
     * @param value the value to be stored
     */
    public ImmutableDimen(final Dimen value) {

        super(value);
    }

    /**
     * Creates a new object.
     *
     * @param value the value to be stored
     */
    public ImmutableDimen(final long value) {

        super(value);
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.Dimen#add(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void add(final Dimen d) {

        throw new RuntimeException("Unable to add to an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.Dimen#divide(long)
     */
    public void divide(final long denom) {

        throw new RuntimeException("Unable to divide an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.Dimen#max(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void max(final Dimen d) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.Dimen#multiply(long)
     */
    public void multiply(final long factor) {

        throw new RuntimeException("Unable to multiply an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.glue.GlueComponent#set(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public void set(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.glue.GlueComponent#set(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter, boolean)
     */
    protected void set(final Context context, final TokenSource source,
            final Typesetter typesetter, final boolean fixed)
            throws InterpreterException {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.Dimen#subtract(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void subtract(final Dimen d) {

        throw new RuntimeException(
                "Unable to subtract from an immutable object");
    }

}
