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
 * The data structure for lig/kern instruction from tfm file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TFMAuxLigKern {

    /**
     * Create a new object.
     * 
     * @param skip      skipbyte
     * @param next      nextchar
     * @param op        opbyte
     * @param r         remainder
     */
    TFMAuxLigKern(final byte skip, final byte next, final byte op, final byte r) {

        super();
        skipbyte = skip;
        nextchar = next;
        opbyte = op;
        remainder = r;
    }

    /**
     * Value of <code>skipbyte()</code> which indicates the boundary
     * information
     */
    private static final short BOUNDARYFLAG = 255;

    /**
     * Value of <code>skipbyte()</code> which indicates the last
     * instruction in a lig/kern program.
     */
    private static final short STOPFLAG = 128;

    /**
     * Value of <code>opbyte()</code> which indicates the kerning
     * instruction
     */
    private static final short KERNFLAG = 128;

    /**
     * Amount of skip or a stop or boundary flag
     */
    private byte skipbyte;

    /**
     * Code of character which must be next to the current one to activate
     * instruction.
     */
    private byte nextchar;

    /**
     * Encoded ligature or kerning operation.
     */
    private byte opbyte;

    /**
     * Remainder which meaning depends on the value of <code>opbyte</code>.
     */
    private byte remainder;

    /**
     * Gives the unsigned value of the <code>skipbyte</code>.
     *
     * @return Return the amount of skip or the stop or boundary flag.
     */
    public short skipbyte() {

        return (short) (skipbyte & 0xff);
    }

    /**
     * Gives the unsigned value of the <code>opbyte</code>.
     *
     * @return Return the encoded ligature or kern operation.
     */
    public short opbyte() {

        return (short) (opbyte & 0xff);
    }

    /**
     * Gives the unsigned value of uninterpreted remainder.
     *
     * @return Return the remainder which meaning depends on the value of <code>opbyte()</code>.
     */
    public short remainder() {

        return (short) (remainder & 0xff);
    }

    /**
     * Tells whether this <code>AuxLigKern</code> contains information
     * about boundary (it must be also first or last in the lig/kern
     * table).
     *
     * @return Return <code>true</code> if it contains boundary information.
     */
    public boolean meansBoundary() {

        return (skipbyte() == BOUNDARYFLAG);
    }

    /**
     * Tells whether this <code>AuxLigKern</code> redirects the actual
     * start of a lig/kern program to some other instruction (it must be
     * also the first instruction of some lig/kern program).
     *
     * @return Return <code>true</code> if it is a restart instruction.
     */
    public boolean meansRestart() {

        return (skipbyte() > STOPFLAG);
    }

    /**
     * Tells whether this <code>AuxLigKern</code> is the last instruction
     * of a lig/kern program.
     *
     * @return Return <code>true</code> if this is the last 
     *         instruction of a lig/kern program.
     */
    public boolean meansStop() {

        return (skipbyte() >= STOPFLAG);
    }

    /**
     * Tells the position of the next lig/kern program instruction given
     * the position of this <code>AuxLigKern</code> in the lig/kern
     * table.
     * @param pos the pos
     * @return index to the <code>ligAuxTab</code> of the next lig/kern
     * instruction.
     */
    public int nextIndex(final int pos) {

        return pos + skipbyte() + 1;
    }

    /**
     * Forces this <code>AuxLigKern</code> to be the last instruction in
     * a lig/kern program.
     */
    public void makeStop() {

        skipbyte = (byte) STOPFLAG;
    }

    /**
     * Gives the code of the character which must be next to the current
     * character if this instruction has to be activated.
     *
     * @return Returns the next character code.
     */
    public short nextChar() {

        return (short) (nextchar & 0xff);
    }

    /**
     * Forces this <code>AuxLigKern</code> to have particular value of
     * <code>nextChar()</code>.
     *
     * @param c the forced value of <code>nextChar()</code>.
     */
    public void setNextChar(final int c) {

        nextchar = (byte) c;
    }

    /**
     * Gives actual starting index of the lig/kern program for restart
     * instruction.
     *
     * @return Returns the actual start of lig/kern program.
     */
    public int restartIndex() {

        return (opbyte() << 8) + remainder();
    }

    /**
     * Tells whether this <code>AuxLigKern</code> is a kerning
     * instruction.
     *
     * @return Returns <code>true</code> for kerning instruction.
     */
    public boolean meansKern() {

        return opbyte() >= KERNFLAG;
    }

    /**
     * Gives the index to the kern table from tfm file for kerning
     * instruction.
     *
     * @return Returns the index to the <code>kernTable</code>.
     */
    public int kernIndex() {

        return (opbyte() - KERNFLAG << 8) + remainder();
    }

    /**
     * Tells whether the current character should be left in place when
     * executing this ligature instructions.
     *
     * @return Returns <code>true</code> if the current character should be left.
     */
    public boolean leaveLeft() {

        return (opbyte() & 0x02) != 0;
    }

    /**
     * Tells whether the next character should be left in place when
     * executing this ligature instructions.
     *
     * @return Returns <code>true</code> if the next character should be left.
     */
    public boolean leaveRight() {

        return (opbyte() & 0x01) != 0;
    }

    /**
     * Tells how many character should be skipped over after executing this
     * ligature instruction.
     *
     * @return the number of characters to be skipped.
     */
    public byte stepOver() {

        return (byte) (opbyte() >>> 2);
    }

    /**
     * Gives the code of charcter which should be inserted between the
     * current and the next characters when executing this ligature
     * instruction.
     *
     * @return Returns the code of the character to be inserted.
     */
    public short ligChar() {

        return remainder();
    }

    /**
     * Forces the <code>ligChar()</code> to have particular value.
     *
     * @param c the forced value of <code>ligChar()</code>.
     */
    public void setLigChar(final short c) {

        remainder = (byte) c;
    }

    /**
     * The value of <code>activity</code> field which means that this
     * lig/kern instruction is not a part of lig/kern program for any
     * character.
     */
    public static final Activity UNREACHABLE = new Activity();

    /**
     * The value of <code>activity</code> field which means that this is
     * restart instruction or the boundary information which was processed.
     */
    public static final Activity PASSTHROUGH = new Activity();

    /**
     * The value of <code>activity</code> field which means that this
     * lig/kern instruction is a part of lig/kern program for some
     * character.
     */
    public static final Activity ACCESSIBLE = new Activity();

    /**
     * The flag determining the status of this lig/kern instruction.
     */
    private Activity activity = UNREACHABLE;

    /**
     * @return Returns the activity.
     */
    public Activity getActivity() {

        return activity;
    }

    /**
     * Set the new value for actifity
     *
     * @param act The activity to set.
     */
    public void setActivity(final Activity act) {

        this.activity = act;
    }

    /**
     * This is a type-safe class to represent activity information.
     */
    public static final class Activity {

        /**
         * Creates a new object.
         */
        public Activity() {

            super();
        }
    }
}