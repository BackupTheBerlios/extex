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

package de.dante.extex.font.type.tfm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.GlyphImpl;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * Adapter for a ModifiableFount for TFM.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class ModifiableFountTFM implements ModifiableFount, Serializable {

    /**
     * Default unitsperem
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
    public ModifiableFountTFM(final FountKey fk, final TFMFont tfmfont) {

        fountkey = fk;
        font = tfmfont;

        calculateSize();
        setFontDimenValues();
    }

    /**
     * Calculate the sizes
     */
    private void calculateSize() {

        designsize = new Dimen((long) (font.getHeader().getDesignsize()
                .toDouble() * Dimen.ONE));

        // scale factor = 0 -> 1000
        if (fountkey.getScale() == null || fountkey.getScale().getValue() == 0) {
            fountkey.setScale(new Count(PERMILLE_FACTOR));
        }

        // calculate actual size
        if (fountkey.getSize() == null) {
            actualsize = new Dimen((designsize.getValue()
                    * fountkey.getScale().getValue() / PERMILLE_FACTOR));
        } else {
            actualsize = new Dimen((fountkey.getSize().getValue()
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
     * set the font dimen values from the tfm parameter
     */
    private void setFontDimenValues() {

        TFMParamArray param = font.getParam();
        TFMFixWord[] fw = param.getTable();
        for (int i = 0; i < fw.length; i++) {
            String labelname = param.getLabelName(i);
            Dimen d = convertFixWordToDimen(fw[i]);
            fontdimen.put(labelname, d);
            fontdimen.put(Integer.toString(i), d); // gene
        }
    }

    /**
     * the fount key
     */
    private FountKey fountkey;

    /**
     * the tfm font
     */
    private TFMFont font;

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
            TFMCharInfoArray charinfo = font.getCharinfo();
            TFMCharInfoWord ci = charinfo.getCharInfoWord(c.getCodePoint());

            g = new GlyphImpl();
            if (ci != null) {
                g.setNumber(String.valueOf(c.getCodePoint() + ci.getBc()));
                if (ci.getGlyphname() != null) {
                    g.setName(ci.getGlyphname().replaceAll("/", ""));
                }
                g.setWidth(convertFixWordToDimen(ci.getWidth()));
                g.setDepth(convertFixWordToDimen(ci.getDepth()));
                g.setHeight(convertFixWordToDimen(ci.getHeight()));
                g.setItalicCorrection(convertFixWordToDimen(ci.getItalic()));

                TFMLigKern[] ligKernTable = font.getLigkern().getLigKernTable();

                // ligature and kerning
                if (fountkey.isLigatures() || fountkey.isKerning()) {
                    int ligstart = ci.getLigkernstart();
                    if (ligstart != TFMCharInfoWord.NOINDEX) {

                        for (int k = ligstart; k != TFMCharInfoWord.NOINDEX; k = ligKernTable[k]
                                .nextIndex(k)) {
                            TFMLigKern lk = ligKernTable[k];

                            if (lk instanceof TFMLigature) {
                                if (fountkey.isLigatures()) {
                                    TFMLigature lig = (TFMLigature) lk;

                                    Ligature lv = new Ligature();
                                    lv.setLetterid(String.valueOf(lig
                                            .getNextChar()));
                                    String sl = Character.toString((char) lig
                                            .getNextChar());
                                    if (sl != null && sl.trim().length() > 0) {
                                        lv.setLetter(sl.trim());
                                    }
                                    lv.setLigid(String.valueOf(lig
                                            .getAddingChar()));
                                    String slig = Character.toString((char) lig
                                            .getAddingChar());
                                    if (slig != null
                                            && slig.trim().length() > 0) {
                                        lv.setLig(slig.trim());
                                    }
                                    g.addLigature(lv);
                                }
                            } else if (lk instanceof TFMKerning) {
                                if (fountkey.isKerning()) {
                                    TFMKerning kern = (TFMKerning) lk;

                                    Kerning kv = new Kerning();
                                    kv
                                            .setId(String.valueOf(kern
                                                    .getNextChar()));
                                    String sk = Character.toString((char) kern
                                            .getNextChar());
                                    if (sk != null && sk.trim().length() > 0) {
                                        kv.setName(sk.trim());
                                    }
                                    kv.setSize(convertFixWordToDimen(kern
                                            .getKern()));
                                    g.addKerning(kv);
                                }
                            }
                        }
                    }
                }
            }
        }
        return g;
    }

    /**
     * convert the fixword value to a dimen value
     *
     * the simple calculation
     *     fw.getValue() * actualsize.getValue() /
     *     TFMFixWord.FIXWORDDENOMINATOR
     * leads to different rounding than in TeX due to
     * a limitation to 4 byte integer precission. Note
     * that fw.getValue() * actualsize.getValue() might
     * exceed the 4 byte range.
     * Hence TTP 571 cancels actualsize.getValue() and
     * TFMFixWord.FIXWORDDENOMINATOR to allow for a
     * bytewise calculation. We do not need this bytewise
     * calculation since we have long integers, but we
     * need to be precise in performing the same rounding.
     *
     * @param fw    the fixword value
     * @return Returns the Dimen value.
     */
    private Dimen convertFixWordToDimen(final TFMFixWord fw) {

        int shift = 20;
        long z = actualsize.getValue();
        while (z >= 8388608) {
            z >>= 1;
            shift -= 1;
        }
        return new Dimen(z * fw.getValue() >> shift);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getSpace()
     */
    public Glue getSpace() {

        // glyph 'space' exists?
        Dimen spacedimen = (Dimen) fontdimen.get("SPACE");
        Dimen stretch = (Dimen) fontdimen.get("STRETCH"); //  gene
        Dimen shrink = (Dimen) fontdimen.get("SHRINK"); //  gene
        if (spacedimen != null) {
            return new Glue(spacedimen, stretch, shrink); // gene
        }

        // mgn Walter fragen, welche spacebreite?
        return new Glue(actualsize);
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
                / DEFAULTUNITSPEREM);
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

        // TODO incomplete
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
