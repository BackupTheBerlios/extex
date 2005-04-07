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
 * @version $Revision: 1.2 $
 */

public class FountKey implements Serializable {

    /**
     * The name of the font
     */
    private String name;

    /**
     * The size of the font
     */
    private Dimen size;

    /**
     * The scale factor of the font.
     */
    private Count scale;

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
     * Create a new object.
     * @param n     the name
     */
    public FountKey(final String n) {

        this(n, null, null, new Glue(0), false, false);
    }

    /**
     * Create a new object.
     * @param n     the name
     * @param s     the size
     */
    public FountKey(final String n, final Dimen s) {

        this(n, s, null, new Glue(0), false, false);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return name + size + scale + letterspaced + String.valueOf(ligatures)
                + String.valueOf(kerning);
    }

    /**
     * Returns the kerning.
     * @return Returns the kerning.
     */
    public boolean isKerning() {

        return kerning;
    }

    /**
     * Set the kerning.
     * @param k The kerning to set.
     */
    public void setKerning(final boolean k) {

        kerning = k;
    }

    /**
     * Returns the letterspaced.
     * @return Returns the letterspaced.
     */
    public Glue getLetterspaced() {

        return letterspaced;
    }

    /**
     * Set the letterspaced.
     * @param l The letterspaced to set.
     */
    public void setLetterspaced(final Glue l) {

        letterspaced = l;
    }

    /**
     * Returns the ligatures.
     * @return Returns the ligatures.
     */
    public boolean isLigatures() {

        return ligatures;
    }

    /**
     * Set the ligatures.
     * @param l The ligatures to set.
     */
    public void setLigatures(final boolean l) {

        ligatures = l;
    }

    /**
     * Returns the name.
     * @return Returns the name.
     */
    public String getName() {

        return name;
    }

    /**
     * Returns the scale.
     * @return Returns the scale.
     */
    public Count getScale() {

        return scale;
    }

    /**
     * Returns the size.
     * @return Returns the size.
     */
    public Dimen getSize() {

        return size;
    }

    /**
     * Set the name.
     * @param n The name to set.
     */
    public void setName(final String n) {

        name = n;
    }

    /**
     * Set the scale.
     * @param s The scale to set.
     */
    public void setScale(final Count s) {

        scale = s;
    }

    /**
     * Set the size.
     * @param s The size to set.
     */
    public void setSize(final Dimen s) {

        size = s;
    }
}
