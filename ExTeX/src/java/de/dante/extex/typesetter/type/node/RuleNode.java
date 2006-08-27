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

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * The rule node represents a rectangular area on the page filled with some
 * color.
 *
 * @see "<logo>TeX</logo> &ndash; The Program [138]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.17 $
 */
public class RuleNode extends AbstractNode implements Node {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>context</tt> the typesetting context.
     */
    private TypesettingContext context;

    /**
     * The field <tt>horizontal</tt> contains the indicator that this is a
     * horizontal rule; otherwise it is a vertical rule.
     */
    private boolean horizontal;

    /**
     * Creates a new object.
     *
     * @param width the width of the rule
     * @param height the height of the rule
     * @param depth the depth of the rule
     * @param horizontal the indicator that this is a
     *  horizontal rule; otherwise it is a vertical rule
     * @param theContext the typesetting context
     *
     * @see "<logo>TeX</logo> &ndash; The Program [139]"
     */
    public RuleNode(final FixedDimen width, final FixedDimen height,
            final FixedDimen depth, final TypesettingContext theContext,
            boolean horizontal) {

        super(width, height, depth);
        this.context = theContext;
        this.horizontal = horizontal;
    }

    /**
     * Getter for the typesetting context.
     *
     * @return the typesetting context.
     */
    public TypesettingContext getTypesettingContext() {

        return context;
    }

    /**
     * Getter for horizontal.
     *
     * @return the horizontal
     */
    public boolean isHorizontal() {

        return this.horizontal;
    }

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a short form only as it is used in error
     * messages to the user.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     * @param breadth the breadth
     * @param depth the depth
     *
     * @see "<logo>TeX</logo> &ndash; The Program [187]"
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String,
     *      int,
     *      int)
     */
    public void toString(final StringBuffer sb, final String prefix,
            final int breadth, final int depth) {

        FixedDimen x = getHeight();
        String h = (x == null ? "*" : x.toString());
        x = getDepth();
        String d = (x == null ? "*" : x.toString());
        x = getWidth();
        String w = (x == null ? "*" : x.toString());
        sb.append(getLocalizer().format("String.Format", h, d, w));
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        FixedDimen x = getHeight();
        String h = (x == null ? "*" : x.toString());
        x = getDepth();
        String d = (x == null ? "*" : x.toString());
        x = getWidth();
        String w = (x == null ? "*" : x.toString());
        sb.append(getLocalizer().format("Text.Format", h, d, w));
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitRule(this, value);
    }

}
