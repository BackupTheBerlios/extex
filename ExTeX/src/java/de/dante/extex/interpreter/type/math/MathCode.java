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
import de.dante.extex.typesetter.type.noad.MathGlyph;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class represents a mathematical character. It consists of a class, a
 * family and a character code.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
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
     * The field <tt>mathGlyph</tt> contains the glyph.
     */
    private MathGlyph mathGlyph;

    /**
     * The field <tt>mathClass</tt> contains the class.
     */
    private MathClass mathClass;

    /**
     * Creates a new object.
     *
     * @param mathClass the class
     * @param mathGlyph the glyph
     */
    public MathCode(final MathClass mathClass, final MathGlyph mathGlyph) {

        super();
        this.mathClass = mathClass;
        this.mathGlyph = mathGlyph;
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
        } else if (code == 0x8000) {
            mathClass = null;
            mathGlyph = null;
        } else {
            mathClass = MathClass.getMathClass((int) (code >> CLASS_SHIFT));
            mathGlyph = new MathGlyph((int) (code >> 8) & FAMILY_MASK,
                    UnicodeChar.get((int) (code & CHAR_MASK)));
        }
    }

    /**
     * Getter for mathGlyph.
     *
     * @return the mathGlyph
     */
    public MathGlyph getMathGlyph() {

        return this.mathGlyph;
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
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return mathClass.toString() + " " + mathGlyph.toString();
    }

    /**
     * Print the instance to a StringBuffer.
     *
     * @param sb the target string buffer
     */
    public void toString(final StringBuffer sb) {

        mathClass.toString(sb);
        sb.append(' ');
        mathGlyph.toString(sb);
    }

}
