/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.hyphenation.liang;

import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.extex.typesetter.type.node.AdjustNode;
import de.dante.extex.typesetter.type.node.AfterMathNode;
import de.dante.extex.typesetter.type.node.AlignedLeadersNode;
import de.dante.extex.typesetter.type.node.BeforeMathNode;
import de.dante.extex.typesetter.type.node.CenteredLeadersNode;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.ExpandedLeadersNode;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.InsertionNode;
import de.dante.extex.typesetter.type.node.KernNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.extex.typesetter.type.node.MarkNode;
import de.dante.extex.typesetter.type.node.PenaltyNode;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.SpaceNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.GeneralException;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
class NV implements NodeVisitor {

    /**
     * The field <tt>hyph</tt> contains the indicator for the hyphenation
     * positions. A hyphenation is possible at a position <i>i</i> whenever the
     * value <i>isHyph[i] == true</i>.
     */
    private boolean[] isHyph;

    /**
     * The field <tt>hyphen</tt> contains the tokens to insert at a hyphenation
     * position into the discretionary.
     */
    private Tokens hyphen;

    /**
     * The field <tt>nodes</tt> contains the node list to add the current
     * node to, with or without additional discretionary node.
     */
    private NodeList nodes;

    /**
     * Creates a new object.
     *
     * @param nodes the list of nodes to add the current node to
     * @param hyphen the tokens to insert into the discretionary
     * @param hyph the indocator for allowed hyphenation positions
     */
    public NV(final NodeList nodes, final Tokens hyphen, final boolean[] hyph) {

        super();
        this.nodes = nodes;
        this.hyphen = hyphen;
        this.isHyph = hyph;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param list the node list to process
     * @param index the index in the word to start with
     */
    private final void processNodeList(final NodeList list, final Count index) {

        this.nodes.add(list);

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
     *      de.dante.extex.typesetter.type.node.AdjustNode,
     *      java.lang.final Object)
     */
    public final Object visitAdjust(final AdjustNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
     *      de.dante.extex.typesetter.type.node.AfterMathNode,
     *      java.lang.final Object)
     */
    public final Object visitAfterMath(final AfterMathNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
     *      de.dante.extex.typesetter.type.node.AlignedLeadersNode,
     *      java.lang.final Object)
     */
    public final Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
     *      de.dante.extex.typesetter.type.node.BeforeMathNode,
     *      java.lang.final Object)
     */
    public final Object visitBeforeMath(final BeforeMathNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
     *      de.dante.extex.typesetter.type.node.CenteredLeadersNode,
     *      java.lang.final Object)
     */
    public final Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
     *      de.dante.extex.typesetter.type.node.CharNode,
     *      java.lang.final Object)
     */
    public final Object visitChar(final CharNode node, final Object value)
            throws GeneralException {

        Count index = (Count) value;
        if (isHyph[(int) index.getValue()]) {
            nodes
                    .add(new DiscretionaryNode(Tokens.EMPTY, hyphen,
                            Tokens.EMPTY));
        }
        nodes.add(node);
        index.add(1);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
     *      de.dante.extex.typesetter.type.node.DiscretionaryNode,
     *      java.lang.final Object)
     */
    public final Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
     *      de.dante.extex.typesetter.type.node.ExpandedLeadersNode,
     *      java.lang.final Object)
     */
    public final Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
     *      de.dante.extex.typesetter.type.node.GlueNode,
     *      java.lang.final Object)
     */
    public final Object visitGlue(final GlueNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode,
     *      java.lang.final Object)
     */
    public final Object visitHorizontalList(final HorizontalListNode node,
            final Object value) throws GeneralException {

        processNodeList(node, (Count) value);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
     *      de.dante.extex.typesetter.type.node.InsertionNode,
     *      java.lang.final Object)
     */
    public final Object visitInsertion(final InsertionNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
     *      de.dante.extex.typesetter.type.node.KernNode,
     *      java.lang.final Object)
     */
    public final Object visitKern(final KernNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
     *      de.dante.extex.typesetter.type.node.LigatureNode,
     *      java.lang.final Object)
     */
    public final Object visitLigature(final LigatureNode node,
            final Object value) throws GeneralException {

        Count index = (Count) value;
        int n = node.countChars();
        int offset = (int) index.getValue();
        int needHyphen = 0;

        for (int i = offset; i < offset + n; i++) {
            if (isHyph[i]) {
                needHyphen++;
            }
        }
        if (needHyphen == 0) {
            nodes.add(node);
            return null;
        }

        int leftLen = node.getLeft().countChars();
        int rightLen = node.getRight().countChars();

        // TODO gene: visitLigature unimplemented
        throw new RuntimeException("unimplemented");
        //return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
     *      de.dante.extex.typesetter.type.node.MarkNode,
     *      java.lang.final Object)
     */
    public final Object visitMark(final MarkNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
     *      de.dante.extex.typesetter.type.node.PenaltyNode,
     *      java.lang.final Object)
     */
    public final Object visitPenalty(final PenaltyNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
     *      de.dante.extex.typesetter.type.node.RuleNode,
     *      java.lang.final Object)
     */
    public final Object visitRule(final RuleNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(
     *      de.dante.extex.typesetter.type.node.SpaceNode,
     *      java.lang.final Object)
     */
    public final Object visitSpace(final SpaceNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
     *      de.dante.extex.typesetter.type.node.VerticalListNode,
     *      java.lang.final Object)
     */
    public final Object visitVerticalList(final VerticalListNode node,
            final Object value) throws GeneralException {

        processNodeList(node, (Count) value);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
     *      de.dante.extex.typesetter.type.node.WhatsItNode,
     *      java.lang.final Object)
     */
    public final Object visitWhatsIt(final WhatsItNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

}