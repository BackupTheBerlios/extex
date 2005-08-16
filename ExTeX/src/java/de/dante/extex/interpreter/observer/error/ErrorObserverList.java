/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.observer.error;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a type-safe list of observers for the error event.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public final class ErrorObserverList implements ErrorObserver {

    /**
     * Take a list and add an observer. If the list is <code>null</code> then
     * a new one is created.
     *
     * @param list the input list or <code>null</code>
     * @param observer the observer to add
     *
     * @return the input list or a new one with the observer added
     */
    public static ErrorObserver register(final ErrorObserver list,
            final ErrorObserver observer) {

        if (list instanceof ErrorObserverList) {
            ((ErrorObserverList) list).add(observer);
        } else if (list == null) {
            ErrorObserverList result = new ErrorObserverList();
            result.add(observer);
            return result;
        } else {
            ErrorObserverList result = new ErrorObserverList();
            result.add(list);
            result.add(observer);
            return result;
        }
        return list;
    }

    /**
     * The field <tt>list</tt> contains the encapsulated list.
     */
    private List list = new ArrayList();

    /**
     * Add an observer to the list.
     *
     * @param observer the observer to add to the list
     */
    public void add(final ErrorObserver observer) {

        list.add(observer);
    }

    /**
     * Invoke all observers on the list to inform them of the error occured.
     *
     * @see de.dante.extex.interpreter.observer.error.ErrorObserver#update(
     *      java.lang.Exception)
     */
    public void update(final Exception e) {

        int size = list.size();
        for (int i = 0; i < size; i++) {
            ((ErrorObserver) list.get(i)).update(e);
        }
    }
}
