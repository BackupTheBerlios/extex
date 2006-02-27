/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.node.pdftex;

import de.dante.extex.interpreter.primitives.pdftex.util.id.IdSpec;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;

/**
 * This node contains an PDF Object.
 * This node type represents the extension node from the perspective of
 * <logo>TeX</logo>.
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class PdfThread extends WhatsItNode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>attr</tt> contains the attribute string.
     */
    private String attr;

    /**
     * The field <tt>id</tt> contains the id.
     */
    private IdSpec id;

    /**
     * The field <tt>rule</tt> contains the rule.
     */
    private RuleNode rule;

    /**
     * Creates a new object.
     *
     * @param rule the rule
     * @param attr the attribute string. This can be <code>null</code>.
     * @param id the id
     */
    public PdfThread(final RuleNode rule, final String attr, final IdSpec id) {

        super();
        this.rule = rule;
        this.attr = attr;
        this.id = id;
    }

    /**
     * Getter for the attribute string.
     *
     * @return the attribute string
     */
    public String getAttr() {

        return this.attr;
    }

    /**
     * Getter for id.
     *
     * @return the id
     */
    public IdSpec getId() {

        return this.id;
    }

    /**
     * Getter for rule.
     *
     * @return the rule
     */
    public RuleNode getRule() {

        return this.rule;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix,
            final int breadth, final int depth) {

        sb.append("(pdfthread " + id + ")");
    }

}
