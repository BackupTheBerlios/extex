/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.WideGlue;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;

/**
 * This class exposes itself as character node but contains an hlist internally.
 * This class is used to represent composed characters from virtual fonts.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.22 $
 */
public class VirtualCharNode extends CharNode implements NodeList {

    /**
     * This inner class provides the means to store nodes in a list.
     * It is here to compensate the missing multiple inheritance of Java.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.22 $
     */
    private class NL extends GenericNodeList {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * The field <tt>node</tt> contains the parent node.
         */
        private VirtualCharNode node;

        /**
         * Creates a new object.
         *
         * @param node the parent node
         */
        public NL(final VirtualCharNode node) {

            super();
            this.node = node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeList#addSkip(
         *      de.dante.extex.interpreter.type.glue.FixedGlue)
         */
        public void addSkip(final FixedGlue glue) {

            // glues are ignored
        }

        /**
         * @see de.dante.extex.typesetter.type.node.AbstractNodeList#updateDimensions(
         *      de.dante.extex.typesetter.type.Node, boolean)
         */
        protected void updateDimensions(final Node n, final boolean first) {

            // This should not be needed
            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.typesetter.type.Node#visit(
         *      de.dante.extex.typesetter.type.NodeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final NodeVisitor visitor, final Object value)
                throws GeneralException {

            return visitor.visitChar(node, value);
        }
    }

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>nodes</tt> contains the encapsulated node list.
     */
    private NodeList nodes;

    /**
     * Creates a new object.
     *
     * @param context the typesetting context
     * @param uc the character represented by this node
     */
    public VirtualCharNode(final TypesettingContext context,
            final UnicodeChar uc) {

        super(context, uc);
        nodes = new NL(this);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#add(int,
     *      de.dante.extex.typesetter.type.Node)
     */
    public void add(final int index, final Node node) {

        this.nodes.add(index, node);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#add(
     *      de.dante.extex.typesetter.type.Node)
     */
    public void add(final Node node) {

        this.nodes.add(node);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#addSkip(
     *      de.dante.extex.interpreter.type.glue.FixedGlue)
     */
    public void addSkip(final FixedGlue glue) {

        this.nodes.addSkip(glue);
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNode#addWidthTo(
     *      de.dante.extex.interpreter.type.glue.WideGlue)
     */
    public void addWidthTo(final WideGlue glue) {

        this.nodes.addWidthTo(glue);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#atShipping(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.typesetter.type.NodeVisitor, boolean)
     */
    public Node atShipping(final Context context, final Typesetter typesetter,
            final NodeVisitor visitor, final boolean inHMode)
            throws GeneralException {

        return this.nodes.atShipping(context, typesetter, visitor, inHMode);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#clear()
     */
    public void clear() {

        this.nodes.clear();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#copy()
     */
    public NodeList copy() {

        try {
            return (NodeList) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new ImpossibleException(e);
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.node.CharNode#countChars()
     */
    public int countChars() {

        return this.nodes.countChars();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#get(int)
     */
    public Node get(final int index) {

        return this.nodes.get(index);
    }

    /**
     * @see de.dante.extex.typesetter.type.node.CharNode#getChars()
     */
    public CharNode[] getChars() {

        return this.nodes.getChars();
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getDepth()
     */
    public FixedDimen getDepth() {

        return this.nodes.getDepth();
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getHeight()
     */
    public FixedDimen getHeight() {

        return this.nodes.getHeight();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#getMove()
     */
    public Dimen getMove() {

        return this.nodes.getMove();
    }

    /**
     * Getter for nodes.
     *
     * @return the nodes
     */
    public NodeList getNodes() {

        return this.nodes;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#getShift()
     */
    public Dimen getShift() {

        return this.nodes.getShift();
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getVerticalSize()
     */
    public FixedDimen getVerticalSize() {

        return this.nodes.getVerticalSize();
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getWidth()
     */
    public FixedDimen getWidth() {

        return this.nodes.getWidth();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#iterator()
     */
    public NodeIterator iterator() {

        return this.nodes.iterator();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#remove(int)
     */
    public Node remove(final int index) {

        return this.nodes.remove(index);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setDepth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setDepth(final FixedDimen depth) {

        this.nodes.setDepth(depth);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setHeight(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setHeight(final FixedDimen height) {

        this.nodes.setHeight(height);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#setMove(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setMove(final FixedDimen d) {

        this.nodes.setMove(d);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#setShift(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setShift(final FixedDimen d) {

        this.nodes.setShift(d);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setWidth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setWidth(final FixedDimen width) {

        this.nodes.setWidth(width);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#size()
     */
    public int size() {

        return this.nodes.size();
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#spreadWidth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen,
     *      de.dante.extex.interpreter.type.glue.FixedGlueComponent)
     */
    public void spreadWidth(final FixedDimen width, final FixedGlueComponent sum) {

        this.nodes.spreadWidth(width, sum);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitVirtualChar(this, value);
    }

}
