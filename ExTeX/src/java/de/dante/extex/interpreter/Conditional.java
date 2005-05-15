/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter;

import java.io.Serializable;

import de.dante.extex.scanner.type.Token;
import de.dante.util.Locator;

/**
 * This class represents a conditional for a normal <tt>\if \else \fi</tt>
 * construct.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Conditional implements Serializable {

    /**
     * The field <tt>locator</tt> contains the locator to the position of the
     * opening <tt>\if</tt>.
     */
    private Locator locator;

    /**
     * The field <tt>primitive</tt> contains the ...
     */
    private String primitive;

    /**
     * Creates a new object.
     *
     * @param locator the locator
     * @param primitive the primitive which started this conditional
     */
    public Conditional(final Locator locator, final String primitive) {

        super();
        this.locator = locator;
        this.primitive = primitive;
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
     * Getter for the primitive which started this conditional.
     *
     * @return the primitive
     */
    public String getPrimitive() {

        return this.primitive;
    }

    /**
     * Getter for the value of the conditional.
     * If it has the value <code>true</code> then the conditional is one of the
     * if-then-else constructs. Otherwise it is a <tt>\ifcase</tt> construction.
     *
     * @return the value
     */
    public boolean getValue() {

        return true;
    }
}