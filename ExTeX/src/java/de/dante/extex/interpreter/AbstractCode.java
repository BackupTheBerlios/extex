/*
 * Copyright (C) 2003 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.interpreter;

import de.dante.extex.interpreter.context.*;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This is the abstract base class which can be used for all classes
 * implementing the interface Code. It provides some useful definitions for
 * most of the methods.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class AbstractCode implements Code {

	/**
	 * This variable contains the name of this code for debugging.
	 */
	private String name = "";

	/**
	 * Creates a new object.
	 */
	public AbstractCode(String name) {
		super();
		this.name = name;
	}

	/**
	 * @see de.dante.extex.interpreter.Code#isIf()
	 */
	public boolean isIf() {
		return false;
	}

	/**
	 * Setter for the name of this code instance. This information is primarily
	 * needed for debugging.
	 * 
	 * @param name
	 *                 the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see de.dante.extex.interpreter.Code#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
	 *         de.dante.extex.interpreter.context.Context,
	 *         de.dante.extex.interpreter.TokenSource,
	 *         de.dante.extex.typesetter.Typesetter)
	 */
	public void execute(Flags prefix, Context context, TokenSource source, Typesetter typesetter) throws GeneralException {
		expand(prefix, context, source, typesetter);
	}

	/**
	 * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags,
	 *         de.dante.extex.interpreter.context.Context,
	 *         de.dante.extex.interpreter.TokenSource,
	 *         de.dante.extex.typesetter.Typesetter)
	 */
	public void expand(Flags prefix, Context context, TokenSource source, Typesetter typesetter) throws GeneralException {
		prefix.clear();
	}

	/**
	 * @see de.dante.extex.interpreter.Code#getThe(de.dante.extex.interpreter.context.Context)
	 */
	public Tokens getThe(Context context, TokenSource source) throws GeneralException {
		// TODO unimplemented
		return null;
	}

	/**
	 * In general this method is simply a noop. Classes which need this feature
	 * can overwrite this method.
	 * 
	 * @see de.dante.extex.interpreter.Code#set(java.lang.String)
	 */
	public void set(Context context, String value) throws GeneralException {
	}
}
