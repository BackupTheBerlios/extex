/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;

/**
 * This node signals the start of a link.
 * This node type represents the extension node from the perspective of
 * <logo>TeX</logo>.
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class PdfStartLink extends WhatsItNode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>action</tt> contains the ...
     */
    private ActionSpec action;

    /**
     * The field <tt>attr</tt> contains the ...
     */
    private String attr;

    /**
     * The field <tt>rule</tt> contains the ...
     */
    private RuleNode rule;

    /**
     * Creates a new object.
     *
     * @param rule
     * @param attr the attribute string. This can be <code>null</code>.
     * @param action
     */
    public PdfStartLink(final RuleNode rule, final String attr,
            final ActionSpec action) {

        super();
        this.rule = rule;
        this.attr = attr;
        this.action = action;
    }

    /**
     * Getter for action.
     *
     * @return the action
     */
    public ActionSpec getAction() {

        return this.action;
    }

    /**
     * Getter for attr.
     *
     * @return the attr
     */
    public String getAttr() {

        return this.attr;
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
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("(pdfstartlink)");
    }

}
