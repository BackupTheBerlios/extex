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
package de.dante.util;


/**
 * This is a bse class for exceptions which carry a return code.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class GeneralException extends Exception {
    /** the exit code attribute */
    private int exitCode = -1;

    /**
     * Creates a new object.
     */
    public GeneralException() {
        super();
    }

    /**
     * Creates a new object.
     */
    public GeneralException(int code) {
        super();
        exitCode = code;
    }

    /**
     * Creates a new object.
     *
     * @param message the message
     */
    public GeneralException(String message) {
        super(message);
    }

    /**
     * Creates a new object.
     *
     * @param message the message
     * @param cause the cause for a chained exception
     */
    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new object.
     *
     * @param cause the cause for a chained exception
     */
    public GeneralException(Throwable cause) {
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
