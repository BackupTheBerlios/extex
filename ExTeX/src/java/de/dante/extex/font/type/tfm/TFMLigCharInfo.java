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
 * TFM-LigCharInfo
 * <p>
 * Container for Character information for character with associated
 * lig/kern program.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class TFMLigCharInfo extends TFMCharInfo {

    /**
     * Create a new object
     *
     * @param w character width.
     * @param h character height.
     * @param d character depth.
     * @param i character italic correction.
     * @param s lig/kern program starting index.
     */
    public TFMLigCharInfo(final TFMFixWord w, final TFMFixWord h,
            final TFMFixWord d, final TFMFixWord i, final int s) {

        super(w, h, d, i);
        start = s;
    }

    /**
     * Index of the starting instruction of lig/kern program 
     * in the <code>ligKernTable</code>.
     */
    private int start;

    /**
     * @return start index of lig/kern program.
     */
    public int ligKernStart() {

        return start;
    }

}