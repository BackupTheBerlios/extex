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

package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * A mark node carries some tokens which can be extracted after the page has
 * been completed. It can be used extract the first and last mark for headlines.
 * <p>
 * The document writer should ignore mark nodes.
 * </p>
 *
 * @see "TeX -- The Program [141]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.13 $
 */
public class MarkNode extends AbstractNode implements Node {

    /**
     * The field <tt>index</tt> contains the index of the mark node for eTeX.
     * The index 0 corresponds to the original mark of TeX.
     * I.e. \marks0 == \mark
     */
    private long index;

    /**
     * The field <tt>mark</tt> contains the tokens of the mark.
     */
    private Tokens mark;

    /**
     * Creates a new object.
     *
     * @param theMark the mark tokens to store
     * @param theIndex the index of the mark
     */
    public MarkNode(final Tokens theMark, final long theIndex) {

        super();
        mark = theMark;
        index = theIndex;
    }

    /**
     * Getter for index.
     *
     * @return the index.
     */
    public long getIndex() {

        return this.index;
    }

    /**
     * Getter for mark.
     *
     * @return the mark.
     */
    public Tokens getMark() {

        return this.mark;
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exhaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     *
     * @see "TeX -- The Program [196]"
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

        sb.append(getLocalizer().format("MarkNode.Text", mark.toString()));
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(
     *      de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitMark(this, value);
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitMark(value, value2);
    }
}

