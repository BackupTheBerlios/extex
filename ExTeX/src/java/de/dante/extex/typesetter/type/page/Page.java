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

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.NodeList;

/**
 * This interface describes a page for the back-end. It carries nodes and allows
 * access to additional parameters.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface Page {

    /**
     * Getter for the node list.
     * The node list describes where on the page to put characters from fonts
     * or other graphical symbols like rules.
     *
     * @return the node list
     */
    NodeList getNodes();

    /**
     * Getter for the width of the media.
     *
     * @return the width of the media
     */
    Dimen getMediaWidth();

    /**
     * Getter for the height of the media.
     *
     * @return the height of the media
     */
    Dimen getMediaHeight();

    /**
     * Setter for the width of the media.
     *
     * @param width the media width
     */
    void setMediaWidth(Dimen width);

    /**
     * Setter for the height of the media.
     *
     * @param height the media height
     */
    void setMediaHeight(Dimen  height);
}
