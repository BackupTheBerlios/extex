/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.dviware.type;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.dviware.Dvi;

/**
 * This class represents the DVI instruction <tt>down</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class DviDown extends AbstractDviCode {

    /**
     * The field <tt>dist</tt> contains the distance to move down.
     */
    private int dist;

    /**
     * Creates a new object.
     *
     * @param dist the distance to move down
     */
    public DviDown(final int dist) {

        super();
        this.dist = dist;
    }

    /**
     * Add some value to the move distance.
     *
     * @param x the value to add
     */
    public void add(final int x) {

        dist += x;
    }

    /**
     * @see de.dante.dviware.type.DviCode#getName()
     */
    public String getName() {

        return "down";
    }

    /**
     * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
     */
    public int write(final OutputStream stream) throws IOException {

        return opcodeSigned(Dvi.DOWN1, (int) dist, stream);
    }

}
