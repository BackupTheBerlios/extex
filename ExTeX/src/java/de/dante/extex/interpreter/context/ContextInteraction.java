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

import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.context.observer.InteractionObserver;
import de.dante.extex.interpreter.exception.InterpreterException;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public interface ContextInteraction {

    /**
     * Getter for the interaction. The interaction determines how verbose the
     * actions are reported and how the interaction with the user is performed
     * in case of an error.
     *
     * @return the current interaction
     */
    Interaction getInteraction();

    /**
     * Register an observer for interaction mode change events.
     * Interaction mode change events are triggered when a new value is assigned
     * to the interaction mode. In this case the appropriate method in the
     * observer is invoked.
     * <p>
     *  A single count register can be observed by giving a name of the count
     *  register to observe. Only changes to this register tigger the
     *  notification. If this name is <code>null</code> the changes to all
     *  registers are reported to the observer.
     * </p>
     *
     * @param observer the observer to receive the events
     */
    void registerInteractionObserver(InteractionObserver observer);

    /**
     * Setter for the interaction in all requested groups. The interaction
     * determines how verbose the actions are reported and how the interaction
     * with the user is performed in case of an error.
     *
     * @param interaction the new value of the interaction
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws InterpreterException in case of an error
     */
    void setInteraction(Interaction interaction, boolean global)
            throws InterpreterException;

    /**
     * Remove a registered observer for interaction mode change events.
     * Interaction mode change events are triggered when a new value is assigned
     * to the interaction mode. In this case the appropriate method in the
     * observer is invoked.
     *
     * @param observer the observer to receive the events
     */
    void unregisterInteractionObserver(InteractionObserver observer);

}