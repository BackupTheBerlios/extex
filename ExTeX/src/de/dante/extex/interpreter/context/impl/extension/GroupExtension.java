/*
 * Copyright (C) 2004 The ExTeX Group
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
package de.dante.extex.interpreter.context.impl.extension;

import java.io.Serializable;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.impl.Group;
import de.dante.extex.interpreter.type.Real;

/**
 * This is the implementation of a group object with ExTeX-functions.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public interface GroupExtension extends Group, Tokenizer, Serializable {

	/**
	 * Setter for the real register in the current group.
	 * 
	 * @param name the name of the register
	 * @param value the value of the register
	 */
	public abstract void setReal(String name, Real value);

	/**
	 * Setter for a real register in the requested groups.
	 * 
	 * @param name the name of the real register
	 * @param value the value of the real register
	 * @param global the indicator for the scope; <code>true</code> means all
	 *            groups; otherwise the current group is affected only
	 */
	public abstract void setReal(String name, Real value, boolean global);

	/**
	 * Getter for the named real register in the current group. The name can
	 * either be a string representing a number or an arbitrary string. In the
	 * first case the behavior of the numbered real registers is emulated. The
	 * other case can be used to store special real values.
	 * 
	 * As a default value 0 is returned.
	 * 
	 * @param name the name of the real register
	 * 
	 * @return the value of the real register or its default
	 */
	public abstract Real getReal(String name);

}
