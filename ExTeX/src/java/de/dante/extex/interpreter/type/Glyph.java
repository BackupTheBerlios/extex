/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.type;

/**
 * Glyph
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class Glyph {

    /**
     * the width of the glyph
     */
    private Dimen width = new Dimen(0);

    /**
     * the height of the glyph
     */
    private Dimen height = new Dimen(0);

    /**
     * the depth of the glyph
     */
    private Dimen depth = new Dimen(0);

    /**
     * the italic of the glyph
     */
    private float italic = 0.0f;

    /**
     * Create a new object.
     */
    public Glyph() {

    }

    /**
     * Create a new object.
     * @param height    the hight
     * @param depth     the depth
     * @param width     the width
     * @param italic    the italic
     */
    public Glyph(final Dimen height, final Dimen depth, final Dimen width,
            final float italic) {
        this.height = height;
        this.depth = depth;
        this.width = width;
        this.italic = italic;
    }

    /**
     * @return Returns the depth.
     */
    public Dimen getDepth() {
        return depth;
    }

    /**
     * @param depth The depth to set.
     */
    public void setDepth(final Dimen depth) {
        this.depth = depth;
    }

    /**
     * @return Returns the height.
     */
    public Dimen getHeight() {
        return height;
    }

    /**
     * @param height The height to set.
     */
    public void setHeight(final Dimen height) {
        this.height = height;
    }

    /**
     * @return Returns the italic.
     */
    public float getItalic() {
        return italic;
    }

    /**
     * @param italic The italic to set.
     */
    public void setItalic(final float italic) {
        this.italic = italic;
    }

    /**
     * @return Returns the width.
     */
    public Dimen getWidth() {
        return width;
    }

    /**
     * @param width The width to set.
     */
    public void setWidth(final Dimen width) {
        this.width = width;
    }
}
