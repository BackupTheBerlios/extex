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
 * @version $Revision: 1.5 $
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(Object, Object)
     */
    public Object visitAdjust(final Object value, final Object value2)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(Object, Object)
     */
    public Object visitAfterMath(final Object value, final Object value2)
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
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(Object, Object)
     */
    public Object visitAlignedLeaders(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitAlignedLeaders", value, value2);
        return nodeVisitor.visitAlignedLeaders(value, value2);
    }


    /**
     * Call the <code>visitBeforeMath</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(Object, Object)
     */
    public Object visitBeforeMath(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitBeforeMath", value, value2);
        return nodeVisitor.visitBeforeMath(value, value2);
    }


    /**
     * Call the <code>visitCenteredLeaders</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(Object, Object)
     */
    public Object visitCenteredLeaders(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitCenteredLeaders", value, value2);
        return nodeVisitor.visitCenteredLeaders(value, value2);
    }


    /**
     * Call the <code>visitChar</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(Object, Object)
     */
    public Object visitChar(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitChar", value, value2);
        return nodeVisitor.visitChar(value, value2);
    }


    /**
     * Call the <code>visitDiscretionary</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(Object,
     *      Object)
     */
    public Object visitDiscretionary(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitDiscretionary", value, value2);
        return nodeVisitor.visitDiscretionary(value, value2);
    }


    /**
     * Call the <code>visitExpandedLeaders</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(Object,
     *      Object)
     */
    public Object visitExpandedLeaders(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitExpandedLeaders", value, value2);
        return nodeVisitor.visitExpandedLeaders(value, value2);
    }


    /**
     * Call the <code>visitGlue</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(Object, Object)
     */
    public Object visitGlue(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitGlue", value, value2);
        return nodeVisitor.visitGlue(value, value2);
    }


    /**
     * Call the <code>visitHorizontalList</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(Object,
     *      Object)
     */
    public Object visitHorizontalList(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitHorizontalList", value, value2);
        return nodeVisitor.visitHorizontalList(value, value2);
    }


    /**
     * Call the <code>visitInsertion</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(Object, Object)
     */
    public Object visitInsertion(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitInsertion", value, value2);
        return nodeVisitor.visitInsertion(value, value2);
    }


    /**
     * Call the <code>visitKern</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(Object, Object)
     */
    public Object visitKern(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitKern", value, value2);
        return nodeVisitor.visitKern(value, value2);
    }


    /**
     * Call the <code>visitLigature</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(Object, Object)
     */
    public Object visitLigature(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitLigature", value, value2);
        return nodeVisitor.visitLigature(value, value2);
    }


    /**
     * Call the <code>visitMark</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(Object, Object)
     */
    public Object visitMark(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitMark", value, value2);
        return nodeVisitor.visitMark(value, value2);
    }


    /**
     * Call the <code>visitPenalty</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(Object, Object)
     */
    public Object visitPenalty(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitPenalty", value, value2);
        return nodeVisitor.visitPenalty(value, value2);
    }


    /**
     * Call the <code>visitRule</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(Object, Object)
     */
    public Object visitRule(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitRule", value, value2);
        return nodeVisitor.visitRule(value, value2);
    }


    /**
     * Call the <code>visitSpace</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(Object, Object)
     */
    public Object visitSpace(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitSpace", value, value2);
        return nodeVisitor.visitSpace(value, value2);
    }


    /**
     * Call the <code>visitVerticalList</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(Object, Object)
     */
    public Object visitVerticalList(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitVerticalList", value, value2);
        return nodeVisitor.visitVerticalList(value, value2);
    }


    /**
     * Call the <code>visitWhatsIt</code> to inspect.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     * @return the visitor specific value of inspecting visitor
     * @exception GeneralException if an error occurs
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(Object, Object)
     */
    public Object visitWhatsIt(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitWhatsIt", value, value2);
        return nodeVisitor.visitWhatsIt(value, value2);
    }
}
