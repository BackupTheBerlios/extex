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
package de.dante.extex.scanner;

/**
 * This exception is thrown when something in the context of catcode creation or
 * access goes wrong.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class CatcodeException extends Exception {

    /**
     * Creates a new object.
     */
    public CatcodeException() {
        super();
    }

    /**
     * Creates a new object.
     * 
     * @param message
     */
    public CatcodeException(String message) {
        super(message);
    }

    /**
     * Creates a new object.
     * 
     * @param message
     * @param cause
     */
    public CatcodeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new object.
     * 
     * @param cause
     */
    public CatcodeException(Throwable cause) {
        super(cause);
    }

}
