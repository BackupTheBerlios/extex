/*
 * Copyright (final C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (final at your
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
 * @version $Revision: 1.1 $
 */
class NV implements NodeVisitor {

    /**
     * The field <tt>nodes</tt> contains the ...
     */
    private NodeList nodes;

    /**
     * Creates a new object.
     *
     * @param nodes TODO
     */
    public NV(final NodeList nodes) {

        super();
        this.nodes = nodes;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(final de.dante.extex.typesetter.type.node.AdjustNode, java.lang.final Object)
     */
    public final Object visitAdjust(final AdjustNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitAdjust unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(final de.dante.extex.typesetter.type.node.AfterMathNode, java.lang.final Object)
     */
    public final Object visitAfterMath(final AfterMathNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitAfterMath unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(final de.dante.extex.typesetter.type.node.AlignedLeadersNode, java.lang.final Object)
     */
    public final Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitAlignedLeaders unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(final de.dante.extex.typesetter.type.node.BeforeMathNode, java.lang.final Object)
     */
    public final Object visitBeforeMath(final BeforeMathNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitBeforeMath unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(final de.dante.extex.typesetter.type.node.CenteredLeadersNode, java.lang.final Object)
     */
    public final Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitCenteredLeaders unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(final de.dante.extex.typesetter.type.node.CharNode, java.lang.final Object)
     */
    public final Object visitChar(final CharNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitChar unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(final de.dante.extex.typesetter.type.node.DiscretionaryNode, java.lang.final Object)
     */
    public final Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitDiscretionary unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(final de.dante.extex.typesetter.type.node.ExpandedLeadersNode, java.lang.final Object)
     */
    public final Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitExpandedLeaders unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(final de.dante.extex.typesetter.type.node.GlueNode, java.lang.final Object)
     */
    public final Object visitGlue(final GlueNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitGlue unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(final de.dante.extex.typesetter.type.node.HorizontalListNode, java.lang.final Object)
     */
    public final Object visitHorizontalList(final HorizontalListNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitHorizontalList unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(final de.dante.extex.typesetter.type.node.InsertionNode, java.lang.final Object)
     */
    public final Object visitInsertion(final InsertionNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitInsertion unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(final de.dante.extex.typesetter.type.node.KernNode, java.lang.final Object)
     */
    public final Object visitKern(final KernNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitKern unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(final de.dante.extex.typesetter.type.node.LigatureNode, java.lang.final Object)
     */
    public final Object visitLigature(final LigatureNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitLigature unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(final de.dante.extex.typesetter.type.node.MarkNode, java.lang.final Object)
     */
    public final Object visitMark(final MarkNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitMark unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(final de.dante.extex.typesetter.type.node.PenaltyNode, java.lang.final Object)
     */
    public final Object visitPenalty(final PenaltyNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitPenalty unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(final de.dante.extex.typesetter.type.node.RuleNode, java.lang.final Object)
     */
    public final Object visitRule(final RuleNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitRule unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(final de.dante.extex.typesetter.type.node.SpaceNode, java.lang.final Object)
     */
    public final Object visitSpace(final SpaceNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitSpace unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(final de.dante.extex.typesetter.type.node.VerticalListNode, java.lang.final Object)
     */
    public final Object visitVerticalList(final VerticalListNode node,
            final Object value) throws GeneralException {

        // TODO gene: visitVerticalList unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(final de.dante.extex.typesetter.type.node.WhatsItNode, java.lang.final Object)
     */
    public final Object visitWhatsIt(final WhatsItNode node, final Object value)
            throws GeneralException {

        // TODO gene: visitWhatsIt unimplemented
        return null;
    }
}