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

package de.dante.extex.typesetter.type.node;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Discardable;

/**
 * This class reresents an explicit kern node for the typesetter.
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
 * @see "<logo>TeX</logo> &ndash; The Program [155]"
 * @see de.dante.extex.typesetter.type.node.ImplicitKernNode
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class ExplicitKernNode extends AbstractKernNode implements Discardable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [156]"
     */
    public ExplicitKernNode(final Dimen kern) {

        super(kern);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append(getLocalizer().format("ExplicitKernNode.String",
                getWidth().toString()));
    }

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a short form only as it is used in error
     * messages to the user.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     *
     * @see "<logo>TeX</logo> &ndash; The Program [191]"
     * @see de.dante.extex.typesetter.type.Node#toText(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append(getLocalizer().format("ExplicitKernNode.Text",
                getWidth().toString()));
    }

}