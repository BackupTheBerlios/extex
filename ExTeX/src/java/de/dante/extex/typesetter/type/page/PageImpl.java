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

package de.dante.extex.typesetter.type.page;

import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.NodeList;

/**
 * This class provides a transport object for pages. Beside the nodes it
 * contains additional administrative information.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class PageImpl implements Page {

    /**
     * The field <tt>background</tt> contains the background color.
     */
    private Color background = null;

    /**
     * The field <tt>mediaHeight</tt> contains the height of the paper.
     */
    private Dimen mediaHeight = new Dimen(
            Dimen.ONE_INCH.getValue() * 2970 / 254);

    /**
     * The field <tt>mediaHOffset</tt> contains the horizontal offset of the
     * upper left corner of the contents on the page. In <logo>TeX</logo>
     * this value is defined to be 1 in.
     */
    private Dimen mediaHOffset = new Dimen(Dimen.ONE_INCH);

    /**
     * The field <tt>mediaVOffset</tt> contains the vertical offset of the
     * upper left corner of the contents on the page. In <logo>TeX</logo>
     * this value is defined to be 1 in.
     */
    private Dimen mediaVOffset = new Dimen(Dimen.ONE_INCH);

    /**
     * The field <tt>mediaWidth</tt> contains the width of the physical paper.
     */
    private Dimen mediaWidth = new Dimen(Dimen.ONE_INCH.getValue() * 2100 / 254);

    /**
     * The field <tt>nodes</tt> contains the nodes describing the objects on
     * the page.
     */
    private NodeList nodes;

    /**
     * The field <tt>pageNo</tt> contains the array of page number indicators.
     */
    private FixedCount[] pageNo;

    /**
     * Creates a new object.
     *
     * @param nodes the nodes to transport
     */
    public PageImpl(final NodeList nodes, final FixedCount[] pageNo) {

        super();
        this.nodes = nodes;
        this.pageNo = pageNo;
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#getColor()
     */
    public Color getColor() {

        return background;
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#getMediaHeight()
     */
    public Dimen getMediaHeight() {

        return mediaHeight;
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#getMediaHOffset()
     */
    public Dimen getMediaHOffset() {

        return mediaHOffset;
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#getMediaVOffset()
     */
    public Dimen getMediaVOffset() {

        return mediaVOffset;
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#getMediaWidth()
     */
    public Dimen getMediaWidth() {

        return mediaWidth;
    }

    /**
     * Getter for nodes.
     *
     * @return the nodes
     */
    public NodeList getNodes() {

        return this.nodes;
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#getPageNo()
     */
    public FixedCount[] getPageNo() {

        return pageNo;
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#setColor(
     *      de.dante.extex.interpreter.context.Color)
     */
    public void setColor(final Color background) {

        this.background = background;
    }

    /**
     * Setter for mediaHeight.
     *
     * @param mediaHeight the mediaHeight to set
     */
    public void setMediaHeight(final Dimen mediaHeight) {

        mediaHeight.set(mediaHeight);
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#setMediaHOffset(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setMediaHOffset(final Dimen offset) {

        mediaHOffset.set(offset);
    }

    /**
     * @see de.dante.extex.typesetter.type.page.Page#setMediaVOffset(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setMediaVOffset(final Dimen offset) {

        mediaVOffset.set(offset);
    }

    /**
     * Setter for media width.
     *
     * @param width the mediaWidth to set
     *
     * @see de.dante.extex.typesetter.type.page.Page#setMediaWidth(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setMediaWidth(final Dimen width) {

        mediaWidth.set(width);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return nodes.toString();
    }

}
