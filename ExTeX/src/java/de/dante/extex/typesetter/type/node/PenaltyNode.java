/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.typesetter.Discardable;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * This class represents a Node which holds a penalty value. It is used during
 * the paragraph breaking or page breaking to control the algorithm. This node
 * should be ignored by the DocumentWriter.
 *
 * @see "<logo>TeX</logo> &ndash; The Program [157]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.13 $
 */
public class PenaltyNode extends AbstractNode implements Node, Discardable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>penalty</tt> contains the penalty value of this node.
     */
    private long penalty = 0;

    /**
     * Creates a new object.
     *
     * @param thePenalty the penalty value
     *
     * @see "<logo>TeX</logo> &ndash; The Program [158]"
     */
    public PenaltyNode(final Count thePenalty) {

        this(thePenalty.getValue());
    }

    /**
     * Creates a new object.
     *
     * @param thePenalty the penalty value
     */
    public PenaltyNode(final long thePenalty) {

        super();
        this.penalty = thePenalty;
    }

    /**
     * Getter for penalty.
     *
     * @return the penalty.
     */
    public long getPenalty() {

        return penalty;
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exhaustive form as it is used in tracing
     * output to the log file.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     * @param breadth the breadth
     * @param depth the depth
     *
     * @see "<logo>TeX</logo> &ndash; The Program [194]"
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String,
     *      int,
     *      int)
     */
    public void toString(final StringBuffer sb, final String prefix,
            final int breadth, final int depth) {

        sb.append(getLocalizer()
                .format("String.Format", Long.toString(penalty)));
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append(getLocalizer().format("Text.Format", Long.toString(penalty)));
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitPenalty(this, value);
    }

}
