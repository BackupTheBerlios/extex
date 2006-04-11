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

package de.dante.extex.color;

import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.HsvColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.interpreter.context.Color;

/**
 * This implementation of a color converter does no conversions at all. It is a
 * dummy which forces that colors are always given in the target color space.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class NoColorConverter implements ColorConverter {

    /**
     * Creates a new object.
     */
    public NoColorConverter() {

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

        }

        return null;
    }

}
