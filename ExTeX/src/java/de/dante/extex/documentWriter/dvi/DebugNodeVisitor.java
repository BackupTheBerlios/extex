/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

// created: 2004-07-30
package de.dante.extex.documentWriter.dvi;

import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.typesetter.type.InspectableNodeVisitor;
import de.dante.extex.typesetter.type.Node;
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
 * This is a implementation of a NodeVisitor for debugging.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.9 $
 */
public class DebugNodeVisitor implements InspectableNodeVisitor {
    /**
     * Visitor for debugging.
     *
     */
    private InspectableNodeVisitor nodeVisitor = null;

    /**
     * Creates a new instance.
     *
     * @param visitor <code>InspectableNodeVisitor</code> to inspect
     */
    public DebugNodeVisitor(final InspectableNodeVisitor visitor) {
        this.nodeVisitor = visitor;
        visitor.setVisitor(this);
    }

    /**
     * Visitor for nested nodes.
     *
     * @param visitor a <code>NodeVisitor</code> value
     * @see
     *   InspectableNodeVisitor#setVisitor(de.dante.extex.typesetter.type.NodeVisitor)
     */
    public void setVisitor(final NodeVisitor visitor) {
        nodeVisitor.setVisitor(visitor);
    }


    /**
     * Convert dimen to String.
     *
     * @param dimen this value is not modified
     * @return a string representing dimen
     */
    private String convertDimen(final FixedDimen dimen) {
        return dimen.getValue() + "sp";
    }


    /**
     * Append information about a value to a <code>StringBuffer</code>.
     *
     * @param buffer for appending information
     * @param value more information is added if this is a Node-object
     */
    private void appendNodeInformation(final StringBuffer buffer,
                                       final Object value) {

        if (value instanceof Node) {
            final Node node = (Node) value;

            buffer.append(" (wd=");
            buffer.append(convertDimen(node.getWidth()));
            buffer.append("  ht=");
            buffer.append(convertDimen(node.getHeight()));
            buffer.append("  dp=");
            buffer.append(convertDimen(node.getDepth()));
            buffer.append(")");
        } else {
            buffer.append(" [no node]");
        }

    }

    /**
     * Write the debug message.
     *
     * @param mesg a <code>String</code>
     * @param node value for additional information
     * @param value value for additional information
     */
    private void debugMessage(final String mesg,
                              final Node node,
                              final Object value) {

        StringBuffer buffer = new StringBuffer("DEBUG: ");

        buffer.append(mesg);
        appendNodeInformation(buffer, node);
        appendNodeInformation(buffer, value);

        // TODO: better the visitor knows where the Node is (TE)
        System.out.println(buffer);
    }


    /*
     * the visit methods
     */


    /**
     * Call the <code>visitAdjust</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode, Object)
     */
    public Object visitAdjust(final AdjustNode node, final Object value)
        throws GeneralException {

        debugMessage("visitAdjust", node, value);
        return nodeVisitor.visitAdjust(node, value);
    }


    /**
     * Call the <code>visitAfterMath</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode, Object)
     */
    public Object visitAfterMath(final AfterMathNode node, final Object value)
        throws GeneralException {

        debugMessage("visitAfterMath", node, value);
        return nodeVisitor.visitAfterMath(node, value);
    }


    /**
     * Call the <code>visitAlignedLeaders</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode, Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode node, final Object value)
        throws GeneralException {

        debugMessage("visitAlignedLeaders", node, value);
        return nodeVisitor.visitAlignedLeaders(node, value);
    }


    /**
     * Call the <code>visitBeforeMath</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode, Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value)
        throws GeneralException {

        debugMessage("visitBeforeMath", node, value);
        return nodeVisitor.visitBeforeMath(node, value);
    }


    /**
     * Call the <code>visitCenteredLeaders</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode, Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node, final Object value)
        throws GeneralException {

        debugMessage("visitCenteredLeaders", node, value);
        return nodeVisitor.visitCenteredLeaders(node, value);
    }


    /**
     * Call the <code>visitChar</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode, Object)
     */
    public Object visitChar(final CharNode node, final Object value)
        throws GeneralException {

        debugMessage("visitChar", node, value);
        return nodeVisitor.visitChar(node, value);
    }


    /**
     * Call the <code>visitDiscretionary</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode,
     *      Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node, final Object value)
        throws GeneralException {

        debugMessage("visitDiscretionary", node, value);
        return nodeVisitor.visitDiscretionary(node, value);
    }


    /**
     * Call the <code>visitExpandedLeaders</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
     *      Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node, final Object value)
        throws GeneralException {

        debugMessage("visitExpandedLeaders", node, value);
        return nodeVisitor.visitExpandedLeaders(node, value);
    }


    /**
     * Call the <code>visitGlue</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode, Object)
     */
    public Object visitGlue(final GlueNode node, final Object value)
        throws GeneralException {

        debugMessage("visitGlue", node, value);
        return nodeVisitor.visitGlue(node, value);
    }


    /**
     * Call the <code>visitHorizontalList</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
     *      Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node, final Object value)
        throws GeneralException {

        debugMessage("visitHorizontalList", node, value);
        return nodeVisitor.visitHorizontalList(node, value);
    }


    /**
     * Call the <code>visitInsertion</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode, Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value)
        throws GeneralException {

        debugMessage("visitInsertion", node, value);
        return nodeVisitor.visitInsertion(node, value);
    }


    /**
     * Call the <code>visitKern</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode, Object)
     */
    public Object visitKern(final KernNode node, final Object value)
        throws GeneralException {

        debugMessage("visitKern", node, value);
        return nodeVisitor.visitKern(node, value);
    }


    /**
     * Call the <code>visitLigature</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode, Object)
     */
    public Object visitLigature(final LigatureNode node, final Object value)
        throws GeneralException {

        debugMessage("visitLigature", node, value);
        return nodeVisitor.visitLigature(node, value);
    }


    /**
     * Call the <code>visitMark</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode, Object)
     */
    public Object visitMark(final MarkNode node, final Object value)
        throws GeneralException {

        debugMessage("visitMark", node, value);
        return nodeVisitor.visitMark(node, value);
    }


    /**
     * Call the <code>visitPenalty</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode, Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value)
        throws GeneralException {

        debugMessage("visitPenalty", node, value);
        return nodeVisitor.visitPenalty(node, value);
    }


    /**
     * Call the <code>visitRule</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode, Object)
     */
    public Object visitRule(final RuleNode node, final Object value)
        throws GeneralException {

        debugMessage("visitRule", node, value);
        return nodeVisitor.visitRule(node, value);
    }


    /**
     * Call the <code>visitSpace</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode, Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value)
        throws GeneralException {

        debugMessage("visitSpace", node, value);
        return nodeVisitor.visitSpace(node, value);
    }


    /**
     * Call the <code>visitVerticalList</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode, Object)
     */
    public Object visitVerticalList(final VerticalListNode node, final Object value)
        throws GeneralException {

        debugMessage("visitVerticalList", node, value);
        return nodeVisitor.visitVerticalList(node, value);
    }


    /**
     * Call the <code>visitWhatsIt</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode, Object)
     */
    public Object visitWhatsIt(final WhatsItNode node, final Object value)
        throws GeneralException {

        debugMessage("visitWhatsIt", node, value);
        return nodeVisitor.visitWhatsIt(node, value);
    }
}
