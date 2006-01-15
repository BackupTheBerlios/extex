/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.afm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * AFM CharMetric.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class AfmCharMetric implements Serializable {

    /**
     * C.
     */
    private int c = -1;

    /**
     * WX.
     */
    private float wx = AfmHeader.NOTINIT;

    /**
     * Name.
     */
    private String n = "";

    /**
     * B llx.
     */
    private float bllx = AfmHeader.NOTINIT;

    /**
     * B lly.
     */
    private float blly = AfmHeader.NOTINIT;

    /**
     * B urx.
     */
    private float burx = AfmHeader.NOTINIT;

    /**
     * B ury.
     */
    private float bury = AfmHeader.NOTINIT;

    /**
     * Ligatur.
     */
    private HashMap l = null;

    /**
     * Add a ligature.
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
     * Kerning.
     */
    private ArrayList k = null;

    /**
     * Add a kerning.
     * @param kp    The kerning pairs.
     */
    public void addK(final AfmKernPairs kp) {

        if (k == null) {
            k = new ArrayList();
        }
        if (kp != null) {
            k.add(kp);
        }
    }

    /**
     * Returns a kerning pair for a letter,
     * or <code>null</code>, if no kerning is found.
     *
     * @param charpost      The post character.
     * @return Returns a kerning pair for a letter,
     *         or <code>null</code>, if no kerning is found.
     */
    public AfmKernPairs getAfmKernPair(final String charpost) {

        AfmKernPairs kp = null;
        if (k != null && charpost != null) {
            for (int i = 0, j = k.size(); i < j; i++) {
                kp = (AfmKernPairs) k.get(i);
                if (kp.getCharpost().equals(charpost)) {
                    return kp;
                }
            }
        }
        return null;
    }

    /**
     * Returns the ligature for a letter or <code>null</code>, if
     * no ligature found.
     * @param letter    The letter.
     * @return Returns the ligature for a letter or <code>null</code>, if
     *         no ligature found.
     */
    public String getLigature(final String letter) {

        if (l != null && letter != null) {
            return (String) l.get(letter);
        }
        return null;
    }

    /**
     * Returns the bllx.
     * @return Returns the bllx.
     */
    public float getBllx() {

        return bllx;
    }

    /**
     * Set the bllx.
     * @param ibllx The bllx to set.
     */
    public void setBllx(final float ibllx) {

        bllx = ibllx;
    }

    /**
     * Returns the blly.
     * @return Returns the blly.
     */
    public float getBlly() {

        return blly;
    }

    /**
     * Set the blly.
     * @param iblly The blly to set.
     */
    public void setBlly(final float iblly) {

        blly = iblly;
    }

    /**
     * Returns the burx.
     * @return Returns the burx.
     */
    public float getBurx() {

        return burx;
    }

    /**
     * Set the burx.
     * @param iburx The burx to set.
     */
    public void setBurx(final float iburx) {

        burx = iburx;
    }

    /**
     * Returns the bury.
     * @return Returns the bury.
     */
    public float getBury() {

        return bury;
    }

    /**
     * Set the bury.
     * @param ibury The bury to set.
     */
    public void setBury(final float ibury) {

        bury = ibury;
    }

    /**
     * Returns the c.
     * @return Returns the c.
     */
    public int getC() {

        return c;
    }

    /**
     * Set the c.
     * @param ic The c to set.
     */
    public void setC(final int ic) {

        c = ic;
    }

    /**
     * Returns the l.
     * @return Returns the l.
     */
    public HashMap getL() {

        return l;
    }

    /**
     * Returns the n.
     * @return Returns the n.
     */
    public String getN() {

        return n;
    }

    /**
     * Set the n.
     * @param s The n to set.
     */
    public void setN(final String s) {

        n = s;
    }

    /**
     * Returns the wx.
     * @return Returns the wx.
     */
    public float getWx() {

        return wx;
    }

    /**
     * Set the wx.
     * @param iwx The wx to set.
     */
    public void setWx(final float iwx) {

        wx = iwx;
    }
}
