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
 * @version $Revision: 1.1 $
 */
public interface Font {

    /**
     * Return the Glyph of a <code>UnicodeChar</code>, or
     * a empty symbol-glyph, if the character is not defined.
     *
     * @param c the unicodechar
     * @return the <code>Glyph</code>
     */
    Glyph getGlyph(UnicodeChar c);

    /**
     * Check, if the <code>UnicodeChar</code> is defined in the font.
     *
     * @param c the unicodechar
     * @return <code>true</code>, if the glyph exists, otherwise <code>false</code>
     */
    boolean isDefined(UnicodeChar c);

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
     */
    void setFontDimen(String key, Dimen value);

    /**
     * Return the font name.
     *
     * @return ...
     */
    String getFontName();

    /**
     * Return the list of all fontfiles.
     * @return  the list of all fontfiles
     */
    FontFileList getFontFiles();

    /**
     * Return the letterspaced
     * @return  the letterspaced
     */
    Glue getLetterSpaced();

    /**
     * Return the ligatures (on/off)
     * @return the ligatures
     */
    boolean getLigatures();

}
