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

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * The ligature node represents a ligature of several characters.
 * Ligatures can be build amoung characters from one common font only. The
 * information where and how to build ligatures comes from the font.
 * The original characters are contained in this node to be restored when
 * required.
 *
 * @see "TeX -- The Program [143]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class LigatureNode extends CharNode implements Node {

    /**
     * The field <tt>first</tt> contains the first node combined in the
     * ligature.
     */
    private Node first;

    /**
     * The field <tt>second</tt> contains the node combined in the
     * ligature.
     */
    private Node second;

    /**
     * Creates a new object.
     *
     * @param context the typesetting context
     * @param uc the Unicode character
     * @param n1 the first node for the ligature
     * @param n2 the second node for the ligature
     *
     * @see "TeX -- The Program [144]"
     */
    public LigatureNode(final TypesettingContext context, final UnicodeChar uc,
            final Node n1, final Node n2) {

        super(context, uc);
        first = n1;
        second = n2;
    }

    /**
     * Getter for first node.
     *
     * @return the first node
     */
    public Node getFirst() {

        return this.first;
    }

    /**
     * Getter for second node.
     *
     * @return the second node
     */
    public Node getSecond() {

        return this.second;
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     *
     * @see "TeX -- The Program [193]"
     */
    public String toString() {

        return " (ligature " + first.toString() + " " + second.toString() + ")";
        //TODO gene: incomplete
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitLigature(value, value2);
    }
}
