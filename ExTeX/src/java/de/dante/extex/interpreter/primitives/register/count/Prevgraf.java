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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\prevgraf</code>.
 *
 * <doc name="prevgraf">
 * <h3>The Primitive <tt>\prevgraf</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;prevgraf&rang;
 *       &rarr; <tt>\prevgraf</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \prevgraf  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class Prevgraf extends CountPrimitive {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Prevgraf(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.count.AbstractCount#getKey(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    protected String getKey(final Context context, final TokenSource source) {

        return getName();
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

        String key = getKey(context, source);
        source.getKeyword(context, "by");

        long value = Count.scanCount(context, source, null)
                + context.getCount(key).getValue();

        if (value < 0) {
            throw new HelpingException(getLocalizer(), "TTP.BadPrevGraf",
                    printableControlSequence(context), Long.toString(value));
        }
        context.setCount(key, value, prefix.isGlobal());
        prefix.clearGlobal();
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

        String key = getKey(context, source);
        source.getOptionalEquals(context);

        long value = Count.scanCount(context, source, typesetter);
        if (value < 0) {
            throw new HelpingException(getLocalizer(), "TTP.BadPrevGraf",
                    printableControlSequence(context), Long.toString(value));
        }
        context.setCount(key, value, prefix.isGlobal());
        prefix.clearGlobal();
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

        String key = getKey(context, source);
        source.getKeyword(context, "by");

        long value = Count.scanCount(context, source, null);

        if (value == 0) {
            throw new ArithmeticOverflowException(
                    printableControlSequence(context));
        }

        value = context.getCount(key).getValue() / value;
        if (value < 0) {
            throw new HelpingException(getLocalizer(), "TTP.BadPrevGraf",
                    printableControlSequence(context), Long.toString(value));
        }
        context.setCount(key, value, prefix.isGlobal());
        prefix.clearGlobal();
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

        String key = getKey(context, source);
        source.getKeyword(context, "by");

        long value = Count.scanCount(context, source, null);
        value *= context.getCount(key).getValue();
        if (value < 0) {
            throw new HelpingException(getLocalizer(), "TTP.BadPrevGraf",
                    printableControlSequence(context), Long.toString(value));
        }
        context.setCount(key, value, prefix.isGlobal());
        prefix.clearGlobal();
    }

}
