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

import java.util.HashMap;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.util.UnicodeChar;

/**
 * GlyphImplementation
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class GlyphImpl implements Glyph {

    /**
     * the width of the glyph
     */
    private Dimen width = new Dimen(0);

    /**
     * the height of the glyph
     */
    private Dimen height = new Dimen(0);

    /**
     * the depth of the glyph
     */
    private Dimen depth = new Dimen(0);

    /**
     * the italic of the glyph
     */
    private Dimen italic = new Dimen(0);

    /**
     * glyph-number
     */
    private String number = "";

    /**
     * glyph-name
     */
    private String name = "";

    /**
     * The kerning for the glyph
     */
    private HashMap kerning = null;

    /**
     * The ligature for the glyph
     */
    private HashMap ligature = null;

    /**
     * The external fontfile or null, if no exists
     */
    private FontFile externalfile = null;

    /**
     * The left space in a glyph
     */
    private Dimen leftSpace = new Dimen(0);

    /**
     * The rigtht space in a glyph
     */
    private Dimen rightSpace = new Dimen(0);

    /**
     * Create a new object.
     */
    public GlyphImpl() {

        super();
    }

    /**
     * Create a new object.
     * @param h  the hight
     * @param d  the depth
     * @param w  the width
     * @param i  the italic
     */
    public GlyphImpl(final Dimen h, final Dimen d, final Dimen w, final Dimen i) {

        super();
        height = h;
        depth = d;
        width = w;
        italic = i;
    }

    /**
     * @return Returns the depth.
     */
    public Dimen getDepth() {

        return depth;
    }

    /**
     * @param d The depth to set.
     */
    public void setDepth(final Dimen d) {

        depth = d;
    }

    /**
     * @param gsize         The size to set as <code>String</code>.
     * @param em            The em-size.
     * @param unitsperem    The unit per em.
     */
    public void setDepth(final String gsize, final Dimen em,
            final int unitsperem) {

        try {
            float fsize = Float.parseFloat(gsize);
            depth = new Dimen((long) (fsize * em.getValue() / unitsperem));
        } catch (Exception e) {
            // use default
            depth = new Dimen(0);
        }
    }

    /**
     * @return Returns the height.
     */
    public Dimen getHeight() {

        return height;
    }

    /**
     * @param h The height to set.
     */
    public void setHeight(final Dimen h) {

        height = h;
    }

    /**
     * @param gsize         The size to set as <code>String</code>.
     * @param em            The em-size.
     * @param unitsperem    The unit per em.
     */
    public void setHeight(final String gsize, final Dimen em,
            final int unitsperem) {

        try {
            float fsize = Float.parseFloat(gsize);
            height = new Dimen((long) (fsize * em.getValue() / unitsperem));
        } catch (Exception e) {
            // use default
            height = new Dimen(0);
        }
    }

    /**
     * @return Returns the italic.
     */
    public Dimen getItalicCorrection() {

        return italic;
    }

    /**
     * @param d the italic to set.
     */
    public void setItalicCorrection(final Dimen d) {

        italic = d;
    }

    /**
     * @param gsize         The size to set as <code>String</code>.
     * @param em            The em-size.
     * @param unitsperem    The unit per em.
     */
    public void setItalicCorrection(final String gsize, final Dimen em,
            final int unitsperem) {

        try {
            float fsize = Float.parseFloat(gsize);
            italic = new Dimen((long) (fsize * em.getValue() / unitsperem));
        } catch (Exception e) {
            // use default
            italic = new Dimen(0);
        }
    }

    /**
     * @return Returns the width.
     */
    public Dimen getWidth() {

        return width;
    }

    /**
     * @param w The width to set.
     */
    public void setWidth(final Dimen w) {

        width = w;
    }

    /**
     * @param gsize         The size to set as <code>String</code>.
     * @param em            The em-size.
     * @param unitsperem    The unit per em.
     */
    public void setWidth(final String gsize, final Dimen em,
            final int unitsperem) {

        try {
            float fsize = Float.parseFloat(gsize);
            width = new Dimen((long) (fsize * em.getValue() / unitsperem));
        } catch (Exception e) {
            // use default
            width = new Dimen(0);
        }
    }

    /**
     * @return Returns the name.
     */
    public String getName() {

        return name;
    }

    /**
     * @param n The name to set.
     */
    public void setName(final String n) {

        name = n;
    }

    /**
     * @return Returns the number.
     */
    public String getNumber() {

        return number;
    }

    /**
     * @param nr The number to set.
     */
    public void setNumber(final String nr) {

        number = nr;
    }

    /**
     * Add kerning for the glyph.
     * @param kern  the kerning
     */
    public void addKerning(final Kerning kern) {

        if (kerning == null) {
            kerning = new HashMap();
        }
        kerning.put(kern.getId(), kern);
    }

    /**
     * Return the kerning for the glyph.
     * @param uc    the following character
     * @return  the kerning-size as <code>Dimen</code>
     */
    public Dimen getKerning(final UnicodeChar uc) {

        Dimen size = new Dimen(0); // default
        if (kerning != null) {
            Kerning kv = (Kerning) kerning.get(String
                    .valueOf(uc.getCodePoint()));
            if (kv != null) {
                size = kv.getSize();
            }
        }
        return size;
    }

    /**
     * Add ligature for the glyph.
     * @param lig  the ligature
     */
    public void addLigature(final Ligature lig) {

        if (ligature == null) {
            ligature = new HashMap();
        }
        ligature.put(lig.getLetterid(), lig);
    }

    /**
     * Return the ligature as <code>UnicodeChar</code>,
     * or <code>null</code>, if no ligature exists.
     *
     * If you get a ligature-character, then you MUST call the
     * method <code>getligature()</code> twice, if a ligature with
     * more then two characters exist.
     * (e.g. f - ff - ffl)
     * @param uc    the following character
     * @return  the ligature
     */
    public UnicodeChar getLigature(final UnicodeChar uc) {

        UnicodeChar liguc = null;
        if (ligature != null) {
            try {
                Ligature lig = (Ligature) ligature.get(String.valueOf(uc
                        .getCodePoint()));
                int i = Integer.parseInt(lig.getLigid());
                liguc = new UnicodeChar(i);
            } catch (Exception e) {
                // use default
                liguc = null;
            }
        }
        return liguc;
    }

    /**
     * @return Returns the externalfile.
     */
    public FontFile getExternalFile() {

        return externalfile;
    }

    /**
     * @param file The externalfile to set.
     */
    public void setExternalFile(final FontFile file) {

        externalfile = file;
    }

    /**
     * @return Returns the leftSpace.
     */
    public Dimen getLeftSpace() {

        return leftSpace;
    }

    /**
     * @param ls The leftSpace to set.
     */
    public void setLeftSpace(final Dimen ls) {

        leftSpace = ls;
    }

    /**
     * @return Returns the rightSpace.
     */
    public Dimen getRightSpace() {

        return rightSpace;
    }

    /**
     * @param rs The rightSpace to set.
     */
    public void setRightSpace(final Dimen rs) {

        rightSpace = rs;
    }
}