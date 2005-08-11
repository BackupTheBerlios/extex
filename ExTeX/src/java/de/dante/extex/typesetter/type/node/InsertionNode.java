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

import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * This node is meant to record an insertion.
 *
 * @see "<logo>TeX</logo> &ndash; The Program [140]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class InsertionNode extends AbstractNode implements Node {

    /**
     * The field <tt>nodes</tt> contains the vertical list to be inserted.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [140]"
     */
    private NodeList nodes;

    /**
     * The field <tt>floatCost</tt> contains the penalty which is used when the
     * insertion floats to the following page.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [140]"
     */
    private long floatCost = 0;

    /**
     * The field <tt>subtype</tt> contains the register number for the
     * associated registers.
     */
    private long subtype = 0;

    /**
     * Creates a new object.
     *
     * @param subtype the register number for the associated registers
     * @param nodes ...
     *
     * @see "<logo>TeX</logo> &ndash; The Program [140]"
     */
    public InsertionNode(final long subtype, final NodeList nodes) {

        super();
        this.subtype = subtype;
        this.nodes = nodes;
        if (nodes != null) {
            setWidth(nodes.getWidth());
            setHeight(nodes.getHeight());
            setDepth(nodes.getDepth());
        }
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     *
     * @see "<logo>TeX</logo> &ndash; The Program [188]"
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append(getLocalizer().format("String.Format",
                Long.toString(subtype), //
                getHeight().toString(), //
                "???", //
                getDepth().toString(), //
                Long.toString(floatCost)));
        nodes.toString(sb, prefix);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitInsertion(this, value);
    }

}