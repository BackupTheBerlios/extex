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


/**
 * An observable is an object which has the feature to register an observer
 * to be invoked upon certain actions.
 * <p>
 * The removing of an observer for an action is not supported until it will be
 * really needed.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface Observable {
    /**
     * This method registers an Observer for a named action. The name is used
     * to distinguish several actions to be monitored. It is modeled as String
     * since it should be readable and it is not expected to have too much
     * impact on the performance. It is not expected that this method is calld
     * too often.
     *
     * @param name the name of the action to be observed
     * @param observer the Observer to be invoked
     *
     * @throws NotObservableException in case that the name does not match any
     *             of the observable actions.
     */
    void registerObserver(final String name, final Observer observer)
            throws NotObservableException;
}
