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

package de.dante.extex.font.type;

import de.dante.extex.interpreter.type.dimen.Dimen;

/**
 * Container for a BoundingBox.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class BoundingBox {

    /**
     * llx
     */
    private Dimen llx;

    /**
     * lly
     */
    private Dimen lly;

    /**
     * urx
     */
    private Dimen urx;

    /**
     * ury
     */
    private Dimen ury;

    /**
     * Create a new object.
     *
     * @param lx    the llx
     * @param ly    the lly
     * @param rx    the urx
     * @param ry    the ury
     */
    public BoundingBox(final Dimen lx, final Dimen ly, final Dimen rx,
            final Dimen ry) {

        super();
        llx = lx;
        lly = ly;
        urx = rx;
        ury = ry;
    }

    /**
     * Returns the llx.
     * @return Returns the llx.
     */
    public Dimen getLlx() {

        return llx;
    }

    /**
     * @param lx The llx to set.
     */
    public void setLlx(final Dimen lx) {

        llx = lx;
    }

    /**
     * Returns the lly.
     * @return Returns the lly.
     */
    public Dimen getLly() {

        return lly;
    }

    /**
     * @param ly The lly to set.
     */
    public void setLly(final Dimen ly) {

        lly = ly;
    }

    /**
     * Returns the urx.
     * @return Returns the urx.
     */
    public Dimen getUrx() {

        return urx;
    }

    /**
     * @param rx The urx to set.
     */
    public void setUrx(final Dimen rx) {

        urx = rx;
    }

    /**
     * Returns the ury.
     * @return Returns the ury.
     */
    public Dimen getUry() {

        return ury;
    }

    /**
     * @param ry The ury to set.
     */
    public void setUry(final Dimen ry) {

        ury = ry;
    }
}