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

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class Fitness {

    /**
     * The field <tt>TIGHT</tt> contains the ...
     */
    public static final Fitness TIGHT = new Fitness(3);

    /**
     * The field <tt>LOOSE</tt> contains the ...
     */
    public static final Fitness LOOSE = new Fitness(1);

    /**
     * The field <tt>VERY_LOOSE</tt> contains the ...
     */
    public static final Fitness VERY_LOOSE = new Fitness(0);

    /**
     * The field <tt>DESCENT</tt> contains the ...
     */
    public static final Fitness DECENT = new Fitness(2);

    /**
     * The field <tt>order</tt> contains the ...
     */
    private int order;

    /**
     * Creates a new object.
     * The coinstructor is private since only the static instances defined
     * above are allowed.
     *
     * @param ord ...
     */
    private Fitness(final int ord) {

        super();
        this.order = ord;
    }

    /**
     * ...
     *
     * @param f ...
     *
     * @return ...
     */
    public boolean adjacent(final Fitness f) {

        return (f.order == order || f.order + 1 == order || f.order - 1 == order);
    }
}