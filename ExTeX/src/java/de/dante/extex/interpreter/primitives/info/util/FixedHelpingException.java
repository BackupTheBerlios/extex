/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.info.util;

import de.dante.extex.interpreter.exception.helping.HelpingException;

/**
 * This class provides an Exception with the possibility to provide additional
 * help on the error encountered. Thus it has two levels of information: the
 * first level is the message and the second level is the additional help.
 * <p>
 * In contrast to
 * {@link de.dante.extex.interpreter.exception.helping.HelpingException HelpingException}
 * the messages are not mapped. Thus they are not subject to
 * internationalization.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class FixedHelpingException extends HelpingException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>help</tt> contains the string which is shown if further
     * help is requested.
     */
    private String help;

    /**
     * The field <tt>message</tt> contains the message. We need to keep it here
     * since the parent class does not provide writing access to the message of
     * the underlying Exception.
     */
    private String message;

    /**
     * Creates a new object.
     *
     * @param message the message of this Exception
     * @param help the help string
     */
    public FixedHelpingException(final String message, final String help) {

        super();
        this.message = message;
        this.help = help;
    }

    /**
     * @see de.dante.util.exception.GeneralException#getHelp()
     */
    public String getHelp() {

        return help;
    }

    /**
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {

        return message;
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {

        return message;
    }

}
