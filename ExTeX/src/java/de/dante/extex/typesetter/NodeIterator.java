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
 *
 */
package de.dante.extex.typesetter;

import java.util.Iterator;
import java.util.List;

/**
 * Type-safe Iterator for a <code>NodeList</code>.
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class NodeIterator {

    /**
     * The field <tt>iterator</tt> contains the wrapped iterator.
     */
    private Iterator iterator;

    /**
     * Creates a new object.
     *
     * @param list the list to iterate through
     */
    public NodeIterator(final List list) {
        super();
        iterator = list.iterator();
    }

    /**
     * Check, if the iterator have a next element.
     *
     * @return <code>true</code>, if there are more elements, otherwise
     *         <code>false</code>
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Return the next element.
     *
     * @return the next element
     */
    public Node next() {
        return ((Node) iterator.next());
    }
}
