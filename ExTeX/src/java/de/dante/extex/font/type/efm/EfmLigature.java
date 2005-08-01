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

package de.dante.extex.font.type.efm;

import java.io.Serializable;

/**
 * Class for a ligature.
 *
 * <pre>
 *  <ligature letter-id="108" letter="l" lig-id="15" />
 * </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EfmLigature implements Serializable {

    /**
     * the id
     */
    private int id;

    /**
     * the letter
     */
    private String letter;

    /**
     * the ligature id
     */
    private int ligid;

    /**
     * Returns the id.
     * @return Returns the id.
     */
    public int getId() {

        return id;
    }

    /**
     * Returns the id as String.
     * @return Returns the id as String.
     */
    public String getIdasString() {

        return String.valueOf(id);
    }

    /**
     * Set the id.
     * @param i The id to set.
     */
    public void setId(final int i) {

        id = i;
    }

    /**
     * Set the id (as String).
     * @param i The id to set.
     */
    public void setId(final String i) {

        try {
            id = Integer.parseInt(i);
        } catch (Exception e) {
            // use default
            id = -1;
        }
    }

    /**
     * Returns the letter.
     * @return Returns the letter.
     */
    public String getLetter() {

        return letter;
    }

    /**
     * Set the letter.
     * @param l The letter to set.
     */
    public void setLetter(final String l) {

        letter = l;
    }

    /**
     * Returns the ligid.
     * @return Returns the ligid.
     */
    public int getLigid() {

        return ligid;
    }

    /**
     * Set the ligid.
     * @param i The ligid to set.
     */
    public void setLigid(final int i) {

        ligid = i;
    }

    /**
     * Set the ligid (as String).
     * @param i The ligid to set.
     */
    public void setLigid(final String i) {

        try {
            ligid = Integer.parseInt(i);
        } catch (Exception e) {
            // use default
            ligid = -1;
        }
    }
}
