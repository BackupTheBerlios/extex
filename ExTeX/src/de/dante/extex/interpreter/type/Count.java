/*
 * Copyright (C) 2003  Gerd Neugebauer
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

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Count implements Serializable {

    public static final Count ZERO = new Count(0);
    public static final Count ONE  = new Count(1);

    /** ... */
    private long value = 0;

    /**
     * Creates a new object.
     */
    public Count(long value) {
        super();
        this.value = value;
    }

    /**
     * Creates a new object.
     */
    public Count(TokenSource source) throws GeneralException {
        super();
        value = source.scanInteger();
    }

    /**
     * Setter for the value.
     *
     * @param l the new value
     */
    public void setValue(long l) {
        value = l;
    }

    /**
     * Getter for the value
     *
     * @return the value
     */
    public long getValue() {
        return value;
    }
}
