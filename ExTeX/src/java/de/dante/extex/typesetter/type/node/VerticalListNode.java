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

import java.util.logging.Logger;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.WideGlue;
import de.dante.extex.typesetter.Badness;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for a vertical list.
 *
 * @see "<logo>TeX</logo> &ndash; The Program [137]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.20 $
 */
public class VerticalListNode extends GenericNodeList implements NodeList {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060930L;

    /**
     * The field <tt>top</tt> contains the indicator that the adjustment
     * should use the reference point of the fist box. This is the mode for
     * <tt>\vtop</tt>. In contrast the last box is used. This is the mode for
     * <tt>\vbox</tt>.
     */
    private boolean top = false;

    /**
     * Creates a new object.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [136]"
     */
    public VerticalListNode() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNodeList#add(
     *      int,
     *      de.dante.extex.typesetter.type.Node)
     */
    public void add(final int index, final Node node) {

        super.add(index, node);
        maxWidth(node.getWidth());

        int size = size();

        if (size == 1) {
            setHeight(node.getHeight());
            setDepth(node.getDepth());
        } else if (top) {
            if (index == 0) {
                advanceDepth(getHeight());
                setHeight(node.getHeight());
            } else {
                advanceDepth(node.getHeight());
            }
            advanceDepth(node.getDepth());
        } else if (index == size) {
            advanceHeight(getDepth());
            advanceHeight(node.getHeight());
            setDepth(node.getDepth());
        } else {
            advanceHeight(node.getHeight());
            advanceHeight(node.getDepth());
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNodeList#add(
     *      de.dante.extex.typesetter.type.Node)
     */
    public void add(final Node node) {

        super.add(node);
        maxWidth(node.getWidth());

        if (size() == 1) {
            setHeight(node.getHeight());
            setDepth(node.getDepth());
        } else if (top) {
            advanceDepth(node.getDepth());
            advanceDepth(node.getHeight());
        } else {
            advanceHeight(getDepth());
            advanceHeight(node.getHeight());
            setDepth(node.getDepth());
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#addSkip(
     *      de.dante.extex.interpreter.type.glue.FixedGlue)
     */
    public void addSkip(final FixedGlue glue) {

        add(new GlueNode(glue, false));
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

        return super.atShipping(context, typesetter, visitor, false);
    }

    /**
     * Getter for top.
     *
     * @return the top
     */
    public boolean isTop() {

        return this.top;
    }

    /**
     * Setter for top.
     *
     * @param top the top to set
     */
    public void setTop(final boolean top) {

        this.top = top;
    }

    /**
     * Split-off material from a vertical list of a desired height. The
     * splitting is performed at a position with minimal penalty. The list is
     * stretched to the desired height.
     *
     * @param height the target height
     * @param logger the logger for normal logging output
     * @param traceLogger the logger for tracing
     *
     * @return the split off material
     */
    public VerticalListNode split(final FixedDimen height, final Logger logger,
            final Logger traceLogger) {

        long penalty;
        long bestPenalty = Badness.INF_PENALTY + 1;
        FixedDimen bestHeight = Dimen.ZERO_PT;
        WideGlue ht = new WideGlue();
        int size = size();
        int bestSplit = size;

        for (int i = 0; i < size; i++) {
            Node node = get(i);
            node.addHeightTo(ht);
            node.addDepthTo(ht);
            if (i + 1 >= size || !(get(i + 1) instanceof PenaltyNode)) {
                penalty = splitPenalty((node instanceof PenaltyNode
                        ? ((PenaltyNode) node).getPenalty()
                        : 0), ht, height);
                if (penalty < bestPenalty) {
                    bestPenalty = penalty;
                    bestSplit = i;
                    bestHeight = ht.getLength();
                }
            }
        }

        if (traceLogger != null) {
            traceLogger.info("% split" + "???" + " to " + height + ","
                    + bestHeight + " p=" + bestPenalty);
        }
        VerticalListNode result = new VerticalListNode();
        for (int i = 0; i < bestSplit; i++) {
            result.add(get(i));
        }

        return result;
    }

    /**
     * Compute the penalty for the split at a given position.
     *
     * @param penalty the base penalty optionally coming from a penalty node
     * @param ht the actual height
     * @param height the desired height
     *
     * @return the penalty in the range 0 to 10000, including
     */
    private long splitPenalty(final long penalty, final WideGlue ht,
            final FixedDimen height) {

        //        long badness = Badness.badness(height.getValue(), //
        //                ht.getLength().getValue());
        long p = penalty;
        // TODO gene: splitPenalty() unimplemented
        return p;
    }

    /**
     * Spread the list vertically to a desired size by distributing the
     * differences to the glues contained.
     *
     * @param height the target size
     *
     * @return the badness of the spread
     */
    public long spread(final FixedDimen height) {

        int size = size();

        WideGlue ht = new WideGlue();

        for (int i = 0; i < size; i++) {
            get(i).addHeightTo(ht);
            get(i).addDepthTo(ht);
        }

        FixedDimen length = ht.getLength();
        for (int i = 0; i < size; i++) {
            get(i).spreadHeight(height, length);
        }

        return Badness.badness(height.getValue(), length.getValue());
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#spreadHeight(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen,
     *      de.dante.extex.interpreter.type.glue.FixedGlueComponent)
     */
    public void spreadHeight(final FixedDimen w, final FixedGlueComponent sum) {

    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String, int, int)
     */
    public void toString(final StringBuffer sb, final String prefix,
            final int breadth, final int depth) {

        sb.append("\\vbox");
        super.toString(sb, prefix, breadth, depth);
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
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitVerticalList(this, value);
    }

    /**
     * Adjust the variable nodes to achieve a given target height.
     *
     * @param targetHeight the target height
     *
     * @return the badness
     */
    public long vpack(final FixedDimen targetHeight) {

        int size = size();

        if (size == 0) {
            setHeight(targetHeight);
            return 0;
        }

        if (top) {
            size = 1;
        }

        Dimen ht = new Dimen();
        WideGlue flexibleHeight = new WideGlue();

        ht.set(get(size - 1).getHeight());

        for (int i = 0; i < size - 1; i++) {
            Node node = get(i);
            ht.add(node.getHeight());
            ht.add(node.getDepth());
            node.addHeightTo(flexibleHeight);
            node.addDepthTo(flexibleHeight);
        }

        if (targetHeight.ne(ht)) {
            ht.subtract(targetHeight);
            ht.negate();
            FixedGlueComponent s = (ht.le(Dimen.ZERO) //
                    ? flexibleHeight.getShrink() //
                    : flexibleHeight.getStretch());
            for (int i = 0; i < size; i++) {
                get(i).spreadHeight(ht, s);
            }
            setHeight(targetHeight);
        } else {
            setHeight(ht);
        }

        return 0; // TODO gene: compute badness
    }

}
