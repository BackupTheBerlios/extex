/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
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
package de.dante.extex.main;

import java.util.logging.Logger;

import de.dante.util.Locator;
import de.dante.util.configuration.Configuration;


/**
 * This is the error handler in TeX compatibility mode: the message is presented
 * in a compatible way.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ErrorHandlerTeXImpl extends ErrorHandlerImpl {

    /**
     * The constant <tt>NL</tt> contains the String with the newline character,
     * since it is needed several times.
     */
    private static final String NL = "\n";

    /**
     * Creates a new object.
     *
     * @param configuration the configuration
     * @param theLogger the logger for the interaction logging
     */
    public ErrorHandlerTeXImpl(final Configuration configuration,
            final Logger theLogger) {

        super(configuration, theLogger);
    }

    /**
     * @see de.dante.extex.main.ErrorHandlerImpl#showErrorLine(
     *      java.util.logging.Logger, java.lang.String, de.dante.util.Locator)
     */
    protected void showErrorLine(final Logger logger, final String message,
            final Locator locator) {

        String file = locator.getFilename();
        String line = locator.getLine();
        int pointer = locator.getLinePointer();
        StringBuffer sb = new StringBuffer();
        file = (file == null ? "<>" : "<" + file + ">");

        for (int i = pointer + file.length(); i > 0; i--) {
            sb.append(' ');
        }

        logger.severe(NL + "!" + message + NL + file
                      + line.substring(0, pointer - 1) + NL + sb.toString()
                      + line.substring(pointer) + "l."
                      + Integer.toString(locator.getLineno()) + NL);
    }

}
