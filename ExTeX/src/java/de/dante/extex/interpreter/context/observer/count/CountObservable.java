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

package de.dante.extex.interpreter.context.observer.count;

/**
 * This interface describes the possibility to register an observer for an
 * expansion event.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface CountObservable {

    /**
     * Register an observer for count change events.
     * Count change events are triggered when a value is assigned to a count
     * register. In this case the appropriate method in the
     * observer is invoked.
     * <p>
     *  A single count register can be observed by giving a name of the count
     *  register to observe. Only changes to this register tigger the
     *  notification. If this name is <code>null</code> the changes to all
     *  registers are reported to the observer.
     * </p>
     *
     * @param name the name or the number of the register
     * @param observer the observer to receive the events
     */
    void registerCountObserver(String name, CountObserver observer);


    /**
     * Remove a registered observer for count change events.
     * Count change events are triggered when a value is assigned to a count
     * register. In this case the appropriate method in the
     * observer is invoked.
     * <p>
     *  A single count register can be observed by giving a name of the count
     *  register to observe. The deregistration removes all instances of the
     *  observer for this register. If none is registered then nothing happens.
     * </p>
     * <p>
     *  If this name is <code>null</code> then the observer for all registers
     *  is removed. Note that the observers for named registeres are not
     *  effected. They have to be unregistered individually.
     * </p>
     *
     * @param name the name or the number of the register
     * @param observer the observer to receive the events
     */
    void unregisterCountObserver(String name, CountObserver observer);

}
