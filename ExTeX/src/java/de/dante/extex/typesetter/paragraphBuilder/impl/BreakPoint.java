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

package de.dante.extex.typesetter.paragraphBuilder.impl;

import de.dante.extex.interpreter.type.glue.FixedGlue;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
class BreakPoint {

    /**
     * The field <tt>active</tt> contains the ...
     */
    private boolean active = false;

    /**
     * The field <tt>penalty</tt> contains the ...
     */
    private int penalty;

    /**
     * The field <tt>pointWidth</tt> contains the ...
     */
    private FixedGlue pointWidth;

    /**
     * The field <tt>position</tt> contains the pointer to the first material
     * in the new line. This material might be skipped. Thus in fact the pointer
     * indicates the material not contained in the previsous line.
     */
    private int position;

    /**
     * The field <tt>width</tt> contains the ...
     */
    private FixedGlue width;

    /**
     * Creates a new object.
     * @param p the position in the hlist
     * @param w the width; i.e. the delta since the previous break point
     * @param penalty TODO
     */
    public BreakPoint(final int p, final FixedGlue w, final FixedGlue wd,
            int penalty) {

        super();
        this.position = p;
        this.width = w;
        this.pointWidth = wd;
        this.penalty = penalty;
    }

    /**
     * Getter for penalty.
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

        return this.active;
    }

    /**
     * Setter for active.
     */
    public void setActive() {

        this.active = true;
    }

    /**
     * Setter for active.
     */
    public void setPassive() {

        this.active = false;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return Integer.toString(position) + "[" + width.toString() + "]";
    }
}