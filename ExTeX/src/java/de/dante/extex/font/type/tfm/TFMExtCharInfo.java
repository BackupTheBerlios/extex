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

package de.dante.extex.font.type.tfm;

/**
 * TFM-ExtCharInfo
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TFMExtCharInfo extends TFMCharInfo {

    /**
     * Create a new object
     *
     * @param w character width.
     * @param h character height.
     * @param d character depth.
     * @param i character italic correction.
     * @param t top part character code.
     * @param m middle part character code.
     * @param b bottom part character code.
     * @param r repeatable part character code.
     */
    public TFMExtCharInfo(final TFMFixWord w, final TFMFixWord h,
            final TFMFixWord d, final TFMFixWord i, final short t,
            final short m, final short b, final short r) {

        super(w, h, d, i);
        top = t;
        mid = m;
        bot = b;
        rep = r;
    }

    /**
     * top part chracter code
     */
    private short top;

    /**
     * middle part chracter code
     */
    private short mid;

    /**
     * bottom part chracter code
     */
    private short bot;

    /**
     * repeatable part chracter code
     */
    private short rep;

    /**
     * @return code of top part character or <code>NOCHARCODE</code> if
     * there is no top part.
     */
    public short extTop() {

        return top;
    }

    /**
     * @return code of middle part character or <code>NOCHARCODE</code>
     * if there is no middle part.
     */
    public short extMid() {

        return mid;
    }

    /**
     * @return code of bottom part character or <code>NOCHARCODE</code>
     * if there is no bottom part.
     */
    public short extBot() {

        return bot;
    }

    /**
     * @return code of repeatable part character or <code>NOCHARCODE</code>
     * if there is no repeatable part.
     */
    public short extRep() {

        return rep;
    }
}

