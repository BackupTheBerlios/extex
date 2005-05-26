/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type;

import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * Fount Interface (only getter)
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public interface Fount {

    /**
     * Return the Glyph of a <code>UnicodeChar</code>, or
     * null, if the character is not defined.
     *
     * @param c the unicodechar
     * @return the <code>Glyph</code>
     */
    Glyph getGlyph(UnicodeChar c);

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
     * Return the font-property
     *
     * @param key   the key
     * @return the value for the key
     */
    String getProperty(String key);

    /**
     * Return the font name.
     *
     * @return the fontname
     */
    String getFontName();

    /**
     * Returns the checksum
     *
     * @return Returns the checksum
     */
    int getCheckSum();

    /**
     * Returns the BoundingBox
     *
     * @return Returns the BoundingBox, or <code>null</code>, if not exists.
     */
    BoundingBox getBoundingBox();

    /**
     * Return the letterspacing
     * @return  the letterspacing
     */
    Glue getLetterSpacing();

    /**
     * Returns the design size.
     * @return Returns the design size.
     */
    Dimen getDesignSize();

    /**
     * Returns the actual size.
     * @return Returns the actual size.
     */
    Dimen getActualSize();

    /**
     * Returns the key for the font.
     */
    FountKey getFontKey();
}