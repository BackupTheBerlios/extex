/*
 * Copyright (C) 2003-2004  Gerd Neugebauer, Michael Niedermair
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

import java.util.Map;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.LineBreaker;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * Maker for a horizontal list.
 * <p>
 * After <code>par()</code>, the linebreak and hyphenation is made.
 * <p>
 * When the horizontal list are closed, the paragraph is split into lines.
 * It use the linebreaker, which is defined with <code>\linebreaker</code>.
 * Is the named linebreaker not found, it use the linebreaker with
 * the name <tt>default</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class HorizontalListMaker extends AbstractListMaker implements ListMaker {

	/**
	 * The constant <tt>DEFAULT_SPACEFACTOR</tt> contains the ...
	 */
	private static final int DEFAULT_SPACEFACTOR = 1000;

	/**
	 * The field <tt>nodes</tt> contains the ...
	 */
	private HorizontalListNode nodes = new HorizontalListNode();

	/**
	 *  ...
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
		long sf = f.getValue();
		if (sf <= 0) {
			throw new GeneralHelpingException("TTP.BadSpaceFactor", Long.toString(sf));
		}
		spacefactor = sf;
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.type.node.CharNode)
	 */
	public void add(final Node c) {
		nodes.add(c);
		spacefactor = DEFAULT_SPACEFACTOR;
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.type.Font,
	 *      java.lang.String)
	 * @see "The TeXbook [p.76]"
	 */
	public void add(final TypesettingContext context, final UnicodeChar symbol) {
		CharNode c = manager.getCharNodeFactory().newInstance(context, symbol);
		nodes.add(c);

		int f = c.getSpaceFactor();

		if (f != 0) {
			spacefactor = (spacefactor < DEFAULT_SPACEFACTOR && f > DEFAULT_SPACEFACTOR ? DEFAULT_SPACEFACTOR : f);
		}
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#addGlue(de.dante.extex.interpreter.type.Glue)
	 */
	public void addGlue(final Glue g) {
		nodes.addSkip(g);
		spacefactor = DEFAULT_SPACEFACTOR;
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#addSpace(de.dante.extex.interpreter.context.TypesettingContext,
	 *      de.dante.extex.interpreter.type.Count)
	 */
	public void addSpace(final TypesettingContext context, final Count sfCount) {
		long sf = (sfCount != null ? sfCount.getValue() : spacefactor);
		Glue space = context.getFont().getSpace();

		// gene: maybe my interpretation of the TeXbook is slightly wrong
		if (sf == DEFAULT_SPACEFACTOR) { // normal case handled first
		} else if (sf == 0) {
			return;
		} else if (sf >= 2000) {
			Glue xspaceskip = null; //TODO unimplemented
			Glue spaceskip = null;

			if (xspaceskip != null) {
				space = xspaceskip.copy();
			} else if (spaceskip != null) {
				space = xspaceskip.copy().multiplyStretch(sf, DEFAULT_SPACEFACTOR).multiplyShrink(DEFAULT_SPACEFACTOR, sf);
			} else {
				space = space.copy().multiplyStretch(sf, DEFAULT_SPACEFACTOR).multiplyShrink(DEFAULT_SPACEFACTOR, sf);
			}
		} else {
			space = space.copy().multiplyStretch(sf, DEFAULT_SPACEFACTOR).multiplyShrink(DEFAULT_SPACEFACTOR, sf);
		}

		addGlue(space);
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#close()
	 */
	public NodeList close() throws GeneralException {
		if (!nodes.isLineBreak()) {
			NodeList nl = makeLineBreak();
			manager = null;
			return nl;
		}
		return nodes;
	}

	/**
	 * make the linebreak
	 */
	private NodeList makeLineBreak() throws GeneralException {
		Map linebreakerMap = manager.getLineBreakerMap();
		String lbname = manager.getContext().getToks("linebreaker").toText();
		if (lbname == null || lbname.trim().length() == 0) {
			lbname = "default";
		}

		LineBreaker lb = (LineBreaker) linebreakerMap.get(lbname);
		if (lb == null) {
			throw new GeneralException("no linebreaker found"); // TODO change
		}
		return lb.breakLines(nodes, manager);
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#par()
	 */
	public void par() throws GeneralException {
		manager.closeTopList();
	}
}
