/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import java.util.logging.Logger;

import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;

/**
 * Observer for tokens which are read from the token stream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class TokenObserver implements Observer {

    /**
     * The field <tt>logger</tt> contains the logger for output
     */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param theLogger the logger for potential output
     */
    public TokenObserver(final Logger theLogger) {

        super();
        this.logger = theLogger;
    }

    /**
     * @see de.dante.util.observer.Observer#update(
     *      de.dante.util.observer.Observable, java.lang.Object)
     */
    public void update(final Observable observable, final Object item) {

        logger.fine(item.toString() + "\n");
    }

}