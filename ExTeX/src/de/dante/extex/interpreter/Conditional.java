/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
 * @version $Revision: 1.5 $
 */
public class Conditional {

    /**
     * The field <tt>locator</tt> contains the locator to the position of the
     * opening <tt>\if</tt>.
     */
    private Locator locator;

    /**
     * The field <tt>value</tt> contains the value of the conditional.
     */
    private long value;

    /**
     * Creates a new object.
     *
     * @param aLocator the locator
     * @param aValue the new value
     */
    public Conditional(final Locator aLocator, final long aValue) {
        super();
        this.locator = aLocator;
        this.value   = aValue;
    }

    /**
     * Getter for the locator of this conditional.
     * The locator points to the initiating <tt>\if</tt>.
     *
     * @return the locator
     */
    public Locator getLocator() {
        return locator;
    }

    /**
     * Getter for the value of the conditional.
     *
     * @return the value
     */
    public long getValue() {
        return value;
    }

}
