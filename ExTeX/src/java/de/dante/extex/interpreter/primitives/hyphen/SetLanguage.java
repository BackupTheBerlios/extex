/*
 * Copyright (C) 2004 Michael Niedermair
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *  
 */
package de.dante.extex.interpreter.primitives.hyphen;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.node.LanguageNode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\setlanguage</code>.
 * <p>
 * Example:
 * 
 * <pre>
 *  \setlanguage<number>
 * </pre>
 * 
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class SetLanguage extends AbstractCode {

	/**
	 * Creates a new object.
	 * 
	 * @param name the name for debugging
	 */
	public SetLanguage(String name) {
		super(name);
	}

	/**
	 * Set the value for the currentlanguage.
	 * <p>
	 * It puts a <code>LanguageNode</code> (a <code>whatsit</code>)
	 * in the actual list from the <code>TypeSetter</code>.
	 *  
	 * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
	 *      de.dante.extex.interpreter.context.Context,
	 *      de.dante.extex.interpreter.TokenSource,
	 *      de.dante.extex.typesetter.Typesetter)
	 */
	public void execute(Flags prefix, Context context, TokenSource source, Typesetter typesetter) throws GeneralException {
		source.scanOptionalEquals();
		int index = (int)source.scanInteger();
		typesetter.add(new LanguageNode(index));
	}
}
