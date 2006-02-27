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

import de.dante.extex.font.type.VirtualFount;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.util.UnicodeChar;

/**
 * This is the factory for
 * {@link de.dante.extex.typesetter.type.node.CharNode CharNode}s
 * and virtual chars.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class SimpleNodeFactory implements NodeFactory {

    /**
     * Creates a new object.
     */
    public SimpleNodeFactory() {

        super();
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

        Font font = typesettingContext.getFont();

        if (font.getGlyph(uc) == null) {
            return null;
        }

        if (font instanceof VirtualFount) {
            VirtualFount vf = (VirtualFount) font;
            return vf.getVirtualCharNode(typesettingContext, uc);
        }

        return new CharNode(typesettingContext, uc);
    }

}