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

package de.dante.extex.hyphenation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
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
 * This class stores the values for hyphenations and
 * hypernates words.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class LiangsHyphenationTable extends BaseHyphenationTable
        implements
            NodeVisitor {

    /**
     * This class provides a container for hyphen values.
     *
     * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
     * @version $Revision: 1.1 $
     */
    private static class HyphValues {

        /**
         * The context
         */
        private Context context;

        /**
         * The parent
         */
        private NodeList parent;

        /**
         * state
         */
        private State state = NORMAL;

        /**
         * The word
         */
        private ArrayList word;

        /**
         * Create a new object.
         * @param acontext  the context
         */
        public HyphValues(final Context acontext) {

            super();
            context = acontext;
        }

        /**
         * Add a new char for the word
         * @param node  the new char
         */
        public void addWordChar(final CharNode node) {

            word.add(node);
        }

        /**
         * @return Returns the context.
         */
        public Context getContext() {

            return context;
        }

        /**
         * @return Returns the parent.
         */
        public NodeList getParent() {

            return parent;
        }

        /**
         * @return Returns the state.
         */
        public State getState() {

            return state;
        }

        /**
         * Create e new word
         */
        public void newWord() {

            word = new ArrayList();
        }

        /**
         * @param aparent The parent to set.
         */
        public void setParent(final NodeList aparent) {

            parent = aparent;
        }

        /**
         * @param astate The state to set.
         */
        public void setState(final State astate) {

            state = astate;
        }
    }

    /**
     * Class State
     */
    private static class State {

        /**
         * Create a new object
         */
        public State() {

            super();
        }
    }

    /**
     * ignore
     */
    private static final State IGNORE = new State();

    /**
     * normal
     */
    private static final State NORMAL = new State();

    /**
     * word
     */
    private static final State WORD = new State();

    /**
     * Map for hyphenation pattern
     */
    private Map patmap = new HashMap();

    /**
     * Creates a new object.
     */
    public LiangsHyphenationTable() {

        super();
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#addPattern(
     *      java.lang.String, java.lang.String)
     */
    public void addPattern(final String word, final String pattern) {

        patmap.put(word, pattern);
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#hyphenate(
     *      de.dante.extex.interpreter.type.node.HorizontalListNode,
     *      de.dante.extex.interpreter.context.Context)
     */
    public HorizontalListNode hyphenate(final HorizontalListNode nodelist,
            final Context context) throws GeneralException {

        if (!isHyphenActive()) {
            return nodelist;
        }

        HorizontalListNode nodes = super.hyphenate(nodelist, context);
        if (nodes != nodelist) {
            return nodes;
        }

        HyphValues hv = new HyphValues(context);
        HorizontalListNode parent = new HorizontalListNode();
        hv.setParent(parent);
        //   nodelist.visit(this, nodelist, hv);
        // TODO gene: incomplete
        // return parent;
        return nodelist;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
     *      AdjustNode, java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
     *      AfterMathNode, java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode node, final Object value2) {

        HyphValues hv = (HyphValues) value2;
        hv.setState(NORMAL);
        hv.getParent().add(node);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
     *      AlignedLeadersNode, java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode value,
            final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
     *      BeforeMathNode, java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value2) {

        HyphValues hv = (HyphValues) value2;
        hv.setState(IGNORE);
        hv.getParent().add(node);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
     *      CenteredLeadersNode, java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
     *      CharNode, java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value) {

        HyphValues hv = (HyphValues) value;

        if (hv.getState() == NORMAL) {
            hv.setState(WORD);
            hv.newWord();
            hv.addWordChar(node);
        } else if (hv.getState() == WORD) {
            hv.addWordChar(node);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
     *      DiscretionaryNode, java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
     *      ExpandedLeadersNode, java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
     *      GlueNode, java.lang.Object)
     */
    public Object visitGlue(final GlueNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
     *      HorizontalListNode, java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node,
            final Object value) {

        HyphValues hv = (HyphValues) value;

        NodeIterator it = node.iterator();
        while (it.hasNext()) {
            Node anode = it.next();
            try {
                anode.visit(this, hv);
            } catch (GeneralException e) {
                System.err.println("\n\nException: " + e.getMessage());
                // TODO: handle exception
            }
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
     *      InsertionNode, java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
     *      KernNode, java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
     *      LigatureNode, java.lang.Object)
     */
    public Object visitLigature(final LigatureNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
     *      MarkNode, java.lang.Object)
     */
    public Object visitMark(final MarkNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
     *      PenaltyNode, java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
     *      RuleNode, java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode, java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
     *      VerticalListNode, java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
     *      WhatsItNode, java.lang.Object)
     */
    public Object visitWhatsIt(final WhatsItNode nde, final Object value) {

        return null;
    }
}