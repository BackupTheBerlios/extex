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

package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * horizontal list
 *
 * @see "TeX -- The Program [135]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class HorizontalListNode extends AbstractNodeList implements NodeList {

    /**
     * Creates a new object.
     *
     * @see "TeX -- The Program [136]"
     */
    public HorizontalListNode() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param node the initialnode to add
     */
    public HorizontalListNode(final Node node) {

        super();
        add(node);
    }

    /**
     * @see de.dante.extex.typesetter.NodeList#addSkip(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addSkip(final Glue glue) {

        Node gNode = new GlueNode(glue); // TODO: use factory?
        gNode.setWidth(glue.getLength());
        add(gNode);
    }

    /**
     * @see de.dante.extex.typesetter.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append("(hlist ");
        super.toText(sb, prefix);
    }

    /**
     * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("\\hbox");
        super.toString(sb, prefix);
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(
     *      de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitHorizontalList(value, value2);
    }

    /**
     * @see de.dante.extex.interpreter.type.node.AbstractNodeList#updateDimensions(
     *      de.dante.extex.typesetter.Node)
     */
    protected void updateDimensions(final Node node) {

        getWidth().add(node.getWidth());
        getHeight().max(node.getHeight());
        getDepth().max(node.getDepth());
    }

}