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
package de.dante.extex.typesetter.impl;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class VerticalListMaker extends AbstractListMaker implements ListMaker {
    /** This value contains the previous depth for baseline calculations.
     *  In contrast to TeX the value null is used to indicate that the next box 
     *  on the vertical list should be exempt from the baseline calculations.
     *  @see "TeX -- The Program [212]" 
     */
    private Dimen prevDepth = new Dimen(-1000*Dimen.ONE);

    /** ... */
    private VerticalListNode nodes = new VerticalListNode();

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     */
    public VerticalListMaker(Manager manager) {
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
    public void add(Node n) throws GeneralException {
        nodes.add(n);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.type.Font, java.lang.String)
     */
    public void add(TypesettingContext font, String symbol) throws GeneralException {
        ListMaker hlist = new HorizontalListMaker(manager);
        hlist.add(font, symbol);
        manager.push(hlist);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(de.dante.extex.interpreter.type.Glue)
     */
    public void addGlue(Glue g) throws GeneralException {
        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(TypesettingContext)
     */
    public void addSpace(TypesettingContext typesettingContext, Count spacefactor) throws GeneralException {
        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public NodeList close() {
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
     * <tt>\par</tt>s are silently ignored in vertical mode.
     *
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public void par() throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(de.dante.extex.interpreter.type.Dimen)
     */
    public void setPrevDepth(Dimen pd) throws GeneralException {
        prevDepth.set(pd);
    }

}
