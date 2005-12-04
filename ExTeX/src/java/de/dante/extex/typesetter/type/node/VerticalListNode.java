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

import java.util.logging.Logger;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.Badness;
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
 * @version $Revision: 1.11 $
 */
public class VerticalListNode extends AbstractNodeList implements NodeList {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [136]"
     */
    public VerticalListNode() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeList#addSkip(
     *      FixedGlue)
     */
    public void addSkip(final FixedGlue glue) {

        Node gNode = new GlueNode(glue, false);
        gNode.setHeight(glue.getLength());
        add(gNode);
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
    private long computePenalty(final long penalty, final Glue ht,
            final FixedDimen height) {

        long badness = Badness.badness(height.getValue(), //
                ht.getLength().getValue());
        long p = penalty;
        // TODO gene: computePenalty unimplemented
        return p;
    }

    /**
     * Split off material from a vertical list of a desired height. The
     * splitting is performed at a position with minimal penalty. The list is
     * stretched to the desired height.
     *
     * @param height the target height
     * @param traceLogger the logger for tracing
     * @param logger the logger of normal logging output
     *
     * @return the split off material
     */
    public VerticalListNode split(final FixedDimen height, final Logger logger,
            final Logger traceLogger) {

        long penalty;
        long bestPenalty = 10001;
        Dimen bestHeight = Dimen.ZERO_PT;
        Glue ht = new Glue(0);
        int size = size();
        int bestSplit = size;

        for (int i = 0; i < size; i++) {
            Node node = get(i);
            node.addHeightTo(ht);
            if (i + 1 >= size || !(get(i + 1) instanceof PenaltyNode)) {
                penalty = computePenalty((node instanceof PenaltyNode
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
     * Spread the list vertically to a desired size by distributing the
     * differences to the glues contained.
     *
     * @param height the target size
     *
     * @return the badness of the spread
     */
    public long spread(final FixedDimen height) {

        Glue ht = new Glue(0);
        int size = size();
        for (int i = 0; i < size; i++) {
            get(i).addHeightTo(ht);
        }

        Dimen length = ht.getLength();
        for (int i = 0; i < size; i++) {
            get(i).spread(height, length);
        }

        return Badness.badness(height.getValue(), length.getValue());
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("\\vbox");
        super.toString(sb, prefix);
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
     * @see de.dante.extex.typesetter.type.node.AbstractNodeList#updateDimensions(
     *      de.dante.extex.typesetter.type.Node, boolean)
     */
    protected void updateDimensions(final Node node, final boolean first) {

        getWidth().max(node.getWidth());
        Dimen d = getDepth();
        if (first) {
            getHeight().set(node.getHeight());
        } else {
            d.add(node.getHeight());
        }
        d.add(node.getDepth());
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
}
