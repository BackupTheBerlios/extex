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

package de.dante.extex.interpreter.primitives.math.symbol;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.math.AbstractMathCode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.math.NoadConsumer;
import de.dante.extex.typesetter.type.noad.InnerNoad;
import de.dante.extex.typesetter.type.noad.Noad;

/**
 * This class provides an implementation for the primitive
 * <code>\mathinner</code>.
 *
 * <doc name="mathinner">
 * <h3>The Math Primitive <tt>\mathinner</tt></h3>
 * <p>
 *  The primitive <tt>\mathinner</tt> takes an argument and treats it as
 *  an inner symbol. It works in math mode only. The argument can either
 *  be a single letter of a math expression enclosed in braces.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;mathinner&rang;
 *       &rarr; <tt>\mathinner</tt>  &lang;formula&rang;
 *
 *    &lang;formula&rang;
 *       &rarr;  &lang;letter&rang;
 *         |  <tt>{</tt> &lang;math material&rang; <tt>}</tt>   </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \mathinner x </pre>
 *
 *  <pre class="TeXSample">
 *    \mathinner\mathchar"1234  </pre>
 *
 *  <pre class="TeXSample">
 *    \mathinner {abc} </pre>
 *
 *  <pre class="TeXSample">
 *    \mathinner{a^b}  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Mathinner extends AbstractMathCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Mathinner(final String name) {

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
        Noad noad = nc.scanNoad(prefix, context, source, typesetter,
                printableControlSequence(context));
        nc.add(new InnerNoad(noad, context.getTypesettingContext()));
    }

}
