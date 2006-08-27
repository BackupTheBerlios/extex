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
 */

package de.dante.extex.interpreter.type.font;

import de.dante.extex.font.Glyph;
import de.dante.extex.font.type.Fount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.util.UnicodeChar;

/**
 * Font Interface
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface Font extends Fount {

    /**
     * Returns the depth of the char.
     *
     * @param uc the Unicode char
     *
     * @return the depth of the char
     */
    FixedGlue getDepth(UnicodeChar uc);

    /**
     * Getter for the ef code.
     *
     * @param uc the character
     *
     * @return the ef code
     */
    long getEfcode(UnicodeChar uc);

    /**
     * Return the Glyph of a <code>UnicodeChar</code>, or
     * null, if the character is not defined.
     *
     * @param uc the Unicode char
     *
     * @return the glyph
     *
     * @deprecated the use should be avoided in favor of the direct access
     *   methods
     */
    Glyph getGlyph(UnicodeChar uc);

    /**
     * Returns the height of the char.
     *
     * @param uc the Unicode char
     *
     * @return the height of the char
     */
    FixedGlue getHeight(UnicodeChar uc);

    /**
     * Return the hyphenation character.
     *
     * @return the hyphenation character
     */
    UnicodeChar getHyphenChar();

    /**
     * Returns the italic correction of the char.
     *
     * @param uc the Unicode char
     *
     * @return Returns the italic correction of the char
     */
    FixedDimen getItalicCorrection(UnicodeChar uc);

    /**
     * Returns the kerning between two chars.
     *
     * @param uc1 the Unicode char (first one)
     * @param uc2 the Unicode char (second one)
     *
     * @return the kerning between two chars
     */
    FixedDimen getKerning(UnicodeChar uc1, UnicodeChar uc2);

    /**
     * Returns the ligature for two chars.
     *
     * @param uc1 the Unicode char (first one)
     * @param uc2 the Unicode char (second one)
     *
     * @return Returns the ligature for two chars
     */
    UnicodeChar getLigature(UnicodeChar uc1, UnicodeChar uc2);

    /**
     * Return the skew character
     *
     * @return the skew character
     */
    UnicodeChar getSkewChar();

    /**
     * Returns the width of the char.
     *
     * @param uc the Unicode char
     *
     * @return the width of the char
     */
    FixedGlue getWidth(UnicodeChar uc);

    /**
     * Setter for the ef code.
     * The ef code influences the stretchability of characters. It has a
     * positive value. 1000 means "normal" stretchability.
     *
     * @param uc the character
     * @param code the associated code
     */
    void setEfcode(UnicodeChar uc, long code);

    /**
     * Setter for the font dimen register.
     *
     * @param key the key
     * @param value the value for the key
     */
    void setFontDimen(String key, Dimen value);

    /**
     * Set the char for hyphenation.
     *
     * @param hyphen the char to set
     */
    void setHyphenChar(UnicodeChar hyphen);

    /**
     * Set the skew character.
     *
     * @param skew the new skew character
     */
    void setSkewChar(UnicodeChar skew);

}
