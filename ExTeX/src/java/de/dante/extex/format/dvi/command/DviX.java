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
 * DVI: x
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviX extends DviCommand {

    /**
     * the value
     */
    private int value;

    /**
     * the x0
     */
    private boolean x0;

    /**
     * Create a new object.
     * @param opc   the opcode
     * @param sp    the start pointer
     * @param v     the value
     */
    public DviX(final int opc, final int sp, final int v) {

        this(opc, sp, v, false);
    }

    /**
     * Create a new object.
     * @param opc   the opcode
     * @param sp    the start pointer
     * @param v     the value
     * @param x     the x0
     */
    public DviX(final int opc, final int sp, final int v, final boolean x) {

        super(opc, sp);
        value = v;
        x0 = x;
    }

    /**
     * Returns the value.
     * @return Returns the value.
     */
    public int getValue() {

        return value;
    }

    /**
     * Returns the x0.
     * @return Returns the x0.
     */
    public boolean isX0() {

        return x0;
    }

    /**
     * x0
     */
    private static final int X0 = 152;

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        StringBuffer buf = new StringBuffer();
        buf.append("x").append(getOpcode() - X0);
        return buf.toString();
    }
}