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
import de.dante.extex.interpreter.exception.pdftex.InterpreterPdftexIdentifierTypeException;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class represents a thread action spec.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ThreadActionSpec extends ActionSpec {

    /**
     * Parse a thread action spec.
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

        String file = null;
        if (source.getKeyword(context, "file")) {
            file = source.scanTokensAsString(context, name);
        }

        String id = null;

        if (source.getKeyword(context, "num")) {
            long num = source.scanNumber(context);
            id = Long.toString(num);
        } else if (source.getKeyword(context, "name")) {
            id = source.scanTokensAsString(context, name);
        } else {
            throw new InterpreterPdftexIdentifierTypeException(name);
        }

        return new ThreadActionSpec(file, id);
    }

    /**
     * The field <tt>file</tt> contains the file.
     */
    private String file;

    /**
     * The field <tt>id</tt> contains the id.
     */
    private String id;

    /**
     * Creates a new object.
     *
     * @param file the file
     * @param id the id
     */
    public ThreadActionSpec(final String file, final String id) {

        super();
        this.file = file;
        this.id = id;
    }

    /**
     * Getter for id.
     * The id can either be a number or a name. Both are returned as String.
     *
     * @return the id
     */
    protected String getId() {

        return this.id;
    }

    /**
     * Getter for file.
     *
     * @return the file
     */
    protected String getFile() {

        return this.file;
    }

    /**
     * @see de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec#visit(
     *      de.dante.extex.interpreter.primitives.pdftex.util.action.ActionVisitor)
     */
    public Object visit(final ActionVisitor visitor) {

        return visitor.visitThread(this);
    }

}
