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
 * DVI: bop
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviBOP extends DviCommand {

    /**
     * the c
     */
    private int[] c;

    /**
     * the p
     */
    private int p;

    /**
     * Create a new object.
     * @param oc        the opcode
     * @param sp        the start pointer
     * @param carray    the c-array
     * @param ap        the p
     */
    public DviBOP(final int oc, final int sp, final int[] carray, final int ap) {

        super(oc, sp);
        c = carray;
        p = ap;
    }

    /**
     * Returns the c.
     * @return Returns the c.
     */
    public int[] getC() {

        return c;
    }

    /**
     * Returns the p.
     * @return Returns the p.
     */
    public int getP() {

        return p;
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        return "bop";
    }
}
