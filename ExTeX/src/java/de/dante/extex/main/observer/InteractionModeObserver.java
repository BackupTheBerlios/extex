/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main.observer;

import java.util.logging.Handler;
import java.util.logging.Level;

import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.context.ContextInternals;
import de.dante.extex.interpreter.context.observer.InteractionObserver;

/**
 * This observer is used to transport the interaction mode changes to the
 * logger. Thus it is guaranteed that only the appropriate messages are shown.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class InteractionModeObserver implements InteractionObserver {

    /**
     * The field <tt>handler</tt> contains the {@link java.util.logging.Handler
     * Handler} at which the logging should be directed.
     */
    private Handler handler;

    /**
     * Creates a new object.
     *
     * @param theHandler the target handler
     */
    public InteractionModeObserver(final Handler theHandler) {

        super();
        this.handler = theHandler;
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.InteractionObserver#receiveInteractionChange(
     *      de.dante.extex.interpreter.context.ContextInternals,
     *      de.dante.extex.interpreter.Interaction)
     */
    public void receiveInteractionChange(final ContextInternals context,
            final Interaction mode) throws Exception {

        handler.setLevel((Interaction) mode == Interaction.BATCHMODE //
                ? Level.SEVERE : Level.INFO);
    }

}