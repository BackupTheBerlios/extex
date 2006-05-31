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

import de.dante.extex.color.ColorUtil;
import de.dante.extex.color.ColorVisitor;
import de.dante.extex.interpreter.context.Color;
import de.dante.util.exception.GeneralException;

/**
 * This class implements a color specification in CMYK mode with an alpha
 * channel.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class CmykColor implements Color {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

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
     * @param cyan the cyan channel
     * @param magenta the magenta channel
     * @param yellow the yellow channel
     * @param black the black channel
     * @param alpha the alpha channel
     */
    protected CmykColor(final int cyan, final int magenta, final int yellow,
            final int black, final int alpha) {

        super();
        this.cyan = (cyan < 0 ? 0 : cyan < MAX_VALUE ? cyan : MAX_VALUE);
        this.magenta = (magenta < 0 ? 0 : magenta < MAX_VALUE
                ? magenta
                : MAX_VALUE);
        this.yellow = (yellow < 0 ? 0 : yellow < MAX_VALUE ? yellow : MAX_VALUE);
        this.black = (black < 0 ? 0 : black < MAX_VALUE ? black : MAX_VALUE);
        this.alpha = (alpha < 0 ? 0 : alpha < MAX_VALUE ? alpha : MAX_VALUE);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {

        if (!(obj instanceof CmykColor)) {
            return false;
        }
        CmykColor other = (CmykColor) obj;
        return cyan == other.getCyan() && magenta == other.getMagenta()
                && yellow == other.getYellow() && black == other.getBlack()
                && alpha == other.getAlpha();
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#getAlpha()
     */
    public int getAlpha() {

        return alpha;
    }

    /**
     * Getter for the black component.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the black component
     */
    public int getBlack() {

        return black;
    }

    /**
     * Getter for the cyan component.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the cyan component
     */
    public int getCyan() {

        return cyan;
    }

    /**
     * Getter for the magenta component.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the magenta component
     */
    public int getMagenta() {

        return magenta;
    }

    /**
     * Getter for the yellow component.
     * It has a value in the range from 0 to {@link #MAX_VALUE MAX_VALUE}.
     *
     * @return the yellow component
     */
    public int getYellow() {

        return yellow;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return (cyan >> 1) | (magenta >> 2) | (yellow >> 3) | (black >> 4)
                | (alpha >> 5);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        ColorUtil.formatAlpha(sb, alpha);
        sb.append("cmyk {");
        ColorUtil.formatComponent(sb, cyan);
        sb.append(" ");
        ColorUtil.formatComponent(sb, magenta);
        sb.append(" ");
        ColorUtil.formatComponent(sb, yellow);
        sb.append(" ");
        ColorUtil.formatComponent(sb, black);
        sb.append("}");
        return sb.toString();
    }

    /**
     * @see de.dante.extex.interpreter.context.Color#visit(
     *      de.dante.extex.color.ColorVisitor,
     *      java.lang.Object)
     */
    public Object visit(final ColorVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitCmyk(this, value);
    }
}