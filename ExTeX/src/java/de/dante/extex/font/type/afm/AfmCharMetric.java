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

package de.dante.extex.font.type.afm;

import java.util.HashMap;

/**
 * AFM CharMetric.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

class AfmCharMetric {

    /**
     * C
     */
    private int c = -1;

    /**
     * WX
     */
    private int wx = AfmHeader.NOTINIT;

    /**
     * Name
     */
    private String n = "";

    /**
     * B llx
     */
    private int bllx = AfmHeader.NOTINIT;

    /**
     * B lly
     */
    private int blly = AfmHeader.NOTINIT;

    /**
     * B urx
     */
    private int burx = AfmHeader.NOTINIT;

    /**
     * B ury
     */
    private int bury = AfmHeader.NOTINIT;

    /**
     * Ligatur
     */
    private HashMap l = null;

    /**
     * Add a ligature
     * @param letter    the basic letter
     * @param lig        the ligature
     */
    public void addL(final String letter, final String lig) {

        if (l == null) {
            l = new HashMap();
        }
        l.put(letter, lig);
    }

    /**
     * @return Returns the bllx.
     */
    public int getBllx() {

        return bllx;
    }

    /**
     * @param ibllx The bllx to set.
     */
    public void setBllx(final int ibllx) {

        bllx = ibllx;
    }

    /**
     * @return Returns the blly.
     */
    public int getBlly() {

        return blly;
    }

    /**
     * @param iblly The blly to set.
     */
    public void setBlly(final int iblly) {

        blly = iblly;
    }

    /**
     * @return Returns the burx.
     */
    public int getBurx() {

        return burx;
    }

    /**
     * @param iburx The burx to set.
     */
    public void setBurx(final int iburx) {

        burx = iburx;
    }

    /**
     * @return Returns the bury.
     */
    public int getBury() {

        return bury;
    }

    /**
     * @param ibury The bury to set.
     */
    public void setBury(final int ibury) {

        bury = ibury;
    }

    /**
     * @return Returns the c.
     */
    public int getC() {

        return c;
    }

    /**
     * @param ic The c to set.
     */
    public void setC(final int ic) {

        c = ic;
    }

    /**
     * @return Returns the l.
     */
    public HashMap getL() {

        return l;
    }

    /**
     * @return Returns the n.
     */
    public String getN() {

        return n;
    }

    /**
     * @param s The n to set.
     */
    public void setN(final String s) {

        n = s;
    }

    /**
     * @return Returns the wx.
     */
    public int getWx() {

        return wx;
    }

    /**
     * @param iwx The wx to set.
     */
    public void setWx(final int iwx) {

        wx = iwx;
    }
}