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

package de.dante.extex.color;

import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.HsvColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.util.exception.GeneralException;

/**
 * This interface implements part of the visitor pattern for colors.
 * <p>
 * In object-oriented design a pattern has been established to let one class
 * react on the type of some object. Consider different color models represented
 * in color objects of different types. All share a common base interface. This
 * is the situation for
 * {@link de.dante.extex.interpreter.context.Color Color}s for which several
 * types exist.
 * </p>
 * <p>
 * The original idea was to attach the desired functionality to the nodes and
 * let it have one common method to invoke it. This turns out not to be
 * practical for modular components where the algorithm might be exchanged.
 * This is the case for the
 * {@link de.dante.extex.color.ColorConverter ColorConverter}
 * which might produce any converted Color in any other model. Any new
 * implementation of the color interface would have the need to extend all Color
 * classes.
 * </p>
 * <p>
 * The other simplistic solution is to use a large switch, or cascaded
 * if-then-else, to differentiate the cases. This kind of code is cumbersome to
 * maintain. Whenever a new type of node is added you have to remember all
 * places which need adaption.
 * </p>
 * <p>
 * This problem is solved in the visitor pattern. Each class or interface of
 * the colors has to provide one method called <tt>visit()</tt> here. This
 * method has as one argument the visitor which should be called back. The
 * visitor is defined by this interface. He has to provide a set of methods
 * which allow him to differentiate the types of the colors.
 * </p>
 * <p>
 * Each color has to implement the method <tt>visit()</tt> in a way that the
 * appropriate method from the visitor interface is invoked. Thus as a result
 * the colors and the algorithm are decoupled.
 * </p>
 *
 * <h3>The Mechanics of the ColorVisitor</h3>
 *
 * <p>
 * A detailed description of the mechanics of a visitor can be found in the
 * documentation for the {@link de.dante.extex.typesetter.type.NodeVisitor NodeVisitor}.
 * </p>
 *
 * <h3>Example Source Code</h3>
 * <p>
 * Consider you have a class implementing DocumentWriter with a method which
 * needs to react differently on different color types. The first approximation
 * looks as follows:
 * </p>
 *
 * <pre class="JavaSample">
 *
 * public class MyDocumentWriter implements DocumentWriter {
 *
 *     public void myMethod(final Color color) {
 *         <i>// Do something with color depending on its type</i>
 *     }
 * }
 * </pre>
 * <p>
 * Now we can add the ColorVisitor interface. Thus we are forced to define a
 * bunch of methods declared in this interface:
 * </p>
 * <pre class="JavaSample">
 *
 * public class MyDocumentWriter implements DocumentWriter<b>, ColorVisitor</b> {
 *
 *     public void myMethod(final Color color) {
 *         <i>// Do something with color depending on its type</i>
 *     }
 * <b>
 *     public Object visitRgb(final RgbColor color, final Object arg) {
 *         <i>// do something for RGB colors</i>
 *     }
 *
 *     public Object visitGray(final GrayscaleColor color, final Object arg) {
 *         <i>// do something for grayscale colors</i>
 *     }
 *
 *     <i>// and many others..</i>
 * </b>
 * }
 * </pre>
 * <p>
 * Now we just have to make sure that those methods are invoked. This is done
 * with the method <tt>visit()</tt> of the Color. The signature allows us to
 * provide one additional argument and receive a return value. Since we want
 * to do something with the color itself, this color is provided with the
 * correct type to the color visitor. The second argument can be casted to the
 * appropriate type.
 * </p>
 * <pre class="JavaSample">
 *
 * public class MyDocumentWriter implements DocumentWriter, ColorVisitor {
 *
 *     public void myMethod(final Color color) {
 *         <b>color.visit(this, "some value");</b>
 *     }
 *
 *     public Object visitRgb(final RgbColor color, final Object arg) {
 *         <b>String s = (String) arg;</b>
 *         <i>// do something for RGB colors</i>
 *     }
 *
 *     public Object visitGray(final GrayscaleColor color, final Object arg) {
 *         <b>String s = (String) arg;</b>
 *         <i>// do something for grayscale colors</i>
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
 * colors it is possible to use another class as visitor, e.g. an inner class.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface ColorVisitor {

    /**
     * This method is called when an
     * {@link de.dante.extex.color.model.CmykColor CmykColor}
     * has been encountered.
     *
     * @param color the first parameter for the visitor is the color visited
     * @param value the second parameter is a  visitor specific object
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitCmyk(CmykColor color, Object value) throws GeneralException;

    /**
     * This method is called when an
     * {@link de.dante.extex.color.model.GrayscaleColor GrayscaleColor}
     * has been encountered.
     *
     * @param color the first parameter for the visitor is the color visited
     * @param value the second parameter is a  visitor specific object
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitGray(GrayscaleColor color, Object value)
            throws GeneralException;

    /**
     * This method is called when an
     * {@link de.dante.extex.color.model.HsvColor HsvColor}
     * has been encountered.
     *
     * @param color the first parameter for the visitor is the color visited
     * @param value the second parameter is a  visitor specific object
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitHsv(HsvColor color, Object value) throws GeneralException;

    /**
     * This method is called when an
     * {@link de.dante.extex.color.model.RgbColor RgbColor}
     * has been encountered.
     *
     * @param color the first parameter for the visitor is the color visited
     * @param value the second parameter is a  visitor specific object
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitRgb(RgbColor color, Object value) throws GeneralException;

}
