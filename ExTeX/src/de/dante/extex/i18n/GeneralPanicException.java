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
 * @version $Revision: 1.2 $
 */
public class GeneralPanicException extends GeneralException {
    /**
     * The constant <tt>PANIC_ERROR_CODE</tt> ...
     */
    private static final int PANIC_ERROR_CODE = -666;

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
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     */
    public GeneralPanicException(final String messageTag) {
        super(PANIC_ERROR_CODE);
        tag = messageTag;
    }

    /**
     * Creates a new object.
     *
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     * @param a1 the first parameter
     */
    public GeneralPanicException(final String messageTag, final String a1) {
        super(PANIC_ERROR_CODE);
        tag       = messageTag;
        this.arg1 = a1;
    }

    /**
     * Creates a new object.
     *
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     * @param a1 the first parameter
     * @param a2 the second parameter
     */
    public GeneralPanicException(final String messageTag, final String a1,
                                    final String a2) {
        super(PANIC_ERROR_CODE);
        tag       = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
    }

    /**
     * Creates a new object.
     *
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     * @param a1 the first parameter
     * @param a2 the second parameter
     * @param a3 the third parameter
     */
    public GeneralPanicException(final String messageTag, final String a1,
                                    final String a2, final String a3) {
        super(PANIC_ERROR_CODE);
        tag       = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
        this.arg3 = a3;
    }

    /**
     * Creates a new object.
     *
     * @param e the cause
     */
    public GeneralPanicException(final Throwable e) {
        super(PANIC_ERROR_CODE);
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return Messages.format(tag, arg1, arg2, arg3);
    }
}
