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
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\gluestretchorder</code>.
 *
 * <doc name="gluestretchorder">
 * <h3>The Primitive <tt>\gluestretchorder</tt></h3>
 * <p>
 *  The primitive <tt>\gluestretchorder</tt> determines the order of the glue
 *  stretch component of the following glue specification.
 *  A fixed, non-stretchable glue returns the value 0.
 *  Glue with the order fil gives 1, fill gives 2, and filll gives 3.
 * </p>
 * <p>
 *  Note that the glue specification of 1&nbsp;fi returns also 1. This is due to
 *  the compatibility with <logo>eTeX</logo> which does not have this unit. This
 *  unit has been introduced by <logo>Omega</logo>.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;gluestretchorder&rang;
 *      &rarr; <tt>\gluestretchorder</tt> &lang;glue&rang; </pre>
 * </p>
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \gluestretchorder\skip1  </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Gluestretchorder extends AbstractCode
        implements
            CountConvertible,
            Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Gluestretchorder(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Glue glue = new Glue(source, context, typesetter);
        int order = glue.getStretch().getOrder();
        return (order < 2 ? order : order - 1);
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, Long.toString(convertCount(context, source,
                typesetter)));
    }
}