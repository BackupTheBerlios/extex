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

package de.dante.extex.interpreter.primitives.math.fraction;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.math.delimiter.AbstractTeXDelimiter;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.math.NoadConsumer;
import de.dante.extex.typesetter.type.math.MathDelimiter;

/**
 * This class provides an implementation for the primitive
 * <code>\overwithdelims</code>.
 *
 * <doc name="overwithdelims">
 * <h3>The Primitive <tt>\overwithdelims</tt></h3>
 * <p>
 *  The math primitive <tt>\over</tt> arranges that the material in the math
 *  group before it is typeset above the material after the primitive.
 *  The two parts are separated by a line of the default width which is taken
 *  from the font parameters.
 *  If the width is less than 0pt then no rule is drawn but the given height is
 *  left blank.
 *  The construction is enclosed in the delimiters given
 * </p>
 * <p>
 *  If several primitives of type <tt>\above</tt>, <tt>\abovewithdelims</tt>,
 *  <tt>\atop</tt>, <tt>\atopwithdelims</tt>, <tt>\over</tt>, or
 *  <tt>\overwithdelims</tt> are encountered in the same math group then the
 *  result is ambiguous and an error is raised.
 * </p>
 * <p>
 *  If the primitive is used outside of math mode then an error is raised.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;overwithdelims&rang;
 *       &rarr; &lang;math material&rang; <tt>\overwithdelims</tt> ... &lang;math material&rang; </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    {a\overwithdelims\delimiter"123456\delimiter"123456 b}  </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.16 $
 */
public class Overwithdelims extends AbstractTeXDelimiter {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060417L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Overwithdelims(final String name) {

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
        MathDelimiter del1 = parseDelimiter(context, source, typesetter, getName());
        MathDelimiter del2 = parseDelimiter(context, source, typesetter, getName());

        nc.switchToFraction(del1, del2, null, context.getTypesettingContext());
    }

}
