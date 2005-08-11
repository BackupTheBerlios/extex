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

package de.dante.extex.language.hyphenation.util;

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
import de.dante.extex.typesetter.type.node.VirtualCharNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;

/**
 * This class provides means to traverse a node recursively and report all
 * characters contained.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class NodeTraverser implements NodeVisitor {

    /**
     * The field <tt>node</tt> contains the node to traverse.
     */
    private Node node;

    /**
     * The field <tt>nt</tt> contains the traverser for sub-nodes.
     */
    private NodeTraverser nt = null;

    /**
     * The field <tt>pointer</tt> contains the pointer to the next item.
     */
    private int pointer = 0;

    /**
     * Creates a new object.
     *
     * @param node the node to traverse
     */
    public NodeTraverser(final Node node) {

        super();
        this.node = node;
    }

    /**
     * Getter for the next character or <code>null</code> if none is present.
     *
     * @return the next character
     */
    public UnicodeChar next() {

        if (node == null) {
            return null;
        }
        try {
            return (UnicodeChar) node.visit(this, null);
        } catch (GeneralException e) {
            // This should not happen since none of the visitor methods throws
            // an exception.
            throw new RuntimeException(e);
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
     *      de.dante.extex.typesetter.type.node.AdjustNode,
     *      java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
     *      de.dante.extex.typesetter.type.node.AfterMathNode,
     *      java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
     *      de.dante.extex.typesetter.type.node.AlignedLeadersNode,
     *      java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode n,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
     *      de.dante.extex.typesetter.type.node.BeforeMathNode,
     *      java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
     *      de.dante.extex.typesetter.type.node.CenteredLeadersNode,
     *      java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode n,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
     *      de.dante.extex.typesetter.type.node.CharNode,
     *      java.lang.Object)
     */
    public Object visitChar(final CharNode n, final Object value) {

        if (pointer == 0) {
            pointer++;
            return n.getCharacter();
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
     *      de.dante.extex.typesetter.type.node.DiscretionaryNode,
     *      java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode n,
            final Object value) throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
     *      de.dante.extex.typesetter.type.node.ExpandedLeadersNode,
     *      java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode n,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
     *      de.dante.extex.typesetter.type.node.GlueNode,
     *      java.lang.Object)
     */
    public Object visitGlue(final GlueNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode,
     *      java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode n,
            final Object value) {

        if (nt != null) {
            UnicodeChar uc = nt.next();
            if (uc != null) {
                return uc;
            }
        }
        while (pointer < n.size()) {
            nt = new NodeTraverser(n.get(pointer++));
            UnicodeChar uc = nt.next();
            if (uc != null) {
                return uc;
            }
        }
        nt = null;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
     *      de.dante.extex.typesetter.type.node.InsertionNode,
     *      java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
     *      de.dante.extex.typesetter.type.node.KernNode,
     *      java.lang.Object)
     */
    public Object visitKern(final KernNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
     *      de.dante.extex.typesetter.type.node.LigatureNode,
     *      java.lang.Object)
     */
    public Object visitLigature(final LigatureNode n, final Object value) {

        UnicodeChar uc;

        if (nt != null) {
            uc = nt.next();
            if (uc != null) {
                return uc;
            }
        }
        switch (pointer) {
            case 0:
                pointer++;
                nt = new NodeTraverser(n.getLeft());
                uc = nt.next();
                if (uc != null) {
                    return uc;
                }
            // continue with the second part
            case 1:
                pointer++;
                nt = new NodeTraverser(n.getRight());
                uc = nt.next();
                if (uc != null) {
                    return uc;
                }
            // finally fail
            default:
                return null;
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
     *      de.dante.extex.typesetter.type.node.MarkNode,
     *      java.lang.Object)
     */
    public Object visitMark(final MarkNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
     *      de.dante.extex.typesetter.type.node.PenaltyNode,
     *      java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
     *      de.dante.extex.typesetter.type.node.RuleNode,
     *      java.lang.Object)
     */
    public Object visitRule(final RuleNode n, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(
     *      de.dante.extex.typesetter.type.node.SpaceNode,
     *      java.lang.Object)
     */
    public Object visitSpace(final SpaceNode n, final Object value) {

        return new UnicodeChar(' ');
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
     *      de.dante.extex.typesetter.type.node.VerticalListNode,
     *      java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode n, final Object value) {

        if (nt != null) {
            UnicodeChar uc = nt.next();
            if (uc != null) {
                return uc;
            }
        }
        while (pointer < n.size()) {
            nt = new NodeTraverser(n.get(pointer++));
            UnicodeChar uc = nt.next();
            if (uc != null) {
                return uc;
            }
        }
        nt = null;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(
     *      de.dante.extex.typesetter.type.node.VirtualCharNode,
     *      java.lang.Object)
     */
    public Object visitVirtualChar(final VirtualCharNode n, final Object value) {

        if (pointer == 0) {
            pointer++;
            return n.getCharacter();
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
     *      de.dante.extex.typesetter.type.node.WhatsItNode,
     *      java.lang.Object)
     */
    public Object visitWhatsIt(final WhatsItNode n, final Object value) {

        return null;
    }
}