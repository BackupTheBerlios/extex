/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font;

import java.io.Serializable;

import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;

/**
 * Font-key-class.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class FountKey implements Serializable {

    /**
     * The name of the font
     */
    private final String name;

    /**
     * The size of the font
     */
    private final Dimen size;

    /**
     * The scale factor of the font.
     */
    private final Count scale;

    /**
     * The glue for letterspace
     */
    private Glue letterspaced;

    /**
     * ligature on/off
     */
    private boolean ligatures;

    /**
     * kerning on/off
     */
    private boolean kerning;

    /**
     * Create a new object.
     * @param n     the name
     * @param s     the size
     * @param sf    the scale factor
     * @param ls    the letterspace
     * @param lig   the ligature
     * @param kern  the kerning
     */
    public FountKey(final String n, final Dimen s, final Count sf,
            final Glue ls, final boolean lig, final boolean kern) {

        name = n;
        size = s;
        scale = sf;
        letterspaced = ls;
        ligatures = lig;
        kerning = kern;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return name + size + scale + letterspaced + String.valueOf(ligatures)
                + String.valueOf(kerning);
    }
}
