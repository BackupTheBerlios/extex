/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.util.observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.dante.util.GeneralException;

/**
 * This class provides an ordered list of {@link Observer Observer}s.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ObserverList implements Observer {

    /**
     * The field <tt>list</tt> contains the internal lit of observers.
     */
    private List list = new ArrayList();

    /**
     * Creates a new object containing no elements.
     */
    public ObserverList() {

        super();
    }

    /**
     * Add an observer to the list.
     * It is not checked that whether the observer is already contained, i.e.
     * it is possible to have the same observer in the list multiple times.
     *
     * @param observer the observer to add
     */
    public void add(final Observer observer) {

        if (!list.contains(observer)) {
            list.add(observer);
        }
    }

    /**
     * The update methods of all contained observers are invoked in turn with
     * the same arguments.
     *
     * @see de.dante.util.observer.Observer#update(de.gene.bcd.util.Observable,
     *      java.lang.Object)
     */
    public void update(final Observable source, final Object object)
            throws GeneralException {

        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            ((Observer) (iterator.next())).update(source, object);
        }
    }

}