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
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.HsvColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.interpreter.context.Color;

/**
 * This interface describes the possibilities of a color conversion.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface ColorConverter {

    /**
     * Convert an arbitrary color to the CMYK model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the CMYK model or <code>null</code>
     *  if a conversion is not supported.
     */
    CmykColor toCmyk(Color color);

    /**
     * Convert an arbitrary color to the RGB model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the RGB model or <code>null</code> if
     *  a conversion is not supported.
     */
    GrayscaleColor toGrayscale(Color color);

    /**
     * Convert an arbitrary color to the HSV model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the HSV model or <code>null</code> if
     *  a conversion is not supported.
     */
    HsvColor toHsv(Color color);

    /**
     * Convert an arbitrary color to the RGB model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the RGB model or <code>null</code> if
     *  a conversion is not supported.
     */
    RgbColor toRgb(Color color);
}