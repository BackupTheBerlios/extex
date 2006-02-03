/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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
package de.dante.extex.main.errorHandler;

import java.util.logging.Logger;

import de.dante.util.Locator;


/**
 * This is the error handler in <logo>TeX</logo> compatibility mode: the message
 * is presented in a compatible way.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ErrorHandlerTeXImpl extends ErrorHandlerImpl {

    /**
     * Creates a new object.
     */
    public ErrorHandlerTeXImpl() {

        super();
    }

    /**
     * @see de.dante.extex.main.errorHandler.ErrorHandlerImpl#showErrorLine(
     *      java.util.logging.Logger, java.lang.String, de.dante.util.Locator)
     */
    protected void showErrorLine(final Logger logger, final String message,
            final Locator locator) {

        String file = locator.getResourceName();
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
                      + Integer.toString(locator.getLineNumber()) + NL);
    }

}
