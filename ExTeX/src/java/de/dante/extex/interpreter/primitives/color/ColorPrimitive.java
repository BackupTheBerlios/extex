/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\color</code>.
 *
 * <doc name="color">
 * <h3>The Primitive <tt>\color</tt></h3>
 * <p>
 *  The primitive <tt>\color</tt> sets the current color value to the value
 *  given. The value can be any color specification for one of the supported
 *  color models.
 * </p>
 * <p>
 *  The color models of <logo>ExTeX</logo> use components of two bytes. This
 *  means that values from 0 to 65535 can be stored in each component. The
 *  external representation is a floating point number in the range from 0.0 to
 *  1.0.
 * </p>
 * <p>
 *  The color models of <logo>ExTeX</logo> support aa alpha channel.
 * </p>
 *
 * <h4>The RGB Color Model</h4>
 * <p>
 *  The RGB color model provides three values for the red, green, and blue
 *  channel. Each is given as floating point number from 0.0 to 1.0.
 * </p>
 *
 * <h4>The CMYK Color Model</h4>
 * <p>
 *  The CMYK color model provides four values for cyan, magenta, yellow, and
 *  black channel. Each is given as floating point number from 0.0 to 1.0.
 * </p>
 *
 * <h4>The Grayscale Model</h4>
 * <p>
 *  The gray-scale color model provides one value for the gray channel.
 *  It is given as floating point number from 0.0 to 1.0.
 * </p>
 *
 * <h4>The HSV Color Model</h4>
 * <p>
 *  The HSV color model provides three values for the hue, saturation, and value
 *  channel. Each is given as floating point number from 0.0 to 1.0.
 * </p>
 *
 * <h4>The Alpha Channel</h4>
 * <p>
 *  The alpha channel determines the opactness of the color. A value of 0 means
 *  that the given color completely overwrites the underlying texture. A value
 *  of 1.0 is the maximal admissible alpha value. In this case the color is
 *  in fact invisible. In between the background shines through to the degree
 *  of the alpha value.
 * </p>
 * <p>
 *  Note that the alpha channel may not be supported by any output device. In
 *  such a case it is up to the back-end driver to make best use of the alpha
 *  value or ignore it at all.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;color&rang;
 *      &rarr; &lang;prefix&rang; <tt>\color</tt> &lang;alpha&rang; &lang;color&rang;
 *
 *    &lang;prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt>
 *
 *    &lang;alpha&rang;
 *      &rarr;
 *       |  <tt>alpha</tt> &lang;number&rang;
 *
 *    &lang;color&rang;
 *      &rarr; <tt>{</tt> &lang;color value&rang; &lang;color value&rang; &lang;color value&rang; <tt>}</tt> 
 *       |  <tt>rgb</tt> <tt>{</tt> &lang;color value&rang; &lang;color value&rang; &lang;color value&rang; <tt>}</tt> 
 *       |  <tt>gray</tt> <tt>{</tt> &lang;color value&rang; <tt>}</tt> 
 *       |  <tt>cmyk</tt> <tt>{</tt> &lang;color value&rang; &lang;color value&rang; &lang;color value&rang; &lang;color value&rang; <tt>}</tt> 
 *       |  <tt>hsv</tt> <tt>{</tt> &lang;color value&rang; &lang;color value&rang; &lang;color value&rang; <tt>}</tt> 
 *       |  &lang;color convertible&rang;
 *
 *    &lang;color value&rang;
 *      &rarr; &lang;number&rang;  </pre>
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
 *  </p>
 *  <pre class="TeXSample">
 *    \color rgb {1 .2 .3333}  </pre>
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
 * @version $Revision: 1.15 $
 */
public class ColorPrimitive extends AbstractColor {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060528L;

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

        Color color = ColorParser.parseColor(context, source, typesetter,
                getName());
        try {
            context.set(color, prefix.clearGlobal());
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.color.ColorConvertible#convertColor(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Color convertColor(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return context.getTypesettingContext().getColor();
    }

}
