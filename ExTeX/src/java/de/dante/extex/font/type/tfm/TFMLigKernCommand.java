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

import java.io.IOException;

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * Class for a lig_kern_command.
 *
 * <table border="1">
 *   <thead>
 *     <tr><td>byte</td><td>description</td></tr>
 *   </thead>
 *   <tr><td>first  </td><td>skip_byte, indicates that this is the
 *                  final program step if the byte is 128 or more,
 *                  otherwise the next step is obtained by skipping
 *                  this number of intervening steps.</td></tr>
 *   <tr><td>second </td><td>next_char: if next_char follows the
 *                  current character, then perform the operation
 *                  and stop, otherwise continue.</td></tr>
 *   <tr><td>third  </td><td>op_byte, indicates a ligature step if
 *                  less than 128, a kern step otherwise.</td></tr>
 *   <tr><td>fourth </td><td>remainder</td></tr>
 * </table>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class TFMLigKernCommand implements XMLConvertible {

    /**
     * the skipbyte
     */
    private short skipbyte;

    /**
     * the nextchar
     */
    private short nextchar;

    /**
     * the opbyte
     */
    private short opbyte;

    /**
     * the remainder
     */
    private short remainder;

    /**
     * the lig/kern id
     */
    private int lkid;

    /**
     * Create a new object
     * @param rar   the input
     * @param id    the id
     * @throws IOException if an IO-error occurs.
     */
    public TFMLigKernCommand(final RandomAccessR rar, final int id)
            throws IOException {

        lkid = id;
        skipbyte = (short) rar.read();
        nextchar = (short) rar.read();
        opbyte = (short) rar.read();
        remainder = (short) rar.read();
    }

    /**
     * Value of skipbyte which indicates the boundary information.
     */
    private static final short BOUNDARYFLAG = 255;

    /**
     * Value of skipbyte which indicates the last
     * instruction in a lig/kern program.
     */
    private static final short STOPFLAG = 128;

    /**
     * Value of opbyte which indicates the kerning instruction
     */
    private static final short KERNFLAG = 128;

    /**
     * Gives the unsigned value of the skipbyte.
     * @return Return the amount of skip or the stop or boundary flag.
     */
    public short skipbyte() {

        return (short) (skipbyte & TFMConstants.CONST_XFF);
    }

    /**
     * Gives the unsigned value of the opbyte.
     * @return Return the encoded ligature or kern operation.
     */
    public short opbyte() {

        return (short) (opbyte & TFMConstants.CONST_XFF);
    }

    /**
     * Gives the unsigned value of uninterpreted remainder.
     * @return Return the remainder which meaning depends on
     *         the value of opbyte.
     */
    public short remainder() {

        return (short) (remainder & TFMConstants.CONST_XFF);
    }

    /**
     * Tells whether this LigKern contains information
     * about boundary.
     * (it must be also first or last in the lig/kern table).
     *
     * @return Return <code>true</code> if it contains boundary information.
     */
    public boolean meansBoundary() {

        return (skipbyte() == BOUNDARYFLAG);
    }

    /**
     * Tells whether this LigKern redirects the actual
     * start of a lig/kern program to some other instruction
     * (it must be also the first instruction of some lig/kern program).
     *
     * @return Return <code>true</code> if it is a restart instruction.
     */
    public boolean meansRestart() {

        return (skipbyte() > STOPFLAG);
    }

    /**
     * Tells whether this LigKern is the last instruction
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
     * the position of this LigKern in the lig/kern table.
     * @param pos   the pos
     * @return Returns index to the <code>ligAuxTab</code>
     *         of the next lig/kern instruction.
     */
    public int nextIndex(final int pos) {

        return pos + skipbyte() + 1;
    }

    /**
     * Forces this LigKern to be the last instruction in a lig/kern program.
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

        return (short) (nextchar & TFMConstants.CONST_XFF);
    }

    /**
     * Forces this LigKern to have particular value of nextchar.
     *
     * @param c     the forced value of >nextchar.
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

        return (opbyte() << TFMConstants.CONST_8) + remainder();
    }

    /**
     * Tells whether this LigKern is a kerning instruction.
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

        return (opbyte() - KERNFLAG << TFMConstants.CONST_8) + remainder();
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
     * Forces the ligChar to have particular value.
     *
     * @param c the forced value of ligChar.
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

    /**
     * Returns the nextchar.
     * @return Returns the nextchar.
     */
    public short getNextchar() {

        return nextchar;
    }

    /**
     * Returns the opbyte.
     * @return Returns the opbyte.
     */
    public short getOpbyte() {

        return opbyte;
    }

    /**
     * Returns the remainder.
     * @return Returns the remainder.
     */
    public short getRemainder() {

        return remainder;
    }

    /**
     * Returns the skipbyte.
     * @return Returns the skipbyte.
     */
    public short getSkipbyte() {

        return skipbyte;
    }

    /**
     * Returns the lkid.
     * @return Returns the lkid.
     */
    public int getLkid() {

        return lkid;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("ligkerncommand");
        element.setAttribute("id", String.valueOf(lkid));
        element.setAttribute("skipbyte", String.valueOf(skipbyte));
        element.setAttribute("nextchar", String.valueOf(nextchar));
        element.setAttribute("opbyte", String.valueOf(opbyte));
        element.setAttribute("remainder", String.valueOf(remainder));
        return element;
    }
}
