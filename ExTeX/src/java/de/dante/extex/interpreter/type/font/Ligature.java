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

package de.dante.extex.interpreter.type.font;

/**
 * Ligature
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class Ligature {

    /**
     * The letter
     */
    private String letter = "";

    /**
     * The letter-id
     */
    private String letterid = "";

    /**
     * The ligature
     */
    private String lig = "";

    /**
     * The ligature-id
     */
    private String ligid = "";

    /**
     * Create a new object.
     */
    public Ligature() {

    }

    /**
     * @return Returns the letter.
     */
    public String getLetter() {

        return letter;
    }

    /**
     * @param let The letter to set.
     */
    public void setLetter(final String let) {

        letter = let;
    }

    /**
     * @return Returns the letterid.
     */
    public String getLetterid() {

        return letterid;
    }

    /**
     * @param letid The letterid to set.
     */
    public void setLetterid(final String letid) {

        letterid = letid;
    }

    /**
     * @return Returns the lig.
     */
    public String getLig() {

        return lig;
    }

    /**
     * @param li The lig to set.
     */
    public void setLig(final String li) {

        lig = li;
    }

    /**
     * @return Returns the ligid.
     */
    public String getLigid() {

        return ligid;
    }

    /**
     * @param liid The ligid to set.
     */
    public void setLigid(final String liid) {

        ligid = liid;
    }
}
