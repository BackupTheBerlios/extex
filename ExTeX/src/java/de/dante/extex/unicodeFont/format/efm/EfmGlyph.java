/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.efm;

import java.util.HashMap;
import java.util.Map;

import de.dante.util.UnicodeChar;

/**
 * Class for the efm glyph.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EfmGlyph {

    /**
     * The unicode char.
     */
    private UnicodeChar uc;

    /**
     * The glyph number.
     */
    private int glyphNumber = -1;

    /**
     * The glyph name.
     */
    private String glyphName = "";

    /**
     * The width, height, depth and italic.
     */
    private EfmWHDI whdi;

    /**
     * The map for the ligature.
     */
    private Map ligature;

    /**
     * The map for the kerning.
     */
    private Map kerning;

    /**
     * Create a new object.
     */
    public EfmGlyph() {

        super();

        ligature = new HashMap();
        kerning = new HashMap();
    }

    /**
     * Returns the glyphName.
     * @return Returns the glyphName.
     */
    public String getGlyphName() {

        return glyphName;
    }

    /**
     * Set the glyphName.
     * @param aglyphName The glyphName to set.
     */
    public void setGlyphName(final String aglyphName) {

        glyphName = aglyphName;
    }

    /**
     * Returns the glyphNumber.
     * @return Returns the glyphNumber.
     */
    public int getGlyphNumber() {

        return glyphNumber;
    }

    /**
     * Set the glyphNumber.
     * @param aglyphNumber The glyphNumber to set.
     */
    public void setGlyphNumber(final int aglyphNumber) {

        glyphNumber = aglyphNumber;
    }

    /**
     * Returns the uc.
     * @return Returns the uc.
     */
    public UnicodeChar getUc() {

        return uc;
    }

    /**
     * Set the uc.
     * @param auc The uc to set.
     */
    public void setUc(final UnicodeChar auc) {

        uc = auc;
    }

    /**
     * Returns the whdi.
     * @return Returns the whdi.
     */
    public EfmWHDI getWhdi() {

        return whdi;
    }

    /**
     * Set the whdi.
     * @param awhdi The whdi to set.
     */
    public void setWhdi(final EfmWHDI awhdi) {

        whdi = awhdi;
    }

    /**
     * Add a ligature.
     * @param lig   The ligature.
     */
    public void addLigature(final EfmLigature lig) {

        ligature.put(lig.getLetterUc(), lig);
    }

    /**
     * Add a kerning.
     * @param kern  The kerning.
     */
    public void addKerning(final EfmKerning kern) {

        kerning.put(kern.getUnicodeChar(), kern);
    }

}
