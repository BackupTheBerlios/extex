/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.TokenSource;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\toks</code>.
 * It sets the numbered toks register to the value given, and as a side effect
 * all prefixes are zeroed.
 *
 * Example:
 * <pre>
 *  \toks12{123}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class NamedToks extends NumberedToks {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NamedToks(final String name) {

        super(name);
    }

    /**
     * Return the key (the number) for the toks register.
     *
     * @param source the source for the next tokens -- if required
     * @param namespace the namespace to use
     *
     * @return the key for the toks register
     *
     * @throws GeneralException in case of an exception
     */
    protected String getKey(final TokenSource source, final String namespace)
            throws GeneralException {

        return namespace + getName();
    }

}
