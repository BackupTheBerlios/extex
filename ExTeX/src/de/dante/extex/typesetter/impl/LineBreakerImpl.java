/*
 * Copyright (C) 2004 Michael Niedermair
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

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.typesetter.LineBreaker;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;

/**
 * Implementation for a <code>LineBreaker</code>.
 * 
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class LineBreakerImpl implements LineBreaker {

	/**
	 * Creates a new Obejct 
	 */
	public LineBreakerImpl() {
		super();
	}

	/**
	 * @see de.dante.extex.typesetter.LineBreaker#breakLines(de.dante.extex.interpreter.type.node.HorizontalListNode, de.dante.extex.interpreter.context.Context)
	 */
	// TODO incomplete (very simple solution, only for test)
	public NodeList breakLines(HorizontalListNode nodes, Manager manager) {

		VerticalListNode vlnode = new VerticalListNode();

		// linebreak already done?
		if (!nodes.isLineBreak()) {

			Dimen hsize = manager.getContext().getDimen("hsize");
			if (nodes.getWidth().lt(hsize)) {
				vlnode.addSkip(new Glue(Dimen.ONE * 12));
				nodes.setLineBreak(true);
				vlnode.add(nodes);
				return vlnode;
			} else {
				// break lines TODO change
				NodeIterator it = nodes.iterator();
				Dimen line = Dimen.ZERO_PT;
				HorizontalListNode hnode = new HorizontalListNode();
				while (it.hasNext()) {
					Node node = it.next();
					line.add(node.getWidth());
					if (line.lt(hsize)) {
						hnode.add(node);
					} else {
						vlnode.addSkip(new Glue(Dimen.ONE * 12));
						vlnode.add(hnode);
						hnode = new HorizontalListNode();
						hnode.add(node);
						line = node.getWidth();
					}
				}
				if (!hnode.empty()) {
					vlnode.addSkip(new Glue(Dimen.ONE * 12));
					vlnode.add(hnode);
				}
			}
		} else {
			vlnode.addSkip(new Glue(Dimen.ONE * 12));
			vlnode.add(nodes);
		}
		return vlnode;
	}
}
