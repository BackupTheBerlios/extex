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

import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for a vertical list.
 *
 * @see "TeX -- The Program [137]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class VerticalListNode extends AbstractNodeList implements NodeList {

    /**
     * Creates a new object.
     *
     * @see "TeX -- The Program [136]"
     */
    public VerticalListNode() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#addSkip(
     *      FixedGlue)
     */
    public void addSkip(final FixedGlue glue) {

        Node gNode = new GlueNode(glue); // TODO gene: use factory?
        gNode.setHeight(glue.getLength());
        add(gNode);
    }

    /**
     * TODO gene: missing JavaDoc.
     *
     * @param size the target size
     */
    public void spread(final FixedDimen size) {

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append("(vlist ");
        super.toText(sb, prefix);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("\\vbox");
        super.toString(sb, prefix);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitVerticalList(value, value2);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitVerticalList(this, value);
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNodeList#updateDimensions(
     *      de.dante.extex.typesetter.Node)
     */
    protected void updateDimensions(final Node node) {

        getWidth().max(node.getWidth());
        getHeight().add(node.getHeight()); //TODO gene: incorrect
        getDepth().add(node.getDepth()); //TODO gene: incorrect
    }

}
