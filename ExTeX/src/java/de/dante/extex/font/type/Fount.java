/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;

/**
 * Fount Interface (only getter)
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public interface Fount {

    /**
     * Returns the actual size.
     * @return Returns the actual size.
     */
    FixedDimen getActualSize();

    /**
     * Returns the BoundingBox.
     *
     * @return the BoundingBox, or <code>null</code>, if it does not exists
     *
     * @deprecated not needed
     */
    BoundingBox getBoundingBox();

    /**
     * Returns the check sum.
     *
     * @return the check sum
     */
    int getCheckSum();

    /**
     * Returns the design size.
     *
     * @return the design size.
     */
    FixedDimen getDesignSize();

    /**
     * Return the em size of the font.
     *
     * @return em-size
     */
    FixedDimen getEm();

    /**
     * Return the ex size of the font.
     *
     * @return ex-size
     */
    FixedDimen getEx();

    /**
     * Returns the byte array for the external file e.g. cmr12.pfb.
     *
     * @return Returns the byte array for the external file e.g. cmr12.pfb
     *
     * @deprecated this single method should be replaced by some way to
     *   retrieve an appropriate font format
     */
    FontByteArray getFontByteArray();

    /**
     * Return font dimen size with a key.
     *
     * @param key   the key
     * @return the value for the key
     */
    FixedDimen getFontDimen(String key);

    /**
     * Returns the key for the font.
     * @return Returns the key for the font.
     */
    FountKey getFontKey();

    /**
     * Return the font name.
     *
     * @return the font name
     */
    String getFontName();

    /**
     * Return the letter spacing
     * @return  the letter spacing
     */
    FixedGlue getLetterSpacing();

    /**
     * Return the font-property
     *
     * @param key   the key
     * @return the value for the key
     */
    String getProperty(String key);

    /**
     * Return the width of space character.
     *
     * @return the width of the space character
     */
    FixedGlue getSpace();

    //    /**
    //     * Check, if the font is a virtual font.
    //     * @return Returns <code>true</code>, if the font is a virtual font
    //     */
    //    boolean isVirtual();

    //    /**
    //     * Returns the scale factor for the font.
    //     * @return Returns the scale factor for the font.
    //     */
    //    float getScaleFactor();

}
