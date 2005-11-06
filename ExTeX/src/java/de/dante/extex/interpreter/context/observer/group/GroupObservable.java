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

package de.dante.extex.interpreter.context.observer.group;

/**
 * This interface describes the possibility to register an observer for an
 * group event.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface GroupObservable {

    /**
     * Register an observer for group change events.
     * Group change events are triggered when a group is opened or closed.
     * In this case the appropriate method in the observer is invoked.
     *
     * @param name the name or the number of the register
     * @param observer the observer to receive the events
     */
    void registerGroupObserver(GroupObserver observer);

    /**
     * Remove a registered observer for group change events.
     * Group change events are triggered when a group is opened or closed.
     * In this case the appropriate method in the observer is invoked.
     *
     * @param name the name or the number of the register
     * @param observer the observer to receive the events
     */
    void unregisterGroupObserver(GroupObserver observer);

}
