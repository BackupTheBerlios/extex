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

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.MarkNode;

/**
 * This class provides a factory for page instances.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class PageFactory {

    /**
     * Creates a new object.
     *
     */
    public PageFactory() {

        super();
    }

    /**
     * Get a new instance of a page.
     *
     * @param nodes the nodes contained
     * @param context the interpreter context
     *
     * @return the new instance
     */
    public Page newInstance(final NodeList nodes, final Context context) {

        PageImpl page = new PageImpl(nodes);
        int size = nodes.size();
        for (int i=0;i<size;i++) {
            Node node = nodes.get(i);
            if ( node instanceof MarkNode) {
                
            }
        }
        return page;
    }
}
