/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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
 * This class implements a color specification in HSV mode with an alpha
 * channel.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class HsvColor implements Color {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * The field <tt>hue</tt> contains the hue value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int hue;

    /**
     * The field <tt>saturation</tt> contains the saturation value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int saturation;

    /**
     * The field <tt>value</tt> contains the value value of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int value;

    /**
     * The field <tt>alpha</tt> contains the alpha channel of the color.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     */
    private int alpha;

    /**
     * Creates a new object.
     *
     * @param thehue the hue channel
     * @param thesaturation the saturation channel
     * @param thevalue the value channel
     * @param theAlpha the alpha channel
     */
    protected HsvColor(final int thehue, final int thesaturation,
            final int thevalue, final int theAlpha) {

        super();
        this.hue = (thehue < 0 ? 0 : thehue < MAX_VALUE ? thehue : MAX_VALUE);
        this.saturation = (thesaturation < 0 ? 0 : thesaturation < MAX_VALUE
                ? thesaturation
                : MAX_VALUE);
        this.value = (thevalue < 0 ? 0 : thevalue < MAX_VALUE
                ? thevalue
                : MAX_VALUE);
        this.alpha = (theAlpha < 0 ? 0 : theAlpha < MAX_VALUE
                ? theAlpha
                : MAX_VALUE);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {

        if (!(obj instanceof HsvColor)) {
            return false;
        }
        HsvColor other = (HsvColor) obj;
        return hue == other.getHue() && saturation == other.getSaturation()
                && value == other.getValue() && alpha == other.getAlpha();
    }

    /**
     * Getter for the hue value.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the hue value.
     */
    public int getHue() {

        return hue;
    }

    /**
     * Getter for the saturation value.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the saturation value.
     */
    public int getSaturation() {

        return saturation;
    }

    /**
     * Getter for the value.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the value.
     */
    public int getValue() {

        return value;
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

        return (hue >> 1) | (saturation >> 2) | (value >> 3) | (alpha >> 4);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "HSV<" + Integer.toHexString(hue) + " "
                + Integer.toHexString(saturation) + " "
                + Integer.toHexString(value) + " alpha=" + (float) alpha
                / Color.MAX_VALUE + " >";
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#visit(
     *      de.dante.extex.color.ColorVisitor,
     *      java.lang.Object)
     */
    public Object visit(final ColorVisitor visitor, final Object argument)
            throws GeneralException {

        return visitor.visitHsv(this, argument);
    }

}