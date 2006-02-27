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

package de.dante.extex.typesetter;

/**
 * This class provides some static methods to deal with badness values.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public final class Badness {

    /**
     * The constant <tt>EJECT_PENALTY</tt> contains the penalty which forces
     * a line break. This is an equivalent to -&infin;.
     */
    public static final int EJECT_PENALTY = -10000;

    /**
     * The constant <tt>INF_BAD</tt> contains the value for infinite badness.
     * This is an equivalent to &infin;.
     *
     * @see "TTP [108]"
     */
    public static final int INF_BAD = 10000;

    /**
     * The constant <tt>INF_BAD</tt> contains the value for infinite penalty.
     * This is an equivalent to &infin;.
     */
    public static final int INF_PENALTY = 10000;

    /**
     * Compute the badness.
     *
     * <i>
     * <p>
     * 108. The next subroutine is used to compute the badness of
     * glue, when a total t is supposed to be made from amounts that
     * sum to s. According to The <logo>TeX</logo>book, the badness of this
     * situation is 100(t/s)<sup>3</sup>; however, badness is simply a
     * heuristic, so we need not squeeze out the last drop of accuracy when
     * computing it. All we really want is an approximation that has
     * similar properties.
     * </p>
     * <p>
     * The actual method used to compute the badness is easier to read
     * from the program than to describe in words. It produces an
     * integer value that is a reasonably close approximation to
     * 100(t/s)<sup>3</sup>, and all implementations of <logo>TeX</logo> should
     * use precisely this method. Any badness of 2<sup>13</sup> or more is
     * treated as infinitely bad, and represented by 10000.
     * </p>
     * <p>
     * It is not difficult to prove that
     * </p>
     * <p>
     * badness(t+1,s) &ge; badness(t,s) &ge; badness(t,s+1).
     * </p>
     * <p>
     * The badness function defined here is capable of computing at
     * most 1095 distinct values, but that is plenty.
     * </p>
     * <pre>
     * define inf_bad=10000 {infinitely bad value}
     * </pre>
     * <pre>
     *   function badness(t,s:scaled): halfword; {compute badness, given t &ge; 0}
     *    var r: integer; {approximation to &alpha;t/s, where &alpha;<sup>3</sup> &asymp; 100&sdot;2<sup>18</sup>}
     *  begin if t=0 then badness &larr; 0
     *    else if s &le; 0 then badness &larr; inf_bad
     *     else begin if t &le; 7230584 then r &larr; (t * 297) div s {297<sup>3</sup> = 99.94 × 2<sup>18</sup>}
     *      else if s &ge; 1663497 then r &larr; t div (s div 297)
     *       else r &larr; t;
     *     if r&gt;1290 then badness &larr; inf_bad {1290<sup>3</sup>&lt;2<sup>31</sup>&lt;1291<sup>3</sup>}
     *      else badness &larr; (r*r*r+'400000) div '1000000;
     *     end ; {that was r<sup>3</sup>/2<sup>18</sup>, rounded to the nearest integer}
     *  end ;
     * </pre>
     * </i>
     *
     * @param total total given total >= 0
     * @param sum sum
     *
     * @return the computed badness
     *
     * @see "TTP [108]"
     */
    public static int badness(final long total, final long sum) {

        if (total <= 0) {
            return 0;
        } else if (sum <= 0) {
            return INF_PENALTY;
        }

        long ts; // approximation to a t/s, where a^3 approx 100 * 2^18}

        if (total <= 7230584) {
            ts = (total * 297) / sum; // 297^3=99.94 × 2^18
        } else if (sum >= 1663497) {
            ts = total / (sum / 297);
        } else {
            ts = total;
        }
        return (ts > 1290 ? INF_PENALTY //1290^3<2^31<129^3
                : (int) ((ts * ts * ts + 0x20000) / 0x40000));
        // that was r^3/2^18, rounded to the nearest integer
    }

    /**
     * Creates a new object.
     */
    private Badness() {

    }

}
