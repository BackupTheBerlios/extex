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
package de.dante.extex.interpreter.context.impl;

import java.io.Serializable;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextExTeX;
import de.dante.extex.interpreter.type.Real;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * This is a reference implementation for an interpreter context with ExTeX-functions.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class ContextExTeXImpl extends ContextImpl implements Context, ContextExTeX, Serializable {

	/**
	 * This is the entry to the linked list of groups with ExTeX-functions. 
	 * The current group is the first one.
	 */
	protected GroupExTeX groupextex = null;

	/**
	 * Creates a new object.
	 */
	public ContextExTeXImpl(Configuration config) throws ConfigurationException {
		super(config);

		groupextex = (GroupExTeX) group; // TODO test with instanceof -> throw Exception
	}

	/**
	 * @see de.dante.extex.interpreter.context.ContextExTeX#getReal(java.lang.String)
	 */
	public Real getReal(String name) {
		return groupextex.getReal(name);
	}

	/**
	 * @see de.dante.extex.interpreter.context.ContextExTeX#setReal(java.lang.String, de.dante.extex.interpreter.type.Real, boolean)
	 */
	public void setReal(String name, Real value, boolean global) {
		groupextex.setReal(name, value, global);
	}

	/**
	 * @see de.dante.extex.interpreter.context.ContextExTeX#setReal(java.lang.String, de.dante.extex.interpreter.type.Real)
	 */
	public void setReal(String name, Real value) {
		groupextex.setReal(name, value);
	}
}
