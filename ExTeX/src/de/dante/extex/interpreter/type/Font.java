/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.interpreter.type;

import java.io.File;

import de.dante.util.UnicodeChar;

/**
 * Font Interface
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.12 $
 */
public interface Font {

    /**
     * Return the Glyph of a <code>UnicodeChar</code>, or
     * <code>null</code>, if the character is not defined.
     *
     * @param c ...
     * @return ...
     */
    Glyph getGlyph(UnicodeChar c);

    /**
     * Check, if the <code>UnicodeChar</code> is defined in the font.
     *
     * @param c ...
     * @return ...
     */
    boolean isDefined(UnicodeChar c);

    /**
     * Return the kerning between c1 und c2.
     *
     * @param c1    the first character
     * @param c2    the second character
     *
     * @return    the kerning
     */
    Dimen kern(UnicodeChar c1, UnicodeChar c2);

    /**
     * Return the ligature as <code>UnicodeChar</code>,
     * or <code>null</code>, if no ligature exists.
     *
     * If you get a ligature character, then you MUST call the
     * method <code>ligature()</code> twice, if a ligature with
     * more then two characters exist.
     * (e.g. f - ff - ffl)
     *
     * @param c1    the first character
     * @param c2    the second character
     *
     * @return    the ligature character as <code>UnicodeChar</code>, or
     *             <code>null</code>, if none exists
     */
    UnicodeChar ligature(UnicodeChar c1, UnicodeChar c2);

    /**
     * ...
     *
     * @param hyphen ...
     */
    void setHyphenChar(UnicodeChar hyphen);

    /**
     * ...
     *
     * @return ...
     */
    UnicodeChar getHyphenChar();

    /**
     * ...
     *
     * @param skew ...
     */
    void setSkewChar(UnicodeChar skew);

    /**
     * ...
     *
     * @return ...
     */
    UnicodeChar getSkewChar();

    /**
     * Return the width of space character.
     *
     * @return the width of the space character
     */
    Glue getSpace();

    /**
     * Return the em size of the font.
     *
     * @return ...
     */
    Dimen getEm();

    /**
     * Return the ex size of the font.
     *
     * @return ...
     */
    Dimen getEx();

    /**
     * Return font dimen size with a key.
     *
     * @param key ...
     * @return ...
     */
    Dimen getFontDimen(String key);

    /**
     * Setter for the font dimen register.
     *
     * @param key ...
     * @param value ...
     *
     * @return ...
     */
    void setFontDimen(String key, Dimen value);

    /**
     * Return the font name.
     *
     * @return ...
     */
    String getFontName();

    /**
     * Return the <code>File</code> object of a external font file or
     * <code>null</code>, if no such file exists.
     *
     * @return ...
     */
    File getExternalFile();

    /**
     * Return the external ID to find the glyph in the external file.
     *
     * @param uc ...
     *
     * @return the external ID, or <code>null</code>, if none exists.
     */
    String getExternalID(UnicodeChar uc);

}
