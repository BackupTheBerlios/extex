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

package de.dante.extex.typesetter.type.noad;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.type.Knot;

/**
 * This class provides a container for Noads and Nodes.
 *
 * @see "TTP [???]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class MathList implements Noad {

    /**
     * The field <tt>list</tt> is the container for the elements of this node
     * list.
     */
    private List list = new ArrayList();

    /**
     * Creates a new object without any items.
     */
    public MathList() {

        super();
    }

    /**
     * Add an arbitrary knot to the list.
     *
     * @param knot the knot to add
     */
    public void add(final Knot knot) {

        list.add(knot);
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
     * Getter for a node at a given posotion.
     *
     * @param index the position
     *
     * @return the node at position <i>index</i> of <code>null</code> if index
     * is out of bounds
     */
    public Knot get(final int index) {

        return (Knot) list.get(index);
    }

    /**
     * Remove an element at a given position.
     *
     * @param index the position
     *
     * @return the element previously located at position <i>index</i>
     */
    public Knot remove(final int index) {

        return (Node) this.list.remove(index);
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