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
 */

package de.dante.util;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.GlueComponent;

/**
 * This class implements a converter e.g for dimen values.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public final class Unit {

    /**
     * private: no instance
     */
    private Unit() {

    }

    /**
     * basis 10
     */
    private static final int BASIS10 = 10;

    /**
     * den
     */
    private static final int DEN = 7227;

    /**
     * mul for bp
     */
    private static final int MULBP = 7200;

    /**
     * mul for mm
     */
    private static final int MULMM = 2540;

    /**
     * mul for cm
     */
    private static final int MULCM = 254;

    /**
     * mul for in
     */
    private static final int MULIN = 100;

    /**
     * shift
     */
    private static final int SHIFT = 16;

    /**
     * Return the <code>Dimen</code>-value as BP (big point)
     * @param value the <code>Dimen</code>
     * @return the <code>Dimen</code>-value as BP
     */
    public static double getDimenAsBP(final Dimen value) {

        return ((double) value.getValue() * MULBP) / (DEN << SHIFT);
    }

    /**
     * Return the <code>Dimen</code>-value as PT (point)
     * @param value the <code>Dimen</code>
     * @return the <code>Dimen</code>-value as PT
     */
    public static double getDimenAsPT(final Dimen value) {

        return ((double) value.getValue()) / GlueComponent.ONE;
    }

    /**
     * Return the <code>Dimen</code>-value as MM (milimeter)
     * @param value the <code>Dimen</code>
     * @return the <code>Dimen</code>-value as MM
     */
    public static double getDimenAsMM(final Dimen value) {

        return ((double) value.getValue() * MULMM) / (DEN << SHIFT);
    }

    /**
     * Return the <code>Dimen</code>-value as CM (centimeter)
     * @param value the <code>Dimen</code>
     * @return the <code>Dimen</code>-value as CM
     */
    public static double getDimenAsCM(final Dimen value) {

        return ((double) value.getValue() * MULCM) / (DEN << SHIFT);
    }

    /**
     * Return the <code>Dimen</code>-value as IN (inch)
     * @param value the <code>Dimen</code>
     * @return the <code>Dimen</code>-value as IN
     */
    public static double getDimenAsIN(final Dimen value) {

        return ((double) value.getValue() * MULIN) / (DEN << SHIFT);
    }

    /**
     * Round the double-value to a number of decimals.
     * @param value the double-value
     * @param round the number of decimals to round (not round: negative value)
     * @return the rounded double-value
     */
    public static double round(final double value, final int round) {

        if (round < 0) {
            return value;
        } else {
            return Math.round(value * Math.pow(BASIS10, round))
                    / Math.pow(BASIS10, round);
        }
    }
}