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
import java.util.HashMap;
import java.util.Map;

/**
 * Class for a efm glyph.
 *
 * <pre>
 *  <glyph ID="11" glyph-number="11" width="598920" height="728177"
 *         depth="0" italic="72121">
 *     <ligature letter-id="108" letter="l" lig-id="15" />
 *     <kerning glyph-id="39" char="'" size="23362" />
 *  </glyph>
 *  </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class EfmGlyph implements Serializable {

    /**
     * the id
     */
    private int id;

    /**
     * the glyph number (from the font)
     */
    private int number;

    /**
     * the glyph name
     */
    private String name;

    /**
     * the width
     */
    private float width;

    /**
     * the hight
     */
    private float height;

    /**
     * the depth
     */
    private float depth;

    /**
     * the italic
     */
    private float italic;

    /**
     * the ligatute map
     */
    private Map ligature = new HashMap();

    /**
     * the kerning map
     */
    private Map kerning = new HashMap();

    /**
     * Returns the depth.
     * @return Returns the depth.
     */
    public float getDepth() {

        return depth;
    }

    /**
     * Set the depth.
     * @param d The depth to set.
     */
    public void setDepth(final float d) {

        depth = d;
    }

    /**
     * Set the depth.
     * @param d The depth to set.
     */
    public void setDepth(final String d) {

        try {
            depth = Float.parseFloat(d);
        } catch (Exception e) {
            // use default
            depth = 0.0f;
        }

    }

    /**
     * Returns the height.
     * @return Returns the height.
     */
    public float getHeight() {

        return height;
    }

    /**
     * Set the height.
     * @param h The height to set.
     */
    public void setHeight(final float h) {

        height = h;
    }

    /**
     * Set the height.
     * @param h The height to set.
     */
    public void setHeight(final String h) {

        try {
            height = Float.parseFloat(h);
        } catch (Exception e) {
            // use default
            height = 0.0f;
        }
    }

    /**
     * Returns the id.
     * @return Returns the id.
     */
    public int getId() {

        return id;
    }

    /**
     * Returns the id  as String.
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
     * Set the id.
     * @param i The id to set (as String).
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
     * Returns the italic.
     * @return Returns the italic.
     */
    public float getItalic() {

        return italic;
    }

    /**
     * Set the italic.
     * @param i The italic to set.
     */
    public void setItalic(final float i) {

        italic = i;
    }

    /**
     * Set the italic.
     * @param i The italic to set.
     */
    public void setItalic(final String i) {

        try {
            italic = Float.parseFloat(i);
        } catch (Exception e) {
            // use default
            italic = 0.0f;
        }
    }

    /**
     * Returns the name.
     * @return Returns the name.
     */
    public String getName() {

        return name;
    }

    /**
     * Set the name.
     * @param n The name to set.
     */
    public void setName(final String n) {

        name = n;
    }

    /**
     * Returns the number.
     * @return Returns the number.
     */
    public int getNumber() {

        return number;
    }

    /**
     * Set the number.
     * @param n The number to set.
     */
    public void setNumber(final int n) {

        number = n;
    }

    /**
     * Set the number (as String).
     * @param n The number to set.
     */
    public void setNumber(final String n) {

        try {
            number = Integer.parseInt(n);
        } catch (Exception e) {
            // use default
            number = -1;
        }
    }

    /**
     * Returns the width.
     * @return Returns the width.
     */
    public float getWidth() {

        return width;
    }

    /**
     * Set the width.
     * @param w The width to set.
     */
    public void setWidth(final float w) {

        width = w;
    }

    /**
     * Set the width.
     * @param w The width to set.
     */
    public void setWidth(final String w) {

        try {
            width = Float.parseFloat(w);
        } catch (Exception e) {
            // use default
            width = 0.0f;
        }
    }

    /**
     * Add a ligature.
     * @param key   the key
     * @param lig   the ligature
     */
    public void addLigature(final String key, final EfmLigature lig) {

        ligature.put(key, lig);
    }

    /**
     * Returns a ligature.
     * @param key   the key
     * @return Returns a ligature.
     */
    public EfmLigature getLigature(final String key) {

        return (EfmLigature) ligature.get(key);
    }

    /**
     * Add a kerning.
     * @param key   the key
     * @param kern  the kerning
     */
    public void addKerning(final String key, final EfmKerning kern) {

        kerning.put(key, kern);
    }

    /**
     * Returns a kerning.
     * @param key the key
     * @return Returns a kerning.
     */
    public EfmKerning getKerning(final String key) {

        return (EfmKerning) kerning.get(key);
    }

    /**
     * Returns the kerning.
     * @return Returns the kerning.
     */
    public Map getKerning() {

        return kerning;
    }

    /**
     * Returns the ligature.
     * @return Returns the ligature.
     */
    public Map getLigature() {

        return ligature;
    }

}