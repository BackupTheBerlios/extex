/*
 * Copyright (C) 2004 Gerd Neugebauer
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
package de.dante.extex.interpreter.primitives.register.font;

import de.dante.extex.interpreter.TokenSource;
import de.dante.util.GeneralException;


/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class NumberedFont extends NamedFont {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NumberedFont(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.font.NamedFont#getKey(
     *      de.dante.extex.interpreter.TokenSource)
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return getName() + "#" + Long.toString(source.scanNumber());
    }

}
