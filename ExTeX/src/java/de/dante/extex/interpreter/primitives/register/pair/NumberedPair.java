/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.pair;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\pair</code>.
 * It sets the named pair register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>
 * All features are inherited from
 * {@link de.dante.extex.interpreter.primitives.register.pair.NamedPair pair}. Just the key
 * has to be provided under which this Pair has to be stored. This key is
 * constructed from the name, a hash mark and the running number.
 * </p>
 *
 * <p>Example</p>
 * <pre>
 * \pair12=345.67 11.23
 * </pre>
 *
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class NumberedPair extends NamedPair {

    /**
     * Creates a new object.
     * @param name the name for debugging
     */
    public NumberedPair(final String name) {

        super(name);
    }

    /**
     * Return the key (the number) for the register.
     *
     * @param source    the tokensource
     * @return Return the key
     * @throws InterpreterException if an ecxeption was occured
     */
    protected String getKey(final TokenSource source)
            throws InterpreterException {

        try {
            return getName() + "#" + Long.toString(source.scanNumber());
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }
}