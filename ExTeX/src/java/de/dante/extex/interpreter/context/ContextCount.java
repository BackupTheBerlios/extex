/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context;

import de.dante.extex.interpreter.context.observer.CountObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.count.Count;

/**
 * This interface describes the container for count registers of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public interface ContextCount {

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.count.Count count}
     * register. Count registers are named, either with a number or an
     * arbitrary string. The numbered registers where limited to 256 in TeX.
     * This restriction does not longer hold for ExTeX.
     *
     * @param name the name or number of the count register
     *
     * @return the count register or <code>null</code> if it is not defined
     */
    Count getCount(String name);

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
     * Setter for the {@link de.dante.extex.interpreter.type.count.Count count}
     * register in all requested groups. Count registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws InterpreterException in case if an exception in a registered
     *  observer
     */
    void setCount(String name, long value, boolean global)
            throws InterpreterException;

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