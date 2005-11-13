/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

// created: 2004-08-01

package de.dante.extex.backend.documentWriter.dvi;

/**
 * This is a implementation of class holding the dvi postions register.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.1 $
 */
public class DviPositions implements Cloneable {

    /**
     * The current horizontal position.
     *
     */
    private int dviH;

    /**
     * The current vertical position.
     *
     */
    private int dviV;

    /**
     * Variable for repeated horizontal space.
     *
     */
    private int dviW;

    /**
     * Variable for repeated horizontal space.
     *
     */
    private int dviX;

    /**
     * Variable for repeated vertical space.
     *
     */
    private int dviY;

    /**
     * Variable for repeated vertical space.
     *
     */
    private int dviZ;

    /**
     * Creates a new instance.
     *
     * @param h horizontal position
     * @param v vertical position
     * @param w for repeated horizontal space
     * @param x for repeated horizontal space
     * @param y for repeated vertical space
     * @param z for repeated vertical space
     */
    public DviPositions(final int h, final int v, final int w, final int x,
            final int y, final int z) {

        super();
        dviH = h;
        dviV = v;
        dviW = w;
        dviX = x;
        dviY = y;
        dviZ = z;
    }

    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance
     */
    public Object clone() {

        DviPositions obj;

        try {
            obj = (DviPositions) super.clone();
            obj.dviH = dviH;
            obj.dviV = dviV;
            obj.dviW = dviW;
            obj.dviX = dviX;
            obj.dviY = dviY;
            obj.dviZ = dviZ;
        } catch (CloneNotSupportedException e) {
            obj = new DviPositions(dviH, dviV, dviW, dviX, dviY, dviZ);
        }

        return obj;
    }

    /**
     * Increment horizontal position.
     *
     * @param increment for incrementing
     */
    public void addToH(final int increment) {

        dviH += increment;
    }

    /**
     * Increment vertical position.
     *
     * @param increment for incrementing
     */
    public void addToV(final int increment) {

        dviV += increment;
    }

    /**
     * Get horizontal position.
     *
     * @return horizontal position
     */
    public int getH() {

        return dviH;
    }

    /**
     * Get vertical position.
     *
     * @return vertical position
     */
    public int getV() {

        return dviV;
    }
}