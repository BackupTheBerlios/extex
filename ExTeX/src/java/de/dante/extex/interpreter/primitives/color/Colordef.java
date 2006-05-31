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
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.color.util.ColorParser;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.color.ColorConvertible;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\colordef</code>.
 *
 * <doc name="colordef">
 * <h3>The Primitive <tt>\colordef</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;colordef&rang;
 *      &rarr; &lang;prefix&rang; <tt>\colordef</tt> &lang;...&rang;   </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \colordef\col alpha 1234 rgb {\r \b \g}  </pre>
 *  <p>
 *  </p>
 *  <pre class="TeXSample">
 *    \colordef\col\color{\r \b \g}  </pre>
 *  <p>
 *  </p>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Colordef extends AbstractAssignment {

    /**
     * This inner class carries a color value for storing it as a code in
     * the context.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private class CC extends AbstractColor {

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
        public CC(final Color color, final String name) {

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
        Token t = source.getNonSpace(context);
        Color color = null;
        if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code instanceof ColorConvertible) {
                color = ((ColorConvertible) code).convertColor(context, source,
                        typesetter);
            }
        } else {
            source.push(t);
            color = ColorParser.parseColor(context, source, typesetter,
                    getName());
        }

        if (color == null) {
            throw new HelpingException(getLocalizer(), "MissingColor");
        }

        context.setCode(cs, new CC(color, cs.getName()), prefix.clearGlobal());
    }

}
