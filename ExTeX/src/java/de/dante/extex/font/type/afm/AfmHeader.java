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

package de.dante.extex.font.type.afm;

import org.jdom.Element;

import de.dante.util.XMLConvertible;

/**
 * AFM-Header.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class AfmHeader implements XMLConvertible {

    /**
     * The Postscript font name.
     */
    private String fontname = "";

    /**
     * The full name of the font.
     */
    private String fullname = "";

    /**
     * The family name of the font.
     */
    private String familyname = "";

    /**
     * The weight of the font: normal, bold, etc.
     */
    private String weight = "";

    /**
     * The italic angle of the font, usually 0.0 or negative.
     */
    private float italicangle;

    /**
     * <code>true</code> if all the characters have the same width.
     */
    private boolean isfixedpitch;

    /**
     * The character set of the font.
     */
    private String characterset = "";

    /**
     * not init
     */
    public static final int NOTINIT = -9999;

    /**
     * The llx of the FontBox.
     */
    private int llx = NOTINIT;

    /**
     * The lly of the FontBox.
     */
    private int lly = NOTINIT;

    /**
     * The lurx of the FontBox.
     */
    private int urx = NOTINIT;

    /**
     * The ury of the FontBox.
     */
    private int ury = NOTINIT;

    /**
     * The underline position.
     */
    private int underlineposition;

    /**
     * The underline thickness.
     */
    private int underlinethickness;

    /**
     * The font's encoding name.
     * This encoding is
     * - StandardEncoding
     * - AdobeStandardEncoding
     * - For all other names the font is treated as symbolic.
     */
    private String encodingscheme;

    /**
     * CapHeight
     */
    private int capheight;

    /**
     * XHeight
     */
    private int xheight;

    /**
     * Ascender
     */
    private int ascender;

    /**
     * Descender
     */
    private int descender;

    /**
     * StdHW
     */
    private int stdhw;

    /**
     * StdVW
     */
    private int stdvw;

    /**
     * Create a new object.
     */
    public AfmHeader() {

        super();
    }

    /**
     * Returns the ascender.
     * @return Returns the ascender.
     */
    public int getAscender() {

        return ascender;
    }

    /**
     * @param a The ascender to set.
     */
    public void setAscender(final int a) {

        ascender = a;
    }

    /**
     * Returns the capheight.
     * @return Returns the capheight.
     */
    public int getCapheight() {

        return capheight;
    }

    /**
     * @param height The capheight to set.
     */
    public void setCapheight(final int height) {

        capheight = height;
    }

    /**
     * Returns the characterset.
     * @return Returns the characterset.
     */
    public String getCharacterset() {

        return characterset;
    }

    /**
     * @param cs The characterset to set.
     */
    public void setCharacterset(final String cs) {

        characterset = cs;
    }

    /**
     * Returns the descender.
     * @return Returns the descender.
     */
    public int getDescender() {

        return descender;
    }

    /**
     * @param d The descender to set.
     */
    public void setDescender(final int d) {

        descender = d;
    }

    /**
     * Returns the encodingscheme.
     * @return Returns the encodingscheme.
     */
    public String getEncodingscheme() {

        return encodingscheme;
    }

    /**
     * @param encoding The encodingscheme to set.
     */
    public void setEncodingscheme(final String encoding) {

        encodingscheme = encoding;
    }

    /**
     * Returns the familyname.
     * @return Returns the familyname.
     */
    public String getFamilyname() {

        return familyname;
    }

    /**
     * @param fname The familyname to set.
     */
    public void setFamilyname(final String fname) {

        familyname = fname;
    }

    /**
     * Returns the fontname.
     * @return Returns the fontname.
     */
    public String getFontname() {

        return fontname;
    }

    /**
     * @param fname The fontname to set.
     */
    public void setFontname(final String fname) {

        fontname = fname;
    }

    /**
     * Returns the fullname.
     * @return Returns the fullname.
     */
    public String getFullname() {

        return fullname;
    }

    /**
     * @param fname The fullname to set.
     */
    public void setFullname(final String fname) {

        fullname = fname;
    }

    /**
     * Returns the isfixedpitch.
     * @return Returns the isfixedpitch.
     */
    public boolean isFixedpitch() {

        return isfixedpitch;
    }

    /**
     * @param fixedpitch The isfixedpitch to set.
     */
    public void setFixedpitch(final boolean fixedpitch) {

        isfixedpitch = fixedpitch;
    }

    /**
     * Returns the italicangle.
     * @return Returns the italicangle.
     */
    public float getItalicangle() {

        return italicangle;
    }

    /**
     * @param i The italicangle to set.
     */
    public void setItalicangle(final float i) {

        italicangle = i;
    }

    /**
     * Returns the llx.
     * @return Returns the llx.
     */
    public int getLlx() {

        return llx;
    }

    /**
     * @param x The llx to set.
     */
    public void setLlx(final int x) {

        llx = x;
    }

    /**
     * Returns the lly.
     * @return Returns the lly.
     */
    public int getLly() {

        return lly;
    }

    /**
     * @param y The lly to set.
     */
    public void setLly(final int y) {

        lly = y;
    }

    /**
     * Returns the stdhw.
     * @return Returns the stdhw.
     */
    public int getStdhw() {

        return stdhw;
    }

    /**
     * @param hw The stdhw to set.
     */
    public void setStdhw(final int hw) {

        stdhw = hw;
    }

    /**
     * Returns the stdvw.
     * @return Returns the stdvw.
     */
    public int getStdvw() {

        return stdvw;
    }

    /**
     * @param vw The stdvw to set.
     */
    public void setStdvw(final int vw) {

        stdvw = vw;
    }

    /**
     * Returns the underlineposition.
     * @return Returns the underlineposition.
     */
    public int getUnderlineposition() {

        return underlineposition;
    }

    /**
     * @param position The underlineposition to set.
     */
    public void setUnderlineposition(final int position) {

        underlineposition = position;
    }

    /**
     * Returns the underlinethickness.
     * @return Returns the underlinethickness.
     */
    public int getUnderlinethickness() {

        return underlinethickness;
    }

    /**
     * @param thickness The underlinethickness to set.
     */
    public void setUnderlinethickness(final int thickness) {

        underlinethickness = thickness;
    }

    /**
     * Returns the urx.
     * @return Returns the urx.
     */
    public int getUrx() {

        return urx;
    }

    /**
     * @param x The urx to set.
     */
    public void setUrx(final int x) {

        urx = x;
    }

    /**
     * Returns the ury.
     * @return Returns the ury.
     */
    public int getUry() {

        return ury;
    }

    /**
     * @param y The ury to set.
     */
    public void setUry(final int y) {

        ury = y;
    }

    /**
     * Returns the weight.
     * @return Returns the weight.
     */
    public String getWeight() {

        return weight;
    }

    /**
     * @param w The weight to set.
     */
    public void setWeight(final String w) {

        weight = w;
    }

    /**
     * Returns the xheight.
     * @return Returns the xheight.
     */
    public int getXheight() {

        return xheight;
    }

    /**
     * @param x The xheight to set.
     */
    public void setXheight(final int x) {

        xheight = x;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("header");
        element.setAttribute("name", fontname);
        element.setAttribute("fullname", fullname);
        element.setAttribute("familyname", familyname);
        element.setAttribute("weight", weight);
        element.setAttribute("italicangle", String.valueOf(italicangle));
        element.setAttribute("isfixedpitch", String.valueOf(isfixedpitch));
        element.setAttribute("characterset", characterset);
        element.setAttribute("llx", String.valueOf(llx));
        element.setAttribute("lly", String.valueOf(lly));
        element.setAttribute("urx", String.valueOf(urx));
        element.setAttribute("ury", String.valueOf(ury));
        element.setAttribute("underlineposition", String
                .valueOf(underlineposition));
        element.setAttribute("underlinethickness", String
                .valueOf(underlinethickness));
        element.setAttribute("encodingscheme", encodingscheme);
        element.setAttribute("capheight", String.valueOf(capheight));
        element.setAttribute("xheight", String.valueOf(xheight));
        element.setAttribute("ascender", String.valueOf(ascender));
        element.setAttribute("descender", String.valueOf(descender));
        element.setAttribute("stdhw", String.valueOf(stdhw));
        element.setAttribute("stdvw", String.valueOf(stdvw));
        return element;
    }
}