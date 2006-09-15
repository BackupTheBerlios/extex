/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.other;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * This class implements a dummy font which does not contain any characters.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.18 $
 */
public class NullFont implements Font, Serializable {

    /**
     * The field <tt>DEFAULT_EF_CODE</tt> contains the default value for the
     * ef code.
     */
    private static final int DEFAULT_EF_CODE = 1000;

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * The field <tt>efCode</tt> contains the ef code.
     */
    private Map efCode = null;

    /**
     * The field <tt>fontDimens</tt> contains the map for font dimens.
     */
    private Map fontDimens = null;

    /**
     * The field <tt>hyphen</tt> contains the hyphen char for this font.
     */
    private UnicodeChar hyphen = UnicodeChar.get('-');

    /**
     * The field <tt>skew</tt> contains the skew char for this font.
     */
    private UnicodeChar skew = null;

    /**
     * Creates a new object.
     */
    public NullFont() {

        super();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getActualSize()
     */
    public FixedDimen getActualSize() {

        return Dimen.ZERO_PT;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getBoundingBox()
     */
    public BoundingBox getBoundingBox() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getCheckSum()
     */
    public int getCheckSum() {

        return -1;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getDepth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getDepth(final UnicodeChar uc) {

        return Glue.ZERO;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getDesignSize()
     */
    public FixedDimen getDesignSize() {

        return Dimen.ZERO_PT;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEfcode()
     */
    public long getEfcode(final UnicodeChar uc) {

        if (efCode == null) {
            return DEFAULT_EF_CODE;
        }
        Long code = (Long) efCode.get(uc);
        return (code == null ? DEFAULT_EF_CODE : code.longValue());
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEm()
     */
    public FixedDimen getEm() {

        return new Dimen(0);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEx()
     */
    public FixedDimen getEx() {

        return new Dimen(0);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontByteArray()
     */
    public FontByteArray getFontByteArray() {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getFontDimen(
     *      java.lang.String)
     */
    public FixedDimen getFontDimen(final String key) {

        return (fontDimens == null ? null : (FixedDimen) fontDimens.get(key));
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontKey()
     */
    public FountKey getFontKey() {

        return new FountKey("nullfont");
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getFontName()
     */
    public String getFontName() {

        return "nullfont";
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getGlyph(
     *      de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHeight(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getHeight(final UnicodeChar uc) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHyphenChar()
     */
    public UnicodeChar getHyphenChar() {

        return hyphen;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getItalicCorrection(
     *      de.dante.util.UnicodeChar)
     */
    public FixedDimen getItalicCorrection(final UnicodeChar uc) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getKerning(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public FixedDimen getKerning(final UnicodeChar uc1, final UnicodeChar uc2) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLetterSpacing()
     */
    public FixedGlue getLetterSpacing() {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLigature(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLigature(final UnicodeChar uc1, final UnicodeChar uc2) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getProperty(
     *      java.lang.String)
     */
    public String getProperty(final String key) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getSkewChar()
     */
    public UnicodeChar getSkewChar() {

        return skew;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getSpace()
     */
    public FixedGlue getSpace() {

        return new Glue();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getWidth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getWidth(final UnicodeChar uc) {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#isVirtualFont()
     */
    public boolean isVirtualFont() {

        return false;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setEfcode(
     *      de.dante.util.UnicodeChar, long)
     */
    public void setEfcode(final UnicodeChar uc, final long code) {

        if (efCode == null) {
            efCode = new HashMap();
        }
        efCode.put(uc, new Long(code));
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setFontDimen(
     *      java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

        if (fontDimens == null) {
            fontDimens = new HashMap();
        }
        fontDimens.put(key, value);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setHyphenChar(
     *      de.dante.util.UnicodeChar)
     */
    public void setHyphenChar(final UnicodeChar ahyphen) {

        this.hyphen = ahyphen;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setSkewChar(
     *      de.dante.util.UnicodeChar)
     */
    public void setSkewChar(final UnicodeChar askew) {

        this.skew = askew;
    }

}
