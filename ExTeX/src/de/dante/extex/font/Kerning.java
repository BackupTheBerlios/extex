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

package de.dante.extex.font;

import de.dante.extex.interpreter.type.dimen.Dimen;

/**
 * Kerning
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class Kerning {

    /**
     * The id for the glyph
     */
    private String id = "";

    /**
     * the name of the glpyh
     */
    private String name = "";

    /**
     * The id for the glyph (left)
     */
    private String idleft = "";

    /**
     * the name of the glpyh (left)
     */
    private String nameleft = "";

    /**
     * The kerning-size
     */
    private Dimen size = new Dimen(0);

    /**
     * Create a new object.
     */
    public Kerning() {

    }

    /**
     * Create a new object.
     *
     * @param gid       the id
     * @param gname     the name
     * @param gsize     the size
     */
    public Kerning(final String gid, final String gname, final Dimen gsize) {

        id = gid;
        name = gname;
        size = gsize;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {

        return id;
    }

    /**
     * @param gid The id to set.
     */
    public void setId(final String gid) {

        id = gid;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {

        return name;
    }

    /**
     * @param gname The name to set.
     */
    public void setName(final String gname) {

        name = gname;
    }

    /**
     * @return Returns the size.
     */
    public Dimen getSize() {

        return size;
    }

    /**
     * @param gsize The size to set as <code>Dimen</code>.
     */
    public void setSize(final Dimen gsize) {

        size = gsize;
    }

    /**
     * @param gsize         The size to set as <code>String</code>.
     * @param em            The em-size.
     * @param unitsperem    The unit per em.
     */
    public void setSize(final String gsize, final Dimen em, final int unitsperem) {

        try {
            float fsize = Float.parseFloat(gsize);
            size = new Dimen((long) (fsize * em.getValue() / unitsperem));
        } catch (Exception e) {
            // use default
            size = new Dimen(0);
        }
    }

    /**
     * @return Returns the idleft.
     */
    public String getIdleft() {

        return idleft;
    }

    /**
     * @param aidleft The idleft to set.
     */
    public void setIdleft(final String aidleft) {

        idleft = aidleft;
    }

    /**
     * @return Returns the nameleft.
     */
    public String getNameleft() {

        return nameleft;
    }

    /**
     * @param anameleft The nameleft to set.
     */
    public void setNameleft(final String anameleft) {

        nameleft = anameleft;
    }
}
