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

import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;


/**
 * This is a implementation of a NodeVisitor for debugging.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.1 $
 */
public class DebugNodeVisitor implements DebugableNodeVisitor {
    /**
     * Visitor for debugging.
     *
     */
    private DebugableNodeVisitor nodeVisitor = null;

    /**
     * Creates a new  instance.
     *
     * @param visitor <code>DebugableNodeVisitor</code> to inspect
     */
    public DebugNodeVisitor(final DebugableNodeVisitor visitor) {
        this.nodeVisitor = visitor;
        visitor.setVisitor(this);
    }

    /**
     * Visitor for nested nodes.
     *
     * @param visitor a <code>NodeVisitor</code> value
     * @see
     *   DebugableNodeVisitor#setVisitor(de.dante.extex.typesetter.NodeVisitor)
     */
    public void setVisitor(final NodeVisitor visitor) {
        nodeVisitor.setVisitor(visitor);
    }


    /**
     * Write the debug message.
     *
     * @param mesg a <code>String</code>
     */
    private void debugMessage(final String mesg) {
        System.out.println("DEBUG: " + mesg);
    }


    /*
     * the visit methods
     */

    public Object visitAdjust(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitAdjust");
        return nodeVisitor.visitAdjust(value, value2);
    }


    public Object visitAfterMath(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitAfterMath");
        return nodeVisitor.visitAfterMath(value, value2);
    }


    public Object visitAlignedLeaders(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitAlignedLeaders");
        return nodeVisitor.visitAlignedLeaders(value, value2);
    }


    public Object visitBeforeMath(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitBeforeMath");
        return nodeVisitor.visitBeforeMath(value, value2);
    }

    public Object visitCenteredLeaders(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitCenteredLeaders");
        return nodeVisitor.visitCenteredLeaders(value, value2);
    }


    public Object visitChar(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitChar");
        return nodeVisitor.visitChar(value, value2);
    }


    public Object visitDiscretionary(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitDiscretionary");
        return nodeVisitor.visitDiscretionary(value, value2);
    }


    public Object visitExpandedLeaders(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitExpandedLeaders");
        return nodeVisitor.visitExpandedLeaders(value, value2);
    }


    public Object visitGlue(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitGlue");
        return nodeVisitor.visitGlue(value, value2);
    }


    public Object visitHorizontalList(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitHorizontalList");
        return nodeVisitor.visitHorizontalList(value, value2);
    }


    public Object visitInsertion(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitInsertion");
        return nodeVisitor.visitInsertion(value, value2);
    }


    public Object visitKern(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitKern");
        return nodeVisitor.visitKern(value, value2);
    }


    public Object visitLigature(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitLigature");
        return nodeVisitor.visitLigature(value, value2);
    }


    public Object visitMark(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitMark");
        return nodeVisitor.visitMark(value, value2);
    }


    public Object visitPenalty(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitPenalty");
        return nodeVisitor.visitPenalty(value, value2);
    }


    public Object visitRule(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitRule");
        return nodeVisitor.visitRule(value, value2);
    }


    public Object visitSpace(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitSpace");
        return nodeVisitor.visitSpace(value, value2);
    }


    public Object visitVerticalList(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitVerticalList");
        return nodeVisitor.visitVerticalList(value, value2);
    }


    public Object visitWhatsIt(final Object value, final Object value2)
        throws GeneralException {

        debugMessage("visitWhatsIt");
        return nodeVisitor.visitWhatsIt(value, value2);
    }
}
