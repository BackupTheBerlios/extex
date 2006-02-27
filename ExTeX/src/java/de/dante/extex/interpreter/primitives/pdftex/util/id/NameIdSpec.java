/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.pdftex.util.id;

/**
 * This class carries an id based on a name.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class NameIdSpec extends IdSpec {

    /**
     * The field <tt>name</tt> contains the name.
     */
    private String name;

    /**
     * Creates a new object.
     *
     * @param name the name
     */
    public NameIdSpec(final String name) {

        super();
        this.name = name;
    }

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {

        return this.name;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "name " + name;
    }

}
