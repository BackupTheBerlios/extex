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

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.WideGlue;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This abstract class provides some methods common to all Nodes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public abstract class AbstractNode implements Node {

    /**
     * The constant <tt>NO_CHAR</tt> contains the empty array of CharNode.
     */
    protected static final CharNode[] NO_CHARS = new CharNode[0];

    /**
     * The field <tt>depth</tt> contains the depth of the node.
     * The depth is the extend of the node below the baseline.
     */
    private Dimen depth;

    /**
     * The field <tt>height</tt> contains the height of the node.
     * The height is the extend of the node above the baseline.
     */
    private Dimen height;

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private Localizer localizer = null;

    /**
     * This is the width of the node.
     * The width is the extend of the node along the baseline.
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
    public AbstractNode(final FixedDimen aWidth) {

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
    public AbstractNode(final FixedDimen aWidth, final FixedDimen aHeight,
            final FixedDimen aDepth) {

        super();
        this.width = new Dimen(aWidth);
        this.height = new Dimen(aHeight);
        this.depth = new Dimen(aDepth);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#addHeightTo(
     *      de.dante.extex.interpreter.type.glue.WideGlue)
     */
    public void addHeightTo(final WideGlue glue) {

        glue.add(height);
        glue.add(depth);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#addWidthTo(
     *      de.dante.extex.interpreter.type.glue.WideGlue)
     */
    public void addWidthTo(final WideGlue glue) {

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
     * @param visitor the node visitor to be invoked when the node is hit. Note
     *  that each node in the output page is visited this way. Thus there is no
     *  need to implement a node traversal for the NodeList types
     * @param inHMode <code>true</code> iff the container is a horizontal list.
     *  Otherwise the container is a vertical list
     *
     * @return the node to be used instead of the current one in the output
     *  list. If the value is <code>null</code> then the node is deleted. If
     *  the value is the node itself then it is preserved.
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.typesetter.type.Node#atShipping(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      boolean)
     */
    public Node atShipping(final Context context, final Typesetter typesetter,
            final NodeVisitor visitor, final boolean inHMode)
            throws GeneralException {

        return (Node) this.visit(visitor, inHMode
                ? Boolean.TRUE
                : Boolean.FALSE);
    }

    /**
     * Compute the amount of adjustment needed to achieve a certain size.
     *
     * @param size the current size in scaled points
     * @param glue the glue
     * @param sum the total stretchability or shrinkability
     *
     * @return the adjustment
     */
    protected long computeAdjustment(final long size, final FixedGlue glue,
            final FixedGlueComponent sum) {

        FixedGlueComponent s = (size > 0 ? glue.getStretch() : glue.getShrink());

        int order = s.getOrder();
        long value = sum.getValue();
        if (order < sum.getOrder() || value == 0) {
            return 0;
        }

        long sValue = s.getValue();
        long adjust = sValue * size / value;
        if (order == 0) {
            if (adjust > sValue) {
                adjust = sValue;
            } else if (adjust < -sValue) {
                adjust = -sValue;
            }
        }
        return adjust;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#countChars()
     */
    public int countChars() {

        return 0;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getChars()
     */
    public CharNode[] getChars() {

        return NO_CHARS;
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
     * @return the localizer
     */
    protected Localizer getLocalizer() {

        if (this.localizer == null) {
            this.localizer = LocalizerFactory.getLocalizer(this.getClass()
                    .getName());
        }
        return this.localizer;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getVerticalSize()
     */
    public FixedDimen getVerticalSize() {

        Dimen h = getHeight();
        Dimen d = new Dimen(getDepth());

        if (h.ge(Dimen.ZERO)) {
            if (d.ge(Dimen.ZERO)) {
                d.add(h);
            } else {
                d.negate();
                d.max(h);
            }
        } else {
            if (d.ge(Dimen.ZERO)) {
                d.negate();
                d.min(h);
                d.negate();
            } else {
                d.add(h);
                d.negate();
            }
        }
        return d;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getWidth()
     */
    public Dimen getWidth() {

        return width;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setDepth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setDepth(final FixedDimen d) {

        depth.set(d);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setHeight(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setHeight(final FixedDimen h) {

        height.set(h);
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#setWidth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setWidth(final FixedDimen w) {

        width.set(w);
    }

    /**
     * Adjust the height of a flexible node. This method is a noop for any but
     * the flexible nodes.
     *
     * @param h the desired height
     * @param sum the total sum of the glues
     *
     * @see de.dante.extex.typesetter.type.Node#spreadHeight(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen,
     *      de.dante.extex.interpreter.type.glue.FixedGlueComponent)
     */
    public void spreadHeight(final FixedDimen h, final FixedGlueComponent sum) {

    }

    /**
     * Adjust the width of a flexible node. This method is a noop for any but
     * the flexible nodes.
     *
     * @param w the desired width
     * @param sum the total sum of the glues
     *
     * @see de.dante.extex.typesetter.type.Node#spreadWidth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen,
     *      de.dante.extex.interpreter.type.glue.FixedGlueComponent)
     */
    public void spreadWidth(final FixedDimen w, final FixedGlueComponent sum) {

    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exhaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb, "", Integer.MAX_VALUE, Integer.MAX_VALUE);
        return sb.toString();
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String,
     *      int,
     *      int)
     */
    public void toString(final StringBuffer sb, final String prefix,
            final int b, final int d) {

        sb.append(getLocalizer().format("String.Format"));
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
     *
     * @see de.dante.extex.typesetter.type.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append(getLocalizer().format("Text.Format"));
    }
}