/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.i18n;

import de.dante.util.GeneralException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This exception can be used to terminate the interpreter loop.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class PanicException extends GeneralException {

    /**
     * The constant <tt>PANIC_ERROR_CODE</tt> contains the return code for this
     * kind of Exception.
     */
    private static final int PANIC_ERROR_CODE = -666;

    /**
     * The field <tt>arg1</tt> contains the first argument.
     */
    private String arg1;

    /**
     * The field <tt>arg2</tt> contains the second argument.
     */
    private String arg2;

    /**
     * The field <tt>arg3</tt> contains the third argument.
     */
    private String arg3;

    /**
     * The field <tt>localizer</tt> contains the ...
     */
    private Localizer localizer;

    /**
     * The field <tt>tag</tt> contains the name of he message format in the
     * localizer.
     */
    private String tag;

    /**
     * Creates a new object.
     *
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     */
    public PanicException(final String messageTag) {

        super(PANIC_ERROR_CODE);
        this.tag = messageTag;
        this.arg1 = "?";
        this.arg2 = "?";
        this.arg3 = "?";
    }

    /**
     * Creates a new object.
     *
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     * @param a1 the first parameter
     *
     * @deprecated missing localizer
     */
    public PanicException(final String messageTag, final String a1) {

        super(PANIC_ERROR_CODE);
        this.tag = messageTag;
        this.arg1 = a1;
        this.arg2 = "?";
        this.arg3 = "?";
    }

    /**
     * Creates a new object.
     *
     * @param localizer the localizer to use
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     */
    public PanicException(final Localizer localizer, final String messageTag) {

        super(PANIC_ERROR_CODE);
        this.localizer = localizer;
        this.tag = messageTag;
        this.arg1 = "?";
        this.arg2 = "?";
        this.arg3 = "?";
    }

    /**
     * Creates a new object.
     *
     * @param localizer the localizer to use
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     * @param a1 the first parameter
     */
    public PanicException(final Localizer localizer, final String messageTag,
            final String a1) {

        super(PANIC_ERROR_CODE);
        this.localizer = localizer;
        this.tag = messageTag;
        this.arg1 = a1;
        this.arg2 = "?";
        this.arg3 = "?";
    }

    /**
     * Creates a new object.
     *
     * @param localizer the localizer to use
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     * @param a1 the first parameter
     * @param a2 the second parameter
     */
    public PanicException(final Localizer localizer, final String messageTag,
            final String a1, final String a2) {

        super(PANIC_ERROR_CODE);
        this.localizer = localizer;
        this.tag = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
        this.arg3 = "?";
    }

    /**
     * Creates a new object.
     *
     * @param localizer the localizer to use
     * @param messageTag the name of the message in the file <i>
     *            messages.properties</i>
     * @param a1 the first parameter
     * @param a2 the second parameter
     * @param a3 the third parameter
     */
    public PanicException(final Localizer localizer, final String messageTag,
            final String a1, final String a2, final String a3) {

        super(PANIC_ERROR_CODE);
        this.localizer = localizer;
        this.tag = messageTag;
        this.arg1 = a1;
        this.arg2 = a2;
        this.arg3 = a3;
    }

    /**
     * Creates a new object.
     *
     * @param e the cause
     */
    public PanicException(final Throwable e) {

        super(PANIC_ERROR_CODE);
        this.localizer = LocalizerFactory.getLocalizer(PanicException.class
                .getName());
        this.tag = "PanicException.help";
        this.arg1 = "?";
        this.arg2 = "?";
        this.arg3 = "?";
    }

    /**
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {

        return getLocalizer().format(tag, arg1, arg2, arg3);
    }

    /**
     * Getter for localizer.
     *
     * @return the localizer.
     */
    protected Localizer getLocalizer() {

        return (this.localizer != null ? this.localizer : super.getLocalizer());
    }
}