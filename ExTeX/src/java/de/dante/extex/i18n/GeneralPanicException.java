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
 * This exception can be used to terminate the interpreter loop.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class GeneralPanicException extends GeneralException {
    /** the first argument */
    private String arg1 = "?";

    /** the second argument */
    private String arg2 = "?";

    /** the third argument */
    private String arg3 = "?";

    /** the name of the message to show; cf. Messages */
    private String tag = "GeneralDetailedException.help";

    /**
     * Creates a new object.
     *
     * @param message the message
     */
    public GeneralPanicException(String messageTag) {
        super(-666);
        tag = messageTag;
    }

    /**
     * Creates a new object.
     *
     * @param message the message
     */
    public GeneralPanicException(String messageTag, String arg1) {
        super(-666);
        tag       = messageTag;
        this.arg1 = arg1;
    }

    /**
     * Creates a new object.
     *
     * @param message the message
     */
    public GeneralPanicException(String messageTag, String arg1,
                                    String arg2) {
        super(-666);
        tag       = messageTag;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     * Creates a new object.
     *
     * @param message the message
     */
    public GeneralPanicException(String messageTag, String arg1,
                                    String arg2, String arg3) {
        super(-666);
        tag       = messageTag;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    /**
     * Creates a new object.
     *
     * @param message the message
     */
    public GeneralPanicException(Throwable e) {
        super(-666);
    }

    public String getMessage() {
        return Messages.format(tag, arg1, arg2, arg3);
    }
}
