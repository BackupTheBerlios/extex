/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ColorImpl implements Color {

    /**
     * The constant <tt>MAX_VALUE</tt> contains the maximal values for all
     * channels.
     */
    private static final int MAX_VALUE = 0xffff;

    /**
     * The constant <tt>BLACK</tt> contains the color black.
     */
    public static final Color BLACK = new ColorImpl(MAX_VALUE, MAX_VALUE,
            MAX_VALUE, 0);

    /**
     * The constant <tt>WHITE</tt> contains the color white.
     */
    public static final Color WHITE = new ColorImpl(0, 0, 0, 0);

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
    public ColorImpl(final int theRed, final int theGreen, final int theBlue,
            final int theAlpha) {

        super();
        this.red = (theRed < 0 ? 0 : theRed < MAX_VALUE ? theRed : MAX_VALUE);
        this.green = (theGreen < 0 ? 0 : theGreen < MAX_VALUE ? theGreen
                : MAX_VALUE);
        this.blue = (theBlue < 0 ? 0 : theBlue < MAX_VALUE ? theBlue
                : MAX_VALUE);
        this.alpha = (theAlpha < 0 ? 0 : theAlpha < MAX_VALUE ? theAlpha
                : MAX_VALUE);
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#getR()
     */
    public int getR() {

        return red;
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#getG()
     */
    public int getG() {

        return green;
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#getB()
     */
    public int getB() {

        return blue;
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#getAlpha()
     */
    public int getAlpha() {

        return alpha;
    }

}