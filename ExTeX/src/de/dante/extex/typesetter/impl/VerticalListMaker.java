/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.typesetter.impl;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class VerticalListMaker extends AbstractListMaker implements ListMaker {
    /**
     * This value contains the previous depth for baseline calculations. In
     * contrast to TeX the value null is used to indicate that the next box on
     * the vertical list should be exempt from the baseline calculations.
     * 
     * @see "TeX -- The Program [212]"
     */
    private Dimen prevDepth = new Dimen(-1000 * Dimen.ONE);

    /**
     * The field <tt>nodes</tt> contains the ...
     */
    private VerticalListNode nodes = new VerticalListNode();

    /**
     * Creates a new object.
     * 
     * @param manager the manager to ask for global changes
     */
    public VerticalListMaker(final Manager manager) {
        super(manager);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {
        return Mode.VERTICAL;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.type.node.CharNode)
     */
    public void add(final Node n) throws GeneralException {
        nodes.add(n);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.type.Font,
     *      java.lang.String)
     */
    public void add(final TypesettingContext font, final UnicodeChar symbol)
        throws GeneralException {
        ListMaker hlist = new HorizontalListMaker(manager);
        hlist.add(font, symbol);
        manager.push(hlist);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(de.dante.extex.interpreter.type.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {
        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(TypesettingContext)
     */
    public void addSpace(final TypesettingContext typesettingContext,
        final Count spacefactor) throws GeneralException {
        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public final NodeList close() {
        propagateSizes();
        manager = null;
        return nodes;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#open()
     */
    public void open() {
        // TODO Auto-generated method stub
    }

    /**
     * <tt>\par</tt> s are silently ignored in vertical mode.
     * 
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public void par() {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(de.dante.extex.interpreter.type.Dimen)
     */
    public void setPrevDepth(final Dimen pd) throws GeneralException {
        prevDepth.set(pd);
    }

    /**
     * ...
     * 
     *  
     */
    private void propagateSizes() {
        NodeIterator iter = nodes.iterator();
        Node node;
        Dimen width = new Dimen();
        Dimen height = new Dimen();
        Dimen depth = new Dimen();

        while (iter.hasNext()) {
            node = iter.next();
            height.add(node.getHeight());
            depth.set(node.getDepth());
            height.add(depth);
            if (width.lt(node.getWidth())) {
                width.set(node.getWidth());
            }
        }

        height.subtract(depth);
        nodes.setWidth(width);
        nodes.setHeight(height);
        nodes.setDepth(depth);
    }
}
