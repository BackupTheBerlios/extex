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

package de.dante.extex.interpreter.type;

import java.io.Serializable;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;

/**
 * This is the abstract base class which can be used for all classes
 * implementing the interface Code. It provides some useful definitions for
 * most of the methods.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class AbstractCode implements Code, Localizable, Serializable {

    /**
     * The field <tt>localizer</tt> contains the localizer or <code>null</code>
     * if none has been set yet.
     */
    private Localizer localizer = null;

    /**
     * The field <tt>name</tt> contains the name of this code for debugging.
     */
    private String name;

    /**
     * Creates a new object.
     *
     * @param codeName the name of the primitive
     */
    public AbstractCode(final String codeName) {

        super();
        this.name = codeName;
    }

    /**
     * Setter for the localizer.
     *
     * @param theLocalizer the new value for the localizer
     *
     * @see de.dante.util.framework.i18n.Localizable#enableLocalization(
     *      de.dante.util.framework.i18n.Localizer)
     */
    public void enableLocalization(final Localizer theLocalizer) {

        this.localizer = theLocalizer;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        return true;
    }

    /**
     * Getter for localizer.
     *
     * @return the localizer.
     */
    protected Localizer getLocalizer() {

        return this.localizer;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#getName()
     */
    public String getName() {

        return name;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#isIf()
     */
    public boolean isIf() {

        return false;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#isOuter()
     */
    public boolean isOuter() {

        return false;
    }

    /**
     * Attach the current escape character in front of the name and return the
     * result.
     * <p>
     * This method is meant to produce a printable version of the control
     * sequence for error messages.
     * </p>
     *
     * @param context the processing context
     *
     * @return the control sequence including the escape character
     */
    protected String printableControlSequence(final Context context) {

        return printableControlSequence(context, name);
    }

    /**
     * Attach the current escape character in front of a name and return the
     * result.
     * <p>
     * This method is meant to produce a printable version of a control
     * sequence for error messages.
     * </p>
     *
     * @param context the processing context
     * @param theName the name of the control sequence
     *
     * @return the control sequence including the escape character
     */
    protected String printableControlSequence(final Context context,
            final String theName) {

        char esc = (char) (context.getCount("escapechar").getValue());
        return Character.toString(esc) + theName;
    }

    /**
     * Return the printable version of a token for error messages.
     *
     * @param context the processing context
     * @param token the token to get a printable representation for
     *
     * @return the control sequence including the escape character
     */
    protected String printable(final Context context, final Token token) {

        if (token instanceof ControlSequenceToken) {
            char esc = (char) (context.getCount("escapechar").getValue());
            return Character.toString(esc) + token.getValue();
        }
        return token.getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#setName(java.lang.String)
     */
    public void setName(final String theName) {

        this.name = theName;
    }
}