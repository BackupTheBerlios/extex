/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.typesetter.Node;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ExtensionNode extends WhatsItNode implements Node {

    /**
     * The field <tt>extension</tt> contains the extension object.
     */
    private Extension extension;

    /**
     * Creates a new object.
     *
     * @param theExtension the extension object
     */
    public ExtensionNode(final Extension theExtension) {

        super();
        this.extension = theExtension;
    }

    /**
     * @see de.dante.extex.interpreter.type.node.AbstractNode#getDepth()
     */
    public Dimen getDepth() {

        return this.extension.getDepth();
    }

    /**
     * @see de.dante.extex.interpreter.type.node.AbstractNode#getHeight()
     */
    public Dimen getHeight() {

        return this.extension.getHeight();
    }

    /**
     * @see de.dante.extex.interpreter.type.node.AbstractNode#getWidth()
     */
    public Dimen getWidth() {

        return this.extension.getWidth();
    }

    /**
     * @see de.dante.extex.interpreter.type.node.AbstractNode#setDepth(de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setDepth(final Dimen depth) {

        this.extension.setDepth(depth);
    }

    /**
     * @see de.dante.extex.interpreter.type.node.AbstractNode#setHeight(de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setHeight(final Dimen height) {

        this.extension.setHeight(height);
    }

    /**
     * @see de.dante.extex.interpreter.type.node.AbstractNode#setWidth(FixedDimen)
     */
    public void setWidth(final FixedDimen width) {

        // TODO gene: incomplete
        throw new RuntimeException("incomplete");
        //this.extension.setWidth(width);
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "extension";
    }

    /**
     * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append("extension");
    }

}