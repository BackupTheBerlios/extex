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
 * DVI: post
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviPost extends DviCommand {

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
     * the pointer of the final page
     */
    private int pointer;

    /**
     * height-plus-depth of the tallest page
     */
    private int heigthdepth;

    /**
     * the width of the widest page
     */
    private int width;

    /**
     * the stack depth
     */
    private int stackdepth;

    /**
     * total pages
     */
    private int totalpage;

    /**
     * Create a new object.
     * @param oc    the opcode
     * @param sp    the start pointer
     * @param p     the pointer
     * @param anum  the num
     * @param aden  the den
     * @param amag  the mag
     * @param l     the height+depth
     * @param u     the width
     * @param s     the stack depth
     * @param t     the total pages
     */
    public DviPost(final int oc, final int sp, final int p, final int anum,
            final int aden, final int amag, final int l, final int u,
            final int s, final int t) {

        super(oc, sp);
        pointer = p;
        num = anum;
        den = aden;
        mag = amag;
        heigthdepth = l;
        width = u;
        stackdepth = s;
        totalpage = t;

    }

    /**
     * Returns the den.
     * @return Returns the den.
     */
    public int getDen() {

        return den;
    }

    /**
     * Returns the heigthdepth.
     * @return Returns the heigthdepth.
     */
    public int getHeigthdepth() {

        return heigthdepth;
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
     * Returns the pointer.
     * @return Returns the pointer.
     */
    public int getPointer() {

        return pointer;
    }

    /**
     * Returns the stackdepth.
     * @return Returns the stackdepth.
     */
    public int getStackdepth() {

        return stackdepth;
    }

    /**
     * Returns the totalpage.
     * @return Returns the totalpage.
     */
    public int getTotalpage() {

        return totalpage;
    }

    /**
     * Returns the width.
     * @return Returns the width.
     */
    public int getWidth() {

        return width;
    }

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        return "post";
    }

}
