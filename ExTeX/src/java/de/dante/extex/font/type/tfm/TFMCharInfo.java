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

package de.dante.extex.font.type.tfm;

/**
 * TFM-CharInfo
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class TFMCharInfo {

    /**
     * Symbolic constant for nonexistent character code
     */
    public static final short NOCHARCODE = -1;

    /**
     * Symbolic constant for index which is not valid
     */
    public static final int NOINDEX = -1;

    /**
     * Create a new object.
     *
     * @param w character width.
     * @param h character height.
     * @param d character depth.
     * @param i character italic correction.
     */
    public TFMCharInfo(final TFMFixWord w, final TFMFixWord h,
            final TFMFixWord d, final TFMFixWord i) {

        width = w;
        height = h;
        depth = d;
        italic = i;
    }

    /**
     * Character width
     */
    private TFMFixWord width;

    /**
     * Character height
     */
    private TFMFixWord height;

    /**
     * Character depth
     */
    private TFMFixWord depth;

    /**
     * Character italic correction
     */
    private TFMFixWord italic;

    /**
     * @return the character width.
     */
    public TFMFixWord getWidth() {

        return width;
    }

    /**
     * @return the character height.
     */
    public TFMFixWord getHeight() {

        return height;
    }

    /**
     * @return the character depth.
     */
    public TFMFixWord getDepth() {

        return depth;
    }

    /**
     * @return the character italic correction.
     */
    public TFMFixWord getItalic() {

        return italic;
    }

    /**
     * @return start index of lig/kern program or <code>NOINDEX</code>
     * if it has no lig/kern program associated.
     */
    public int ligKernStart() {

        return NOINDEX;
    }

    /**
     * @return next larger character code or <code>NOCHARCODE</code> if
     * there is no larger character.
     */
    public short nextChar() {

        return NOCHARCODE;
    }

    /**
     * Gets the character code for top part of extensible character.
     *
     * @return code of top part character or <code>NOCHARCODE</code> if
     * there is no top part or the character is not extensible.
     */
    public short extTop() {

        return NOCHARCODE;
    }

    /**
     * Gets the character code for middle part of extensible character.
     *
     * @return code of middle part character or <code>NOCHARCODE</code>
     * if there is no middle part or the character is not extensible.
     */
    public short extMid() {

        return NOCHARCODE;
    }

    /**
     * Gets the character code for bottom part of extensible character.
     *
     * @return code of bottom part character or <code>NOCHARCODE</code>
     * if there is no bottom part or the character is not extensible.
     */
    public short extBot() {

        return NOCHARCODE;
    }

    /**
     * Gets the character code for repeatable part of extensible character.
     *
     * @return code of repeatable part character or <code>NOCHARCODE</code>
     * if there is no repeatable part or the character is not extensible.
     */
    public short extRep() {

        return NOCHARCODE;
    }

}