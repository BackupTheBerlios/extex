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
import de.dante.extex.interpreter.context.Context;
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
 * @version $Revision: 1.1 $
 */
public class NumberedToks extends NamedToks {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NumberedToks(final String name) {
        super(name);
    }

    /**
     * Set the value for the register.
     * This method is void since a whole array of values can not be initialized.
     *
     * @param context the interpreter context
     * @param value the value for the tokens
     */
    public void set(final Context context, final String value)
        throws GeneralException {
        //TODO
    }

    /**
     * Return the key (the number) for the register.
     *
     * @param source ...
     *
     * @return ...
     *
     * @throws GeneralException ...
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return getName() + "#" + Long.toString(source.scanNumber());
    }

}
