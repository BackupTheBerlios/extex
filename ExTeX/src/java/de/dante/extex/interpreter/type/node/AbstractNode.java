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
package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.typesetter.Node;

/**
 * Abstract class for all <code>Nodes</code>.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public abstract class AbstractNode implements Node {

	/** 
	 * This is the depth of the node.
	 *  The depth is the extend of the node below the baseline.
	 *  A <code>null</code> value indicates
	 *  a running value which has to be set or computed later.
	 */
	private Dimen theDepth;

	/** 
	 * This is the height of the node.
	 *  The height is the extend of the node above the baseline.
	 *  A <code>null</code> value indicates
	 *  a running value which has to be set or computed later.
	 */
	private Dimen theHeight;

	/** 
	 * This is the width of the node.
	 *  The width is the extend of the node along the baseline.
	 *  <code>null</code> indicates
	 *  a running value which has to be set or computed later.
	 */
	private Dimen theWidth;

	/**
	 * Creates a new object.
	 * All dimensions (width, height, depth) are initially unset.
	 */
	public AbstractNode() {
		super();
		theWidth = new Dimen();
		theHeight = new Dimen();
		theDepth = new Dimen();
	}

	/**
	 * Creates a new object.
	 *
	 * @param width the width of the node; <code>null</code> denotes the
	 *            unset value
	 */
	public AbstractNode(final Dimen width) {
		super();
		theWidth = width;
		theHeight = new Dimen();
		theDepth = new Dimen();
	}

	/**
	 * Creates a new object.
	 *
	 * @param width the width of the node; <code>null</code> denotes the
	 *            unset value
	 * @param height the height of the node; <code>null</code> denotes the
	 *            unset value
	 * @param depth the depth of the node; <code>null</code> denotes the
	 *            unset value
	 */
	public AbstractNode(final Dimen width, final Dimen height, final Dimen depth) {
		super();
		theWidth = width;
		theHeight = height;
		theDepth = depth;
	}

	/**
	 * @see de.dante.extex.typesetter.Node#setDepth(de.dante.extex.interpreter.type.Dimen)
	 */
	public void setDepth(final Dimen depth) {
		theDepth.set(depth);
	}

	/**
	 * @see de.dante.extex.typesetter.Node#getDepth()
	 */
	public Dimen getDepth() {
		return new Dimen(theDepth);
	}

	/**
	 * @see de.dante.extex.typesetter.Node#setHeight(de.dante.extex.interpreter.type.Dimen)
	 */
	public void setHeight(final Dimen height) {
		theHeight.set(height);
	}

	/**
	 * @see de.dante.extex.typesetter.Node#getHeight()
	 */
	public Dimen getHeight() {
		return new Dimen(theHeight);
	}

	/**
	 * @see de.dante.extex.typesetter.Node#setWidth(de.dante.extex.interpreter.type.Dimen)
	 */
	public void setWidth(final Dimen width) {
		theWidth.set(width);
	}

	/**
	 * @see de.dante.extex.typesetter.Node#getWidth()
	 */
	public Dimen getWidth() {
		return new Dimen(theWidth);
	}

	/**
	 * ...
	 *
	 * @param prefix ...
	 *
	 * @return ...
	 */
	protected String toText(final String prefix) {
		StringBuffer sb = new StringBuffer();
		toText(sb, prefix);
		return sb.toString();
	}

	/**
	 * ...
	 *
	 * @param sb the output string buffer
	 * @param prefix ...
	 */
	public void toText(final StringBuffer sb, final String prefix) {
		sb.append(prefix);

		//TODO incomplete
	}

}
