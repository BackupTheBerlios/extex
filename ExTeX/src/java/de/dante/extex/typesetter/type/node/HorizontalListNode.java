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

package de.dante.extex.typesetter.type.node;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.WideGlue;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
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
 * @version $Revision: 1.17 $
 */
public class HorizontalListNode extends GenericNodeList {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060417L;

    /**
     * Creates a new object. The list is empty initially.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [136]"
     */
    public HorizontalListNode() {

        super();
    }

    /**
     * Creates a new object. The list is filled with the node given.
     *
     * @param width the width of the box
     */
    public HorizontalListNode(final FixedDimen width) {

        super();
        setWidth(width);
        setTargetWidth(width);
    }

    /**
     * Creates a new object. The list is filled with the node given.
     *
     * @param node the initial node to add
     */
    public HorizontalListNode(final Node node) {

        super(node);
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
     * @see de.dante.extex.typesetter.type.node.AbstractNodeList#add(
     *      int,
     *      de.dante.extex.typesetter.type.Node)
     */
    public void add(final int index, final Node node) {

        super.add(index, node);
        getWidth().add(node.getWidth());
        getHeight().max(node.getHeight());
        getDepth().max(node.getDepth());
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNodeList#add(
     *      de.dante.extex.typesetter.type.Node)
     */
    public void add(final Node node) {

        super.add(node);
        getWidth().add(node.getWidth());
        getHeight().max(node.getHeight());
        getDepth().max(node.getDepth());
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#addSkip(
     *      FixedGlue)
     */
    public void addSkip(final FixedGlue glue) {

        Node gNode = new GlueNode(glue, true);
        gNode.setWidth(glue.getLength());
        add(gNode);
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNodeList#atShipping(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      boolean)
     */
    public Node atShipping(final Context context, final Typesetter typesetter,
            final NodeVisitor visitor, final boolean inHMode)
            throws GeneralException {

        return super.atShipping(context, typesetter, visitor, true);
    }

    /**
     * Adjust the variable nodes to achieve a given target width.
     *
     */
    public void hpack() {

        Dimen w = new Dimen(getTargetWidth());
        if (w == null) {
            return;
        }
        w.subtract(getWidth());
        if (w.gt(Dimen.ZERO_PT)) {
            //          TODO gene
        } else if (w.lt(Dimen.ZERO_PT)) {
            //          TODO gene
        }
    }

    /**
     * Adjust the variable nodes to achieve a given target width.
     *
     * @param width the new target width
     */
    public void hpack(final FixedDimen width) {

        setTargetWidth(width);
        Dimen w = new Dimen(width);
        w.subtract(getWidth());
        if (w.gt(Dimen.ZERO_PT)) {
            //          TODO gene
        } else if (w.lt(Dimen.ZERO_PT)) {
            //          TODO gene
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNode#spreadWidth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen,
     *      de.dante.extex.interpreter.type.glue.FixedGlueComponent)
     */
    public void spreadWidth(final FixedDimen w, final FixedGlueComponent sum) {

        int size = size();
        FixedGlueComponent s;

        if (sum == null) {
            WideGlue sx = new WideGlue();

            for (int i = 0; i < size; i++) {
                get(i).addWidthTo(sx);
            }
            s = (sx.getLength().ge(w) ? sx.getShrink() : sx.getStretch());
        } else {
            s = sum;
        }

        for (int i = 0; i < size; i++) {
            get(i).spreadWidth(w, s);
        }

        getWidth().add(w);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(java.lang.StringBuffer,
     *      java.lang.String, int, int)
     */
    public void toString(final StringBuffer sb, final String prefix,
            final int breadth, final int depth) {

        sb.append("\\hbox");
        super.toString(sb, prefix, breadth, depth);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append("\n");
        sb.append(prefix);
        sb.append("(hlist ");
        super.toText(sb, prefix);
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
