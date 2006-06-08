/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.count;

import java.lang.Integer;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.typesetter.Typesetter;
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
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\lastnodetype</code>.
 *
 * <doc name="lastnodetype">
 * <h3>The Primitive <tt>\lastnodetype</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;lastnodetype&rang;
 *       &rarr; <tt>\lastnodetype</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \count42=\lastnodetype  </pre>
 *  <pre class="TeXSample">
 *    Test\the\lastnodetype  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.15 $
 */

public class Lastnodetype extends AbstractReadonlyCount {

    /**
     * A class for getting the type number of an node.
     */
    static class NodetypeReader implements NodeVisitor {

        /**
         * Type number for an empty list.
         */
        private static final int NODE_TYPE_EMPTY_LIST = -1;

        /**
         * Type number for adjust nodes.
         */
        private static final Integer NODETYPE_ADJUST = new Integer(6);

        /**
         * Type number for aftermath nodes.
         */
        // TODO: is this correct? (TE)
        private static final Integer NODETYPE_AFTERMATH = new Integer(15);

        /**
         * Type number for aligned leaders nodes.
         */
        // TODO
        //private static final int NODETYPE_ALIGNEDLEADERS = 4444;
        /**
         * Type number for before math nodes.
         */
        // TODO: is this correct? (TE)
        private static final Integer NODETYPE_BEFOREMATH = new Integer(10);

        /**
         * Type number for centered leaders nodes.
         */
        // TODO
        //private static final int NODETYPE_CENTEREDLEADERS = 4444;
        /**
         * Type number for char nodes.
         */
        private static final Integer NODETYPE_CHAR = new Integer(0);

        /**
         * Type number for discretionary nodes.
         */
        private static final Integer NODETYPE_DISCRETIONARY = new Integer(8);

        /**
         * Type number for expanded leaders nodes.
         */
        // TODO
        //private static final int NODETYPE_EXPANDEDLEADERS = 4444;
        /**
         * Type number for glue nodes.
         */
        private static final Integer NODETYPE_GLUE = new Integer(11);

        /**
         * Type number for horizontallist nodes.
         */
        private static final Integer NODETYPE_HORIZONTALLIST = new Integer(1);

        /**
         * Type number for insertion nodes.
         */
        private static final Integer NODETYPE_INSERTION = new Integer(4);

        /**
         * Type number for kern nodes.
         */
        private static final Integer NODETYPE_KERN = new Integer(12);

        /**
         * Type number for ligature nodes.
         */
        private static final Integer NODETYPE_LIGATURE = new Integer(7);

        /**
         * Type number for mark nodes.
         */
        private static final Integer NODETYPE_MARK = new Integer(5);

        /**
         * Type number for penalty nodes.
         */
        private static final Integer NODETYPE_PENALTY = new Integer(13);

        /**
         * Type number for rule nodes.
         */
        private static final Integer NODETYPE_RULE = new Integer(3);

        /**
         * Type number for space nodes.
         */
        // TODO
        //private static final int NODETYPE_SPACE = 4444;
        /**
         * Type number for vertical list nodes.
         */
        private static final Integer NODETYPE_VERTICALLIST = new Integer(2);

        /**
         * Type number for whatsit nodes.
         */
        private static final Integer NODETYPE_WHATSIT = new Integer(9);

        /**
         * Returns the Node for an specified node.
         *
         * @param node a <code>Node</code> value
         * @return the type of the node
         * @exception GeneralException if an error occurs
         */
        public int getNodetype(final Node node) throws GeneralException {

            if (node == null) {
                return NODE_TYPE_EMPTY_LIST;
            }

            return ((Integer) node.visit(this, null)).intValue();
        }

        /**
         * Return type number for adjust nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
         *     java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node, final Object arg) {

            return NODETYPE_ADJUST;
        }

        /**
         * Return type number for aftermath nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
         *     java.lang.Object)
         */
        public Object visitAfterMath(final AfterMathNode node, final Object arg) {

            return NODETYPE_AFTERMATH;
        }

        /**
         * Return type number for aligned leaders nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
         *     java.lang.Object)
         */
        public Object visitAlignedLeaders(final AlignedLeadersNode node,
                final Object arg) {

            //TODO unimplemented
            throw new RuntimeException("unimplemented");
            // return new Integer(NODETYPE_ALIGNEDLEADERS);
        }

