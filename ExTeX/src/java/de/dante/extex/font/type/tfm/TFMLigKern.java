/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.tfm;

/**
 * TFM-LigKern
 * <p>
 * Base class for <code>Ligature</code> and <code>Kerning</code> instructions.
 * It handles the skip amount to the next instruction in the
 * kern/lig program and the character code for the next character.
 * @see <a href="package-summary.html#lig_kern">lig_kern</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public abstract class TFMLigKern {

    /**
     * The skip amount
     */
    private int skip;

    /**
     * Character code representing the character which must be next to the
     * current one to activate this instruction.
     */
    private short nextChar;

    /**
     * Create a new object
     *
     * @param askip the skip amount to the next instruction. <code>0</code>
     *        means the folowing instruction is the next, a number <code>< 0</code>
     *        means that there is no next instruction (this is the last).
     * @param next the code of the next character.
     */
    public TFMLigKern(final int askip, final short next) {

        skip = askip;
        nextChar = next;
    }

    /**
     * Return the kern
     * @return Return the kern
     */
    public TFMFixWord getKern() {

        return TFMFixWord.NULL;
    }

    /**
     * Tells the index to the ligtable of the next instruction of lig/kern
     * program for given index of this instruction.
     *
     * @param pos the index of this instruction.
     * @return the index of the next instruction or <code>NOINDEX</code>
     * if this is the last instruction of the lig/kern program.
     */
    public int nextIndex(final int pos) {

        return (skip < 0) ? TFMCharInfo.NOINDEX : pos + skip + 1;
    }

    /**
     * @return Returns the nextChar.
     */
    public short getNextChar() {

        return nextChar;
    }
}