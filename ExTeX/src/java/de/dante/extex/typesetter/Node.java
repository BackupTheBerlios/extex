/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.type.Knot;
import de.dante.util.GeneralException;

// TODO:This interface should be moved to the package type
/**
 * A node is the basic data structure for the typesetter. It has a reference
 * point and three dimensions, namely width, height, and depth (see figure).
 * <img src="doc-files/Node.png" alt="" align="right"/>
 * <p>
 * Note that those dimensions are sometimes used for different purposes.
 * For instance a KernNode does use the width to denote the actual size which
 * can be applied in horizontal or in vertical direction, depending on the
 * context.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.18 $
 */
public interface Node extends Knot {

    /**
     * Add the flexible width of the current node to the given glue.
     *
     * @param glue the glue to add to.
     */
    void addWidthTo(Glue glue);

    /**
     * This method performs any action which are required to executed at the
     * time of shipping the node to the DocumentWriter.
     *
     * @param context the interpreter context
     *
     * @throws GeneralException in case of an error
     */
    void atShipping(Context context) throws GeneralException;

    /**
     * Getter for the depth of the node.
     *
     * @return the depth
     */
    Dimen getDepth();

    /**
     * Getter for the height of the node.
     *
     * @return the height
     */
    Dimen getHeight();

    /**
     * Getter for the width of the node.
     *
     * @return the width
     */
    Dimen getWidth();

    /**
     * Setter for the depth of the node.
     *
     * @param depth the nde depth
     */
    void setDepth(Dimen depth);

    /**
     * Setter for the heigth of the node.
     *
     * @param height the new height
     */
    void setHeight(Dimen height);

    /**
     * Setter for the width of the node.
     *
     * @param width the new width
     */
    void setWidth(FixedDimen width);

    /**
     * Adjust the width of a flexible node. This method is a noop for any but
     * the flexible nodes.
     *
     * @param width the desired with
     * @param sum the total sum of the glues
     */
    void spread(FixedDimen width, FixedGlueComponent sum);

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     */
    void toString(StringBuffer sb, String prefix);

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a short form only as it is used in error
     * messages to the user.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     */
    void toText(StringBuffer sb, String prefix);

    /**
     * This method provides an entry point for the visitor pattern.
     *
     * @param visitor the visitor to apply
     * @param value the argument for the visitor
     *
     * @return the result of the method invocation of the visitor
     *
     * @throws GeneralException in case of an error
     */
    Object visit(NodeVisitor visitor, Object value) throws GeneralException;

    /**
     * This method provides an entry point for the visitor pattern.
     *
     * @param visitor the visitor to apply
     * @param value  the first argument for the visitor
     * @param value2 the second argument for the visitor
     *
     * @return the result of the method invocation of the visitor
     *
     * @throws GeneralException in case of an error
     * @deprecated use the other visit method instead
     */
    Object visit(NodeVisitor visitor, Object value, Object value2)
            throws GeneralException;

}