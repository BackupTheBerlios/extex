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

import de.dante.extex.interpreter.context.observer.DimenObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.dimen.Dimen;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface ContextDimen {

    /**
     * Get the current value of the dimen register with a given name.
     *
     * @param name the name or the number of the register
     *
     * @return the dimen register for the given name
     */
    Dimen getDimen(String name);

    /**
     * Register an observer for dimen change events.
     * Count change events are triggered when a value is assigned to a dimen
     * register. In this case the appropriate method in the
     * observer is invoked.
     * <p>
     *  A single dimen register can be observed by giving a name of the dimen
     *  register to observe. Only changes to this register tigger the
     *  notification. If this name is <code>null</code> the changes to all
     *  registers are reported to the observer.
     * </p>
     *
     * @param name the name or the number of the register
     * @param observer the observer to receive the events
     */
    void registerDimenObserver(String name, DimenObserver observer);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.dimen.Dimen Dimen}
     * register in all requested groups. Dimen registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in <logo>TeX</logo>. This restriction does no longer hold for
     * <logo>ExTeX</logo>.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws InterpreterException in case of problems in an observer
     */
    void setDimen(String name, Dimen value, boolean global)
            throws InterpreterException;

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.dimen.Dimen Dimen}
     * register in all requested groups. Dimen registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in <logo>TeX</logo>. This restriction does no longer hold for
     * <logo>ExTeX</logo>.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws InterpreterException in case of problems in an observer
     */
    void setDimen(String name, long value, boolean global)
            throws InterpreterException;

    /**
     * Remove a registered observer for dimen change events.
     * Count change events are triggered when a value is assigned to a dimen
     * register. In this case the appropriate method in the
     * observer is invoked.
     * <p>
     *  A single dimen register can be observed by giving a name of the dimen
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
    void unregisterDimenObserver(String name, DimenObserver observer);

}