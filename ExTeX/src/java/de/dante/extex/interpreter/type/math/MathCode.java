/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.math;

import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class represents a mathematical character. It consists of a class, a
 * family and a character code.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class MathCode {

    /**
     * The constant <tt>CHAR_MASK</tt> contains the mask for filtering a
     * character code from an integer.
     */
    private static final int CHAR_MASK = 0xff;

    /**
     * The field <tt>CLASS_SHIFT</tt> contains the shift value for the class.
     */
    private static final int CLASS_SHIFT = 12;

    /**
     * The constant <tt>FAMILY_MASK</tt> contains the mask for filtering the
     * family from an integer.
     */
    private static final int FAMILY_MASK = 0xf;

    /**
     * The field <tt>mathChar</tt> contains the character.
     */
    private UnicodeChar mathChar;

    /**
     * The field <tt>mathClass</tt> contains the class.
     */
    private MathClass mathClass;

    /**
     * The field <tt>mathFamily</tt> contains the family.
     */
    private int mathFamily;

    /**
     * Creates a new object.
     *
     * @param mathClass the class
     * @param mathFamily the family
     * @param mathChar the character
     */
    public MathCode(final MathClass mathClass, final int mathFamily,
            final UnicodeChar mathChar) {

        super();
        this.mathClass = mathClass;
        this.mathFamily = mathFamily;
        this.mathChar = mathChar;
    }

    /**
     * Creates a new object.
     *
     * @param code the integer to analyze for the desired field values
     */
    public MathCode(final long code) throws InterpreterException {

        super();
        if (code < 0 || code > 0x8000) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(MathCode.class), "TTP.InvalidCode", //
                    Long.toString(code));
        }
        mathClass = MathClass.getMathClass((int) (code >> CLASS_SHIFT));
        mathFamily = (int) (code >> 8) & FAMILY_MASK;
        mathChar = UnicodeChar.get((int) (code & CHAR_MASK));
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
    public MathClass getMathClass() {

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

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return mathClass.toString() + " " + Integer.toString(mathFamily) + " "
                + mathChar.toString();
    }

}
