/*
 * Copyright (C) 2004 Gerd Neugebauer
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

import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MathCode {

    /**
     * The field <tt>mathClass</tt> contains the ...
     */
    private int mathClass;

    /**
     * The field <tt>mathFamily</tt> contains the ...
     */
    private int mathFamily;

    /**
     * The field <tt>uc</tt> contains the ...
     */
    private UnicodeChar mathChar;

    /**
     * Creates a new object.
     * 
     * @param aClass ...
     * @param aFamily ...
     * @param aChar ...
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
     * @param code ...
     */
    public MathCode(final long code) {

        super();
        mathClass = (int) (code >> 12);
        mathFamily = (int) (code >> 8) & 4;
        mathChar = new UnicodeChar((int) (code & 0xff));
    }

    /**
     * Getter for mathChar.
     *
     * @return the mathChar.
     */
    private UnicodeChar getMathChar() {

        return mathChar;
    }

    /**
     * Getter for mathClass.
     *
     * @return the mathClass.
     */
    private int getMathClass() {

        return mathClass;
    }

    /**
     * Getter for mathFamily.
     *
     * @return the mathFamily.
     */
    private int getMathFamily() {

        return mathFamily;
    }
}