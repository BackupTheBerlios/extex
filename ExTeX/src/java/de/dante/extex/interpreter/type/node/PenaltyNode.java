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

import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.typesetter.Discartable;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;

import de.dante.util.GeneralException;

/**
 * ...
 *
 * @see "TeX -- The Program [157]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.8 $
 */
public class PenaltyNode extends AbstractNode implements Node, Discartable {

    /**
     * The field <tt>penalty</tt> contains the ...
     */
    private long penalty = 0;

    /**
     * Creates a new object.
     *
     * @param thePenalty the penalty value
     *
     * @see "TeX -- The Program [158]"
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
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     *
     * @see "TeX -- The Program [194]"
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb, "");
        return sb.toString();
    }

    /**
     * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("penalty ");
        sb.append(Long.toString(penalty)); //TODO i18n
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitPenalty(value, value2);
    }

}