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

package de.dante.extex.main.exception;

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

    /**
     * The field <tt>message</tt> contains the message for this exception.
     */
    private String message = null;

    /**
     * The field <tt>code</tt> contains the exit code.
     */
    private int code;

    /**
     * Creates a new object.
     *
     * @param theCode the exit code
     * @param theMessage the message
     */
    public MainException(final int theCode, final String theMessage) {

        super(theMessage);
        this.message = theMessage;
        this.code = theCode;
    }

    /**
     * Creates a new object.
     *
     * @param theCode the exit code
     * @param cause the cause for this Exception
     */
    public MainException(final int theCode, final Throwable cause) {

        super(cause);
        this.code = theCode;
    }

    /**
     * Creates a new object.
     *
     * @param cause the cause for this Exception
     */
    public MainException(final GeneralException cause) {

        super(cause);
        this.code = cause.getExitCode();
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

        return (message != null ? message : getCause().getMessage());
    }

}