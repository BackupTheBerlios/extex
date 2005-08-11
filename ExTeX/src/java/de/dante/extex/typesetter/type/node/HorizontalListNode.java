/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.node;

import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * This class provides a container for nodes which is interpreted as horizontal
 * list.
 *
 * @see "<logo>TeX</logo> &ndash; The Program [135]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class HorizontalListNode extends AbstractNodeList implements NodeList {

    /**
     * Creates a new object.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [136]"
     */
    public HorizontalListNode() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param node the initial node to add
     */
    public HorizontalListNode(final Node node) {

        super();
        add(node);
    }

    /**
     * Creates a new object.
     *
     * @param node1 the initial node
     * @param node2 the node to add after node1
     */
    public HorizontalListNode(final Node node1, final Node node2) {

        super();
        add(node1);
        add(node2);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#addSkip(
     *      FixedGlue)
     */
    public void addSkip(final FixedGlue glue) {

        Node gNode = new GlueNode(glue);
        gNode.setWidth(glue.getLength());
        add(gNode);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("\\hbox");
        super.toString(sb, prefix);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append("(hlist ");
        super.toText(sb, prefix);
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNodeList#updateDimensions(
     *      de.dante.extex.typesetter.type.Node, boolean)
     */
    protected void updateDimensions(final Node node, boolean first) {

        getWidth().add(node.getWidth());
        getHeight().max(node.getHeight());
        getDepth().max(node.getDepth());
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitHorizontalList(this, value);
    }

}