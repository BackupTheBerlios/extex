/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.exception;

import de.dante.util.GeneralException;
import de.dante.util.framework.i18n.Localizer;

/**
 * This is the base class for all exceptions of the interpreter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class InterpreterException extends GeneralException {

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private Localizer localizer = null;

    /**
     * Creates a new object.
     */
    public InterpreterException() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param message the message field
     */
    public InterpreterException(final String message) {

        super(message);
    }

    /**
     * Creates a new object.
     *
     * @param cause the root of all evil
     */
    public InterpreterException(final Throwable cause) {

        super(cause);
    }

    /**
     * Creates a new object.
     *
     * @param cause the root of all evil
     */
    public InterpreterException(final Localizer localizer) {

        super();
        this.localizer = localizer;
    }

    /**
     * Getter for localizer.
     * If no localizer is stored within the current instance than the localizer
     * is created with the class name as key.
     *
     * @return the localizer
     */
    public Localizer getLocalizer() {

        return (this.localizer != null ? this.localizer : super.getLocalizer());
    }
}