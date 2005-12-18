/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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
 * This class carries an id based on a number.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class NumIdSpec extends IdSpec {

    /**
     * The field <tt>num</tt> contains the number.
     */
    private long num;

    /**
     * Creates a new object.
     *
     * @param num the num
     */
    public NumIdSpec(final long num) {

        super();
        this.num = num;
    }

    /**
     * Getter for the number.
     *
     * @return the number
     */
    public long getNum() {

        return this.num;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "num " + Long.toString(num);
    }
    
}
