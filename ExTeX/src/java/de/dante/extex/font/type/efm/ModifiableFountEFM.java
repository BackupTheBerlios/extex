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

package de.dante.extex.font.type.efm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.GlyphImpl;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * ModifiableFount for EFM.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class ModifiableFountEFM implements ModifiableFount, Serializable {

    /**
     * Default unitsperem (type1)
     */
    public static final int DEFAULTUNITSPEREM_TYPE1 = 1000;

    /**
     * Default unitsperem (ttf)
     */
    public static final int DEFAULTUNITSPEREM_TTF = 2048;

    /**
     * unitsperem
     */
    private int unitsperem = DEFAULTUNITSPEREM_TYPE1;

    /**
     * permille factor for scale factor
     */
    private static final int PERMILLE_FACTOR = 1000;

    /**
     * is is a tfm font with fixword?
     */
    private boolean isTFM = false;

    /**
     * Create a new object.
     *
     * @param fk        the fount key
     * @param tfmfont   the tfm font
     */
    public ModifiableFountEFM(final FountKey fk, final EfmReader efmfont) {

        fountkey = fk;
        font = efmfont;

        if ("tfm".equals(font.getType())) {
            unitsperem = DEFAULTUNITSPEREM_TYPE1;
            isTFM = true;
        }

        calculateSize();
        setFontDimenValues();
    }

    /**
     * calcualte the sizes
     */
    private void calculateSize() {

        designsize = new Dimen((long) (font.getDefaultsize() * Dimen.ONE));

        // scale factor = 0 -> 1000
        if (fountkey.getScale() == null || fountkey.getScale().getValue() == 0) {
            fountkey.setScale(new Count(PERMILLE_FACTOR));
        }

        // calculate actualsize
        if (fountkey.getSize() == null) {
            actualsize = new Dimen((long) (designsize.getValue()
                    * fountkey.getScale().getValue() / PERMILLE_FACTOR));
        } else {
            actualsize = new Dimen((long) (fountkey.getSize().getValue()
                    * fountkey.getScale().getValue() / PERMILLE_FACTOR));
        }
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
     * set the fontdimen values from the tfm param
     */
    private void setFontDimenValues() {

        Map map = font.getDimenmap();
        Iterator it = map.keySet().iterator();

        while (it.hasNext()) {
            String labelname = (String) it.next();
            float f = ((Float) map.get(labelname)).floatValue();
            if (isTFM) {
                TFMFixWord fw = new TFMFixWord();
                fw.setValue((long) f);
                f = (float) fw.toDouble();
            }
            Dimen d = new Dimen((long) (f * actualsize.getValue() / unitsperem));
            fontdimen.put(labelname, d);
        }
    }

    /**
     * the fount key
     */
    private FountKey fountkey;

    /**
     * the efm font
     */
    private EfmReader font;

    /**
     * property-map
     */
    private Map property = new HashMap();

    /**
     * @see de.dante.extex.font.type.ModifiableFount#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(final String key, final String value) {

        property.put(key, value);
    }

    /**
     * hash for fontdimen-keys
     */
    private HashMap fontdimen = new HashMap();

    /**
     * @see de.dante.extex.font.type.ModifiableFount#setFontDimen(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

        fontdimen.put(key, value);
    }

    /**
     * the map for the glyphs
     */
    private Map glyphmap = new HashMap();

    /**
     * @see de.dante.extex.font.type.Fount#getGlyph(de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        Glyph g = (Glyph) glyphmap.get(c);
        if (g == null) {
            g = new GlyphImpl();
            EfmGlyph efmglyph = (EfmGlyph) font.getGlyphmap().get(
                    String.valueOf(c.getCodePoint()));

            if (efmglyph != null) {
                g.setNumber(String.valueOf(efmglyph.getId()));
                if (efmglyph.getName() != null) {
                    g.setName(efmglyph.getName().replaceAll("/", ""));
                }
                if (isTFM) {
                    g
                            .setWidth(convertFixWordAsFloatToDimen(efmglyph
                                    .getWidth()));
                    g
                            .setDepth(convertFixWordAsFloatToDimen(efmglyph
                                    .getDepth()));
                    g.setHeight(convertFixWordAsFloatToDimen(efmglyph
                            .getHeight()));
                    g.setItalicCorrection(convertFixWordAsFloatToDimen(efmglyph
                            .getItalic()));
                } else {
                    g.setWidth(new Dimen(
                            (long) (efmglyph.getWidth() * Dimen.ONE)));
                    g.setDepth(new Dimen(
                            (long) (efmglyph.getDepth() * Dimen.ONE)));
                    g.setHeight(new Dimen(
                            (long) (efmglyph.getHeight() * Dimen.ONE)));
                    g.setItalicCorrection(new Dimen((long) (efmglyph
                            .getItalic() * Dimen.ONE)));
                }
                if (fountkey.isLigatures()) {

                    Map map = efmglyph.getLigature();
                    Iterator it = map.keySet().iterator();

                    while (it.hasNext()) {
                        String key = (String) it.next();
                        EfmLigature lig = (EfmLigature) map.get(key);
                        Ligature lv = new Ligature();
                        lv.setLetterid(lig.getIdasString());
                        lv.setLetter(lig.getLetter());
                        lv.setLigid(String.valueOf(lig.getLigid()));
                        g.addLigature(lv);
                    }
                }
                if (fountkey.isKerning()) {
                    Map map = efmglyph.getKerning();
                    Iterator it = map.keySet().iterator();

                    while (it.hasNext()) {
                        String key = (String) it.next();
                        EfmKerning kern = (EfmKerning) map.get(key);
                        Kerning kv = new Kerning();

                        kv.setId(kern.getIdasString());
                        kv.setName(kern.getCharacter());
                        if (isTFM) {
                            kv.setSize(convertFixWordAsFloatToDimen(kern
                                    .getSize()));
                        } else {
                            kv.setSize(new Dimen(
                                    (long) (kern.getSize() * Dimen.ONE)));

                        }
                        g.addKerning(kv);
                    }
                }
            }
        }
        return g;
    }

    /**
     * convert the fixword value (as float) to a dimen value
     * @param fw    the fixword value
     * @return  Returns the Dimen value.
     */
    private Dimen convertFixWordAsFloatToDimen(final float fw) {

        Dimen rt = new Dimen(0);
        try {
            long l = (long) (fw * actualsize.getValue() / TFMFixWord.FIXWORDDENOMINATOR);
            rt = new Dimen(l);
        } catch (Exception e) {
            // use default
            rt = new Dimen(0);
        }
        return rt;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getSpace()
     */
    public Glue getSpace() {

        // use actual-size for 'space'
        Glue rt = new Glue(actualsize);

        // glyph 'space' exists?
        Dimen spacedimen = (Dimen) fontdimen.get("SPACE");
        if (spacedimen != null) {
            rt = new Glue(actualsize);
        }
        return rt;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEm()
     */
    public Dimen getEm() {

        return actualsize;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEx()
     */
    public Dimen getEx() {

        Dimen xheight = (Dimen) fontdimen.get("XHEIGHT");
        if (xheight == null) {
            return new Dimen(actualsize);
        }
        return new Dimen(xheight.getValue() * actualsize.getValue()
                / unitsperem);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontDimen(java.lang.String)
     */
    public Dimen getFontDimen(final String key) {

        Dimen rt = (Dimen) fontdimen.get(key);
        if (rt == null) {
            rt = new Dimen(0);
        }
        return rt;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getProperty(java.lang.String)
     */
    public String getProperty(final String key) {

        return (String) property.get(key);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontName()
     */
    public String getFontName() {

        return fountkey.getName();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getCheckSum()
     */
    public int getCheckSum() {

        return font.getChecksum();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getBoundingBox()
     */
    public BoundingBox getBoundingBox() {

        //      TODO incomplete
        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getLetterSpacing()
     */
    public Glue getLetterSpacing() {

        return fountkey.getLetterspaced();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getDesignSize()
     */
    public Dimen getDesignSize() {

        return designsize;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getActualSize()
     */
    public Dimen getActualSize() {

        return actualsize;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontKey()
     */
    public FountKey getFontKey() {

        return fountkey;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontByteArray()
     */
    public FontByteArray getFontByteArray() {

        return null;
    }
}
