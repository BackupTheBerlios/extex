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

import de.dante.extex.font.type.Fount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.util.UnicodeChar;

/**
 * Font Interface
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public interface Font extends Fount {

    /**
     * Return the hyphenationchar
     *
     * @return the hypenationchar
     */
    UnicodeChar getHyphenChar();

    /**
     * Return the skewchar
     *
     * @return the skewxchar
     */
    UnicodeChar getSkewChar();

    /**
     * Set the char for hyphenation
     *
     * @param hyphen the char to set
     */
    void setHyphenChar(UnicodeChar hyphen);

    /**
     * Set the skewchar
     *
     * @param skew the new skewchar
     */
    void setSkewChar(UnicodeChar skew);

    /**
     * Setter for the font dimen register.
     *
     * @param key       the key
     * @param value     the value for the key
     */
    void setFontDimen(String key, Dimen value);

}