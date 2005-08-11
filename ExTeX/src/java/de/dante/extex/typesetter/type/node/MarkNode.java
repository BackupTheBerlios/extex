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

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * A mark node carries some tokens which can be extracted after the page has
 * been completed. It can be used extract the first and last mark for headlines.
 * <p>
 * The document writer should ignore mark nodes.
 * </p>
 *
 * @see "<logo>TeX</logo> &ndash; The Program [141]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class MarkNode extends AbstractNode implements Node {

    /**
     * The field <tt>index</tt> contains the index of the mark node for eTeX.
     * The index 0 corresponds to the original mark of <logo>TeX</logo>.
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
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     *
     * @see "<logo>TeX</logo> &ndash; The Program [196]"
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append(getLocalizer().format("String.Format", mark.toString()));
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append(getLocalizer().format("Text.Format", mark.toString()));
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitMark(this, value);
    }

}
