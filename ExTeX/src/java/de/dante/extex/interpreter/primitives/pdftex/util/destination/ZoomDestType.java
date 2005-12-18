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

package de.dante.extex.interpreter.primitives.pdftex.util.destination;

/**
 * This class carries a destination type ZOOM as used in PDF nodes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ZoomDestType extends DestType {

    /**
     * The field <tt>zoom</tt> contains the zoom value.
     */
    private long zoom;

    /**
     * Creates a new object.
     *
     * @param zoom the zoom value
     */
    public ZoomDestType(final long zoom) {

        super();
        this.zoom = zoom;
    }

    /**
     * Getter for zoom.
     *
     * @return the zoom
     */
    public long getZoom() {

        return this.zoom;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "zoom " + Long.toString(zoom);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
     *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
     */
    public Object visit(final DestinationVisitor visitor) {

        return visitor.visitZoom(this);
    }

}
