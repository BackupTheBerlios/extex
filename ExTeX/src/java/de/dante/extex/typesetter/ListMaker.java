/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.typesetter;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @see "TeX -- The Program [211]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public interface ListMaker {

    /**
     * ...
     *
     * @param node ...
     *
     * @throws GeneralException in case of an error
     */
    void add(Node node) throws GeneralException;

    /**
     * ...
     * 
     * @param typesettingContext ...
     * @param symbol ...
     *
     * @throws GeneralException in case of an error
     */
    void add(TypesettingContext typesettingContext, UnicodeChar symbol)
        throws GeneralException;

    /**
     * ...
     *
     * @param typesettingContext ...
     * @param spacefactor ...
     *
     * @throws GeneralException in case of an error
     */
    void addSpace(TypesettingContext typesettingContext, Count spacefactor)
        throws GeneralException;

    /**
     * ...
     *
     * @param g the glue to add
     *
     * @throws GeneralException in case of an error
     */
    void addGlue(Glue g) throws GeneralException;

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    void par() throws GeneralException;

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    void toggleMath() throws GeneralException;

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    void toggleDisplaymath() throws GeneralException;

    /**
     * Setter for the space factor.
     *
     * @param sf the space factor to set
     *
     * @throws GeneralException in case of an error
     */
    void setSpacefactor(Count sf) throws GeneralException;

    /**
     * ...
     *
     * @param pd the prec depth parameter
     *
     * @throws GeneralException in case of an error
     */
    void setPrevDepth(Dimen pd) throws GeneralException;

    /**
     * Getter for the current mode.
     *
     * @return the mode
     */
    Mode getMode();

    /**
     * ...
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    NodeList close() throws GeneralException;

}
