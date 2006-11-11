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

package de.dante.extex.typesetter.type.noad;

import java.util.logging.Logger;

import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.noad.util.MathSpacing;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.KernNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This noad contains a node which is passed through the math apparatus.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class NodeNoad implements Noad {

    /**
     * The field <tt>node</tt> contains the encapsulated node.
     */
    private Node node;

    /**
     * Creates a new object.
     *
     * @param node the node to
     */
    public NodeNoad(final Node node) {

        super();
        this.node = node;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSpacingClass()
     */
    public MathSpacing getSpacingClass() {

        return MathSpacing.UNDEF; // TODO gene: correct?
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSubscript()
     */
    public Noad getSubscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSuperscript()
     */
    public Noad getSuperscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSubscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSubscript(final Noad subscript) {

        throw new UnsupportedOperationException();
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSuperscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSuperscript(final Noad superscript) {

        throw new UnsupportedOperationException();
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        node.toString(sb, "", Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer, int)
     */
    public void toString(final StringBuffer sb, final int depth) {

        node.toString(sb, "", Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.Noad,
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public void typeset(final Noad previousNoad, final NoadList noads,
            final int index, final NodeList list,
            final MathContext mathContext, final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        if (node instanceof GlueNode || node instanceof KernNode) {
            if (previousNoad instanceof GlueNoad
                    && ((GlueNoad) previousNoad).isKill()) {
                StyleNoad style = mathContext.getStyle();
                if (style == StyleNoad.SCRIPTSTYLE
                        || style == StyleNoad.SCRIPTSCRIPTSTYLE) {
                    return;
                }
            }
        }

        getSpacingClass().addClearance(
                (previousNoad != null ? previousNoad.getSpacingClass() : null),
                list, mathContext);

        list.add(node);
    }

}
