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

import java.util.logging.Logger;

import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;

/**
 * This observer waits for an update event and writes the argument as info to
 * the Logger specified upon construction.
 * <p>
 * This observer is meant for writing the message of the primitive \message to
 * the appropriate output stream.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de"> Gerd Neugebauer </a>
 *
 * @version $Revision: 1.2 $
 */
public class LogMessageObserver implements Observer {

    /**
     * The field <tt>logger</tt> contains the logger for output.
     */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param aLogger the logger for potential output
     */
    public LogMessageObserver(final Logger aLogger) {
        super();
        this.logger = aLogger;
    }

    /**
     * @see de.dante.util.observer.Observer#update(
     *      de.dante.util.observer.Observable, java.lang.Object)
     */
    public void update(final Observable observable, final Object item) {
        logger.fine(item.toString() + " ");
    }

}
