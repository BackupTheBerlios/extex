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

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;
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
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class LigatureNode extends CharNode implements Node {

    /**
     * The field <tt>list</tt> contains the ...
     */
    private Node first;

    /**
     * The field <tt>second</tt> contains the ...
     */
    private Node second;

    /**
     * Creates a new object.
     *
     * @param context the typesetting context
     * @param uc the unicode character
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
     * Getter for first.
     *
     * @return the first.
     */
    public Node getFirst() {

        return this.first;
    }

    /**
     * Getter for second.
     *
     * @return the second.
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

        return " (ligature " + first.toString() + " " + second.toString() + ")"; //TODO
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitLigature(value, value2);
    }
}