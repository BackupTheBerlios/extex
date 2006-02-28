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

package de.dante.util;

import com.ibm.icu.lang.UCharacter;

/**
 * This class provides a Unicode character with some combing characters
 * attached to it.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ComposedUnicodeChar extends UnicodeChar {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 28022006L;

    /**
     * The field <tt>combiningCodePoints</tt> contains the combining characters.
     */
    private int[] combiningCodePoints;

    /**
     * Creates a new object from an integer code point and some combining
     * characters.
     *
     * @param codePoint the 32-bit code point
     * @param combining the array of code points for the combining characters
     */
    public ComposedUnicodeChar(final int codePoint, final int[] combining) {

        super(codePoint);
        for (int i = 0; i < combining.length; i++) {
            if (combining[i] < UCharacter.MIN_VALUE
                    || combining[i] > UCharacter.MAX_VALUE) {
                throw new IllegalArgumentException("Codepoint out of bounds");
            }
        }
        combiningCodePoints = combining;
    }

    /**
     * Getter for combining character's code points.
     *
     * @return the combining character's code points
     */
    protected int[] getCombiningCodePoints() {

        return this.combiningCodePoints;
    }

}
