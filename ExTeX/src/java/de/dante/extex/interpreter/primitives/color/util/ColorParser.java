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

package de.dante.extex.interpreter.primitives.color.util;

import de.dante.extex.color.model.ColorFactory;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.color.ColorConvertible;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.interpreter.type.scaled.ScaledNumber;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a parser for color specifications.
 * Several color models are supported.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public final class ColorParser {

    /**
     * This internal interface is used to describe the parsers for the differnt
     * color models.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.2 $
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
     * The field <tt>CMYK_MODE</tt> contains the parser for a cmyk color.
     */
    private static final ColorMode CMYK_MODE = new ColorMode() {

        /**
         * @see de.dante.extex.interpreter.primitives.color.ColorPrimitive.ColorMode#parse()
         */
        public Color parse(final Context context, final TokenSource source,
                final int alpha, final String name) throws InterpreterException {

            int c = scanColorComponent(context, source, name);
            int m = scanColorComponent(context, source, name);
            int y = scanColorComponent(context, source, name);
            int k = scanColorComponent(context, source, name);
            return ColorFactory.getCmyk(c, m, y, k, alpha);
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
                final int alpha, final String name) throws InterpreterException {

            int gray = scanColorComponent(context, source, name);
            return ColorFactory.getGray(gray, alpha);
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
                final int alpha, final String name) throws InterpreterException {

            int h = scanColorComponent(context, source, name);
            int s = scanColorComponent(context, source, name);
            int v = scanColorComponent(context, source, name);
            return ColorFactory.getHsv(h, s, v, alpha);
        }

    };

    /**
     * The field <tt>RGB_MODE</tt> contains the parser for a rgb color.
     */
    private static final ColorMode RGB_MODE = new ColorMode() {

        /**
         * @see de.dante.extex.interpreter.primitives.color.ColorPrimitive.ColorMode#parse()
         */
        public Color parse(final Context context, final TokenSource source,
                final int alpha, final String name) throws InterpreterException {

            int r = scanColorComponent(context, source, name);
            int g = scanColorComponent(context, source, name);
            int b = scanColorComponent(context, source, name);
            return ColorFactory.getRgb(r, g, b, alpha);
        }

    };

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060528L;

    /**
     * Parse a color specification made up of a color constant for one of the
     * supported color models or a control sequence which is bound to
     * color convertible code.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param name the name of the invoking primitive
     *
     * @return th color found
     *
     * @throws InterpreterException in case of an error
     */
    public static Color parseColor(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String name) throws InterpreterException {

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
            color = ColorParser.parseColorConstant(context, source, typesetter,
                    name);
        }
        if (color == null) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(ColorParser.class.getName()), "MissingColor");
        }

        return color;
    }

    /**
     * Parse a color specification made up of a color constant for one of the
     * supported color models.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param name the name of the invoking primitive
     *
     * @return the color found
     *
     * @throws InterpreterException in case of an error
     */
    public static Color parseColorConstant(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String name) throws InterpreterException {

        int alpha = 0;
        ColorMode mode = RGB_MODE;

        for (;;) {
            if (source.getKeyword(context, "alpha")) {
                alpha = scanColorComponent(context, source, name);
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
            throw new EofException(name);
        } else if (!(t instanceof LeftBraceToken)) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(ColorParser.class.getName()),
                    "MissingLeftBrace");
        }

        Color color = mode.parse(context, source, alpha, name);
        t = source.getNonSpace(context);
        if (!(t instanceof RightBraceToken)) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(ColorParser.class.getName()),
                    "MissingRightBrace");
        }
        return color;
    }

    /**
     * Scan a color component and translate it into a color value.
     *
     * @param context the interpreter context
     * @param source the token source
     * @param name the name of the primitive for error messages
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

        long cc = ScaledNumber.scanFloat(context, source, t);
        if (cc < 0 || cc > ScaledNumber.ONE) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(ColorParser.class.getName()), "IllegalValue",
                    Long.toString(cc));
        }
        return (int) (cc * Color.MAX_VALUE / GlueComponent.ONE);
    }

    /**
     * Creates a new object.
     */
    private ColorParser() {

    }

}
