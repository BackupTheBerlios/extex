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
import de.dante.extex.interpreter.type.Glyph;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @see "TeX -- The Program [134]"
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class CharNode extends AbstractNode implements Node {

	/**
	 * The field <tt>character</tt> contains the single character represented 
	 * by this node.
	 */
	private UnicodeChar character;

	/**
	 * The field <tt>typesettingContext</tt> contains the typesetting context
	 */
	private TypesettingContext typesettingContext;

	/**
	 * Creates a new object.
	 *
	 * @param context the typesetting context
	 * @param uc the unicode character
	 */
	public CharNode(final TypesettingContext context, final UnicodeChar uc) {
		super();
		typesettingContext = context;
		character = uc;
		Glyph glyph = context.getFont().getGlyph(uc);
		setWidth(glyph.getWidth());
		setHeight(glyph.getHeight());
		setDepth(glyph.getDepth());
	}

	/**
	 * ...
	 *
	 * @return ...
	 */
	public int getSpaceFactor() {
		return 0; // TODO incomplete
	}

	/**
	 * @see de.dante.extex.typesetter.Node#toText(java.lang.StringBuffer,
	 *      java.lang.String)
	 */
	public void toText(final StringBuffer sb, final String prefix) {
		sb.append(character.toString());
	}

	/**
	 * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer)
	 */
	public void toString(final StringBuffer sb, final String prefix) {
		sb.append('\\');
		sb.append(typesettingContext.getFont().getFontName());
		sb.append(' ');
		sb.append(character.toString());
		sb.append(" (");
		sb.append(getHeight().toString());
		sb.append("+");
		sb.append(getDepth().toString());
		sb.append(")x");
		sb.append(getWidth().toString());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return character.toString();
	}

	/**
	 * @see de.dante.extex.typesetter.Node#visit(de.dante.extex.typesetter.NodeVisitor,
	 *      java.lang.Object, java.lang.Object)
	 */
	public Object visit(final NodeVisitor visitor, final Object value, final Object value2) throws GeneralException {
		return visitor.visitChar(value, value2);
	}

	/**
	 * @see de.dante.extex.typesetter.Node#getType()
	 */
	public String getType() {
		return "char";
	}

}
