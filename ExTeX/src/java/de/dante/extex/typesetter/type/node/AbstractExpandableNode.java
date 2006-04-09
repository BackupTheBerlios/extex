/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.glue.WideGlue;
import de.dante.extex.typesetter.Discardable;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * This node represents a <logo>TeX</logo> "glue" node.
 * <p>
 * For the document writer it acts like a kern node. The width contains
 * the distance to add.
 * </p>
 * <p>
 * The stretchability is adjusted by the typesetter and the width is adjusted
 * accordingly.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractExpandableNode extends AbstractNode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060320L;

    /**
     * The field <tt>size</tt> contains the glue specification for this node.
     * The natural size of the glue is the initial width of this node.
     */
    private Glue size;

    /**
     * The field <tt>horizontal</tt> contains the ...
     */
    private boolean horizontal;

    /**
     * Creates a new object.
     * The size is used to determine the width in horizontal mode and the height
     * in vertical mode.
     *
     * @param size the actual size
     * @param horizontal indicator that the glue is used in horizontal
     *  mode
     */
    public AbstractExpandableNode(final FixedDimen size,
            final boolean horizontal) {

        super(Dimen.ZERO_PT);
        this.size = new Glue(size);
        this.horizontal = horizontal;

        if (horizontal) {
            setWidth(this.size.getLength());
        } else {
            setHeight(this.size.getLength());
        }
    }

    /**
     * Creates a new object.
     * The size is used to determine the width in horizontal mode and the height
     * in vertical mode.
     *
     * @param size the actual size
     * @param horizontal indicator that the glue is used in horizontal
     *  mode
     */
    public AbstractExpandableNode(final FixedGlue size, final boolean horizontal) {

        super(Dimen.ZERO_PT);
        this.size = new Glue(size);
        this.horizontal = horizontal;

        if (horizontal) {
            setWidth(size.getLength());
        } else {
            setHeight(size.getLength());
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#addHeightTo(
     *      de.dante.extex.interpreter.type.glue.WideGlue)
     */
    public void addHeightTo(final WideGlue glue) {

        if (horizontal) {
            glue.add(getHeight());
            glue.add(getDepth());
        } else {
            glue.add(this.size);
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#addWidthTo(
     *      de.dante.extex.interpreter.type.glue.WideGlue)
     */
    public void addWidthTo(final WideGlue glue) {

        if (horizontal) {
            glue.add(this.size);
        } else {
            glue.add(getWidth());
        }
    }

    /**
     * Getter for size.
     *
     * @return the size
     */
    public FixedGlue getSize() {

        return this.size;
    }

    /**
     * Setter for the size
     *
     * @param skip the new value
     */
    public void setSize(final FixedGlue skip) {

        size.set(skip);
    }

    /**
     * @see de.dante.extex.typesetter.type.node.AbstractNode#spreadHeight(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen,
     *      de.dante.extex.interpreter.type.glue.FixedGlueComponent)
     */
    public void spreadHeight(final FixedDimen height,
            final FixedGlueComponent sum) {

        if (horizontal) {
            return;
        }

        long adjust = computeAdjustment(height.getValue(), this.size, sum);
        if (adjust != 0) {
            getHeight().add(adjust);
        }
    }

    /**
     * Adjust the width of the flexible node.
     *
     * @param width the desired with
     * @param sum the total sum of the glues
     *
     * @see de.dante.extex.typesetter.type.Node#spreadWidth(
     *      de.dante.extex.interpreter.type.dimen.FixedDimen,
     *      de.dante.extex.interpreter.type.glue.FixedGlueComponent)
     */
    public void spreadWidth(final FixedDimen width, final FixedGlueComponent sum) {

        if (!horizontal) {
            return;
        }

        long adjust = computeAdjustment(width.getValue(), this.size, sum);
        if (adjust != 0) {
            getWidth().add(adjust);
        }
    }

}
