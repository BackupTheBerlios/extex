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

package de.dante.extex.interpreter.primitives.color;

import de.dante.extex.color.model.ColorFactory;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.color.ColorConvertible;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\color</code>.
 *
 * <doc name="color">
 * <h3>The Primitive <tt>\color</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;color&rang;
 *      &rarr; &lang;prefix&rang; <tt>\color</tt> &lang;...&rang;   </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \color{\r \b \g}  </pre>
 *  <p>
 *  </p>
 *  <pre class="TeXSample">
 *    \color gray {\gray}  </pre>
 *  <p>
 *  </p>
 *  <pre class="TeXSample">
 *    \color rgb {\r \b \g}  </pre>
 *  <p>
 *  </p>
 *  <pre class="TeXSample">
 *    \color hsv {\h \s \v}  </pre>
 *  <p>
 *  </p>
 *  <pre class="TeXSample">
 *    \color alpha 500 rgb {\r \b \g} </pre>
 *  <p>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class ColorPrimitive extends AbstractAssignment
        implements
            ColorConvertible {

    /**
     * This internal interface is used to describe the parsers for the differnt
     * color models.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.11 $
     */
    private interface ColorMode {

        /**
         * Parse a color value.
         *
         * @param context the interpreter context
         * @param source the token source
         * @param alpha the alpha channel
         * @param name the name of the primitive
         *
         * @return the color found
         *
         * @throws InterpreterException in case of an error
         */
        Color parse(Context context, TokenSource source, int alpha, String name)
                throws InterpreterException;
    }

    /**
     * The field <tt>RGB_MODE</tt> contains the parser for a rgb color.
     */
    private static final ColorMode RGB_MODE = new ColorMode() {

        /**
         * @see de.dante.extex.interpreter.primitives.color.ColorPrimitive.ColorMode#parse()
         */
        public Color parse(final Context context, final TokenSource source,
                final int alpha, String name) throws InterpreterException {

            int r = scanColorComponent(context, source, name);
            int g = scanColorComponent(context, source, name);
            int b = scanColorComponent(context, source, name);
            return ColorFactory.getRgb(r, g, b, alpha);
        }

    };

    /**
     * The field <tt>HSV_MODE</tt> contains the parser for a hsv color.
     */
    private static final ColorMode HSV_MODE = new ColorMode() {

        /**
         * @see de.dante.extex.interpreter.primitives.color.ColorPrimitive.ColorMode#parse()
         */
        public Color parse(final Context context, final TokenSource source,
                final int alpha, String name) throws InterpreterException {

            int h = scanColorComponent(context, source, name);
            int s = scanColorComponent(context, source, name);
            int v = scanColorComponent(context, source, name);
            return ColorFactory.getHsv(h, s, v, alpha);
        }

    };

    /**
     * The field <tt>GRAY_MODE</tt> contains the parser for a grayscale color.
     */
    private static final ColorMode GRAY_MODE = new ColorMode() {

        /**
         * @see de.dante.extex.interpreter.primitives.color.ColorPrimitive.ColorMode#parse()
         */
        public Color parse(final Context context, final TokenSource source,
                final int alpha, String name) throws InterpreterException {

            int gray = scanColorComponent(context, source, name);
            return ColorFactory.getGray(gray, alpha);
        }

    };

    /**
     * The field <tt>CMYK_MODE</tt> contains the parser for a cmyk color.
     */
    private static final ColorMode CMYK_MODE = new ColorMode() {

        /**
         * @see de.dante.extex.interpreter.primitives.color.ColorPrimitive.ColorMode#parse()
         */
        public Color parse(final Context context, final TokenSource source,
                final int alpha, String name) throws InterpreterException {

            int c = scanColorComponent(context, source, name);
            int m = scanColorComponent(context, source, name);
            int y = scanColorComponent(context, source, name);
            int k = scanColorComponent(context, source, name);
            return ColorFactory.getCmyk(c, m, y, k, alpha);
        }

    };

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Scan a color component and translate it into a color value.
     *
     * @param context the interpreter context
     * @param source the token source
     *
     * @return the color component in units of Color.MAX_VALUE
     *
     * @throws InterpreterException in case of an error
     */
    private static int scanColorComponent(final Context context,
            final TokenSource source, final String name)
            throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException(name);
        }

        return (int) (GlueComponent.scanFloat(context, source, t)
                * Color.MAX_VALUE / GlueComponent.ONE);
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public ColorPrimitive(final String name) {

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

        Color color = convertColor(context, source, typesetter);
        try {
            context.set(color, prefix.isGlobal());
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
        prefix.setGlobal(false);
    }

    /**
     * @see de.dante.extex.interpreter.type.color.ColorConvertible#convertColor(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Color convertColor(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        int alpha = 0;
        ColorMode mode = RGB_MODE;

        for(;;) {
            if (source.getKeyword(context, "alpha")) {
                alpha = scanColorComponent(context, source, getName());
            } else if (source.getKeyword(context, "rgb")) {
                mode = RGB_MODE;
            } else if (source.getKeyword(context, "gray")) {
                mode = GRAY_MODE;
            } else if (source.getKeyword(context, "hsv")) {
                mode = HSV_MODE;
            } else if (source.getKeyword(context, "cmyk")) {
                mode = CMYK_MODE;
            } else {
                break;
            }
        }
        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException(getName());
        } else if (!(t instanceof LeftBraceToken)) {
            throw new HelpingException(getLocalizer(), "MissingLeftBrace");
        }

        Color color = mode.parse(context, source, alpha, getName());
        t = source.getNonSpace(context);
        if (!(t instanceof RightBraceToken)) {
            throw new HelpingException(getLocalizer(), "MissingRightBrace");
        }
        return color;
    }

}