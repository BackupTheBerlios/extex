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

import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This exception s thrown when the error count exceeds the given limit.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ErrorLimitException extends InterpreterException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>limit</tt> contains the maximal allowed number of errors.
     */
    private int limit;

    /**
     * Creates a new object.
     *
     * @param limit the maximal allowed number of errors
     */
    public ErrorLimitException(final int limit) {

        super();
        this.limit = limit;
    }

    /**
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {

        Localizer l = LocalizerFactory.getLocalizer(ErrorLimitException.class
                .getName());
        return l.format("TTP.ErrorLimitReached", Integer.toString(limit));
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {

        return getLocalizedMessage();
    }
}