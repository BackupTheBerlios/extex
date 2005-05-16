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
package de.dante.extex.main.observer;

import java.util.logging.Logger;

import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;

/**
 * This observer waits for update events when files are closed. According to the
 * reference in <logo>TeX</logo> a closing parenthesis is written to the log
 * file.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class FileCloseObserver implements Observer {
    /**
     * The field <tt>logger</tt> contains the logger for output
     */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param theLogger the logger for potential output
     */
    public FileCloseObserver(final Logger theLogger) {
        super();
        this.logger = theLogger;
    }

    /**
     * @see de.dante.util.observer.Observer#update(
     *      de.dante.util.observer.Observable, java.lang.Object)
     */
    public void update(final Observable observable, final Object item) {
        TokenStream stream = (TokenStream) item;

        if (stream.isFileStream()) {
            logger.info(")");
        }
    }
}
