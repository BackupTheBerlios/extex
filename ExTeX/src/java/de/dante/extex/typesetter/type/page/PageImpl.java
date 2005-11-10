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

package de.dante.extex.typesetter.type.page;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.NodeList;

/**
 * This class provides a transport object for pages. Beside the nodes it
 * contains additional administrative information.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class PageImpl implements Page {

    /**
     * The field <tt>marks</tt> contains the ...
     */
    private Map marks = new HashMap();

    /**
     * The field <tt>mediaHeight</tt> contains the ...
     */
    private Dimen mediaHeight;

    /**
     * The field <tt>mediaWidth</tt> contains the ...
     */
    private Dimen mediaWidth;

    /**
     * The field <tt>nodes</tt> contains the ...
     */
    private NodeList nodes;

    /**
     * Creates a new object.
     *
     */
    public PageImpl(final NodeList nodes) {

        super();
        this.nodes = nodes;
        mediaWidth = new Dimen(Dimen.ONE_INCH);
        mediaWidth.multiply(2100, 254); // A4 paper
        mediaHeight = new Dimen(Dimen.ONE_INCH);
        mediaHeight.multiply(2970, 254); // A4 paper
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#getMediaHeight()
     */
    public Dimen getMediaHeight() {

        return mediaHeight;
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#getMediaWidth()
     */
    public Dimen getMediaWidth() {

        return mediaWidth;
    }

    /**
     * Getter for nodes.
     *
     * @return the nodes
     */
    public NodeList getNodes() {

        return this.nodes;
    }

}
