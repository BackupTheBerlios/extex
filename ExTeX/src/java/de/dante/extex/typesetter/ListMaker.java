/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This interface describes the capabiliteis of a list maker.
 *
 * @see "TeX -- The Program [211]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.27 $
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
     * @param context the typesetter options mapping a fragment of the
     *  interpreter context
     *
     * @return the node list enclosed in this instance.
     *
     * @throws GeneralException in case of an error
     */
    NodeList close(TypesetterOptions context) throws GeneralException;

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
     * Notification method to deal the case that a left brace hs been
     * encountered.
     */
    void leftBrace();

    /**
     * Add a letter to the current list or treat it in some other appropriate
     * way.
     *
     * @param context the interpreter context
     * @param tc the typesetting context
     * @param uc the character
     *
     * @throws GeneralException in case of an error
     */
    void letter(Context context, TypesettingContext tc, UnicodeChar uc)
            throws GeneralException;

    /**
     * Treat a math shift character.
     * Usually this leads to entering or leaving math mode -- maybe after
     * inspection of a following token.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual math shift character token
     *
     * @throws GeneralException in case of an error
     */
    void mathShift(Context context, TokenSource source, Token t)
            throws GeneralException;

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
     * Notification method to deal the case that a right brace hs been
     * encountered.
     *
     * @throws GeneralException in case of an error
     */
    void rightBrace() throws GeneralException;

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

    /**
     * Treat a subscript mark. This might be meaningful in math mode only.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual sub mark token
     *
     * @throws GeneralException in case of an error
     */
    void subscriptMark(Context context, TokenSource source, Token t)
            throws GeneralException;

    /**
     * Treat a superscript mark. This might be meaningful in math mode only.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual super mark token
     *
     * @throws GeneralException in case of an error
     */
    void superscriptMark(Context context, TokenSource source, Token t)
            throws GeneralException;

    /**
     * Treat a alignment tab character.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual tab token
     *
     * @throws GeneralException in case of an error
     */
    void tab(Context context, TokenSource source, Token t)
            throws GeneralException;

}