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
package de.dante.extex.typesetter;


/**
 * This class provides some static methods to deal with badness values.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class Badness {

    /**
     * The constant <tt>INF_BAD</tt> contains the value for infinite badness.
     * This is an equivalent to &infin;.
     *
     * @see "TTP [108]"
     */
    public static final int INF_BAD = 10000;

    /**
     * Creates a new object.
     */
    private Badness() {
    }


    /**
     * Compute the badness.
     *
     * @param t total given t >= 0
     * @param s sum
     *
     * @return the computed badness
     *
     * @see "TTP [108]"
     */
    public static int badness(final long t, final long s) {

        if (t == 0) {
            return 0;
        } else if (s <= 0) {
            return INF_BAD;
        }

        long r; // approximation to a t/s, where a^3 approx 100 * 2^18}

        if (t <= 7230584) {
            r = (t * 297) / s; // 297^3=99.94 × 2^18
        } else if (s >= 1663497) {
            r = t / (s / 297);
        } else {
            r = t;
        }
        return (r > 1290 ? INF_BAD //1290^3<2^31<129^3
                : (int) ((r * r * r + 0x20000) / 0x40000));
        // that was r^3/2^18, rounded to the nearest integer
    }

}
