/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.toks;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractAssignment;

/**
 * This abstract base class provides the methods to compute the keys for
 * numbered toks registers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public abstract class AbstractToks extends AbstractAssignment {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractToks(final String name) {

        super(name);
    }

    /**
     * Return the key (the name of the primitive) for the numbered toks
     * register.
     *
     * @param source the source for new tokens
     * @param context the interpreter context to use
     *
     * @return the key for the current register
     *
     * @throws InterpreterException in case that a derived class needs to
     *  throw an Exception this one is declared.
     */
    protected String getKey(final TokenSource source, final Context context)
            throws InterpreterException {

        String name = source.scanRegisterName(context);

        if (Namespace.SUPPORT_NAMESPACE_TOKS) {
            return context.getNamespace() + "toks#" + name;
        } else {
            return "toks#" + name;
        }
    }

}
