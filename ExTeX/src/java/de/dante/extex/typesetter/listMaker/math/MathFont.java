/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.listMaker.math;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.util.UnicodeChar;

/**
 * This class encapsulates a font and provides access to the font dimens with
 * convenience methods.
 *
 *
 * @see "TTP [700]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class MathFont implements Font {

    /**
     * The field <tt>font</tt> contains the encapsulated font.
     */
    private Font font;

    /**
     * Creates a new object.
     *
     * @param font the font encapsulated
     */
    public MathFont(final Font font) {

        super();
        this.font = font;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getActualSize()
     */
    public FixedDimen getActualSize() {

        return this.font.getActualSize();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getBoundingBox()
     */
    public BoundingBox getBoundingBox() {

        return this.font.getBoundingBox();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getCheckSum()
     */
    public int getCheckSum() {

        return this.font.getCheckSum();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getDepth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getDepth(final UnicodeChar uc) {

        return font.getDepth(uc);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getDesignSize()
     */
    public FixedDimen getDesignSize() {

        return this.font.getDesignSize();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEfcode()
     */
    public long getEfcode(final UnicodeChar uc) {

        return font.getEfcode(uc);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEm()
     */
    public FixedDimen getEm() {

        return this.font.getEm();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEx()
     */
    public FixedDimen getEx() {

        return this.font.getEx();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontByteArray()
     */
    public FontByteArray getFontByteArray() {

        return this.font.getFontByteArray(); // add by mgn
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getFontDimen(String)
     */
    public FixedDimen getFontDimen(final String key) {

        return this.font.getFontDimen(key);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontKey()
     */
    public FountKey getFontKey() {

        return this.font.getFontKey(); // add by mgn
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getFontName()
     */
    public String getFontName() {

        return this.font.getFontName();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getGlyph(UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        return this.font.getGlyph(c);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHeight(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getHeight(final UnicodeChar uc) {

        return font.getHeight(uc);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHyphenChar()
     */
    public UnicodeChar getHyphenChar() {

        return this.font.getHyphenChar();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getItalicCorrection(
     *      de.dante.util.UnicodeChar)
     */
    public FixedDimen getItalicCorrection(final UnicodeChar uc) {

        return font.getItalicCorrection(uc);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getKerning(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public FixedDimen getKerning(final UnicodeChar uc1, final UnicodeChar uc2) {

        return font.getKerning(uc1, uc2);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLetterSpacing()
     */
    public FixedGlue getLetterSpacing() {

        return this.font.getLetterSpacing();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLigature(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLigature(final UnicodeChar uc1, final UnicodeChar uc2) {

        return font.getLigature(uc1, uc2);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getProperty(String)
     */
    public String getProperty(final String key) {

        return this.font.getProperty(key);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getSkewChar()
     */
    public UnicodeChar getSkewChar() {

        return this.font.getSkewChar();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getSpace()
     */
    public FixedGlue getSpace() {

        return this.font.getSpace();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getWidth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getWidth(final UnicodeChar uc) {

        return font.getWidth(uc);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setEfcode(
     *      de.dante.util.UnicodeChar, long)
     */
    public void setEfcode(final UnicodeChar uc, final long code) {

        font.setEfcode(uc, code);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setFontDimen(String, Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

        this.font.setFontDimen(key, value);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setHyphenChar(UnicodeChar)
     */
    public void setHyphenChar(final UnicodeChar hyphen) {

        this.font.setHyphenChar(hyphen);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setSkewChar(UnicodeChar)
     */
    public void setSkewChar(final UnicodeChar skew) {

        this.font.setSkewChar(skew);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return this.font.toString();
    }

}
