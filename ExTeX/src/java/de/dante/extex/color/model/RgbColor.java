/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

import de.dante.extex.color.ColorVisitor;
import de.dante.extex.interpreter.context.Color;
import de.dante.util.exception.GeneralException;

/**
 * This class implements a color specification in RGB mode with an alpha
 * channel.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class RgbColor implements Color {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>red</tt> contains the red value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int red;

    /**
     * The field <tt>green</tt> contains the green value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int green;

    /**
     * The field <tt>blue</tt> contains the blue value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int blue;

    /**
     * The field <tt>alpha</tt> contains the alpha channel of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int alpha;

    /**
     * Creates a new object.
     *
     * @param red the red channel
     * @param green the green channel
     * @param blue the blue channel
     * @param alpha the alpha channel
     */
    protected RgbColor(final int red, final int green, final int blue,
            final int alpha) {

        super();
        this.red = (red < 0 ? 0 : red < MAX_VALUE ? red : MAX_VALUE);
        this.green = (green < 0 ? 0 : green < MAX_VALUE ? green : MAX_VALUE);
        this.blue = (blue < 0 ? 0 : blue < MAX_VALUE ? blue : MAX_VALUE);
        this.alpha = (alpha < 0 ? 0 : alpha < MAX_VALUE ? alpha : MAX_VALUE);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {

        if (!(obj instanceof RgbColor)) {
            return false;
        }
        RgbColor other = (RgbColor) obj;
        return red == other.getRed() && green == other.getGreen()
                && blue == other.getBlue() && alpha == other.getAlpha();
    }

    /**
     * Getter for the red value.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the red value.
     */
    public int getRed() {

        return red;
    }

    /**
     * Getter for the green value.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the green value.
     */
    public int getGreen() {

        return green;
    }

    /**
     * Getter for the blue value.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the blue value.
     */
    public int getBlue() {

        return blue;
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#getAlpha()
     */
    public int getAlpha() {

        return alpha;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return (red >> 1) | (green >> 2) | (blue >> 3) | (alpha >> 4);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "RGB<" + Integer.toHexString(red) + " "
                + Integer.toHexString(green) + " "
                + Integer.toHexString(blue) + " alpha=" + (float) alpha
                / Color.MAX_VALUE + " >";
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#visit(
     *      de.dante.extex.color.ColorVisitor,
     *      java.lang.Object)
     */
    public Object visit(final ColorVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitRgb(this, value);
    }

}