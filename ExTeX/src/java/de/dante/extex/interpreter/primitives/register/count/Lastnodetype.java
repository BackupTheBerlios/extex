/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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
 * This class provides an implementation for the primitive <code>\lastnodetype</code>.
 *
 * <doc name="lastnodetype">
 * <h3>The Primitive <tt>\lastnodetype</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    Test\the\lastnodetype  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.12 $
 */

public class Lastnodetype extends AbstractReadonlyCount {

    /**
     * The field <tt>serialVersionUID</tt> contains the ...
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class for getting the type of a node.
     *
     */
    private static final NodetypeReader NODETYPE_READER = new NodetypeReader();

    // TODO: type 14 (unset) is missing (TE)

    /**
     * A class for getting the type number of an node.
     *
     */
    static class NodetypeReader implements NodeVisitor {

        /**
         * Type number for an empty list.
         *
         */

        private static final int NODE_TYPE_EMPTY_LIST = -1;

        /**
         * Type number for adjust nodes.
         *
         */
        private static final int NODETYPE_ADJUST = 6;

        /**
         * Type number for aftermath nodes.
         *
         */
        // TODO: is this correct? (TE)
        private static final int NODETYPE_AFTERMATH = 15;

        /**
         * Type number for alignedleaders nodes.
         *
         */
        // TODO
        //private static final int NODETYPE_ALIGNEDLEADERS = 4444;
        /**
         * Type number for beforemath nodes.
         *
         */
        // TODO: is this correct? (TE)
        private static final int NODETYPE_BEFOREMATH = 10;

        /**
         * Type number for centeredleaders nodes.
         *
         */
        // TODO
        //private static final int NODETYPE_CENTEREDLEADERS = 4444;
        /**
         * Type number for char nodes.
         *
         */
        private static final int NODETYPE_CHAR = 0;

        /**
         * Type number for discretionary nodes.
         *
         */
        private static final int NODETYPE_DISCRETIONARY = 8;

        /**
         * Type number for expandedleaders nodes.
         *
         */
        // TODO
        //private static final int NODETYPE_EXPANDEDLEADERS = 4444;
        /**
         * Type number for glue nodes.
         *
         */
        private static final int NODETYPE_GLUE = 11;

        /**
         * Type number for horizontallist nodes.
         *
         */
        private static final int NODETYPE_HORIZONTALLIST = 1;

        /**
         * Type number for insertion nodes.
         *
         */
        private static final int NODETYPE_INSERTION = 4;

        /**
         * Type number for kern nodes.
         *
         */
        private static final int NODETYPE_KERN = 12;

        /**
         * Type number for ligature nodes.
         *
         */
        private static final int NODETYPE_LIGATURE = 7;

        /**
         * Type number for mark nodes.
         *
         */
        private static final int NODETYPE_MARK = 5;

        /**
         * Type number for penalty nodes.
         *
         */
        private static final int NODETYPE_PENALTY = 13;

        /**
         * Type number for rule nodes.
         *
         */
        private static final int NODETYPE_RULE = 3;

        /**
         * Type number for space nodes.
         *
         */
        // TODO
        //private static final int NODETYPE_SPACE = 4444;
        /**
         * Type number for verticallist nodes.
         *
         */
        private static final int NODETYPE_VERTICALLIST = 2;

        /**
         * Type number for whatsit nodes.
         *
         */
        private static final int NODETYPE_WHATSIT = 9;

        /**
         * Return type number for adjust nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
         *     java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_ADJUST);
        }

        /**
         * Return type number for aftermath nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
         *     java.lang.Object)
         */
        public Object visitAfterMath(final AfterMathNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_AFTERMATH);
        }

        /**
         * Return type number for alignedleaders nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
         *     java.lang.Object)
         */
        public Object visitAlignedLeaders(final AlignedLeadersNode node,
                final Object arg) throws GeneralException {

            //TODO unimplemented
            throw new RuntimeException("unimplemented");
            // return new Integer(NODETYPE_ALIGNEDLEADERS);
        }

        /**
         * Return type number for beforemath nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
         *     java.lang.Object)
         */
        public Object visitBeforeMath(final BeforeMathNode node,
                final Object arg) throws GeneralException {

            return new Integer(NODETYPE_BEFOREMATH);
        }

        /**
         * Return type number for centeredleaders nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
         *     java.lang.Object)
         */
        public Object visitCenteredLeaders(final CenteredLeadersNode node,
                final Object arg) throws GeneralException {

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
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode,
         *     java.lang.Object)
         */
        public Object visitChar(final CharNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_CHAR);
        }

        /**
         * Return type number for discretionary nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode,
         *     java.lang.Object)
         */
        public Object visitDiscretionary(final DiscretionaryNode node,
                final Object arg) throws GeneralException {

            return new Integer(NODETYPE_DISCRETIONARY);
        }

        /**
         * Return type number for expandedleaders nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
         *     java.lang.Object)
         */
        public Object visitExpandedLeaders(final ExpandedLeadersNode node,
                final Object arg) throws GeneralException {

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
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode,
         *     java.lang.Object)
         */
        public Object visitGlue(final GlueNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_GLUE);
        }

        /**
         * Return type number for horizontallist nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
         *     java.lang.Object)
         */
        public Object visitHorizontalList(final HorizontalListNode node,
                final Object arg) throws GeneralException {

            return new Integer(NODETYPE_HORIZONTALLIST);
        }

        /**
         * Return type number for insertion nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
         *     java.lang.Object)
         */
        public Object visitInsertion(final InsertionNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_INSERTION);
        }

        /**
         * Return type number for kern nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
         *     java.lang.Object)
         */
        public Object visitKern(final KernNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_KERN);
        }

        /**
         * Return type number for ligature nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode,
         *     java.lang.Object)
         */
        public Object visitLigature(final LigatureNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_LIGATURE);
        }

        /**
         * Return type number for mark nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode,
         *     java.lang.Object)
         */
        public Object visitMark(final MarkNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_MARK);
        }

        /**
         * Return type number for penalty nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
         *     java.lang.Object)
         */
        public Object visitPenalty(final PenaltyNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_PENALTY);
        }

        /**
         * Return type number for rule nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
         *     java.lang.Object)
         */
        public Object visitRule(final RuleNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_RULE);
        }

        /**
         * Return type number for SPACE nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
         *     java.lang.Object)
         */
        public Object visitSpace(final SpaceNode node, final Object arg)
                throws GeneralException {

            //TODO unimplemented
            throw new RuntimeException("unimplemented");
            // return new Integer(NODETYPE_SPACE);
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(de.dante.extex.typesetter.type.node.VirtualCharNode, java.lang.Object)
         */
        public Object visitVirtualChar(final VirtualCharNode node,
                final Object value) throws GeneralException {

            // TODO visitVirtualChar unimplemented
            return null;
        }

        /**
         * Return type number for verticallist nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode,
         *     java.lang.Object)
         */
        public Object visitVerticalList(final VerticalListNode node,
                final Object arg) throws GeneralException {

            return new Integer(NODETYPE_VERTICALLIST);
        }

        /**
         * Return type number for whatsit nodes.  Both arguments are
         * not used.
         *
         * @param node the visited node
         * @param arg null
         * @return typenumber of node as <code>Integer</code>
         * @exception GeneralException if an error occurs
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode,
         *     java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node, final Object arg)
                throws GeneralException {

            return new Integer(NODETYPE_WHATSIT);
        }

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
            } else {
                Integer integer = (Integer) node.visit(this, null);
                return integer.intValue();
            }
        }
    }

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