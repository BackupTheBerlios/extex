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

package de.dante.test.font;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * This class provides a memory-only font for test cases. Since no external file
 * is required no problem with parsing can happen.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class LauncherFont implements Font, Serializable {

    /**
     * This inner class contains the glyph definition for the launcher font.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    final class LauncherGlyph implements Glyph, Serializable {

        /**
         * The field <tt>serialVersionUID</tt> contains the version number for
         * serialization.
         */
        protected static final long serialVersionUID = 1L;

        /**
         * The field <tt>c</tt> contains the character associated with this glyph.
         */
        private final UnicodeChar c;

        /**
         * Creates a new object.
         *
         * @param c the character
         */
        private LauncherGlyph(final UnicodeChar c) {

            super();
            this.c = c;
        }

        /**
         * @see de.dante.extex.font.Glyph#addKerning(de.dante.extex.font.Kerning)
         */
        public void addKerning(final Kerning kern) {

        }

        /**
         * @see de.dante.extex.font.Glyph#addLigature(de.dante.extex.font.Ligature)
         */
        public void addLigature(final Ligature lig) {

        }

        /**
         * @see de.dante.extex.font.Glyph#getDepth()
         */
        public Dimen getDepth() {

            switch (c.getCodePoint()) {
                case 'q':
                case 'p':
                case 'g':
                case 'j':
                case 'y':
                    return new Dimen(Dimen.ONE * 2);
                case ',':
                case ';':
                case '/':
                case '(':
                case ')':
                    return new Dimen(Dimen.ONE);
                default:
                    return Dimen.ZERO_PT;
            }
        }

        /**
         * @see de.dante.extex.font.Glyph#getExternalFile()
         */
        public FontByteArray getExternalFile() {

            return null;
        }

        /**
         * @see de.dante.extex.font.Glyph#getHeight()
         */
        public Dimen getHeight() {

            return new Dimen(Dimen.ONE * 8);
        }

        /**
         * @see de.dante.extex.font.Glyph#getItalicCorrection()
         */
        public Dimen getItalicCorrection() {

            return new Dimen(Dimen.ZERO_PT);
        }

        /**
         * @see de.dante.extex.font.Glyph#getKerning(de.dante.util.UnicodeChar)
         */
        public Dimen getKerning(final UnicodeChar uc) {

            return new Dimen(Dimen.ZERO_PT);
        }

        /**
         * @see de.dante.extex.font.Glyph#getLeftSpace()
         */
        public Dimen getLeftSpace() {

            return Dimen.ZERO_PT;
        }

        /**
         * @see de.dante.extex.font.Glyph#getLigature(de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLigature(final UnicodeChar uc) {

            if (c.getCodePoint() == 'f') {
                if (uc.getCodePoint() == 'f') {
                    return UnicodeChar.get(222); //TODO
                }
            }
            return null;
        }

        /**
         * @see de.dante.extex.font.Glyph#getName()
         */
        public String getName() {

            return c.toString();
        }

        /**
         * @see de.dante.extex.font.Glyph#getNumber()
         */
        public String getNumber() {

            return this.c.toString();
        }

        /**
         * @see de.dante.extex.font.Glyph#getRightSpace()
         */
        public Dimen getRightSpace() {

            return Dimen.ZERO_PT;
        }

        /**
         * @see de.dante.extex.font.Glyph#getWidth()
         */
        public Dimen getWidth() {

            return new Dimen(Dimen.ONE * 8);
        }

        /**
         * @see de.dante.extex.font.Glyph#setDepth(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setDepth(final Dimen d) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setExternalFile(de.dante.extex.font.FontByteArray)
         */
        public void setExternalFile(final FontByteArray file) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setHeight(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setHeight(final Dimen h) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setItalicCorrection(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setItalicCorrection(final Dimen d) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setLeftSpace(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setLeftSpace(final Dimen ls) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setName(java.lang.String)
         */
        public void setName(final String n) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setNumber(java.lang.String)
         */
        public void setNumber(final String nr) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setRightSpace(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setRightSpace(final Dimen rs) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setWidth(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setWidth(final Dimen w) {

        }
    }

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * The field <tt>hyphen</tt> contains the hyphen char.
     */
    private UnicodeChar hyphen = UnicodeChar.get(45);

    /**
     * The field <tt>skew</tt> contains the skew char.
     */
    private UnicodeChar skew = null;

    /**
     * The field <tt>fontdimen</tt> contains the font dimens.
     */
    private Map fontdimen = new HashMap();

    /**
     * @see de.dante.extex.font.type.Fount#getActualSize()
     */
    public FixedDimen getActualSize() {

        return new Dimen(Dimen.ONE_PT.getValue() * 10);
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

        return 0;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getDepth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getDepth(final UnicodeChar uc) {

        return new Glue(Dimen.ZERO_PT);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getDesignSize()
     */
    public FixedDimen getDesignSize() {

        return new Dimen(Dimen.ONE_PT.getValue() * 10);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEfcode()
     */
    public long getEfcode(final UnicodeChar uc) {

        return 1000;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEm()
     */
    public FixedDimen getEm() {

        return new Dimen(Dimen.ONE_PT.getValue() * 10);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEx()
     */
    public FixedDimen getEx() {

        return new Dimen(Dimen.ONE_PT.getValue() * 5);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontByteArray()
     */
    public FontByteArray getFontByteArray() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontDimen(java.lang.String)
     */
    public FixedDimen getFontDimen(final String key) {

        return Dimen.ZERO_PT;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontKey()
     */
    public FountKey getFontKey() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontName()
     */
    public String getFontName() {

        return "testfont";
    }

    /**
     * @see de.dante.extex.font.type.Fount#getGlyph(
     *      de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        return new LauncherGlyph(c);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHeight(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getHeight(final UnicodeChar uc) {

        return new Glue(getGlyph(uc).getHeight());
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

        return new Dimen(getGlyph(uc).getItalicCorrection());
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getKerning(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public FixedDimen getKerning(final UnicodeChar uc1, final UnicodeChar uc2) {

        return new Dimen(getGlyph(uc1).getKerning(uc2));
    }

    /**
     * @see de.dante.extex.font.type.Fount#getLetterSpacing()
     */
    public FixedGlue getLetterSpacing() {

        return new Glue(Dimen.ZERO_PT);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLigature(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLigature(final UnicodeChar uc1, final UnicodeChar uc2) {

        return getGlyph(uc1).getLigature(uc2);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getProperty(java.lang.String)
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
     * @see de.dante.extex.font.type.Fount#getSpace()
     */
    public FixedGlue getSpace() {

        return new Glue(Dimen.ONE_PT.getValue() * 10);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getWidth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getWidth(final UnicodeChar uc) {

        return new Glue(getGlyph(uc).getWidth());
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#hasGlyph(
     *      de.dante.util.UnicodeChar)
     */
    public boolean hasGlyph(final UnicodeChar uc) {

        return true;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setEfcode(de.dante.util.UnicodeChar, long)
     */
    public void setEfcode(final UnicodeChar uc, final long code) {

        // TODO gene: setEfcode unimplemented

    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setFontDimen(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

        fontdimen.put(key, value);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setHyphenChar(
     *      de.dante.util.UnicodeChar)
     */
    public void setHyphenChar(final UnicodeChar hyphen) {

        this.hyphen = hyphen;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setSkewChar(
     *      de.dante.util.UnicodeChar)
     */
    public void setSkewChar(final UnicodeChar skew) {

        this.skew = skew;
    }

}
