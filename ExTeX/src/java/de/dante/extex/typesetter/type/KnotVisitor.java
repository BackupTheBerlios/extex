/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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



/**
 * This interface describes a visitor for knot nodes.
 * With the help of this interface the visitor pattern can be implemented.
 * <p>
 * The user of the visitor pattern has to provide an implementation of
 * this interface. Then the <tt>visit</tt> method is invoked and the caller
 * is forwarded to the appropriate <tt>visit</tt> method in the visitor.
 * </p>
 * <p>
 * Consider we have a Node <tt>node</tt> at hand and a method invokes
 * <pre>
 *   node.visit(visitor, a);
 * </pre>
 * then the following method in the object <tt>visitor</tt> is invoked:
 * <pre>
 *   visitNode(visitor, a)
 * </pre>
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface KnotVisitor {

    /**
     * Invoke the method in case of a node type.
     *
     * @param node the visited Node
     * @param arg the argument
     */
    void visitNode(Node node, Object arg);

}
