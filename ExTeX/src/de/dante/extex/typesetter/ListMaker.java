/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @see "TeX -- The Program [211]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public interface ListMaker {

    /**
     * Add an arbitrary node to the internal list of nodes gathered so far.
     * The node should not be one of the special nodes treated by methods of
     * their own.
     *
     * @param node the node to add
     *
     * @throws GeneralException in case of an error
     */
    void add(Node node) throws GeneralException;

    /**
     * Add a character node to the list.
     *
     * @param typesettingContext the typesetting context for the symbol
     * @param symbol the symbol to add
     *
     * @throws GeneralException in case of an error
     */
    void add(TypesettingContext typesettingContext, UnicodeChar symbol)
        throws GeneralException;

    /**
     * Add a space node to the list.
     *
     * @param typesettingContext the typesetting context for the space
     * @param spacefactor the spacefactor to use for this space or
     * <code>null</code> to indicate that the default speacefactor should
     * be used.
     *
     * @throws GeneralException in case of an error
     */
    void addSpace(TypesettingContext typesettingContext, Count spacefactor)
        throws GeneralException;

    /**
     * Add a glue node to the list.
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
     * Toggle the math mode.
     *
     * @throws GeneralException in case of an error
     */
    void toggleMath() throws GeneralException;

    /**
     * Toggle the displaymath mode.
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
     * Setter for the previous depth parameter.
     *
     * @param pd the prec depth parameter
     *
     * @throws GeneralException in case of an error
     */
    void setPrevDepth(Dimen pd) throws GeneralException;

    /**
     * Getter for the current mode.
     *
     * @return the mode which is one of the values defined in
     * {@link de.dante.extex.typesetter.Mode Mode}.
     */
    Mode getMode();

    /**
     * Close the node list.
     *
     * @return the node list enclosed in this instance.
     *
     * @throws GeneralException in case of an error
     */
    NodeList close() throws GeneralException;

}
