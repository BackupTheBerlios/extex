/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.paragraphBuilder;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ParagraphShape {

    /**
     * The field <tt>points</tt> contains the list of reference points for
     * the parshape. There are always two consecutive points for the left and
     * the right margin.
     */
    private List points = new ArrayList();

    /**
     * Creates a new object.
     */
    public ParagraphShape() {

        super();
    }

    /**
     * Add another pair of points.
     *
     * @param left the left margin
     * @param right the right margin
     */
    public void add(final Dimen left, final Dimen right) {

        points.add(left);
        points.add(right);
    }

    /**
     * Getter for the left hand margin of a certain position.
     * The position is given by an index into the list.
     *
     * @param index the index of the position
     *
     * @return the left hand margin
     */
    public FixedDimen getLeft(final int index) {

        int i = points.size() / 2;
        return (Dimen) points.get((index > i ? i : index) * 2);
    }

    /**
     * Getter for the right hand margin of a certain position.
     * The position is given by an index into the list.
     *
     * @param index the index of the position
     *
     * @return the right hand margin
     */
    public FixedDimen getRight(final int index) {

        int i = points.size() / 2;
        return (Dimen) points.get((index > i ? i : index) * 2 + 1);
    }

    /**
     * Getter for the number of points.
     *
     * @return the number of points stored in this instance
     */
    public long getSize() {

        return points.size();
    }
}