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
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;

import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class HorizontalListMaker extends AbstractListMaker
    implements ListMaker {
    private static final int DEFAULT_SPACEFACTOR = 1000;
	/** ... */
    private HorizontalListNode nodes = new HorizontalListNode();

    /** ...
     * @see "TeX -- The Program [212]"
     */
    private int lang = 0;

    /** ...
     * @see "TeX -- The Program [212]"
     */
    private long spacefactor = DEFAULT_SPACEFACTOR;

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     */
    public HorizontalListMaker(final Manager manager) {
        super(manager);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {
        return Mode.HORIZONTAL;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(int)
     */
    public void setSpacefactor(final Count f) throws GeneralException {
        spacefactor = f.getValue();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.type.node.CharNode)
     */
    public void add(final Node c) throws GeneralException {
        nodes.add(c);
        spacefactor = DEFAULT_SPACEFACTOR;
    }

	/**
	 * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.type.Font,
	 *      java.lang.String)
	 * @see "The TeXbook [p.76]"
	 */
	public void add(final TypesettingContext context,
			final UnicodeChar symbol) throws GeneralException {
		CharNode c = manager.getCharNodeFactory().newInstance(context, symbol);
		nodes.add(c);

		int f = c.getSpaceFactor();

		if (f != 0) {
			spacefactor = (spacefactor < DEFAULT_SPACEFACTOR
							&& f > DEFAULT_SPACEFACTOR ? DEFAULT_SPACEFACTOR
					: f);
		}
	}

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(de.dante.extex.interpreter.type.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {
        nodes.add(new GlueNode(g)); // TODO: use factory?
        spacefactor = DEFAULT_SPACEFACTOR;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace()
     */
    public void addSpace(final TypesettingContext context,
    		final Count sfCount) throws GeneralException {
        long sf    = (sfCount != null ? sfCount.getValue() : spacefactor);
        Glue space = context.getFont()
                            .getSpace();

        // gene: maybe my interpretation of the TeXbook is slightly wrong
        if (sf == DEFAULT_SPACEFACTOR) { // normal case handled first
        } else if (sf == 0) {
            return;
        } else if (sf >= 2000) {
            Glue xspaceskip = null; //TODO unimplemented
            Glue spaceskip  = null;

            if (xspaceskip != null) {
                space = xspaceskip.copy();
            } else if (spaceskip != null) {
                space = xspaceskip.copy()
                                  .multiplyStretch(sf, DEFAULT_SPACEFACTOR)
                                  .multiplyShrink(DEFAULT_SPACEFACTOR, sf);
            } else {
                space = space.copy()
                             .multiplyStretch(sf, DEFAULT_SPACEFACTOR)
                             .multiplyShrink(DEFAULT_SPACEFACTOR, sf);
            }
        } else {
            space = space.copy()
                         .multiplyStretch(sf, DEFAULT_SPACEFACTOR)
                         .multiplyShrink(DEFAULT_SPACEFACTOR, sf);
        }

        addGlue(space);
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
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public void par() throws GeneralException {
        manager.closeTopList();
    }
}
