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
 * This class implements a color specification in grayscale mode with an alpha
 * channel.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class GrayscaleColor implements Color {

    /**
     * The constant <tt>MAX_VALUE</tt> contains the maximal values for all
     * channels.
     */
    public static final int MAX_VALUE = 0xffff;

    /**
     * The constant <tt>BLACK</tt> contains the color black.
     */
    public static final Color BLACK = new GrayscaleColor(MAX_VALUE, 0);

    /**
     * The constant <tt>WHITE</tt> contains the color white.
     */
    public static final Color WHITE = new GrayscaleColor(0, 0);

    /**
     * The field <tt>alpha</tt> contains the alpha channel of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int alpha;

    /**
     * The field <tt>gray</tt> contains the gray value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int gray;

    /**
     * Creates a new object.
     *
     * @param thegray the gray channel
     * @param theAlpha the alpha channel
     */
    public GrayscaleColor(final int thegray, final int theAlpha) {

        super();
        this.gray = (thegray < 0 ? 0 : thegray < MAX_VALUE
                ? thegray
                : MAX_VALUE);
        this.alpha = (theAlpha < 0 ? 0 : theAlpha < MAX_VALUE
                ? theAlpha
                : MAX_VALUE);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {

        if (!(obj instanceof GrayscaleColor)) {
            return false;
        }
        GrayscaleColor other = (GrayscaleColor) obj;
        return gray == other.getGray() && alpha == other.getAlpha();
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#getAlpha()
     */
    public int getAlpha() {

        return alpha;
    }

    /**
     * Getter for the gray value.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the gray value.
     */
    public int getGray() {

        return gray;
    }
}