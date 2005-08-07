/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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
 */

package de.dante.extex.font.type.afm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.font.type.efm.EfmReader;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * ModifiableFount for AFM.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class ModifiableFountAFM implements ModifiableFount, Serializable {

    /**
     * Default unitsperem (type1)
     */
    public static final int DEFAULTUNITSPEREM = 1000;

    /**
     * permille factor for scale factor
     */
    private static final int PERMILLE_FACTOR = 1000;

    /**
     * Create a new object.
     *
     * @param fk        the fount key
     * @param tfmfont   the tfm font
     */
    public ModifiableFountAFM(final FountKey fk, final AfmFont afmfont) {

        fountkey = fk;
        font = afmfont;

        calculateSize();
        //setFontDimenValues();
    }

    /**
     * the fount key
     */
    private FountKey fountkey;

    /**
     * the afm font
     */
    private AfmFont font;

    /**
     * property-map
     */
    private Map property = new HashMap();

    /**
     * calcualte the sizes
     */
    private void calculateSize() {

//        designsize = new Dimen((long) (font.getDefaultsize() * Dimen.ONE));
//
//        // scale factor = 0 -> 1000
//        if (fountkey.getScale() == null || fountkey.getScale().getValue() == 0) {
//            fountkey.setScale(new Count(PERMILLE_FACTOR));
//        }
//
//        // calculate actualsize
//        if (fountkey.getSize() == null) {
//            actualsize = new Dimen((long) (designsize.getValue()
//                    * fountkey.getScale().getValue() / PERMILLE_FACTOR));
//        } else {
//            actualsize = new Dimen((long) (fountkey.getSize().getValue()
//                    * fountkey.getScale().getValue() / PERMILLE_FACTOR));
//        }
    }

    /**
     * the actual font size
     */
    private Dimen actualsize;

    /**
     * the design font size
     */
    private Dimen designsize;


    
    /**
     * @see de.dante.extex.font.type.ModifiableFount#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(final String key, final String value) {

    }

    /**
     * @see de.dante.extex.font.type.ModifiableFount#setFontDimen(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

    }

    /**
     * @see de.dante.extex.font.type.Fount#getGlyph(de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getSpace()
     */
    public Glue getSpace() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEm()
     */
    public Dimen getEm() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEx()
     */
    public Dimen getEx() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontDimen(java.lang.String)
     */
    public Dimen getFontDimen(final String key) {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getProperty(java.lang.String)
     */
    public String getProperty(final String key) {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontName()
     */
    public String getFontName() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getCheckSum()
     */
    public int getCheckSum() {

        return 0;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getBoundingBox()
     */
    public BoundingBox getBoundingBox() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getLetterSpacing()
     */
    public Glue getLetterSpacing() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getDesignSize()
     */
    public Dimen getDesignSize() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getActualSize()
     */
    public Dimen getActualSize() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontKey()
     */
    public FountKey getFontKey() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontByteArray()
     */
    public FontByteArray getFontByteArray() {
    
        return null;
    }
}
