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
package de.dante.extex.i18n;

import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class GeneralHelpingException extends GeneralException {
    /**
     * The field <tt>arg1</tt> contains the first argument.
     */
    private String arg1 = "?";

    /**
     * The field <tt>arg2</tt> contains the second argument.
     */
    private String arg2 = "?";

    /**
     * The field <tt>arg3</tt> contains the third argument.
     */
    private String arg3 = "?";

    /**
     * The field <tt>tag</tt> contains the name of the message to show.
     *
     * @see de.dante.extex.i18n.Messages
     */
    private String tag = "GeneralDetailedException.help";

    /**
     * Creates a new object.
     *
     * @param messageTag the message
     */
    public GeneralHelpingException(final String messageTag) {
        super();
        tag = messageTag;
    }

    /**
     * Creates a new object.
     *
     * @param messageTag the message
     * @param a1 the first argument
     */
    public GeneralHelpingException(final String messageTag, final String a1) {
        super();
        tag = messageTag;
        this.arg1 = a1;
    }

    /**
     * Creates a new object.
     *
     * @param messageTag the message
     * @param a1 the first argument
     * @param a2 the second argument
     */
    public GeneralHelpingException(final String messageTag, final String a1,
            final String a2) {
        super();
        tag = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
    }

    /**
     * Creates a new object.
     *
     * @param messageTag the message
     * @param a1 the first argument
     * @param a2 the second argument
     * @param a3 the third argument
     */
    public GeneralHelpingException(final String messageTag, final String a1,
            final String a2, final String a3) {
        super();
        tag = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
        this.arg3 = a3;
    }

    /**
     * Creates a new object.
     *
     * @param e the cause
     */
    public GeneralHelpingException(final Throwable e) {
        super();
    }

    /**
     * Getter for further help information.
     *
     * @return the help information
     */
    public String getHelp() {
        return Messages.format(tag + ".help", arg1, arg2, arg3);
    }

    /**
     * Getter for further help information.
     *
     * @return the help information
     */
    public String getMessage() {
        return Messages.format(tag, arg1, arg2, arg3);
    }
}
