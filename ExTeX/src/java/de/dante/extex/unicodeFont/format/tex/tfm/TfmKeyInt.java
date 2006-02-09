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

package de.dante.extex.unicodeFont.format.tex.tfm;

import java.io.Serializable;

/**
 * TFM: key-value-container.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class TfmKeyInt implements Serializable {

    /**
     * key.
     */
    private int key;

    /**
     * int-value.
     */
    private int val;

    /**
     * Create a new object.
     *
     * @param k the key
     * @param v the value
     */
    TfmKeyInt(final int k, final int v) {

        key = k;
        val = v;
    }

    /**
     * Returns the key.
     * @return Returns the key.
     */
    public int getKey() {

        return key;
    }

    /**
     * Returns the val.
     * @return Returns the val.
     */
    public int getVal() {

        return val;
    }
}
