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

package de.dante.extex.interpreter.context.color;

import de.dante.extex.interpreter.context.Color;

/**
 * This class implements a color specification in RGB mode with an alpha
 * channel.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class RgbColor implements Color {

    /**
     * The constant <tt>MAX_VALUE</tt> contains the maximal values for all
     * channels.
     */
    public static final int MAX_VALUE = 0xffff;

    /**
     * The constant <tt>BLACK</tt> contains the color black.
     */
    public static final Color BLACK = new RgbColor(MAX_VALUE, MAX_VALUE,
            MAX_VALUE, 0);

    /**
     * The constant <tt>WHITE</tt> contains the color white.
     */
    public static final Color WHITE = new RgbColor(0, 0, 0, 0);

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
     * @param theRed the red channel
     * @param theGreen the green channel
     * @param theBlue the blue channel
     * @param theAlpha the alpha channel
     */
    public RgbColor(final int theRed, final int theGreen, final int theBlue,
            final int theAlpha) {

        super();
        this.red = (theRed < 0 ? 0 : theRed < MAX_VALUE ? theRed : MAX_VALUE);
        this.green = (theGreen < 0 ? 0 : theGreen < MAX_VALUE
                ? theGreen
                : MAX_VALUE);
        this.blue = (theBlue < 0 ? 0 : theBlue < MAX_VALUE
                ? theBlue
                : MAX_VALUE);
        this.alpha = (theAlpha < 0 ? 0 : theAlpha < MAX_VALUE
                ? theAlpha
                : MAX_VALUE);
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

}