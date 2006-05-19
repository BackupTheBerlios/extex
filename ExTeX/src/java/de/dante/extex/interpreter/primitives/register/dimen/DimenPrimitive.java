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

package de.dante.extex.interpreter.primitives.register.dimen;

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
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\dimen</code>.
 * It sets the named dimen register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <doc name="dimen">
 * <h3>The Primitive <tt>\dimen</tt></h3>
 * <p>
 *  The primitive <tt>\dimen</tt> provides access to the dimen registers. Those
 *  registers contain length values.
 *  TODO gene documentation incomplete
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;dimen&rang;
 *      &rarr; &lang;prefix&rang; <tt>\dimen</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanRegisterName(Context,String)
 *        &lang;register name&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;}  &lang;dimen value&rang;
 *
 *    &lang;dimen value&rang;
 *      &rarr; &lang;dimen register&rang;
 *       |  &lang;float&rang; &lang;dimen unit&rang;
 *       |  &lang;float&rang; <tt>true</tt> &lang;dimen unit&rang;
 *
 *    &lang;float&rang;
 *      &rarr; [+-]?[0-9]*[.]?[0-9]*
 *
 *    &lang;dimen unit&rang;
 *      &rarr; <tt>sp</tt> | <tt>pt</tt> | <tt>in</tt>
 *       |  <tt>bp</tt> | <tt>mm</tt> | <tt>cm</tt> | <tt>dm</tt> | <tt>km</tt>
 *       |  <tt>dd</tt> | <tt>cc</tt> | <tt>pc</tt>
 *       |  <tt>nd</tt> | <tt>nc</tt>
 *       |  <tt>ex</tt> | <tt>em</tt>   </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \dimen1=12 pt </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.25 $
 */
public class DimenPrimitive extends AbstractDimen
        implements
            Advanceable,
            ExpandableCode,
            CountConvertible,
            DimenConvertible,
            Multiplyable,
            Divideable,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public DimenPrimitive(final String name) {

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

        String key = getKey(context, source);
        source.getKeyword(context, "by");

        Dimen d = Dimen.parse(context, source, typesetter);
        d.add(context.getDimen(key));
        context.setDimen(key, d, prefix.isGlobal());
        prefix.clearGlobal();
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
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

        Dimen dimen = Dimen.parse(context, source, typesetter);
        context.setDimen(key, dimen, prefix.isGlobal());
        prefix.clearGlobal();
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        String key = getKey(context, source);
        Dimen d = context.getDimen(key);
        return (d != null ? d.getValue() : 0);
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.DimenConvertible#convertDimen(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertDimen(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return convertCount(context, source, typesetter);
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

        Dimen d = new Dimen(context.getDimen(key).getValue() / value);
        context.setDimen(key, d, prefix.isGlobal());
        prefix.clearGlobal();
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

        String key = getKey(context, source);
        try {
            Tokens toks = context.getDimen(key).toToks(
                    context.getTokenFactory());
            source.push(toks);
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
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
        Dimen d = new Dimen(context.getDimen(key).getValue() * value);
        context.setDimen(key, d, prefix.isGlobal());
        prefix.clearGlobal();
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        String key = getKey(context, source);
        try {
            return context.getDimen(key).toToks(context.getTokenFactory());
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }

}
