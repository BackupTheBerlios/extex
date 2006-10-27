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

package de.dante.extex.interpreter.type.math;

/**
 * This interface implements part of the visitor pattern for the math class.
 * <p>
 * In object-oriented design a pattern has been established to let one class
 * react on the type of some object. Consider a tree composed of different
 * types of nodes all implementing a same base interface and some of them
 * having extensions of the interface. This is the situation for
 * {@link de.dante.extex.typesetter.type.math.MathClass Mathclass}es for which
 * several types exist.
 * </p>
 * <p>
 * The original idea was to attach the desired functionality to the sunb-types
 * and let it have one common method to invoke it. This turns out not to be
 * practical for modular components where the algorithm might be exchanged.
 * Any new implementation would have the need to extend the MathClass
 * classes.
 * </p>
 * <p>
 * The other simplistic solution is to use a large switch, or cascaded
 * if-then-else, to differntiate the cases. This kind of code is cumbersome to
 * maintain. Whenever a new type of node is added you have to remember all
 * places which need adaption.
 * </p>
 * <p>
 * This problem is solved in the visitor pattern. Each sub-class of MathClass
 * has to provide one method called <tt>visit()</tt> here. This
 * method has as one argument the visitor which should be called back. The
 * visitor is defined by the current interface. It has to provide a set of
 * methods which allow him to differntiate the types of the math class.
 * </p>
 * <p>
 * Each math class has to implement the method <tt>visit()</tt> in a way that
 * the appropriate method from the visitor interface is invoked. Thus as a
 * result the math class and the algorithm are decoupled.
 * </p>
 *
 * <h3>Example Source Code</h3>
 * <p>
 * Consider you have a class with a method which
 * needs to react differently on different mode classes. The first approximation
 * looks as follows:
 * </p>
 *
 * <pre class="JavaSample">
 *
 * public class MyClass {
 *
 *     public void myMethod(final MathClass mc) {
 *         <i>// Do something with node depending on its type</i>
 *     }
 * }
 * </pre>
 * <p>
 * Now we can add the MathClassVisitor interface. Thus we are forced to define a
 * bunch of methods declared in this interface:
 * </p>
 * <pre class="JavaSample">
 *
 * public class MyClass <b>implements NodeVisitor</b> {
 *
 *     public void myMethod(final MathClass mc) {
 *         <i>// Do something with mc depending on its type</i>
 *     }
 * <b>
 *     public Object visitBinary(final Object arg, Object arg2) {
 *         <i>// do something for binary class</i>
 *     }
 *
 *     public Object visitClosing(final Object arg, Object arg2) {
 *         <i>// do something for closing delimiters</i>
 *     }
 *
 *     <i>// and many others..</i>
 * </b>
 * }
 * </pre>
 * <p>
 * Now we just have to make sure that those methods are invoked. This is done
 * with the method <tt>visit()</tt> of the MathClass. The signature allows us to
 * provide one additional argument and receive a return value. Since we want
 * to do something with a Noad, we use the argument for the noad.
 * </p>
 * <p>
 * The definition of the parameter and the return value are rather general.
 * Thus it is possible to use the visitor pattern in several different
 * situations.
 * </p>
 * <p>
 * The visitor is not necessarily the class Myclass itself. If this class
 * contains several methods which need to distinguish the types of the math
 * class it is possible to use another class as visitor, e.g. an inner class.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface MathClassVisitor {

    /**
     * Invoke the visitor method for a binary operator.
     *
     * @param arg the argument
     * @param arg2 the second argument
     *
     * @return the result
     */
    Object visitBinary(Object arg, Object arg2);

    /**
     * Invoke the visitor method for a closing delimiter.
     *
     * @param arg the argument
     * @param arg2 the second argument
     *
     * @return the result
     */
    Object visitClosing(Object arg, Object arg2);

    /**
     * Invoke the visitor method for a large operator.
     *
     * @param arg the argument
     * @param arg2 the second argument
     *
     * @return the result
     */
    Object visitLarge(Object arg, Object arg2);

    /**
     * Invoke the visitor method for a opening delimiter.
     *
     * @param arg the argument
     * @param arg2 the second argument
     *
     * @return the result
     */
    Object visitOpening(Object arg, Object arg2);

    /**
     * Invoke the visitor method for an ordinary symbol .
     *
     * @param arg the argument
     * @param arg2 the second argument
     *
     * @return the result
     */
    Object visitOrdinary(Object arg, Object arg2);

    /**
     * Invoke the visitor method for a punctation symbol.
     *
     * @param arg the argument
     * @param arg2 the second argument
     *
     * @return the result
     */
    Object visitPunctation(Object arg, Object arg2);

    /**
     * Invoke the visitor method for a relation operator.
     *
     * @param arg the argument
     * @param arg2 the second argument
     *
     * @return the result
     */
    Object visitRelation(Object arg, Object arg2);

    /**
     * Invoke the visitor method for a variable width symbol.
     *
     * @param arg the argument
     * @param arg2 the second argument
     *
     * @return the result
     */
    Object visitVariable(Object arg, Object arg2);

}