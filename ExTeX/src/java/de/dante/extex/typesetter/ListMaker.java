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
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.UnicodeChar;

/**
 * This interface describes the capabilities of a list maker.
 *
 * @see "TeX -- The Program [211]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.35 $
 */
public interface ListMaker {

    /**
     * Add an arbitrary node to the internal list of nodes gathered so far.
     * The node should not be one of the special nodes treated by methods of
     * their own.
     *
     * @param node the node to add
     *
     * @throws TypesetterException in case of an error
     */
    void add(Node node) throws TypesetterException;

    /**
     * Add a glue node to the list.
     *
     * @param g the glue to add
     *
     * @throws TypesetterException in case of an error
     */
    void addGlue(Glue g) throws TypesetterException;

    /**
     * Add a space node to the list.
     *
     * @param typesettingContext the typesetting context for the space
     * @param spacefactor the space factor to use for this space or
     * <code>null</code> to indicate that the default space factor should
     * be used.
     *
     * @throws TypesetterException in case of an error
     */
    void addSpace(TypesettingContext typesettingContext, Count spacefactor)
            throws TypesetterException;

    /**
     * TODO gene: missing JavaDoc
     *
     * @param observer ...
     */
    void afterParagraph(ParagraphObserver observer);

    /**
     * Close the node list. This means that everything is done to ship the
     * closed node list to the document writer. Nevertheless the invoking
     * application might decide not to modify the node list and continue
     * processing. In the other case some  nodes might be taken from the node
     * list returned by this method. Then the processing has to continue with
     * the reduced node list.
     *
     * @param context the typesetter options mapping a fragment of the
     *  interpreter context
     *
     * @return the node list enclosed in this instance.
     *
     * @throws TypesetterException in case of an error
     */
    NodeList complete(TypesetterOptions context) throws TypesetterException;

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
     * Notification method to deal the case that a left brace has been
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
     * @throws TypesetterException in case of an error
     */
    void letter(Context context, TypesettingContext tc, UnicodeChar uc)
            throws TypesetterException;

    /**
     * Treat a math shift character.
     * Usually this leads to entering or leaving math mode &ndash; maybe after
     * inspection of a following token.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual math shift character token
     *
     * @throws TypesetterException in case of an error
     */
    void mathShift(Context context, TokenSource source, Token t)
            throws TypesetterException;

    /**
     * Emit a new paragraph.
     * This might be a noop under certain circumstances.
     *
     * @throws TypesetterException in case of an error
     */
    void par() throws TypesetterException;

    /**
     * Removes the last node from the list.
     * If the list is empty then nothing is done.
     */
    void removeLastNode();

    /**
     * Notification method to deal the case that a right brace has been
     * encountered.
     *
     * @throws TypesetterException in case of an error
     */
    void rightBrace() throws TypesetterException;

    /**
     * Setter for the previous depth parameter.
     *
     * @param pd the previous depth parameter
     *
     * @throws TypesetterException in case of an error
     */
    void setPrevDepth(Dimen pd) throws TypesetterException;

    /**
     * Setter for the space factor.
     *
     * @param sf the space factor to set
     *
     * @throws TypesetterException in case of an error
     */
    void setSpacefactor(Count sf) throws TypesetterException;

    /**
     * Treat a subscript mark. This might be meaningful in math mode only.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual sub mark token
     *
     * @throws TypesetterException in case of an error
     */
    void subscriptMark(Context context, TokenSource source, Token t)
            throws TypesetterException;

    /**
     * Treat a superscript mark. This might be meaningful in math mode only.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual super mark token
     *
     * @throws TypesetterException in case of an error
     */
    void superscriptMark(Context context, TokenSource source, Token t)
            throws TypesetterException;

    /**
     * Treat a alignment tab character.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual tab token
     *
     * @throws TypesetterException in case of an error
     */
    void tab(Context context, TokenSource source, Token t)
            throws TypesetterException;

}