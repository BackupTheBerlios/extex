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
package de.dante.extex.typesetter.impl;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.ListMaker;
import de.dante.util.GeneralException;

/**
 * Interface for the Manager of a typesetter.
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public interface Manager {

	/**
	 * ...
	 *
	 * @throws GeneralException ...
	 */
	void pop() throws GeneralException;

	/**
	 * ...
	 *
	 * @param listMaker ...
	 *
	 * @throws GeneralException ...
	 */
	void push(ListMaker listMaker) throws GeneralException;

	/**
	 * ...
	 *
	 * @throws GeneralException ...
	 */
	void closeTopList() throws GeneralException;

	/**
	 * ...
	 *
	 * @return ...
	 */
	CharNodeFactory getCharNodeFactory();

	/**
	 * ...
	 *
	 * @return ...
	 */
	DocumentWriter getDocumentWriter();

	/**
	 * Getter for the context.
	 * 
	 * @return ...
	 */
	Context getContext();

}
