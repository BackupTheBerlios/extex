/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.main;

import de.dante.extex.logging.Logger;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.Observable;
import de.dante.util.Observer;

/**
 * This observer waits for update events when files are closed. According to the
 * reference in TeX a closing parenthesis is written to the log file.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FileCloseObserver implements Observer {
    /** the logger for output */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param logger the logger for potential output
     */
    public FileCloseObserver(Logger logger) {
        super();
        this.logger = logger;
    }

    /**
     * @see de.dante.util.Observer#update(de.dante.util.Observable, java.lang.Object)
     */
    public void update(Observable observable, Object item) {
        TokenStream stream = (TokenStream) item;

        if (stream.isFileStream()) {
            logger.info(")");
        }
    }
}
