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
 * DVI: fnt_num
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviFntNum extends DviCommand {

    /**
     * the font
     */
    private int font;

    /**
     * Create a new object.
     * @param opc   the opcode
     * @param sp    the start pointer
     * @param f     the font
     */
    public DviFntNum(final int opc, final int sp, final int f) {

        super(opc, sp);
        font = f;
    }

    /**
     * Returns the font.
     * @return Returns the font.
     */
    public int getFont() {

        return font;
    }

    /**
     * fnt1
     */
    private static final int FNT1 = 235;

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        StringBuffer buf = new StringBuffer();
        if (getOpcode() < FNT1) {
            buf.append("fntnum").append(font);
        } else {
            buf.append("fnt").append(getOpcode() - FNT1 + 1);
        }
        return buf.toString();
    }
}
