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
import de.dante.util.GeneralException;

/**
 * This node represents a glyph which can be broken if required.
 *
 * @see "TeX -- The Program [145]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class DiscretionaryNode extends AbstractNode implements Node {

    /**
     * The field <tt>noBreak</tt> contains the Tokens to be inserted in case
     * of no line breaking at this position.
     */
    private Tokens noBreak;

    /**
     * The field <tt>postBreak</tt> contains the Tokens to be inserted at the
     * beginning of th next ine in case of a line breaking at this position.
     */
    private Tokens postBreak;
    /**
     * The field <tt>preBreak</tt> contains the Tokens to be inserted at the
     * end of the line in case of a line breaking at this position.
     */
    private Tokens preBreak;

    /**
     * Creates a new object.
     *
     * @param pre the Tokens to be inserted at the end of the line in case of a
     *  line breaking at this position.
     * @param post the Tokens to be inserted at the beginning of th next line
     *  in case of a line breaking at this position.
     * @param no the Tokens to be inserted in case of no line breaking at this
     *  position.
     */
    public DiscretionaryNode(final Tokens pre, final Tokens post,
            final Tokens no) {

        super();
        preBreak = pre;
        postBreak = post;
        noBreak = no;
    }

    /**
     * Getter for noBreak.
     *
     * @return the noBreak.
     */
    public Tokens getNoBreak() {

        return noBreak;
    }

    /**
     * Getter for postBreak.
     *
     * @return the postBreak.
     */
    public Tokens getPostBreak() {

        return postBreak;
    }

    /**
     * Getter for preBreak.
     *
     * @return the preBreak.
     */
    public Tokens getPreBreak() {

        return preBreak;
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     *
     * @see "TeX -- The Program [195]"
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb, "");
        return sb.toString();
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("discretionary"); //TODO gene: incomplete
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(java.lang.StringBuffer, java.lang.String)
     */
    public void toText(StringBuffer sb, String prefix) {

        // TODO gene: toText unimplemented

    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitDiscretionary(this, value);
    }

}
