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

package de.dante.extex.interpreter.context.color;

import de.dante.extex.interpreter.context.Color;

/**
 * This class implements a color specification in HSV mode with an alpha
 * channel.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class CmykColor implements Color {

    /**
     * The constant <tt>MAX_VALUE</tt> contains the maximal values for all
     * channels.
     */
    private static final int MAX_VALUE = 0xffff;

    /**
     * The constant <tt>BLACK</tt> contains the color black.
     */
    public static final Color BLACK = new CmykColor(MAX_VALUE, MAX_VALUE,
            MAX_VALUE, MAX_VALUE, 0);

    /**
     * The constant <tt>WHITE</tt> contains the color white.
     */
    public static final Color WHITE = new CmykColor(0, 0, 0, 0, 0);

    /**
     * The field <tt>alpha</tt> contains the alpha channel of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int alpha;

    /**
     * The field <tt>black</tt> contains the black value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int black;

    /**
     * The field <tt>cyan</tt> contains the cyan value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int cyan;

    /**
     * The field <tt>magenta</tt> contains the magenta value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int magenta;

    /**
     * The field <tt>yellow</tt> contains the value yellow of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int yellow;

    /**
     * Creates a new object.
     *
     * @param thecyan the cyan channel
     * @param themagenta the magenta channel
     * @param theyellow the yellow channel
     * @param theBlack the black channel
     * @param theAlpha the alpha channel
     */
    public CmykColor(final int thecyan, final int themagenta,
            final int theyellow, final int theBlack, final int theAlpha) {

        super();
        this.cyan = (thecyan < 0 ? 0 : thecyan < MAX_VALUE
                ? thecyan
                : MAX_VALUE);
        this.magenta = (themagenta < 0 ? 0 : themagenta < MAX_VALUE
                ? themagenta
                : MAX_VALUE);
        this.yellow = (theyellow < 0 ? 0 : theyellow < MAX_VALUE
                ? theyellow
                : MAX_VALUE);
        this.black = (theBlack < 0 ? 0 : theBlack < MAX_VALUE
                ? theBlack
                : MAX_VALUE);
        this.alpha = (theAlpha < 0 ? 0 : theAlpha < MAX_VALUE
                ? theAlpha
                : MAX_VALUE);
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#getAlpha()
     */
    public int getAlpha() {

        return alpha;
    }

    /**
     * ...
     *
     * @return ...
     */
    public int getBlack() {

        return black;
    }

    /**
     * ...
     *
     * @return ...
     */
    public int getCyan() {

        return cyan;
    }

    /**
     * ...
     *
     * @return ...
     */
    public int getMagenta() {

        return magenta;
    }

    /**
     * ...
     *
     * @return ...
     */
    public int getYellow() {

        return yellow;
    }

}