        /**
         * Return type number for before math nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
         *     java.lang.Object)
         */
        public Object visitBeforeMath(final BeforeMathNode node,
                final Object arg) {

            return NODETYPE_BEFOREMATH;
        }

        /**
         * Return type number for centered leaders nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
         *     java.lang.Object)
         */
        public Object visitCenteredLeaders(final CenteredLeadersNode node,
                final Object arg) {

            //TODO unimplemented
            throw new RuntimeException("unimplemented");
            // return new Integer(NODETYPE_CENTEREDLEADERS);
        }

        /**
         * Return type number for char nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return type number of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode,
         *     java.lang.Object)
         */
        public Object visitChar(final CharNode node, final Object arg) {

            return NODETYPE_CHAR;
        }

        /**
         * Return type number for discretionary nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode,
         *     java.lang.Object)
         */
        public Object visitDiscretionary(final DiscretionaryNode node,
                final Object arg) {

            return NODETYPE_DISCRETIONARY;
        }

        /**
         * Return type number for expanded leaders nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return type number of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
         *     java.lang.Object)
         */
        public Object visitExpandedLeaders(final ExpandedLeadersNode node,
                final Object arg) {

            //TODO unimplemented
            throw new RuntimeException("unimplemented");
            //return new Integer(NODETYPE_EXPANDEDLEADERS);
        }

        /**
         * Return type number for glue nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode,
         *     java.lang.Object)
         */
        public Object visitGlue(final GlueNode node, final Object arg) {

            return NODETYPE_GLUE;
        }

        /**
         * Return type number for horizontallist nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
         *     java.lang.Object)
         */
        public Object visitHorizontalList(final HorizontalListNode node,
                final Object arg) {

            return NODETYPE_HORIZONTALLIST;
        }

        /**
         * Return type number for insertion nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
         *     java.lang.Object)
         */
        public Object visitInsertion(final InsertionNode node, final Object arg) {

            return NODETYPE_INSERTION;
        }

        /**
         * Return type number for kern nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
         *     java.lang.Object)
         */
        public Object visitKern(final KernNode node, final Object arg) {

            return NODETYPE_KERN;
        }

        /**
         * Return type number for ligature nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode,
         *     java.lang.Object)
         */
        public Object visitLigature(final LigatureNode node, final Object arg) {

            return NODETYPE_LIGATURE;
        }

        /**
         * Return type number for mark nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode,
         *     java.lang.Object)
         */
        public Object visitMark(final MarkNode node, final Object arg) {

            return NODETYPE_MARK;
        }

        /**
         * Return type number for penalty nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
         *     java.lang.Object)
         */
        public Object visitPenalty(final PenaltyNode node, final Object arg) {

            return NODETYPE_PENALTY;
        }

        /**
         * Return type number for rule nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
         *     java.lang.Object)
         */
        public Object visitRule(final RuleNode node, final Object arg) {

            return NODETYPE_RULE;
        }

        /**
         * Return type number for SPACE nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
         *     java.lang.Object)
         */
        public Object visitSpace(final SpaceNode node, final Object arg) {

            //TODO unimplemented
            throw new RuntimeException("unimplemented");
            // return new Integer(NODETYPE_SPACE);
        }

        /**
         * Return type number for vertical list nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
         *     VerticalListNode,
         *     java.lang.Object)
         */
        public Object visitVerticalList(final VerticalListNode node,
                final Object arg) {

            return NODETYPE_VERTICALLIST;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(de.dante.extex.typesetter.type.node.VirtualCharNode, java.lang.Object)
         */
        public Object visitVirtualChar(final VirtualCharNode node,
                final Object value) {

            return NODETYPE_CHAR;
        }

        /**
         * Return type number for whatsit nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         *
         * @return type number of node as <code>Integer</code>
         *
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode,
         *     java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node, final Object arg) {

            return NODETYPE_WHATSIT;
        }
    }

    /**
     * Class for getting the type of a node.
     */
    private static final NodetypeReader NODETYPE_READER = new NodetypeReader();

    // TODO: type 14 (unset) is missing (TE)

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Lastnodetype(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Node lastNode = typesetter.getLastNode();

        try {
            return NODETYPE_READER.getNodetype(lastNode);
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

}
