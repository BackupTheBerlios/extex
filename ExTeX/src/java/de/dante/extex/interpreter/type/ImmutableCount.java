/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.type;

import de.dante.util.GeneralException;


/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ImmutableCount extends Count {

    /**
     * Creates a new object.
     *
     * @param value ...
     */
    public ImmutableCount(final long value) {

        super(value);
    }

    /**
     * @see de.dante.extex.interpreter.type.Count#add(long)
     */
    public void add(final long val) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.Count#divide(long)
     */
    public void divide(final long val) throws GeneralException {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.Count#multiply(long)
     */
    public void multiply(final long val) {

        throw new RuntimeException("Unable to set an immutable object");
    }
}
