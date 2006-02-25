/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.paragraphBuilder.impl;

import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.WideGlue;

/**
 * This class represents a break point for the paragraph breaking.
 *
 * @see "TTP [819]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class BreakPoint {

    /**
     * The field <tt>fitness</tt> contains the fitness for the break point.
     * If this field is <code>null</code> then this break point is not
     * active.
     * <p>
     * When a list of break points has been constructed, the active flag marks
     * those breakpoints which are currently considered. The passive break
     * points are silently ignored for the current pass.
     * </p>
     */
    private Fitness fitness = null;

    /**
     * The field <tt>penalty</tt> contains the penalty associated to the
     * break point.
     */
    private int penalty;

    /**
     * The field <tt>pointWidth</tt> contains the width to be added in case
     * that this break point is not active.
     */
    private FixedGlue pointWidth;

    /**
     * The field <tt>position</tt> contains the pointer to the first material
     * in the new line. This material might be skipped. Thus in fact the pointer
     * indicates the material not contained in the previous line.
     */
    private int position;

    /**
     * The field <tt>width</tt> contains the width of the line from the
     * previous break point to this one.
     */
    private FixedGlue width;

    /**
     * Creates a new object.
     *
     * @param pos the position in the hlist
     * @param wd the width; i.e. the delta since the previous break point
     * @param pwd the point width for this break point
     * @param pen the penalty for this break point
     */
    public BreakPoint(final int pos, final WideGlue wd, final WideGlue pwd,
            final int pen) {

        super();
        this.position = pos;
        this.width = wd.toGlue();
        this.pointWidth = pwd.toGlue();
        this.penalty = pen;
    }

    /**
     * Getter for fitness.
     * The fitness is one of the fitness classes defined in Fitness.
     * The fitness is <code>null</code> for break points which are not active.
     *
     * @return the fitness.
     */
    public Fitness getFitness() {

        return this.fitness;
    }

    /**
     * Getter for penalty.
     * The penalty has to be added to the overall penalty in case that this
     * break point is active.
     *
     * @return the penalty.
     */
    public int getPenalty() {

        return this.penalty;
    }

    /**
     * Getter for pointWidth.
     *
     * @return the pointWidth.
     */
    public FixedGlue getPointWidth() {

        return this.pointWidth;
    }

    /**
     * Getter for position.
     *
     * @return the position.
     */
    public int getPosition() {

        return this.position;
    }

    /**
     * Getter for width.
     *
     * @return the width.
     */
    public FixedGlue getWidth() {

        return this.width;
    }

    /**
     * Getter for active.
     *
     * @return the active.
     */
    public boolean isActive() {

        return (this.fitness != null);
    }

    /**
     * Setter for active.
     */
    public void setActive() {

        this.fitness = Fitness.DECENT;
    }

    /**
     * Setter for fitness.
     *
     * @param fitness the fitness to set.
     */
    public void setFitness(final Fitness fitness) {

        this.fitness = fitness;
    }

    /**
     * Setter for active.
     */
    public void setPassive() {

        this.fitness = null;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "<" + Integer.toString(position) + ": "
                + (width == null ? "null" : width.toString()) + " ++ "
                + (pointWidth == null ? "null" : pointWidth.toString()) + ">";
    }

}