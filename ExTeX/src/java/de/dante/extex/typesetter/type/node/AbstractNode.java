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

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.util.GeneralException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This abstract class provides some methods common to all Nodes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractNode implements Node {

    /**
     * The field <tt>depth</tt> contains the depth of the node.
     * The depth is the extend of the node below the baseline.
     */
    private Dimen depth;

    /**
     * The field <tt>height</tt> contains the height of the node.
     *  The height is the extend of the node above the baseline.
     */
    private Dimen height;

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private Localizer localizer = null;

    /** This is the width of the node.
     *  The width is the extend of the node along the baseline.
     */
    private Dimen width;

    /**
     * Creates a new object.
     * All dimensions (width, height, depth) are initially set to 0pt.
     */
    public AbstractNode() {

        super();
        width = new Dimen();
        height = new Dimen();
        depth = new Dimen();
    }

    /**
     * Creates a new object.
     *
     * @param aWidth the width of the node
     */
    public AbstractNode(final Dimen aWidth) {

        super();
        this.width = new Dimen(aWidth);
        this.height = new Dimen();
        this.depth = new Dimen();
    }

    /**
     * Creates a new object.
     *
     * @param aWidth the width of the node
     * @param aHeight the height of the node
     * @param aDepth the depth of the node
     */
    public AbstractNode(final Dimen aWidth, final Dimen aHeight,
            final Dimen aDepth) {

        super();
        this.width = new Dimen(aWidth);
        this.height = new Dimen(aHeight);
        this.depth = new Dimen(aDepth);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#addWidthTo(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addWidthTo(final Glue glue) {

        glue.add(width);
    }

    /**
     * This method performs any action which are required to executed at the
     * time of shipping the node to the DocumentWriter. It is a NOOP in the
     * abstract base class and should be overwritten by sub-classes if
     * required.
     *
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.typesetter.type.Node#atShipping(Context, Typesetter)
     */
    public void atShipping(final Context context, final Typesetter typesetter)
            throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getDepth()
     */
    public Dimen getDepth() {

        return depth;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getHeight()
     */
    public Dimen getHeight() {

        return height;
    }

    /**
     * Getter for localizer.
     *
     * @return the localizer.
     */
    protected Localizer getLocalizer() {

        if (this.localizer == null) {
            this.localizer = LocalizerFactory
                    .getLocalizer(Node.class.getName());
        }
        return this.localizer;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getWidth()
     */
    public Dimen getWidth() {

        return width;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setDepth(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setDepth(final Dimen aDepth) {

        depth.set(aDepth);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setHeight(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setHeight(final Dimen aHeight) {

        height.set(aHeight);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setWidth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setWidth(final FixedDimen theWidth) {

        width.set(theWidth);
    }

    /**
     * Adjust the width of a flexible node. This method is a noop for any but
     * the flexible nodes.
     *
     * @param theWidth the desired with
     * @param sum the total sum of the glues
     *
     * @see de.dante.extex.typesetter.type.Node#spread(FixedDimen, FixedGlueComponent)
     */
    public void spread(final FixedDimen theWidth, final FixedGlueComponent sum) {

    }

    /**
     * Compute a text representation of this object.
     *
     * @param prefix the string prepended to each line of the resulting text
     *
     * @return the text representation of this object
     */
    protected String toText(final String prefix) {

        StringBuffer sb = new StringBuffer();
        toText(sb, prefix);
        return sb.toString();
    }

    /**
     * Puts a text representation of the object into a string buffer.
     *
     * @param sb the output string buffer
     * @param prefix the string prepended to each line of the resulting text
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append(prefix);

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }
}