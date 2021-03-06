/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.paragraphBuilder;

import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;

/**
 * This interface describes the function to split a large line into several
 * junks.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public interface ParagraphBuilder {

    /**
     * Break a horizontal list into lines.
     * The horizontal list passed in might be modified under way.
     *
     * @param nodes the horizontal node list containing all nodes for the
     *   paragraph
     *
     * @return the
     *   {@link de.dante.extex.typesetter.type.node.VerticalListNode
     *   VerticalListNode} containing the hboxes of the lines
     *
     * @throws TypesetterException in case of an error
     */
    NodeList build(HorizontalListNode nodes) throws TypesetterException;

    /**
     * Setter for the node factory.
     *
     * @param nodeFactory the node factory
     */
    void setNodefactory(NodeFactory nodeFactory);

    /**
     * Setter for options.
     *
     * @param options the options to set.
     */
    void setOptions(TypesetterOptions options);
}
