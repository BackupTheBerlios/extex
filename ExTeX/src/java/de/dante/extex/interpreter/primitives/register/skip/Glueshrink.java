/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.skip;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\glueshrink</code>.
 *
 * <doc name="glueshrink">
 * <h3>The Primitive <tt>\glueshrink</tt></h3>
 * <p>
 *  The primitive <tt>\glueshrink</tt> translates a shrink part of a glue
 *  value into a length. The shrink order is stripped and just the size is
 *  preserved. The unit is changed to pt. For instance, if the value considered
 *  is 8pt minus 1.23 fil then <tt>\glueshrink</tt> returns 1.23 pt.
 * </p>
 * <p>
 *  The primitive <tt>\glueshrink</tt> can be used wherever a length is
 *  expected. The primitive is also applicable to <tt>\the</tt>.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;glueshrink&rang;
 *      &rarr; <tt>\glueshrink</tt> &lang;glue&rang; </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \glueshrink\skip1  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Glueshrink extends AbstractCode
        implements
            DimenConvertible,
            Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Glueshrink(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.DimenConvertible#convertDimen(
     *     de.dante.extex.interpreter.context.Context,
     *     de.dante.extex.interpreter.TokenSource,
     *     de.dante.extex.typesetter.Typesetter)
     */
    public long convertDimen(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Glue glue = new Glue(source, context, typesetter);
        return glue.getShrink().getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, (new Dimen(convertDimen(context, source,
                typesetter))).toString());
    }
}