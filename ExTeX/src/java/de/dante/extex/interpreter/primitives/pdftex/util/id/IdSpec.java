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

package de.dante.extex.interpreter.primitives.pdftex.util.id;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.pdftex.InterpreterPdftexIdentifierTypeException;

/**
 * This is the abstract base class for ids.
 * An id cn either be a number or a name.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class IdSpec {

    /**
     * This method parses an id spec.
     *
     * @param source the source for new tokens
     * @param context the interpreter context
     * @param name the name of the current primitive
     *
     * @return the id instance
     *
     * @throws InterpreterException in case of an parse error
     */
    public static IdSpec parseIdSpec(final TokenSource source,
            final Context context, final String name)
            throws InterpreterException {

        if (source.getKeyword(context, "num")) {
            long num = source.scanNumber(context);
            return new NumIdSpec(num);
        } else if (source.getKeyword(context, "name")) {
            String id = source.scanTokensAsString(context, name);
            return new NameIdSpec(id);
        } else {
            throw new InterpreterPdftexIdentifierTypeException(name);
        }
    }

    /**
     * Creates a new object.
     */
    protected IdSpec() {

        super();
    }

}
