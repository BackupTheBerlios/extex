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
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.GeneralException;

/**
 * This class store the values for hyphenations and
 * hypernate words.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
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
     *      Node, java.lang.Object)
     */
    public Object visitAdjust(final Node value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
     *      Node, java.lang.Object)
     */
    public Object visitAfterMath(final Node value, final Object value2) {

        Node node = (Node) value;
        HyphValues hv = (HyphValues) value2;
        hv.setState(NORMAL);
        hv.getParent().add(node);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
     *      Node, java.lang.Object)
     */
    public Object visitAlignedLeaders(final Node value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
     *      Node, java.lang.Object)
     */
    public Object visitBeforeMath(final Node node, final Object value2) {

        HyphValues hv = (HyphValues) value2;
        hv.setState(IGNORE);
        hv.getParent().add(node);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
     *      Node, java.lang.Object)
     */
    public Object visitCenteredLeaders(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
     *      Node, java.lang.Object)
     */
    public Object visitChar(final Node node, final Object value) {

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
     *      Node, java.lang.Object)
     */
    public Object visitDiscretionary(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
     *      Node, java.lang.Object)
     */
    public Object visitExpandedLeaders(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
     *      Node, java.lang.Object)
     */
    public Object visitGlue(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
     *      Node, java.lang.Object)
     */
    public Object visitHorizontalList(final Node node, final Object value) {

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
     *      Node, java.lang.Object)
     */
    public Object visitInsertion(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
     *      Node, java.lang.Object)
     */
    public Object visitKern(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
     *      Node, java.lang.Object)
     */
    public Object visitLigature(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
     *      Node, java.lang.Object)
     */
    public Object visitMark(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
     *      Node, java.lang.Object)
     */
    public Object visitPenalty(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
     *      Node, java.lang.Object)
     */
    public Object visitRule(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(Node, java.lang.Object)
     */
    public Object visitSpace(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
     *      Node, java.lang.Object)
     */
    public Object visitVerticalList(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
     *      Node, java.lang.Object)
     */
    public Object visitWhatsIt(final Node nde, final Object value) {

        return null;
    }
}
