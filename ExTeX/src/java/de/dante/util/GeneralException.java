/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.util;


/**
 * This is a base class for exceptions which carry a return code.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class GeneralException extends Exception {
    /**
     * The field <tt>exitCode</tt> contains the exit code.
     */
    private int exitCode = -1;

    /**
     * Creates a new object with the default exit code of -1.
     */
    public GeneralException() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param code the exit code
     */
    public GeneralException(final int code) {
        super();
        exitCode = code;
    }

    /**
     * Creates a new object with the default exit code of -1.
     *
     * @param message the message
     */
    public GeneralException(final String message) {
        super(message);
    }

    /**
     * Creates a new object with the default exit code of -1.
     *
     * @param message the message
     * @param cause the cause for a chained exception
     */
    public GeneralException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new object with the default exit code of -1.
     *
     * @param cause the cause for a chained exception
     */
    public GeneralException(final Throwable cause) {
        super(cause);
    }

    /**
     * Getter for the exit code.
     *
     * @return the exit code
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Getter for further help information.
     *
     * @return the help information
     */
    public String getHelp() {
        return null;
    }
}
