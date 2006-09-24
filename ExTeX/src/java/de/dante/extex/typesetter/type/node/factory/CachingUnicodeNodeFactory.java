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

package de.dante.extex.typesetter.type.node.factory;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.typesetter.type.Node;
import de.dante.util.UnicodeChar;

/**
 * This is the factory for
 * {@link de.dante.extex.typesetter.type.node.CharNode CharNode}s
 * and virtual chars.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class CachingUnicodeNodeFactory extends SimpleUnicodeNodeFactory {

    /**
     * The field <tt>cache</tt> contains the cache for previously created nodes.
     */
    private Map cache;

    /**
     * Creates a new object.
     */
    public CachingUnicodeNodeFactory() {

        super();
        cache = new HashMap();
    }

    /**
     * Create a new instance for the node.
     * If the character is not defined in the font given then <code>null</code>
     * is returned instead.
     *
     * @param typesettingContext the typographic context for the node
     * @param uc the Unicode character
     *
     * @return the new character node
     *
     * @see de.dante.extex.typesetter.type.node.factory.NodeFactory#getNode(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public Node getNode(final TypesettingContext typesettingContext,
            final UnicodeChar uc) {

        Map map = (Map) cache.get(typesettingContext);
        if (map == null) {
            map = new HashMap();
            cache.put(typesettingContext, map);
        }

        Node node = (Node) map.get(uc);

        if (node == null) {
            node = super.getNode(typesettingContext, uc);
            if (node != null) {
                map.put(uc, node);
            }
        }
        return node;
    }

}
