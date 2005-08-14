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

package de.dante.extex.color.model;

import de.dante.extex.interpreter.context.Color;

/**
 * This factory can be used to acquire Color objects.
 * This factory is provided as a singleton in a utility class. Thus all
 * methods are implemented as static methods.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public final class ColorFactory {

    /**
     * The constant <tt>BLACK</tt> contains the color black.
     */
    public static final Color BLACK = new GrayscaleColor(0, 0);

    /**
     * The constant <tt>WHITE</tt> contains the color white.
     */
    public static final Color WHITE = new GrayscaleColor(Color.MAX_VALUE, 0);

    /**
     * Get a new color in the CMYK color model.
     *
     * @param cyan the cyan value
     * @param magenta the magenta value
     * @param yellow the yellow value
     * @param black the black value
     * @param alpha the alpha channel
     *
     * @return the new color
     */
    public static CmykColor getCmyk(final int cyan, final int magenta,
            final int yellow, final int black, final int alpha) {

        return new CmykColor(cyan, magenta, yellow, black, alpha);
    }

    /**
     * Get a new color in the grayscale color model.
     *
     * @param gray the gray value
     * @param alpha the alpha channel
     *
     * @return the new color
     */
    public static GrayscaleColor getGray(final int gray, final int alpha) {

        return new GrayscaleColor(gray, alpha);
    }

    /**
     * Get a new color in the HSV color model.
     *
     * @param hue the hue value
     * @param saturation the saturation value
     * @param value the value
     * @param alpha the alpha channel
     *
     * @return the new color
     */
    public static HsvColor getHsv(final int hue, final int saturation,
            final int value, final int alpha) {

        return new HsvColor(hue, saturation, value, alpha);
    }

    /**
     * Get a new color in the RGB color model.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     * @param alpha the alpha channel
     *
     * @return the new color
     */
    public static RgbColor getRgb(final int red, final int green,
            final int blue, final int alpha) {

        return new RgbColor(red, green, blue, alpha);
    }

    /**
     * Creates a new object.
     * This constructor is private to avoid instantiation for this utility class.
     */
    private ColorFactory() {

        super();
    }

}
