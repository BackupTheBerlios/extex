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
 * DVI: pre
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviPre extends DviCommand {

    /**
     * the identifier
     */
    private int identifier;

    /**
     * the num
     */
    private int num;

    /**
     * the den
     */
    private int den;

    /**
     * the mag
     */
    private int mag;

    /**
     * the comment
     */
    private String comment;

    /**
     * Create a new object.
     * @param oc    the opcode
     * @param sp    the start pointer
     * @param i     the indentification
     * @param anum  the num
     * @param aden  the den
     * @param amag  the mag
     * @param com   the comment
     */
    public DviPre(final int oc, final int sp, final int i, final int anum,
            final int aden, final int amag, final String com) {

        super(oc, sp);
        identifier = i;
        num = anum;
        den = aden;
        mag = amag;
        comment = com;

    }

    /**
     * Returns the comment.
     * @return Returns the comment.
     */
    public String getComment() {

        return comment;
    }

    /**
     * Returns the den.
     * @return Returns the den.
     */
    public int getDen() {

        return den;
    }

    /**
     * Returns the identifier.
     * @return Returns the identifier.
     */
    public int getIdentifier() {

        return identifier;
    }

    /**
     * Returns the mag.
     * @return Returns the mag.
     */
    public int getMag() {

        return mag;
    }

    /**
     * Returns the num.
     * @return Returns the num.
     */
    public int getNum() {

        return num;
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        return "pre";
    }
}
