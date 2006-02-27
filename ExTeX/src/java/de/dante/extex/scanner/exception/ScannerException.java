/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.scanner.exception;

import de.dante.util.exception.GeneralException;

/**
 * This is the base class for all exceptions thrown by the scanner.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ScannerException extends GeneralException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     */
    public ScannerException() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param message the textual representation
     */
    public ScannerException(final String message) {

        super(message);
    }

    /**
     * Creates a new object.
     *
     * @param message the textual representation
     * @param cause the root of all evil
     */
    public ScannerException(final String message, final Throwable cause) {

        super(message, cause);
    }

    /**
     * Creates a new object.
     *
     * @param cause the root of all evil
     */
    public ScannerException(final Throwable cause) {

        super(cause);
    }

}
