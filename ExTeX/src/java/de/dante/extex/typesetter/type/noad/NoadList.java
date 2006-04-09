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

package de.dante.extex.typesetter.type.noad;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a container for a list of Noads.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class NoadList {

    /**
     * The field <tt>list</tt> is the container for the elements of this node
     * list.
     */
    private List list = new ArrayList();

    /**
     * Creates a new object containing no elements.
     *
     */
    public NoadList() {

        super();
    }

    /**
     * Add an arbitrary noad to the list.
     *
     * @param noad the noad to add
     */
    public void add(final Noad noad) {

        list.add(noad);
    }

    /**
     * Test whether the node list is empty.
     *
     * @return <code>true</code>, if the list ist emtpy,
     * otherwise <code>false</code>.
     */
    public boolean empty() {

        return list.size() == 0;
    }

    /**
     * Getter for a node at a given position.
     *
     * @param index the position
     *
     * @return the node at position <i>index</i> of <code>null</code> if index
     * is out of bounds
     */
    public Noad get(final int index) {

        return (Noad) list.get(index);
    }

    /**
     * Getter for the last noad previously stored.
     *
     * @return the last noad or <code>null</code> if none is available
     */
    Noad getLastNoad() {

        int i = list.size();
        return (i > 0 ? (Noad) list.get(i - 1) : null);
    }

    /**
     * Remove an element at a given position.
     *
     * @param index the position
     *
     * @return the element previously located at position <i>index</i>
     */
    public Noad remove(final int index) {

        return (Noad) list.remove(index);
    }

    /**
     * Return the size of the <code>MathList</code>.
     *
     * @return the size of the <code>MathList</code>
     */
    public int size() {

        return list.size();
    }

}
