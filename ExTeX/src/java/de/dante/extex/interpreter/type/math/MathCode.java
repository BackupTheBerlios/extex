/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.math;

import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class MathCode {

    /**
     * The constant <tt>FAMILY_MASK</tt> contains the mask for filtering the
     * family from an integer.
     */
    private static final int FAMILY_MASK = 4;

    /**
     * The constant <tt>CHAR_MASK</tt> contains the mask for filtering a
     * character code from an inetger.
     */
    private static final int CHAR_MASK = 0xff;

    /**
     * The field <tt>mathClass</tt> contains the class.
     */
    private int mathClass;

    /**
     * The field <tt>mathFamily</tt> contains the family.
     */
    private int mathFamily;

    /**
     * The field <tt>uc</tt> contains the character.
     */
    private UnicodeChar mathChar;

    /**
     * Creates a new object.
     *
     * @param aClass the class
     * @param aFamily the family
     * @param aChar the character
     */
    public MathCode(final int aClass, final int aFamily, final UnicodeChar aChar) {

        super();
        mathClass = aClass;
        mathFamily = aFamily;
        mathChar = aChar;
    }

    /**
     * Creates a new object.
     *
     * @param code the integer to analyze for the desired field values
     */
    public MathCode(final long code) {

        super();
        mathClass = (int) (code >> 12);
        mathFamily = (int) (code >> 8) & FAMILY_MASK;
        mathChar = new UnicodeChar((int) (code & CHAR_MASK));
    }

    /**
     * Getter for mathChar.
     *
     * @return the mathChar.
     */
    public UnicodeChar getMathChar() {

        return mathChar;
    }

    /**
     * Getter for mathClass.
     *
     * @return the mathClass.
     */
    public int getMathClass() {

        return mathClass;
    }

    /**
     * Getter for mathFamily.
     *
     * @return the mathFamily.
     */
    public int getMathFamily() {

        return mathFamily;
    }
}