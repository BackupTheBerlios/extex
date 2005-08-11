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
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * This node represents a <logo>TeX</logo> "math" node with the subtype "before".
 * <p>
 * For the document writer it acts like a glue or kern node. The width contains
 * the distance to add.
 * </p>
 *
 * @see "<logo>TeX</logo> &ndash; The Program [147]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.8 $
 */
public class BeforeMathNode extends AbstractNode implements Node, Discardable {

    /**
     * Creates a new object.
     */
    public BeforeMathNode() {

        super();
    }

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     *
     * @see "<logo>TeX</logo> &ndash; The Program [192]"
     * @see de.dante.extex.typesetter.type.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        Dimen width = getWidth();

        if (width.eq(Dimen.ZERO_PT)) {
            sb.append(getLocalizer().format("String.Format"));
        } else {
            sb.append(getLocalizer().format("StringSurrounded.Format",
                    width.toString()));
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitBeforeMath(this, value);
    }

}
