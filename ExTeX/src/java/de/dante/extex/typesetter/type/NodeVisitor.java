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

package de.dante.extex.typesetter.type;

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
import de.dante.util.GeneralException;

/**
 * This interface implements part of the visitor pattern for nodes.
 * <p>
 * In object-oriented design a pattern has been established to let one class
 * react on the type of some object. Consider a tree composed of different
 * types of nodes all implementing a same base interface and some of them
 * having extensions of the interface. This is the situation for
 * {@link de.dante.extex.typesetter.type.Node Node}s for which several types exist.
 * </p>
 * <p>
 * The original idea was to attach the desired functionality to the nodes and
 * let it have one common method to invoke it. This turns out not to be
 * practical for modular components where the algorithm might be exchanged.
 * This is the case for the
 * {@link de.dante.extex.documentWriter.DocumentWriter DocumentWriter}
 * which might produce Text, PDF, DVI, or some other output format. Any new
 * implementation of this interface would have the need to extend the Node
 * classes.
 * </p>
 * <p>
 * The other simplistic solution is to use a large switch, or cascaded
 * if-then-else, to differntiate the cases. This kind of code is cumbersome to
 * maintain. Whenever a new type of node is added you have to remember all
 * places which need adaption.
 * </p>
 * <p>
 * This problem is solved in the visitor pattern. Each class or interface of
 * the nodes has to provide one method called <tt>visit()</tt> here. This
 * method has as one argument the visitor which should be called back. The
 * visitor is defined by this interface. He has to provide a set of methods
 * which allow him to differntiate the types of the nodes.
 * </p>
 * <p>
 * Each node has to implement the method <tt>visit()</tt> in a way that the
 * appropriate method from the visitor interface is invoked. Thus as a result
 * the nodes and the algorithm are decoupled.
 * </p>
 *
 * <h3>The Mechanics of the NodeVisitor</h3>
 *
 * <p>
 * The actions during the use of a NodeVisitor is illustrated in the following
 * sequence diagram.
 * </p>
 * <div class="Figure">
 *  <img src="doc-files/nodeVisitor.png" />
 *  <br />
 *  <caption>A sequence diagram for the NodeVisitor</caption>
 * </div>
 * <p>
 * In this diagram a NodeVisitor is assumed to process two nodes. The first one
 * is a {@link de.dante.extex.typesetter.type.node.CharNode CharNode} and the
 * second one is a {@link de.dante.extex.typesetter.type.node.GlueNode GlueNode}.
 * We assume that the concrete sub-type of
 * {@link de.dante.extex.typesetter.type.Node Node} to be processed is not known.
 * For instance it can be passed to the initial method in a
 * {@link de.dante.extex.typesetter.type.NodeList NodeList}.
 * </p>
 * <p>
 * The first Node is processed by invoking the method <tt>visit()</tt>. The
 * first argument is the reference to the current instanceof the NodeVisitor.
 * Since the method is defined in CharNode to delegate it to the NodeVisitor
 * by invoking the method <tt>visitChar()</tt>. Now the real work can be
 * performed in the calling instance. Here the sub-type is known and can be
 * taken into account.
 * </p>
 * <p>
 * After the return to the caller the second node can be taken into account.
 * The procedure is the same: <tt>visit()</tt> is invoked. But now the
 * delegation used the method <tt>visitGlue()</tt>. Thus in the calling
 * instance the GlueNode can be processed specially.
 * </p>
 *
 * <h3>Example Source Code</h3>
 * <p>
 * Consider you have a class implementing DocumentWriter with a method which
 * needs to react differently on different node types. The first approximation
 * looks as follows:
 * </p>
 *
 * <pre class="JavaSample">
 *
 * public class MyDocumentWriter implements DocumentWriter {
 *
 *     public void myMethod(final Node node) {
 *         <i>// Do something with node depending on its type</i>
 *     }
 * }
 * </pre>
 * <p>
 * Now we can add the NodeVisitor interface. Thus we are forced to define a
 * bunch of methods declared in this interface:
 * </p>
 * <pre class="JavaSample">
 *
 * public class MyDocumentWriter implements DocumentWriter<b>, NodeVisitor</b> {
 *
 *     public void myMethod(final Node node) {
 *         <i>// Do something with node depending on its type</i>
 *     }
 * <b>
 *     public Object visitAdjust(final AdjustNode node, final Object arg) {
 *         <i>// do something for adjust nodes</i>
 *     }
 *
 *     public Object visitChar(final CharNode node, final Object arg) {
 *         <i>// do something for char nodes</i>
 *     }
 *
 *     <i>// and many others..</i>
 * </b>
 * }
 * </pre>
 * <p>
 * Now we just have to make sure that those methods are invoked. This is done
 * with the method <tt>visit()</tt> of the Node. The signature allows us to
 * provide two additional arguments and receive a return value. Since we want
 * to do something with the node itself, this node is provided with the
 * correct type to the node visitor. The second argument can be casted to the
 * appropriate type.
 * </p>
 * <p>
 * In the <tt>visit</tt> methods we can now savely assume that the node is of
 * the named type and cast the object to have access to its public methods.
 * </p>
 * <pre class="JavaSample">
 *
 * public class MyDocumentWriter implements DocumentWriter, NodeVisitor {
 *
 *     public void myMethod(final Node node) {
 *         <b>node.visit(this, "some value");</b>
 *     }
 *
 *     public Object visitAdjust(final AdjustNode node, final Object arg) {
 *         <b>String s = (String) arg;</b>
 *         <i>// do something for adjust nodes</i>
 *     }
 *
 *     public Object visitChar(final CharNode node, final Object arg) {
 *         <b>String s = (String) arg;</b>
 *         <i>// do something for char nodes</i>
 *     }
 *
 *     <i>// and many others..</i>
 *
 * }
 * </pre>
 * <p>
 * In the example above we have not used the additional argument or the
 * return value. In the <tt>visit</tt> methods we are free to use them in all
 * ways we like.
 * </p>
 * <p>
 * The definition of the parameters and the return value are rather general.
 * Thus it is possible to use the visitor pattern in several different
 * situations.
 * </p>
 * <p>
 * The visitor is not necessarily the class <tt>MyDocumentWriter</tt>. If this
 * class contains several methods which need to distinguish the types of the
 * nodes it is possible to use another class as visitor, e.g. an inner class.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public interface NodeVisitor {

    /**
     * This method is called when an
     * {@link de.dante.extex.typesetter.type.node.AdjustNode AdjustNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitAdjust(AdjustNode node, Object value) throws GeneralException;

    /**
     * This method is called when an
     * {@link de.dante.extex.typesetter.type.node.AfterMathNode AfterMathNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitAfterMath(AfterMathNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when an
     * {@link de.dante.extex.typesetter.type.node.AlignedLeadersNode AlignedLeadersNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitAlignedLeaders(AlignedLeadersNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.BeforeMathNode BeforeMathNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitBeforeMath(BeforeMathNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.CenteredLeadersNode CenteredLeadersNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitCenteredLeaders(CenteredLeadersNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.CharNode CharNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitChar(CharNode node, Object value) throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.DiscretionaryNode DiscretionaryNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitDiscretionary(DiscretionaryNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when an
     * {@link de.dante.extex.typesetter.type.node.ExpandedLeadersNode ExpandedLeadersNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitExpandedLeaders(ExpandedLeadersNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.GlueNode GlueNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitGlue(GlueNode node, Object value) throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.HorizontalListNode HorizontalListNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitHorizontalList(HorizontalListNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when an
     * {@link de.dante.extex.typesetter.type.node.InsertionNode InsertionNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitInsertion(InsertionNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.KernNode KernNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitKern(KernNode node, Object value) throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.LigatureNode LigatureNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitLigature(LigatureNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.MarkNode MarkNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitMark(MarkNode node, Object value) throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.PenaltyNode PenaltyNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitPenalty(PenaltyNode node, Object value) throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.RuleNode RuleNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitRule(RuleNode node, Object value) throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.SpaceNode SpaceNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitSpace(SpaceNode node, Object value) throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.VerticalListNode VerticalListNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitVerticalList(VerticalListNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.VirtualCharNode VirtualCharNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitVirtualChar(VirtualCharNode node, Object value)
            throws GeneralException;

    /**
     * This method is called when a
     * {@link de.dante.extex.typesetter.type.node.WhatsItNode WhatsItNode}
     * has been encoutered.
     *
     * @param node the first parameter for the visitor is the node visited
     * @param value the second parameter for the visitor
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitWhatsIt(WhatsItNode node, Object value) throws GeneralException;

}