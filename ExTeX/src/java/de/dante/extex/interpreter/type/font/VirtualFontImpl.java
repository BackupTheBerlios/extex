/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.font;

import de.dante.extex.font.Glyph;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.font.type.VirtualFount;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.UnicodeChar;

/**
 * Implemetation for a virtual font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class VirtualFontImpl extends FontImpl implements VirtualFount {

    /**
     * Create a new Object
     * @param afount  the fount
     */
    public VirtualFontImpl(final ModifiableFount afount) {

        super(afount);
    }

    /**
     * @see de.dante.extex.font.type.VirtualFount#getNodeList(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public HorizontalListNode getNodeList(final TypesettingContext context,
            final UnicodeChar uc) {

        HorizontalListNode node = new HorizontalListNode();
        // incomplete
        CharNode cnode = new CharNode(context, uc);

        node.add(cnode);
        Glyph vglyph = getFount().getGlyph(uc);

        // set the dimension from the glyph to the nodelist
        node.setHeight(vglyph.getHeight());
        node.setDepth(vglyph.getDepth());
        node.setWidth(vglyph.getWidth());
        return node;
    }
}