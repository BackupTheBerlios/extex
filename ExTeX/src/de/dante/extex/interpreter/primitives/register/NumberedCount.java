/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.interpreter.TokenSource;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\count</code>.
 * It sets the named count register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>
 * All features are inherited from
 * {@link de.dante.extex.interpreter.primitives.register.NamedCount NamedCount}.
 * Just the key has to be provided under which this Count has to be stored.
 * This key is constructed from the name, a hash mark and the running number.
 * </p>
 *
 * <p>Example</p>
 * <pre>
 * \count12=345
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class NumberedCount extends NamedCount {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NumberedCount(final String name) {
        super(name);
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
