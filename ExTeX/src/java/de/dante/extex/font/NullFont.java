/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontFileList;
import de.dante.extex.interpreter.type.font.Glyph;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * This class implements a dummy font which does not contain any characters.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.15 $
 */
public class NullFont implements Font {

    /**
     * The field <tt>hyphen</tt> contains the hyphen char for this font.
     */
    private UnicodeChar hyphen = new UnicodeChar('-');

    /**
     * The field <tt>skew</tt> contains the skew char for this font
     */
    private UnicodeChar skew = new UnicodeChar('-');

    /**
     * Creates a new object.
     */
    public NullFont() {

        super();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHyphenChar()
     */
    public UnicodeChar getHyphenChar() {

        return hyphen;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getSkewChar()
     */
    public UnicodeChar getSkewChar() {

        return skew;
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

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getSpace()
     */
    public Glue getSpace() {

        return new Glue(0);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEm()
     */
    public Dimen getEm() {

        return new Dimen(0);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setFontDimen(
     *      java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEx()
     */
    public Dimen getEx() {

        return new Dimen(0);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getFontDimen(java.lang.String)
     */
    public Dimen getFontDimen(final String key) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getFontName()
     */
    public String getFontName() {

        return "nullFont";
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#isDefined(
     *      de.dante.util.UnicodeChar)
     */
    public boolean isDefined(final UnicodeChar c) {

        return false;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getGlyph(
     *      de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getFontFiles()
     */
    public FontFileList getFontFiles() {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLetterSpaced()
     */
    public Glue getLetterSpaced() {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLigatures()
     */
    public boolean getLigatures() {
        return false;
    }
}
