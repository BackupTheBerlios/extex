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
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * This class store the values for hyphenations and
 * hypernate words.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
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
            return parent;
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
     * @see de.dante.extex.typesetter.NodeVisitor#visitAdjust(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitAdjust(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitAfterMath(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitAfterMath(final Object value, final Object value2) {

        Node node = (Node) value;
        HyphValues hv = (HyphValues) value2;
        hv.setState(NORMAL);
        hv.getParent().add(node);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitAlignedLeaders(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitAlignedLeaders(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitBeforeMath(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitBeforeMath(final Object value, final Object value2) {

        Node node = (Node) value;
        HyphValues hv = (HyphValues) value2;
        hv.setState(IGNORE);
        hv.getParent().add(node);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitCenteredLeaders(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitCenteredLeaders(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitChar(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitChar(final Object value, final Object value2) {

        CharNode node = (CharNode) value;
        HyphValues hv = (HyphValues) value2;

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
     * @see de.dante.extex.typesetter.NodeVisitor#visitDiscretionary(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitDiscretionary(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitExpandedLeaders(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitExpandedLeaders(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitGlue(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitGlue(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitHorizontalList(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitHorizontalList(final Object value, final Object value2) {

        HorizontalListNode nodelist = (HorizontalListNode) value;
        HyphValues hv = (HyphValues) value2;

        NodeIterator it = nodelist.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            try {
                node.visit(this, node, hv);
            } catch (GeneralException e) {
                System.err.println("\n\nException: " + e.getMessage());
                // TODO: handle exception
            }
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitInsertion(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitInsertion(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitKern(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitKern(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitLigature(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitLigature(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitMark(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitMark(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitPenalty(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitPenalty(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitRule(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitRule(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitSpace(java.lang.Object, java.lang.Object)
     */
    public Object visitSpace(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitVerticalList(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitVerticalList(final Object value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitWhatsIt(
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitWhatsIt(final Object value, final Object value2) {

        return null;
    }
}
