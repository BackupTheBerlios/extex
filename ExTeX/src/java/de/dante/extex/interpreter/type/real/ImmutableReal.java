/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.real;


/**
 * An immutable Real.
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class ImmutableReal extends Real {

    /**
     * Creates a new object.
     *
     * @param val    init with double-value
     */
    public ImmutableReal(final double val) {

        super(val);
    }

    /**
     * Creates a new object.
     * @param l the value as long
     */
    public ImmutableReal(final long l) {

        super(l);
    }

    /**
     * Setter for the value.
     *
     * @param d the new value
     */
    public void setValue(final double d) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * ...
     *
     * @param val ...
     */
    public void add(final double val) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * ...
     *
     * @param real ...
     */
    public void add(final Real real) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * ...
     *
     * @param val ...
     */
    public void divide(final double val) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * ...
     *
     * @param val ...
     */
    public void divide(final Real val) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * ...
     *
     * @param val ...
     */
    public void multiply(final double val) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * ...
     *
     * @param val ...
     */
    public void multiply(final Real val) {

        throw new RuntimeException("Unable to set an immutable object");
    }

}