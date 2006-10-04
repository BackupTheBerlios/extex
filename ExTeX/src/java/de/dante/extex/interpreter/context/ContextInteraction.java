/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.interaction.Interaction;

/**
 * This interface describes the container for interaction-related data of an
 * interpreter context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public interface ContextInteraction {

    /**
     * Getter for the interaction. The interaction determines how verbose the
     * actions are reported and how the interaction with the user is performed
     * in case of an error.
     *
     * @return the current interaction
     *
     * @see #setInteraction(Interaction)
     */
    Interaction getInteraction();

    /**
     * Setter for the interaction in all requested groups. The interaction
     * determines how verbose the actions are reported and how the interaction
     * with the user is performed in case of an error.
     * @param interaction the new value of the interaction
     *
     * @throws InterpreterException in case of an error
     *
     * @see #getInteraction()
     */
    void setInteraction(Interaction interaction) throws InterpreterException;

}
