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
 * DVI: rule
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviRule extends DviCommand {

    /**
     * the height
     */
    private int height;

    /**
     * the width
     */
    private int width;

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
     * @param sp    the start pointer
     * @param a     the height
     * @param b     the width
     */
    public DviRule(final int opc, final int sp, final int a, final int b) {

        this(opc, sp, a, b, false);
    }

    /**
     * Create a new object.
     * @param opc   the opcode
     * @param sp    the start pointer
     * @param a     the height
     * @param b     the width
     * @param pm    the put mode
     */
    public DviRule(final int opc, final int sp, final int a, final int b,
            final boolean pm) {

        super(opc, sp);
        height = a;
        width = b;
        put = pm;
    }

    /**
     * Returns the height.
     * @return Returns the height.
     */
    public int getHeight() {

        return height;
    }

    /**
     * Returns the width.
     * @return Returns the width.
     */
    public int getWidth() {

        return width;
    }

    /**
     * Returns the put.
     * @return Returns the put.
     */
    public boolean isPut() {

        return put;
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        if (put) {
            return "putrule";
        }
        return "setrule";
    }
}