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

package de.dante.extex.interpreter.primitives.math.delimiter;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.math.MathDelimiter;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.math.NoadConsumer;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.extex.typesetter.type.noad.RadicalNoad;

/**
 * This class provides an implementation for the primitive
 * <code>\oradical</code>.
 *
 * <doc name="oradical">
 * <h3>The Math Primitive <tt>\oradical</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;oradical&rang;
 *       &rarr; <tt>\oradical</tt> ...  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \oradical\delimiter"123456{a^2 + b^2}  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Oradical extends AbstractOmegaDelimiter {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Oradical(final String name) {

        super(name);
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

        NoadConsumer nc = getListMaker(context, typesetter);
        MathDelimiter delcode = parseDelimiter(context, source, typesetter,
                getName());
        Noad noad = nc.scanNoad(prefix, context, source, typesetter, getName());
        nc.add(new RadicalNoad(delcode, noad, context.getTypesettingContext()));
    }

}