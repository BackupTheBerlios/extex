/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.hyphenation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.hyphenation.HyphenationTable;
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
 * This class store the values for hyphenations and
 * hypernate words.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class HyphenationTableImpl implements HyphenationTable, NodeVisitor {

    /**
     * Creates a new object.
     */
    public HyphenationTableImpl() {

        super();
    }

    /**
     * Map for hyphenation
     */
    private Map hypmap = new HashMap();

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#addHyphenation(
     *      java.lang.String, java.lang.String)
     */
    public void addHyphenation(final String word, final String hyphword) {

        hypmap.put(word, hyphword);
    }

    /**
     * Map for hyphenation-pattern
     */
    private Map patmap = new HashMap();

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#addPattern(
     *      java.lang.String, java.lang.String)
     */
    public void addPattern(final String word, final String pattern) {

        patmap.put(word, pattern);
    }

    /**
     * lefthyphenmin
     */
    private int lefthyphnemin;

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#getLeftHyphenmin()
     */
    public int getLeftHyphenmin() {

        return lefthyphnemin;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setLeftHyphenmin(int)
     */
    public void setLeftHyphenmin(final int left) {

        lefthyphnemin = left;
    }

    /**
     * righthyphenmin
     */
    private int righthyphenmin;

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#getRightHyphenmin()
     */
    public int getRightHyphenmin() {

        return righthyphenmin;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setRightHyphenmin(int)
     */
    public void setRightHyphenmin(final int right) {

        righthyphenmin = right;
    }

    /**
     * hyphenactive
     */
    private boolean hyphenactive = true;

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#isHyphenActive()
     */
    public boolean isHyphenActive() {

        return hyphenactive;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setHyphenActive(boolean)
     */
    public void setHyphenActive(final boolean active) {

        hyphenactive = active;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#hyphenate(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.type.node.HorizontalListNode)
     */
    public HorizontalListNode hyphenate(final Context context,
            final HorizontalListNode nodelist) throws GeneralException {

        if (hyphenactive) {
            HyphValues hv = new HyphValues(context);
            HorizontalListNode parent = new HorizontalListNode();
            hv.setParent(parent);
         //   nodelist.visit(this, nodelist, hv);
            // TODO incomplete
            // MGN hier gehts weiter!!!
           // return parent;
        }
        return nodelist;
    }

    /**
     * Container for hyphnevalues
     */
    private static class HyphValues {

        /**
         * state
         */
        private State state = NORMAL;

        /**
         * The context
         */
        private Context context;

        /**
         * The parent
         */
        private NodeList parent;

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
         * Create e new word
         */
        public void newWord() {

            word = new ArrayList();
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
         * @return Returns the state.
         */
        public State getState() {

            return state;
        }

        /**
         * @param astate The state to set.
         */
        public void setState(final State astate) {

            state = astate;
        }

        /**
         * @return Returns the parent.
         */
        public NodeList getParent() {

            return parent;
        }

        /**
         * @param aparent The parent to set.
         */
        public void setParent(final NodeList aparent) {

            parent = aparent;
        }
    }

    /**
     * word
     */
    private static final State WORD = new State();

    /**
     * ignore
     */
    private static final State IGNORE = new State();

    /**
     * normal
     */
    private static final State NORMAL = new State();

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

    // ---------------------------------------------------------
    // ---------------------------------------------------------
    // ---------------------------------------------------------
    // ---------------------------------------------------------

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
    public Object visitAfterMath(final AfterMathNode value, final Object value2) {

        Node node = (Node) value;
        HyphValues hv = (HyphValues) value2;
        hv.setState(NORMAL);
        hv.getParent().add(node);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
     *      AlignedLeadersNode, java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode value, final Object value2) {

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
    public Object visitCenteredLeaders(final CenteredLeadersNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
     *      CharNode, java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value) {

        CharNode cnode = (CharNode) node;
        HyphValues hv = (HyphValues) value;

        if (hv.getState() == NORMAL) {
            hv.setState(WORD);
            hv.newWord();
            hv.addWordChar(cnode);
        } else if (hv.getState() == WORD) {
            hv.addWordChar(cnode);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
     *      DiscretionaryNode, java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
     *      ExpandedLeadersNode, java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node, final Object value) {

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
    public Object visitHorizontalList(final HorizontalListNode node, final Object value) {

        HorizontalListNode nodeList = (HorizontalListNode) node;
        HyphValues hv = (HyphValues) value;

        NodeIterator it = nodeList.iterator();
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
    public Object visitVerticalList(final VerticalListNode node, final Object value) {

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
