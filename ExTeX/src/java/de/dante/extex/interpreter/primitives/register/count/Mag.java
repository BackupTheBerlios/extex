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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.arithmetic.Advanceable;
import de.dante.extex.interpreter.type.arithmetic.Divideable;
import de.dante.extex.interpreter.type.arithmetic.Multiplyable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\mag</code>.
 * It sets the named count register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <doc name="mag">
 * <h3>The Primitive <tt>\mag</tt></h3>
 * <p>
 *  The primitive <tt>\mag</tt> provides a means to set the magnification
 *  factor for the current document. The primitive acts like a normal count
 *  register. The magnification factor is given in multiples of 1000. This means
 *  that the default value 1000 corresponds to an unmagnified output.
 * </p>
 * <p>
 *  The effect of the setting of the magnification factor is that all length
 *  values are multiplied with the magnification factor (divided by 1000). An
 *  exception are the <i>true</i> length values. This means a length of
 *  1&nbsp;pt at a magnification of 1200 is in effect 1.2&nbsp;pt long.
 *  Whereas a length of 1&nbsp;true&nbsp;pt remains unaffected by the
 *  magnification.
 * </p>
 * <p>
 *  The magnification can only changed once at the beginning of a run.
 * </p>
 * <p>
 *  An attempt to assign a non-positive number to <tt>\mag</tt> leads to an
 *  error.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;mag&rang;
 *      &rarr; <tt>\mag</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \mag=1600  </pre>
 * </doc>
 *
 * @see de.dante.extex.interpreter.type.arithmetic.Advanceable
 * @see de.dante.extex.interpreter.type.arithmetic.Divideable
 * @see de.dante.extex.interpreter.type.arithmetic.Multiplyable
 * @see de.dante.extex.interpreter.type.Theable
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public class Mag extends AbstractCount
        implements
            ExpandableCode,
            Advanceable,
            Multiplyable,
            Divideable,
            Theable,
            CountConvertible {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Mag(final String name) {

        super(name);
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

        long value = Count.scanCount(context, source, null);
        value += context.getMagnification();

        context.setMagnification(value);
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

        long value = Count.scanCount(context, source, typesetter);
        context.setMagnification(value);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return context.getMagnification();
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

        long value = Count.scanCount(context, source, null);

        if (value == 0) {
            throw new ArithmeticOverflowException(
                    printableControlSequence(context));
        }

        value = context.getMagnification() / value;
        context.setMagnification(value);
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

        source.push(new Tokens(context, Long.toString(context
                .getMagnification())));
    }

    /**
     * @see de.dante.extex.interpreter.type.InitializableCode#init(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void init(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            return;
        }
        source.push(t);
        long value = Count.scanCount(context, source, typesetter);
        context.setMagnification(value);
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

        long value = Count.scanCount(context, source, null);
        value *= context.getMagnification();
        context.setMagnification(value);
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, Long.toString(context.getMagnification()));
    }

}
