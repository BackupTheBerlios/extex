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
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.color.ColorConvertible;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.scanner.type.LeftBraceToken;
import de.dante.extex.scanner.type.LetterToken;
import de.dante.extex.scanner.type.RightBraceToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\color</code>.
 *
 * <doc name="csname">
 * <h3>The Primitive <tt>\color</tt></h3>
 * <p>
 *  TODO missing documentation
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
 * @version $Revision: 1.2 $
 */
public class ColorPrimitive extends AbstractAssignment
        implements
            ColorConvertible {

    /**
     * The constant <tt>CMYK</tt> contains the indicator for the CMYK color model.
     */
    private static final int CMYK = 2;

    /**
     * The constant <tt>GRAY</tt> contains the indictor for the grayscale color
     * model.
     */
    private static final int GRAY = 3;

    /**
     * The constant <tt>HSV</tt> contains the indicator for the HSV color model.
     */
    private static final int HSV = 1;

    /**
     * The constant <tt>RGB</tt> contains the indicator for the RGB color model.
     */
    private static final int RGB = 0;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public ColorPrimitive(final String name) {

        super(name);
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
        int mode = RGB;

        Token t = source.getNonSpace(context);
        while (t instanceof LetterToken) {
            source.push(t);
            if (source.getKeyword(context, "alpha")) {
                alpha = scanColorComponent(context, source);
            } else if (source.getKeyword(context, "rgb")) {
                mode = RGB;
            } else if (source.getKeyword(context, "gray")) {
                mode = GRAY;
            } else if (source.getKeyword(context, "hsv")) {
                mode = HSV;
            } else if (source.getKeyword(context, "cmyk")) {
                mode = CMYK;
            }
            t = source.getToken(context);
        }
        if (t == null) {
            throw new EofException(getName());
        } else if (!(t instanceof LeftBraceToken)) {
            throw new HelpingException(getLocalizer(), "MissingLeftBrace");
        }

        Color color = null;
        switch (mode) {
            case RGB:
                int r = scanColorComponent(context, source);
                int g = scanColorComponent(context, source);
                int b = scanColorComponent(context, source);
                color = ColorFactory.getRgb(r, g, b, alpha);
                break;
            case HSV:
                int h = scanColorComponent(context, source);
                int s = scanColorComponent(context, source);
                int v = scanColorComponent(context, source);
                color = ColorFactory.getHsv(h, s, v, alpha);
                break;
            case GRAY:
                int gray = scanColorComponent(context, source);
                color = ColorFactory.getGray(gray, alpha);
                break;
            case CMYK:
                int c = scanColorComponent(context, source);
                int m = scanColorComponent(context, source);
                int y = scanColorComponent(context, source);
                int k = scanColorComponent(context, source);
                color = ColorFactory.getCmyk(c, m, y, k, alpha);
                break;
            default:
                throw new ImpossibleException("convertColor");
        }
        t = source.getNonSpace(context);
        if (!(t instanceof RightBraceToken)) {
            throw new HelpingException(getLocalizer(), "MissingRightBrace");
        }
        return color;
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
    }

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
    private int scanColorComponent(final Context context,
            final TokenSource source) throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException(getName());
        }

        return (int) (GlueComponent.scanFloat(context, source, t)
                * Color.MAX_VALUE / GlueComponent.ONE);
    }

}