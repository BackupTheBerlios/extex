/*
 * Copyright (C) 2003  Gerd Neugebauer
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.typesetter.Node;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractNode implements Node {
    /** This is the depth of the node.
     *  The depth is the extend of the node below the baseline.
     *  A <code>null</code> value indicates
     *  a running value which has to be set or computed later.
     */
    private Dimen depth;

    /** This is the height of the node.
     *  The height is the extend of the node above the baseline.
     *  A <code>null</code> value indicates
     *  a running value which has to be set or computed later.
     */
    private Dimen height;

    /** This is the width of the node.
     *  The width is the extend of the node along the baseline.
     *  <code>null</code> indicates
     *  a running value which has to be set or computed later.
     */
    private Dimen width;

    /**
     * Creates a new object.
     * All dimensions (width, height, depth) are initially unset.
     */
    public AbstractNode() {
        super();
        this.width  = null;
        this.height = null;
        this.depth  = null;
    }

    /**
     * Creates a new object.
     *
     * @param width the width of the node;
     *  <code>null</code> denotes the unset value
     * @param height the height of the node;
     *  <code>null</code> denotes the unset value
     * @param depth the depth of the node;
     *  <code>null</code> denotes the unset value
     */
    public AbstractNode(Dimen width, Dimen height, Dimen depth) {
        super();
        this.width  = width;
        this.height = height;
        this.depth  = depth;
    }

    /**
     * @see de.dante.extex.typesetter.Node#setDepth(de.dante.extex.interpreter.type.Dimen)
     */
    public void setDepth(Dimen d) {
        depth.set(d);
    }

    /**
     * @see de.dante.extex.typesetter.Node#getDepth()
     */
    public Dimen getDepth() {
        return depth;
    }

    /**
     * @see de.dante.extex.typesetter.Node#setHeight(de.dante.extex.interpreter.type.Dimen)
     */
    public void setHeight(Dimen h) {
        height.set(h);
    }

    /**
     * @see de.dante.extex.typesetter.Node#getHeight()
     */
    public Dimen getHeight() {
        return height;
    }

    /**
     * @see de.dante.extex.typesetter.Node#setWidth(de.dante.extex.interpreter.type.Dimen)
     */
    public void setWidth(Dimen w) {
        width.set(w);
    }

    /**
     * @see de.dante.extex.typesetter.Node#getWidth()
     */
    public Dimen getWidth() {
        return width;
    }

    /**
     * ...
     *
     * @param prefix ...
     *
     * @return ...
     */
    protected String toString(String prefix) {
        StringBuffer sb = new StringBuffer();
        toString(sb, prefix);
        return sb.toString();
    }

    /**
     * ...
     *
     * @param sb ...
     * @param prefix ...
     */
    protected void toString(StringBuffer sb, String prefix) {
        sb.append(prefix);

        //TODO
    }
}
