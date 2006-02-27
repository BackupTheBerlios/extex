/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.listMaker;

import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Interface for the Manager of a list maker.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.11 $
 */
public interface ListManager {

    /**
     * Invoke the paragraph builder on a list of nodes.
     *
     * @param nodes the nodes to make a paragraph from
     *
     * @return the vertical node list containing the lines of the paragraph
     *
     * @throws TypesetterException in case of an error
     */
    NodeList buildParagraph(HorizontalListNode nodes)
            throws TypesetterException;

    /**
     * End the current paragraph.
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of an configuration problem
     */
    void endParagraph() throws TypesetterException, ConfigurationException;

    /**
     * Getter for the char node factory.
     *
     * @return the char node factory
     */
    NodeFactory getNodeFactory();

    /**
     * Getter for the options object.
     *
     * @return the options
     */
    TypesetterOptions getOptions();

    /**
     * Discard the top of the stack of list makers.
     *
     * @return the list maker popped from the stack
     *
     * @throws TypesetterException in case of an error
     */
    ListMaker pop() throws TypesetterException;

    /**
     * Push a new element to the stack of list makers.
     *
     * @param listMaker the new element to push
     *
     * @throws TypesetterException in case of an error
     */
    void push(ListMaker listMaker) throws TypesetterException;

}