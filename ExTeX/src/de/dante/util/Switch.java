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
package de.dante.util;


/**
 * This class provides a modifyable boolean object.
 * In contrast to the class {@link java.lang.Boolean Boolean} this class has
 * also a setter for the encapsulated boolean value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Switch {

    /**
     * The field <tt>on</tt> contains the encapsulated boolean value.
     */
    private boolean on;

    /**
     * Creates a new object with a given boolean value.
     *
     * @param on the initial value
     */
    public Switch(final boolean on) {

        super();
        this.on = on;
    }

    /**
     * Getter for on.
     *
     * @return the on.
     */
    public boolean isOn() {

        return on;
    }
    /**
     * Setter for on.
     *
     * @param on the on to set.
     */
    public void setOn(final boolean on) {

        this.on = on;
    }
}
