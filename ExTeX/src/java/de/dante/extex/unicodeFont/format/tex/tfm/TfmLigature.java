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

package de.dante.extex.unicodeFont.format.tex.tfm;

import java.io.Serializable;

/**
 * TFM-Ligature.
 * <p>
 * Ligature instruction
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TfmLigature extends TfmLigKern implements Serializable {

    /**
     * Create a new object.
     *
     * @param skip  the skip amount to the next instruction.
     * @param next  the code of the next character.
     * @param a     character code of ligature character to be inserted.
     * @param l     indication that the current character should
     *              not be removed.
     * @param r     indication that the next character should not be removed.
     * @param s     number of characters from the current one to be stepped
     *              over after performing of this instruction.
     */
    public TfmLigature(final int skip, final short next, final short a,
            final boolean l, final boolean r, final byte s) {

        super(skip, next);
        addingChar = a;
        keepLeft = l;
        keepRight = r;
        stepOver = s;
    }

    /**
     * Character code representing the ligature character to be added
     * between the current and next character in the text if this
     * instruction is activated.
     */
    private short addingChar;

    /**
     * If some of the following flags are not set, the corresponding
     * character in the text is removed after inserting the ligature
     * character (in the process of constituing of ligatures).
     */

    /**
     * Indication that the current character should not be removed.
     */
    private boolean keepLeft;

    /**
     * Indication that the next character should not be removed.
     */
    private boolean keepRight;

    /**
     * Tells how many characters from the current position in the text
     * should be skiped over after performing this instruction.
     */
    private byte stepOver;

    /**
     * Returns the addingChar.
     * @return Returns the addingChar.
     */
    public short getAddingChar() {

        return addingChar;
    }

    /**
     * Returns the keepLeft.
     * @return Returns the keepLeft.
     */
    public boolean isKeepLeft() {

        return keepLeft;
    }

    /**
     * Returns the keepRight.
     * @return Returns the keepRight.
     */
    public boolean isKeepRight() {

        return keepRight;
    }

    /**
     * Returns the stepOver.
     * @return Returns the stepOver.
     */
    public byte getStepOver() {

        return stepOver;
    }
}
