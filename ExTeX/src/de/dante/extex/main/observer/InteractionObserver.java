/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.main.observer;

import java.util.logging.Handler;
import java.util.logging.Level;

import de.dante.extex.interpreter.Interaction;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class InteractionObserver implements Observer {

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
    public InteractionObserver(final Handler theHandler) {
        super();
        this.handler = theHandler;
    }

    /**
     * @see de.dante.util.observer.Observer#update(
     *      de.dante.util.observer.Observable, java.lang.Object)
     */
    public void update(final Observable observable, final Object item) {

        handler.setLevel((Interaction) item == Interaction.BATCHMODE //
                ? Level.SEVERE : Level.INFO);
    }

}
