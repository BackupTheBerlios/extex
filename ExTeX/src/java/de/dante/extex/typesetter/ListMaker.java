/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.typesetter;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This interface describes the capabiliteis of a list maker.
 *
 * @see "TeX -- The Program [211]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
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
     * Add an arbitrary Noad to the internal list if it is prepared to hold one.
     * This is usually the case in math modes. In other modes an exception
     * should be thrown.
     *
     * @param noad the noad to add
     *
     * @throws GeneralException in case of an error
     */
    void add(Noad noad) throws GeneralException;

    /**
     * Add a glue node to the list.
     *
     * @param g the glue to add
     *
     * @throws GeneralException in case of an error
     */
    void addGlue(Glue g) throws GeneralException;

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
     * Close the node list.
     *
     * @return the node list enclosed in this instance.
     *
     * @throws GeneralException in case of an error
     */
    NodeList close() throws GeneralException;

    /**
     * Access the last node on the list.
     *
     * @return the last node in the current list or <code>null</code> if the
     *   list is empty
     */
    Node getLastNode();

    /**
     * Getter for the current mode.
     *
     * @return the mode which is one of the values defined in
     * {@link de.dante.extex.typesetter.Mode Mode}.
     */
    Mode getMode();

    /**
     * Emit a new paragraph.
     * This might be a noop under certain circumstances.
     *
     * @throws GeneralException in case of an error
     */
    void par() throws GeneralException;

    /**
     * Removes the last node from the list.
     * If the list is empty then nothing is done.
     */
    void removeLastNode();

    /**
     * Setter for the previous depth parameter.
     *
     * @param pd the prec depth parameter
     *
     * @throws GeneralException in case of an error
     */
    void setPrevDepth(Dimen pd) throws GeneralException;

    /**
     * Setter for the space factor.
     *
     * @param sf the space factor to set
     *
     * @throws GeneralException in case of an error
     */
    void setSpacefactor(Count sf) throws GeneralException;

    void treatLetter(TypesettingContext context, UnicodeChar uc) throws GeneralException;

    void treatTabMark(TypesettingContext context, Token t) throws GeneralException;

    void treatSubMark(TypesettingContext context, Token t) throws GeneralException;

    void treatSupMark(TypesettingContext context, Token t) throws GeneralException;

    void treatMathShift(Token t, TokenSource source) throws GeneralException;

}
