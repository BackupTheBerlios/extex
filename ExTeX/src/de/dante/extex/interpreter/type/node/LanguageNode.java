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
package de.dante.extex.interpreter.type.node;

import de.dante.extex.typesetter.Node;

/**
 * Language-whatsis-node (set with <code>\setlanguge</code>
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class LanguageNode extends WhatsItNode implements Node {

	/**
	 *	the language
	 */
	private int language;

	/**
	 * Creates a new object.
	 */
	public LanguageNode(final int language) {
		super();
		this.language = language;
	}

	/**
	 * Getter for language
	 */
	public int getLanguage() {
		return language;
	}

	/**
	 * @see de.dante.extex.typesetter.Node#getType()
	 */
	public String getType() {
		return "language";
	}

}
