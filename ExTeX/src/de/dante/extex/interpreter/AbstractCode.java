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

package de.dante.extex.interpreter;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This is the abstract base class which can be used for all classes
 * implementing the interface Code. It provides some useful definitions for
 * most of the methods.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.16 $
 */
public class AbstractCode implements Code {

    /**
     * The field <tt>name</tt> contains the name of this code for debugging.
     */
    private String name = "";

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
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.Code#getName()
     */
    public String getName() {

        return name;
    }

    /**
     * @see de.dante.extex.interpreter.Code#isIf()
     */
    public boolean isIf() {

        return false;
    }

    /**
     * @see de.dante.extex.interpreter.Code#isOuter()
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

        char esc = (char) (context.getCount("escapechar").getValue());
        return Character.toString(esc) + name;
    }

    /**
     * @see de.dante.extex.interpreter.Code#setName(java.lang.String)
     */
    public void setName(final String theName) {

        this.name = theName;
    }

}