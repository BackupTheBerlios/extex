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

package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Discartable;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * From The TeXbook
 * <p>
 *  A kern_node has a width field to specify a (normally negative) amount of
 *  spacing. This spacing correction appears in horizontal lists between
 *  letters like A and V when the font designer said that it looks better to
 *  move them closer together or further apart. A kern node can also appear in
 *  a vertical list, when its `width' denotes additional spacing in the
 *  vertical direction.
 * </p>
 *
 * @see "TeX -- The Program [155]"
 * @see de.dante.extex.interpreter.type.node.ImplicitKernNode
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.13 $
 */
public class KernNode extends AbstractNode implements Node, Discartable {

    /**
     * Creates a new object.
     *
     * @see "TeX -- The Program [156]"
     */
    public KernNode(final Dimen kern) {

        super(kern);
    }

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a short form only as it is used in error
     * messages to the user.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     *
     * @see de.dante.extex.typesetter.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     * @see "TeX -- The Program [191]"
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append("kern");
        getWidth().toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("\\kern ");
        getWidth().toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitKern(value, value2);
    }

}