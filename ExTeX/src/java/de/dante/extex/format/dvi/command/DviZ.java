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
 * DVI: z
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviZ extends DviCommand {

    /**
     * the value
     */
    private int value;

    /**
     * the z0
     */
    private boolean z0;

    /**
     * Create a new object.
     * @param opc   the opcode
     * @param sp    the start pointer
     * @param v     the value
     */
    public DviZ(final int opc, final int sp, final int v) {

        this(opc, sp, v, false);
    }

    /**
     * Create a new object.
     * @param opc   the opcode
     * @param sp    the start pointer
     * @param v     the value
     * @param z     the z0
     */
    public DviZ(final int opc, final int sp, final int v, final boolean z) {

        super(opc, sp);
        value = v;
        z0 = z;
    }

    /**
     * Returns the value.
     * @return Returns the value.
     */
    public int getValue() {

        return value;
    }

    /**
     * Returns the z0.
     * @return Returns the zy0.
     */
    public boolean isZ0() {

        return z0;
    }

    /**
     * z0
     */
    private static final int Z0 = 166;

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        StringBuffer buf = new StringBuffer();
        buf.append("z").append(getOpcode() - Z0);
        return buf.toString();
    }

}