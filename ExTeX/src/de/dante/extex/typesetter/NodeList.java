/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.typesetter;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.CharNode;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public interface NodeList extends Node {

    /**
     * Add a node to the node list.
     *
     * @param node the node to add
     */
    void add(Node node);

    /**
     * Add a character to the node list.
     *
     * @param node the character to add
     */
    void addGlyph(CharNode node);

    /**
     * add some glue to the node list.
     *
     * @param glue the glue to add
     */
    void addSkip(Glue glue);

    /**
     * Setter for the shift value of the node list.
     *
     * @param d the amount to be shifted
     */
    void setShift(Dimen d);

    /**
     * Getter for the shift value of the node list.
     *
     * @return the shift value
     */
    Dimen getShift();

    /**
     * Setter for the move value of the node list.
     *
     * @param d the move value
     */
    void setMove(Dimen d);

    /**
     * Getter for the move value of the node list.
     *
     * @return the move value
     */
    Dimen getMove();

    /**
     * Get a new iterator for all nodes in the list.
     *
     * @return the iuterator for all nodes in the list
     */
    NodeIterator iterator();

}
