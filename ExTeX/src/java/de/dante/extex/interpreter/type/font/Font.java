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

package de.dante.extex.interpreter.type.font;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * Font Interface
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface Font {

    /**
     * Return the Glyph of a <code>UnicodeChar</code>, or
     * null, if the character is not defined.
     *
     * @param c the unicodechar
     * @return the <code>Glyph</code>
     */
    Glyph getGlyph(UnicodeChar c);

    /**
     * Set the char for hyphenation
     *
     * @param hyphen the char to set
     */
    void setHyphenChar(UnicodeChar hyphen);

    /**
     * Return the hyphenationchar
     *
     * @return the hypenationchar
     */
    UnicodeChar getHyphenChar();

    /**
     * Set the skewchar
     *
     * @param skew the new skewchar
     */
    void setSkewChar(UnicodeChar skew);

    /**
     * Return the skewchar
     *
     * @return the skewxchar
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
     * @return em-size
     */
    Dimen getEm();

    /**
     * Return the ex size of the font.
     *
     * @return ex-size
     */
    Dimen getEx();

    /**
     * Return font dimen size with a key.
     *
     * @param key   the key
     * @return the value for the key
     */
    Dimen getFontDimen(String key);

    /**
     * Setter for the font dimen register.
     *
     * @param key       the key
     * @param value     the value for the key
     */
    void setFontDimen(String key, Dimen value);

    /**
     * Return the font-property
     *
     * @param key   the key
     * @return the value for the key
     */
    String getProperty(String key);

    /**
     * Setter for the font-property
     *
     * @param key       the key
     * @param value     the value for the key
     */
    void setProperty(String key, String value);

    /**
     * Return the font name.
     *
     * @return the fontname
     */
    String getFontName();

    /**
     * Return the letterspacing
     * @return  the letterspacing
     */
    Glue getLetterSpacing();

}