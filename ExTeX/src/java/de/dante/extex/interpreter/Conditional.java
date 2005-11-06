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

import de.dante.extex.interpreter.type.Code;
import de.dante.util.Locator;

/**
 * This class represents a conditional for a normal <tt>\if \else \fi</tt>
 * construct. It records which <tt>\if</tt> has initiated it and where this has
 * happened.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class Conditional implements Serializable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>branch</tt> contains the ...
     */
    private long branch;

    /**
     * The field <tt>locator</tt> contains the locator to the position of the
     * opening <tt>\if</tt>.
     */
    private Locator locator;

    /**
     * The field <tt>primitive</tt> contains the name of the primitive which
     * has lead to this conditional.
     */
    private Code primitive;

    /**
     * Creates a new object.
     *
     * @param locator the locator
     * @param primitive the primitive which started this conditional
     * @param branch <code>true</code> iff the then branch is taken
     */
    public Conditional(final Locator locator, final Code primitive,
            final long branch) {

        super();
        this.locator = locator;
        this.primitive = primitive;
        this.branch = branch;
    }

    /**
     * Getter for branch.
     *
     * @return the branch
     */
    public long getBranch() {

        return this.branch;
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
     * Getter for primitive.
     *
     * @return the primitive
     */
    public Code getPrimitive() {

        return this.primitive;
    }

    /**
     * Getter for the primitive which started this conditional.
     *
     * @return the primitive name
     */
    public String getPrimitiveName() {

        return this.primitive.getName();
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

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return primitive + "[" + locator.toString() + "]";
    }

}