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

package de.dante.extex.interpreter.primitives.register.box;

import java.io.Serializable;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\wd</code>.
 *
 * <doc name="wd">
 * <h3>The Primitive <tt>\wd</tt></h3>
 * <p>
 *  The primitive <tt>\wd</tt> refers to the width of a box register.
 *  It can be used in various contexts.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;wd&rang;
 *      &rarr; <tt>\wd</tt> {@linkplain
 *        de.dante.extex.interpreter.primitives.register.box.AbstractBox#getKey(Context,Source,String)
 *        &lang;box register name&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.type.dimen#Dimen(Context,TokenSource)
 *        &lang;dimen&rang;} </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \wd42  </pre>
 * </p>
 * </doc>
 *
 *
 * <pre>
 *  \advance\dimen12 \wd0
 *  \wd12=123pt
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.30 $
 */
public class Wd extends Setbox
        implements
            Serializable,
            ExpandableCode,
            Theable,
            CountConvertible,
            DimenConvertible {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Wd(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return convertDimen(context, source, typesetter);
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.DimenConvertible#convertDimen(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertDimen(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Box b = context.getBox(getKey(context, source, getName()));
        return (b == null ? 0 : b.getWidth().getValue());
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Box box = context.getBox(getKey(context, source, getName()));
        source.getOptionalEquals(context);
        Dimen d = Dimen.parse(context, source, typesetter);

        if (box != null) {
            box.setWidth(d);
        }
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

        source.push(the(context, source, typesetter));
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Box box = context.getBox(getKey(context, source, getName()));
        FixedDimen d = (box == null ? Dimen.ZERO_PT : box.getWidth());
        try {
            return d.toToks(context.getTokenFactory());
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

}
