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

package de.dante.extex.interpreter.primitives.pdftex.util.action;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class represents a user action of PDF.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class UserActionSpec extends ActionSpec {

    /**
     * Parse a user action spec.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param name the name of the primitive
     *
     * @return the action spec found
     *
     * @throws InterpreterException in case of an error
     */
    public static ActionSpec parseActionSpec(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String name) throws InterpreterException {

        String user = source.scanTokensAsString(context, name);
        return new UserActionSpec(user);
    }

    /**
     * The field <tt>user</tt> contains the name of the user.
     */
    private String user;

    /**
     * Creates a new object.
     *
     * @param user the user
     */
    public UserActionSpec(final String user) {

        super();
        this.user = user;
    }

    /**
     * Getter for user.
     *
     * @return the user
     */
    protected String getUser() {

        return this.user;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "user " + user;
    }

    /**
     * @see de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec#visit(
     *      de.dante.extex.interpreter.primitives.pdftex.util.action.ActionVisitor)
     */
    public Object visit(final ActionVisitor visitor) {

        return visitor.visitUser(this);
    }

}
