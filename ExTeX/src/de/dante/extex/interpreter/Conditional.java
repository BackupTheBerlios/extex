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
package de.dante.extex.interpreter;

import de.dante.util.Locator;


/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Conditional {
    /** the locator to the position of the opening <tt>\if</tt> */
    private Locator locator;

    /** the value of the conditional */
    private boolean value;

    /**
     * Creates a new object.
     */
    public Conditional(Locator locator, boolean value) {
        super();
        this.locator = locator;
        this.value   = value;
    }

    /**
     * Getter for the locator of this conditional.
     * the locator points to the initiating <tt>\if</tt>.
     *
     * @return the locator
     */
    public Locator getLocator() {
        return locator;
    }

    /**
     * Getter for the boolean value of the conditional.
     *
     * @return the boolean value
     */
    public boolean isValue() {
        return value;
    }
}
