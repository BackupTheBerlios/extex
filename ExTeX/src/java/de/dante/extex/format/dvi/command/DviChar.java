/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.format.dvi.command;

/**
 * DVI: character
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class DviChar extends DviCommand {

    /**
     * the character
     */
    private int ch;

    /**
     * put-mode
     * (The 'put' commands are exactly like the 'set' commands,
     * except that they simply put out a character or a rule without moving
     * the reference point afterwards.)
     */
    private boolean put;

    /**
     * Create a new object.
     * @param opc   the opcode
     * @param sp        the start pointer
     * @param c     the character
     */
    public DviChar(final int opc, final int sp, final int c) {

        this(opc, sp, c, false);
    }

    /**
     * Create a new object.
     * @param opc   the opcode
     * @param sp        the start pointer
     * @param c     the character
     * @param pm    the put-mode
     */
    public DviChar(final int opc, final int sp, final int c, final boolean pm) {

        super(opc, sp);
        ch = c;
        put = pm;
    }

    /**
     * Returns the ch.
     * @return Returns the ch.
     */
    public int getCh() {

        return ch;
    }

    /**
     * Returns the put.
     * @return Returns the put.
     */
    public boolean isPut() {

        return put;
    }

    /**
     * Returns <code>true</code>, if the command ist setx oder putx.
     * @return Returns <code>true</code>, if the command ist setx oder putx.
     */
    public boolean isSetPut() {

        if ((getOpcode() >= MIN_SET && getOpcode() <= MAX_SET)
                || (getOpcode() >= MIN_PUT && getOpcode() <= MAX_PUT)) {
            return true;
        }
        return false;
    }

    /**
     * MIN_SET_CHAR
     */
    private static final int MIN_SET_CHAR = 0;

    /**
     * MAX_SET_CHAR
     */
    private static final int MAX_SET_CHAR = 127;

    /**
     * MIN_SET
     */
    private static final int MIN_SET = 128;

    /**
     * MAX_SET
     */
    private static final int MAX_SET = 131;

    /**
     * MIN_PUT
     */
    private static final int MIN_PUT = 133;

    /**
     * MAX_PUT
     */
    private static final int MAX_PUT = 136;

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        StringBuffer buf = new StringBuffer();
        if (getOpcode() >= MIN_SET_CHAR && getOpcode() <= MAX_SET_CHAR) {
            buf.append("setchar").append(getOpcode());
        } else if (getOpcode() >= MIN_SET && getOpcode() <= MAX_SET) {
            buf.append("set").append(getOpcode() - MAX_SET_CHAR);
        } else {
            buf.append("put").append(getOpcode() - MIN_PUT + 1);
        }
        return buf.toString();
    }
}
