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

package de.dante.extex.color;

import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.ColorFactory;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.HsvColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.interpreter.context.Color;

/**
 * This implementation of a color converter is based on the formulas in the
 * color space FAQ.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class BasicColorConverter implements ColorConverter {

    /**
     * Creates a new object.
     */
    public BasicColorConverter() {

        super();
    }

    /**
     * Convert an arbitrary color to the CMYK model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the CMYK model or <code>null</code>
     *  if a conversion is not supported.
     *
     * @see de.dante.extex.color.ColorConverter#toCmyk(
     *      de.dante.extex.interpreter.context.Color)
     */
    public CmykColor toCmyk(final Color color) {

        if (color instanceof CmykColor) {
            return (CmykColor) color;

        } else if (color instanceof RgbColor) {
            RgbColor rgb = (RgbColor) color;
            int r = Color.MAX_VALUE - rgb.getRed();
            int g = Color.MAX_VALUE - rgb.getGreen();
            int b = Color.MAX_VALUE - rgb.getBlue();
            int black = (r > b ? (b > g ? g : b) : (r > g ? g : r));
            return ColorFactory.getCmyk(//
                    black == Color.MAX_VALUE ? 0 : (r - black)
                            / (Color.MAX_VALUE - black), //
                    black == Color.MAX_VALUE ? 0 : (g - black)
                            / (Color.MAX_VALUE - black), //
                    black == Color.MAX_VALUE ? 0 : (r - black)
                            / (Color.MAX_VALUE - black), //
                    black, rgb.getAlpha());

        }

        return null;
    }

    /**
     * Convert an arbitrary color to the RGB model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the RGB model or <code>null</code> if
     *  a conversion is not supported.
     *
     * @see de.dante.extex.color.ColorConverter#toGrayscale(
     *      de.dante.extex.interpreter.context.Color)
     */
    public GrayscaleColor toGrayscale(final Color color) {

        if (color instanceof GrayscaleColor) {
            return (GrayscaleColor) color;

        } else if (color instanceof RgbColor) {
            RgbColor rgb = (RgbColor) color;
            return ColorFactory.getGray((222 * rgb.getRed() + 707
                    * rgb.getGreen() + 713 * rgb.getBlue()) / 1000, rgb
                    .getAlpha());

        }

        return null;
    }

    /**
     * Convert an arbitrary color to the HSV model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the HSV model or <code>null</code> if
     *  a conversion is not supported.
     *
     * @see de.dante.extex.color.ColorConverter#toHsv(
     *      de.dante.extex.interpreter.context.Color)
     */
    public HsvColor toHsv(final Color color) {

        if (color instanceof HsvColor) {
            return (HsvColor) color;

        }

        return null;
    }

    /**
     * Convert an arbitrary color to the RGB model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the RGB model or <code>null</code> if
     *  a conversion is not supported.
     *
     * @see de.dante.extex.color.ColorConverter#toRgb(
     *      de.dante.extex.interpreter.context.Color)
     */
    public RgbColor toRgb(final Color color) {

        if (color instanceof RgbColor) {
            return (RgbColor) color;

        } else if (color instanceof CmykColor) {
            CmykColor cmyk = (CmykColor) color;
            int r = Color.MAX_VALUE - cmyk.getCyan()
                    * (Color.MAX_VALUE - cmyk.getBlack()) / Color.MAX_VALUE
                    + cmyk.getBlack();
            int g = Color.MAX_VALUE - cmyk.getMagenta()
                    * (Color.MAX_VALUE - cmyk.getBlack()) / Color.MAX_VALUE
                    + cmyk.getBlack();
            int b = Color.MAX_VALUE - cmyk.getYellow()
                    * (Color.MAX_VALUE - cmyk.getBlack()) / Color.MAX_VALUE
                    + cmyk.getBlack();
            return ColorFactory.getRgb((r < 0 ? 0 : r), //
                    (g < 0 ? 0 : g), //
                    (b < 0 ? 0 : b), //
                    cmyk.getAlpha());

        }

        return null;
    }
}
