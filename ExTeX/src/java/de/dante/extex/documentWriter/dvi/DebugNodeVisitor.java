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
import de.dante.util.GeneralException;


/**
 * This is a implementation of a NodeVisitor for debugging.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.6 $
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
     *   InspectableNodeVisitor#setVisitor(de.dante.extex.typesetter.NodeVisitor)
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

        Node node = null;

        if (value instanceof Node) {
            node = (Node) value;

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
     * @param value1 value for additional information
     * @param value2 value for additional information
     */
    private void debugMessage(final String mesg,
                              final Object value1,
                              final Object value2) {

        StringBuffer buffer = new StringBuffer("DEBUG: ");

        buffer.append(mesg);
        appendNodeInformation(buffer, value1);
        appendNodeInformation(buffer, value2);

        // TODO: better the visitor knows where the Node is (TE)
        System.out.println(buffer);
    }


    /*
     * the visit methods
     */


    /**
     * Call the <code>visitAdjust</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(Node, Object)
     */
    public Object visitAdjust(final Node value, final Object value2)
        throws GeneralException {

        debugMessage("visitAdjust", value, value2);
        return nodeVisitor.visitAdjust(value, value2);
    }


    /**
     * Call the <code>visitAfterMath</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(Node, Object)
     */
    public Object visitAfterMath(final Node value, final Object value2)
        throws GeneralException {

        debugMessage("visitAfterMath", value, value2);
        return nodeVisitor.visitAfterMath(value, value2);
    }


    /**
     * Call the <code>visitAlignedLeaders</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(Node, Object)
     */
    public Object visitAlignedLeaders(final Node value, final Object value2)
        throws GeneralException {

        debugMessage("visitAlignedLeaders", value, value2);
        return nodeVisitor.visitAlignedLeaders(value, value2);
    }


    /**
     * Call the <code>visitBeforeMath</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(Node, Object)
     */
    public Object visitBeforeMath(final Node node, final Object value2)
        throws GeneralException {

        debugMessage("visitBeforeMath", node, value2);
        return nodeVisitor.visitBeforeMath(node, value2);
    }


    /**
     * Call the <code>visitCenteredLeaders</code> to inspect.
     *
     * @param node the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(Node, Object)
     */
    public Object visitCenteredLeaders(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(Node, Object)
     */
    public Object visitChar(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(Node,
     *      Object)
     */
    public Object visitDiscretionary(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(Node,
     *      Object)
     */
    public Object visitExpandedLeaders(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(Node, Object)
     */
    public Object visitGlue(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(Node,
     *      Object)
     */
    public Object visitHorizontalList(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(Node, Object)
     */
    public Object visitInsertion(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(Node, Object)
     */
    public Object visitKern(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(Node, Object)
     */
    public Object visitLigature(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(Node, Object)
     */
    public Object visitMark(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(Node, Object)
     */
    public Object visitPenalty(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(Node, Object)
     */
    public Object visitRule(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(Node, Object)
     */
    public Object visitSpace(final Node node, final Object value)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(Node, Object)
     */
    public Object visitVerticalList(final Node node, final Object value)
        throws GeneralException {

        debugMessage("visitVerticalList", node, value);
        return nodeVisitor.visitVerticalList(node, value);
    }


    /**
     * Call the <code>visitWhatsIt</code> to inspect.
     *
     * @param nde the first parameter for the visitor
     * @param value the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(Node, Object)
     */
    public Object visitWhatsIt(final Node nde, final Object value)
        throws GeneralException {

        debugMessage("visitWhatsIt", nde, value);
        return nodeVisitor.visitWhatsIt(nde, value);
    }
}
