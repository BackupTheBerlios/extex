/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @see "TeX -- The Program [138]"
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class RuleNode extends AbstractNode implements Node {

	/**
	 * The field <tt>context</tt> ...
	 */
	private TypesettingContext context;

	/**
	 * Creates a new object.
	 * 
	 * @see "TeX -- The Program [139]"
	 */
	public RuleNode(final Dimen width, final Dimen height, final Dimen depth, final TypesettingContext context) {
		super(width, height, depth);
		this.context = context;
	}

	/**
	 * ...
	 *
	 * @return ...
	 * @see "TeX -- The Program [187]"
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		toString(sb, "");
		return sb.toString();
	}

	/**
	 * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer)
	 * @see "TeX -- The Program [187]"
	 */
	public void toString(final StringBuffer sb, String prefix) {
		sb.append("rule(");

		Dimen x = getHeight();

		if (x == null) {
			sb.append("*");
		} else {
			x.toString(sb);
		}

		sb.append("+");
		x = getDepth();

		if (x == null) {
			sb.append("*");
		} else {
			x.toString(sb);
		}

		sb.append(")x");
		x = getWidth();

		if (x == null) {
			sb.append("*");
		} else {
			x.toString(sb);
		}

		sb.append(")");
	}

	/**
	 * @see de.dante.extex.typesetter.Node#visit(de.dante.extex.typesetter.NodeVisitor,
	 *      java.lang.Object, java.lang.Object)
	 */
	public Object visit(final NodeVisitor visitor, final Object value, final Object value2) throws GeneralException {
		return visitor.visitRule(value, value2);
	}
}
