/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.typesetter;

/**
 * This interface implements part of the visitor pattern for nodes.
 * <p>
 * In object-oriented design a pattern has been established to let one class
 * react on the type of some object. Consider a tree composed of different
 * types of nodes all implementing a same base interface and some of them
 * having extensions of the interface. This is the situation for
 * {@link de.dante.extex.typesetter.Node Node}s for which several types exist.
 * </p>
 * <p>
 * The original idea was to attach the desired functionality to the nodes and
 * let it have one common method to invoke it. This turns out not to be
 * practical for modular components where the algorithm might be exchanged.
 * This is the case for the DocumentWriter which might produce Text, PDF, DVI,
 * or some other output format.
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
 * <h3>Example</h3>
 * <p>
 * Consider you have a class implementing DocumentWriter with a method which
 * needs to react differently on different node types. The first approximation
 * looks as follows:
 * </p>
 *
 * <pre>
 * public class MyDocumentWriter implements DocumentWriter {
 *
 *     public void myMethod(final Node node) {
 *         // Do something with node depending on its type
 *     }
 * }
 * </pre>
 *
 * Now we can add the NodeVisitor interface. Thus we are forced to define a
 * bunch of methods declared in this interface:
 * </p>
 * <pre>
 * public class MyDocumentWriter implements DocumentWriter<b>, NodeVisitor</b> {
 *
 *     public void myMethod(final Node node) {
 *         // Do something with node depending on its type
 *     }
 * <b>
 *     public Object visitAdjustNode(final Object arg1, final Object arg2) {
 *         // do something for adjust nodes
 *     }
 *
 *     public Object visitCharNode(final Object arg1, final Object arg2) {
 *         // do something for char nodes
 *     }
 *
 *     // and many others..
 * </b>
 * }
 * </pre>
 * <p>
 * Now we just have to make sure that those methods are invoked. This is done
 * with the method <tt>visit()</tt> of the Node. The signature allows us to
 * provide two additional arguments and receive a return value. Since we want
 * to do something with the node itself, we use the first free argument for the
 * node and <code>null</codeXS> as the second argument.
 * </p>
 * <p>
 * In the <tt>visit</tt> methods we can now savely assume that the node is of
 * the named type and cast the object to have access to its public methods.
 * </p>
 * <pre>
 * public class MyDocumentWriter implements DocumentWriter, NodeVisitor {
 *
 *     public void myMethod(final Node node) {
 *         <b>node.visit(this,node,null);</b>
 *     }
 *
 *     public Object visitAdjustNode(final Object arg1, final Object arg2) {
 *         <b>AdjustNode node = (AdjustNode) arg1;</b>
 *         // do something for adjust nodes
 *     }
 *
 *     public Object visitCharNode(final Object arg1, final Object arg2) {
 *         <b>CharNode node = (CharNode) arg1;</b>
 *         // do something for char nodes
 *     }
 *
 *     // and many others..
 *
 * }
 * </pre>
 * <p>
 * In the example above we have not used the second additional argument or the
 * return value. In the <tt>visit</tt> methods we are free to use them in all
 * ways we like.
 * </p>
 * <p>
 * The visitor is not necessarily the class MyDocumentWriter. If this class
 * contains several methods which need to distinguish the types of the nodes
 * it is possible to use another class as visitor, e.g. an inner class.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface NodeVisitor {

    /**
     * This method is called when an AdjustNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitAdjust(Object value, Object value2);

    /**
     * This method is called when an AfterMathNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitAfterMath(Object value, Object value2);

    /**
     * This method is called when an AlignedLeadersNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitAlignedLeaders(Object value, Object value2);

    /**
     * This method is called when a BeforeMathNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitBeforeMath(Object value, Object value2);

    /**
     * This method is called when a CenteredLeadersNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitCenteredLeaders(Object value, Object value2);

    /**
     * This method is called when a CharNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitChar(Object value, Object value2);

    /**
     * This method is called when a DiscretionaryNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitDiscretionary(Object value, Object value2);

    /**
     * This method is called when an ExpandedLeadersNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitExpandedLeaders(Object value, Object value2);

    /**
     * This method is called when a GlueNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitGlue(Object value, Object value2);

    /**
     * This method is called when a HoizontalListNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitHorizontalList(Object value, Object value2);

    /**
     * This method is called when an InstertionNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitInsertion(Object value, Object value2);

    /**
     * This method is called when a KernNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitKern(Object value, Object value2);

    /**
     * This method is called when a LigatureNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitLigature(Object value, Object value2);

    /**
     * This method is called when a MarkNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitMark(Object value, Object value2);

    /**
     * This method is called when a PenaltyNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitPenalty(Object value, Object value2);

    /**
     * This method is called when a RuleNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitRule(Object value, Object value2);

    /**
     * This method is called when a SpaceNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitSpace(Object value, Object value2);

    /**
     * This method is called when a VerticalListNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitVerticalList(Object value, Object value2);

    /**
     * This method is called when a WhatsitNode has been encoutered.
     *
     * @param value the first parameter for the visitor
     * @param value2 the second parameter for the visitor
     *
     * @return the visitor specific value
     */
    Object visitWhatsIt(Object value, Object value2);

}
