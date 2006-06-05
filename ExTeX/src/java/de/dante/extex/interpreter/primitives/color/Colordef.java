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

package de.dante.extex.interpreter.primitives.color;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.color.util.ColorParser;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\colordef</code>.
 *
 * <doc name="colordef">
 * <h3>The Primitive <tt>\colordef</tt></h3>
 * <p>
 *  The primitive <tt>\colordef</tt> defines a color variable and assigns it to
 *  a control sequence. The color is initialized with a given color &ndash;
 *  either a color constant or a color variable.
 * </p>
 * <p>
 *  The control sequence can later be used wherever a color is expected.
 * </p>
 * <p>
 *  The primitive <tt>\colordef</tt> constitutes an assignment. Thus the
 *  count register <tt>\globaldefs</tt> and the token register
 *  <tt>\afterassignment</tt> interact with it as for each assignment.
 * </p>
 * <p>
 *  The primitive can be prefixed with the <tt>\global</tt> flag. In this case
 *  the definition is performed globally. Otherwise the control sequence holds
 *  the color value in the current group only. It is reset to the previous
 *  value when the group is ended.
 * </p>
 * <p>
 *  The color variable can be manipulated by assigning new colors to it. The
 *  assignment is accomplished by specifying the new value after an optional
 *  equals sign. Note that the assignment can not be prefixed by a
 *  <tt>\global</tt> modifier since the scope has already been specified in the
 *  declaration with <tt>\colordef</tt>.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;colordef&rang;
 *      &rarr; &lang;prefix&rang; <tt>\colordef</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *       &lang;control sequence&rang;} &lang;color&rang;
 *
 *    &lang;prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt>   </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \colordef\col alpha .1234 rgb {.2 .3 .4}  </pre>
 *  <p>
 *  </p>
 *  <pre class="TeXSample">
 *    \global\colordef\col\color  </pre>
 *  <p>
 *  </p>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Colordef extends AbstractAssignment {

    /**
     * This class carries a color value for storing it as code in the context.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.2 $
     */
    private class ColorCode extends AbstractColor {

        /**
         * The field <tt>serialVersionUID</tt> contains the version number for
         * serialization.
         */
        protected static final long serialVersionUID = 20060528L;

        /**
         * The field <tt>color</tt> contains the color.
         */
        private Color color;

        /**
         * Creates a new object.
         *
         * @param color the color
         * @param name the name of the primitive
         */
        public ColorCode(final Color color, final String name) {

            super(name);
            this.color = color;
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

            color = ColorParser.parseColor(context, source, typesetter,
                    getName());
        }

        /**
         * @see de.dante.extex.interpreter.type.color.ColorConvertible#convertColor(
         *      de.dante.extex.interpreter.context.Context,
         *      de.dante.extex.interpreter.TokenSource,
         *      de.dante.extex.typesetter.Typesetter)
         */
        public Color convertColor(final Context context,
                final TokenSource source, final Typesetter typesetter)
                throws InterpreterException {

            return color;
        }

    }

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    protected static final long serialVersionUID = 20060528L;

    /**
     * Creates a new object.
     *
     * @param name the name of the primitive for tracing
     */
    public Colordef(final String name) {

        super(name);
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

        CodeToken cs = source.getControlSequence(context);
        source.getOptionalEquals(context);
        Color color = ColorParser.parseColor(context, source, typesetter,
                getName());
        context.setCode(cs, new ColorCode(color, cs.getName()), prefix
                .clearGlobal());
    }

}
