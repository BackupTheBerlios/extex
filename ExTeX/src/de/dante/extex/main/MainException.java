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

import de.dante.util.GeneralException;


/**
 * This is the base class for all exceptions of the main class. In addition to
 * the message and cause attributes of the Exception it also carries an integer
 * which is meant to be used as exit status for the main program.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MainException extends GeneralException {
    /** The message for this exception */
    private String message = null;

    /** The exit code
     */
    private int code = -1;

    /**
     * Creates a new object.
     *
     * @param code the exit code
     * @param message the message
     */
    public MainException(int code, String message) {
        super(message);
        this.message = message;
        this.code    = code;
    }

    /**
     * Creates a new object.
     *
     * @param code the exit code
     * @param e the cause for this Exception
     */
    public MainException(int code, Throwable e) {
        super(e);
        this.code = code;
    }

    /**
     * Creates a new object.
     *
     * @param e the cause for this Exception
     */
    public MainException(GeneralException e) {
        super(e);
        this.code = e.getExitCode();
    }

    /**
     * Getter for the exit code
     *
     * @return the exit code
     */
    public int getCode() {
        return code;
    }

    /**
     * Getter for the message.
     *
     * @return the message
     */
    public String getMessage() {
        return (message != null ? message : getCause()
                                                .getMessage());
    }
}
