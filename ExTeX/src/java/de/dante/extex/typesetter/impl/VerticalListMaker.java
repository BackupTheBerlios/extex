/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * Maker for a vertical list.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.12 $
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
     * The field <tt>nodes</tt> contains the list of nodes encapsulated.
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
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.Node)
     */
    public void add(final Node n) throws GeneralException {

        nodes.add(n);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void add(final TypesettingContext font, final UnicodeChar symbol)
            throws GeneralException {

        ListMaker hlist = new HorizontalListMaker(getManager());
        hlist.add(font, symbol);
        getManager().push(hlist);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        nodes.addSkip(g);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
        final Count spacefactor) throws GeneralException {

        // TODO unimplemented
        //throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public final NodeList close() {

        return nodes;
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
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setPrevDepth(final Dimen pd) throws GeneralException {

        prevDepth.set(pd);
    }

}
