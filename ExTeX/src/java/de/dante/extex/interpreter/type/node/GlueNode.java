/*
 * Copyright (C) 2003-2004 The ExTeX Group
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

import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.typesetter.Discartable;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * Node for a glue.
 *
 * @see "TeX -- The Program [149]"
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class GlueNode extends AbstractNode implements Node, Discartable {

	/**
	 * The field <tt>theSize</tt> ...
	 */
	private Glue theSize;

	/**
	 * Creates a new object.
	 */
	public GlueNode() {
		super();
		theSize = null;
	}

	/**
	 * Creates a new object.
	 *
	 * @param size the actual size
	 */
	public GlueNode(final Glue size) {
		this(size,false);
	}

	/**
	 * Creates a new object.
	 *
	 * @param size 		the actual size
	 * @param vertical	if <code>true</code>, the size-length is used
	 * 	                fopr the hight, otherwise for the width. 
	 */
	public GlueNode(final Glue size, boolean vertical) {
		super();
		theSize = size;
		if (vertical) {
			setHeight(size.getLength());
		} else {
			setWidth(size.getLength());
		}
	}
	
	/**
	 * ...
	 *
	 * @return ...
	 * @see "TeX -- The Program [186]"
	 */
	public String toText() {
		return " ";
	}

	/**
	 * @see de.dante.extex.typesetter.Node#toText(java.lang.StringBuffer,
	 *      java.lang.String)
	 */
	public void toText(final StringBuffer sb, final String prefix) {
		sb.append(" ");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		toText(sb,"");
		return sb.toString();
	}

	/**
	 * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer)
	 */
	public void toString(final StringBuffer sb, String prefix) {
		sb.append("\\skip ");
		sb.append(theSize.toString());
		
		// TODO delete after test
		sb.append(" (" + theSize.toPT() + ")");
	}

	/**
	 * @see de.dante.extex.typesetter.Node#visit(de.dante.extex.typesetter.NodeVisitor,
	 *      java.lang.Object, java.lang.Object)
	 */
	public Object visit(final NodeVisitor visitor, final Object value, final Object value2) throws GeneralException {
		return visitor.visitGlue(value, value2);
	}
	
	/**
	 * @return Returns the size of the <code>GlueNode</code>.
	 */
	public Glue getSize() {
		return theSize;
	}
}
