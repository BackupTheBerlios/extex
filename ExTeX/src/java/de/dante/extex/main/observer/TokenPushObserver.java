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

import java.util.logging.Logger;

import de.dante.extex.interpreter.observer.push.PushObserver;
import de.dante.extex.scanner.type.token.Token;

/**
 * Observer for the operation of pushing a token to the token stream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class TokenPushObserver implements PushObserver {

    /**
     * The field <tt>logger</tt> contains the logger for output
     */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param theLogger the logger for potential output
     */
    public TokenPushObserver(final Logger theLogger) {

        super();
        this.logger = theLogger;
    }

    /**
     * @see de.dante.extex.interpreter.observer.push.PushObserver#update(
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void update(final Token token) {

        logger.fine(": push " + token.toString() + "\n");
    }
}