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

package de.dante.extex.format.dvi;

/**
 * The values of <code>h</code>, <code>v</code>, <code>w</code>,
 * <code>x</code>, <code>y</code>, and <code>z</code> are signed integers
 * having up to 32 bits, including the sign. Since they represent
 * physical distances, there is a small unit of measurement such that
 * increasing <code>h</code> by 1 means moving a certain tiny distance to the
 * right. The actual unit of measurement is variable.
 * <p>
 * The current font <code>f</code> is an integer;
 * this value is changed only by <code>fnt</code> and <code>fnt_num</code>
 * commands.
 * <p>
 * The current position on the page is given by two numbers called
 * the horizontal and vertical coordinates, <code>h</code> and <code>v</code>.
 * Both coordinates are zero at the upper left corner of the page; moving to
 * the right corresponds to increasing the horizontal coordinate, and
 * moving down corresponds to increasing the vertical coordinate. Thus,
 * the coordinates are essentially Cartesian, except that vertical
 * directions are flipped; the Cartesian version of <code>(h,v)</code> would be
 * <code>(h,-v)</code>.
 * </p>
 * <p>
 * The current spacing amounts are given by four numbers
 * <code>w</code>, <code>x</code>, <code>y</code>, and <code>z</code>,
 * where <code>w</code> and <code>x</code> are used for horizontal
 * spacing and where <code>y</code> and <code>z</code> are used for
 * vertical spacing.
 * </p>
 * <p>
 * There is a stack containing <code>(h,v,w,x,y,z)</code> values;
 * the DVI commands <code>push</code> and <code>pop</code> are used
 * to change the current level of operation.
 * Note that the current font <code>f</code> is not pushed and
 * popped; the stack contains only information about positioning.
 * <p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class DviValues {

    /**
     * Create a new object.
     */
    public DviValues() {

        clear();
    }

    /**
     * clear all values (without f!).
     */
    public void clear() {

        h = 0;
        v = 0;
        w = 0;
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * f
     */
    private int f;

    /**
     * h
     */
    private int h;

    /**
     * v
     */
    private int v;

    /**
     * w
     */
    private int w;

    /**
     * x
     */
    private int x;

    /**
     * y
     */
    private int y;

    /**
     * z
     */
    private int z;

    /**
     * Returns the h.
     * @return Returns the h.
     */
    public int getH() {

        return h;
    }

    /**
     * @param ah The h to set.
     */
    public void setH(final int ah) {

        h = ah;
    }

    /**
     * @param ah The h to add.
     */
    public void addH(final int ah) {

        h += ah;
    }

    /**
     * Returns the v.
     * @return Returns the v.
     */
    public int getV() {

        return v;
    }

    /**
     * @param av The v to set.
     */
    public void setV(final int av) {

        v = av;
    }

    /**
     * @param av The v to add.
     */
    public void addV(final int av) {

        v += av;
    }

    /**
     * Returns the w.
     * @return Returns the w.
     */
    public int getW() {

        return w;
    }

    /**
     * @param aw The w to set.
     */
    public void setW(final int aw) {

        w = aw;
    }

    /**
     * Returns the x.
     * @return Returns the x.
     */
    public int getX() {

        return x;
    }

    /**
     * @param ax The x to set.
     */
    public void setX(final int ax) {

        x = ax;
    }

    /**
     * Returns the y.
     * @return Returns the y.
     */
    public int getY() {

        return y;
    }

    /**
     * @param ay The y to set.
     */
    public void setY(final int ay) {

        y = ay;
    }

    /**
     * Returns the z.
     * @return Returns the z.
     */
    public int getZ() {

        return z;
    }

    /**
     * @param az The z to set.
     */
    public void setZ(final int az) {

        z = az;
    }

    /**
     * Returns the f.
     * @return Returns the f.
     */
    public int getF() {

        return f;
    }

    /**
     * @param af The f to set.
     */
    public void setF(final int af) {

        f = af;
    }

    /**
     * set the values (without f!)
     * @param val   the new values
     */
    public void setValues(final DviValues val) {

        h = val.h;
        v = val.v;
        w = val.w;
        x = val.x;
        y = val.y;
        z = val.z;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("f=").append(f);
        buf.append(" h=").append(h);
        buf.append(" v=").append(v);
        buf.append(" w=").append(w);
        buf.append(" x=").append(x);
        buf.append(" y=").append(y);
        buf.append(" z=").append(z);
        return buf.toString();
    }
}
