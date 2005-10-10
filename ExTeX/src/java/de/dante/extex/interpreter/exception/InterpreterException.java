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

import de.dante.util.exception.GeneralException;
import de.dante.util.framework.i18n.Localizer;

/**
 * This is the base class for all exceptions of the interpreter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class InterpreterException extends GeneralException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private Localizer localizer = null;

    /**
     * The field <tt>processed</tt> contains the indicator that the exception
     * has been processed by an error handler already.
     */
    private boolean processed = false;

    /**
     * Creates a new object.
     */
    public InterpreterException() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param localizer the localizer
     */
    public InterpreterException(final Localizer localizer) {

        super();
        this.localizer = localizer;
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
     * Getter for localizer.
     * If no localizer is stored within the current instance than the localizer
     * is created with the class name as key.
     *
     * @return the localizer
     */
    public Localizer getLocalizer() {

        return (this.localizer != null ? this.localizer : super.getLocalizer());
    }

    /**
     * Getter for processed.
     *
     * @return the processed
     */
    public boolean isProcessed() {

        return this.processed;
    }

    /**
     * Setter for processed.
     *
     * @param processed the processed to set
     */
    public void setProcessed(boolean processed) {

        this.processed = processed;
    }
}