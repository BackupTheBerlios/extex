/*
 * Copyright (final C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, final or (final at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, final but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, final write to the Free Software Foundation, final
 * Inc., final 59 Temple Place, final Suite 330, final Boston, final MA 02111-1307 USA
 *
 */
package de.dante.extex.typesetter.type;

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
 * This abstract class can be used as base for node visitors for which only a
 * few methods carry any functionality.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractNodeVisitor implements NodeVisitor {

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(final de.dante.extex.typesetter.type.node.AdjustNode, final java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(final de.dante.extex.typesetter.type.node.AfterMathNode, final java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(final de.dante.extex.typesetter.type.node.AlignedLeadersNode, final java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(final de.dante.extex.typesetter.type.node.BeforeMathNode, final java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(final de.dante.extex.typesetter.type.node.CenteredLeadersNode, final java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(final de.dante.extex.typesetter.type.node.CharNode, final java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(final de.dante.extex.typesetter.type.node.DiscretionaryNode, final java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(final de.dante.extex.typesetter.type.node.ExpandedLeadersNode, final java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(final de.dante.extex.typesetter.type.node.GlueNode, final java.lang.Object)
     */
    public Object visitGlue(final GlueNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(final de.dante.extex.typesetter.type.node.HorizontalListNode, final java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(final de.dante.extex.typesetter.type.node.InsertionNode, final java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(final de.dante.extex.typesetter.type.node.KernNode, final java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(final de.dante.extex.typesetter.type.node.LigatureNode, final java.lang.Object)
     */
    public Object visitLigature(final LigatureNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(final de.dante.extex.typesetter.type.node.MarkNode, final java.lang.Object)
     */
    public Object visitMark(final MarkNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(final de.dante.extex.typesetter.type.node.PenaltyNode, final java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(final de.dante.extex.typesetter.type.node.RuleNode, final java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(final de.dante.extex.typesetter.type.node.SpaceNode, final java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(final de.dante.extex.typesetter.type.node.VerticalListNode, final java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(final de.dante.extex.typesetter.type.node.WhatsItNode, final java.lang.Object)
     */
    public Object visitWhatsIt(final WhatsItNode n0de, final Object value)
            throws GeneralException {

        return null;
    }

}
