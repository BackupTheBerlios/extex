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
 * This class provides an implementation for the primitive <code>\dimen</code>.
 * It sets the named count register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>Example</p>
 * <pre>
 * \dimen12=345.67pt
 * </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class NumberedDimen extends NamedDimen {

	/**
	 * Creates a new object.
	 * @param name the name for debugging
	 */
	public NumberedDimen(final String name) {
		super(name);
	}

	/**
	 * Return the key (the number) for the register.
	 *
	 * @param source 	the tokensource
	 * @return Return the key
	 * @throws GeneralException in case of an error
	 */
	protected String getKey(TokenSource source) throws GeneralException {
		return getName() + "#" + Long.toString(source.scanNumber());
	}
}
