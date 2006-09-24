/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.exception.InvalidSpacefactorException;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.exception.TypesetterUnsupportedException;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This interface describes the capabilities of a list maker.
 *
 * @see "<logo>TeX</logo> &ndash; The Program [211]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.52 $
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
     * @throws ConfigurationException in case of a configuration error
     */
    void add(Node node) throws TypesetterException, ConfigurationException;

    /**
     * Add a node list to the current list maker and adjust the spacing between
     * the elements of the list.
     *
     * @param list the list
     * @param options the options to use
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of a configuration error
     */
    void addAndAdjust(NodeList list, TypesetterOptions options)
            throws TypesetterException,
                ConfigurationException;

    /**
     * Add a glue node to the list.
     *
     * @param g the glue to add
     *
     * @throws TypesetterException in case of an error
     */
    void add(FixedGlue g) throws TypesetterException;

    /**
     * Add a space node to the list.
     *
     * @param typesettingContext the typesetting context for the space
     * @param spacefactor the space factor to use for this space or
     *  <code>null</code> to indicate that the default space factor should
     *  be used.
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of a configuration error
     */
    void addSpace(TypesettingContext typesettingContext, Count spacefactor)
            throws TypesetterException,
                ConfigurationException;

    /**
     * Register an observer to be invoked at the end of the paragraph.
     *
     * @param observer the observer to register
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
     * @return the node list enclosed in this instance
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of a configuration error
     */
    NodeList complete(TypesetterOptions context)
            throws TypesetterException,
                ConfigurationException;

    /**
     * Process a carriage return.
     *
     * @param context the interpreter context
     * @param tc the typesetting context
     * @param uc the character
     *
     * @throws TypesetterException in case of an error
     */
    void cr(Context context, TypesettingContext tc, UnicodeChar uc)
            throws TypesetterException;

    /**
     * Access the last node on the list.
     *
     * @return the last node in the current list or <code>null</code> if the
     *   list is empty
     */
    Node getLastNode();

    /**
     * Getter for the locator.
     *
     * @return the locator
     */
    Locator getLocator();

    /**
     * Getter for the current mode.
     *
     * @return the mode which is one of the values defined in
     * {@link de.dante.extex.typesetter.Mode Mode}.
     */
    Mode getMode();

    /**
     * Getter for the previous depth parameter.
     *
     * @return the previous depth
     *
     * @throws TypesetterUnsupportedException in case of an error
     */
    FixedDimen getPrevDepth() throws TypesetterUnsupportedException;

    /**
     * Getter for the space factor.
     *
     * @return the space factor
     *
     * @throws TypesetterUnsupportedException in case of an error
     */
    long getSpacefactor() throws TypesetterUnsupportedException;

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
     * @param locator the locator
     *
     * @return <code>true</code> iff the character has been discarded because
     *   it is not defined in the current font.
     *
     * @throws TypesetterException in case of an error
     */
    boolean letter(Context context, TypesettingContext tc, UnicodeChar uc,
            Locator locator) throws TypesetterException;

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
     * @throws ConfigurationException in case of a configuration error
     */
    void mathShift(Context context, TokenSource source, Token t)
            throws TypesetterException,
                ConfigurationException;

    /**
     * Emit a new paragraph.
     * This might be a noop under certain circumstances.
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of a configuration error
     */
    void par() throws TypesetterException, ConfigurationException;

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
     * @throws TypesetterUnsupportedException in case of an error
     */
    void setPrevDepth(FixedDimen pd) throws TypesetterUnsupportedException;

    /**
     * Setter for the space factor.
     *
     * @param sf the space factor to set
     *
     * @throws TypesetterUnsupportedException in case of an error
     * @throws InvalidSpacefactorException in case of an invalid space factor
     */
    void setSpacefactor(FixedCount sf)
            throws TypesetterUnsupportedException,
                InvalidSpacefactorException;

    /**
     * Print the status for <tt>\showlists</tt>.
     *
     * @param sb the target buffer
     * @param depth the depth of the list display
     * @param breadth the breadth of the list display
     */
    void showlist(StringBuffer sb, long depth, long breadth);

    /**
     * Treat a subscript mark. This might be meaningful in math mode only.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param t the actual sub mark token
     *
     * @throws TypesetterException in case of an error
     */
    void subscriptMark(Context context, TokenSource source,
            Typesetter typesetter, Token t) throws TypesetterException;

    /**
     * Treat a superscript mark. This might be meaningful in math mode only.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param t the actual super mark token
     *
     * @throws TypesetterException in case of an error
     */
    void superscriptMark(Context context, TokenSource source,
            Typesetter typesetter, Token t) throws TypesetterException;

    /**
     * Treat a alignment tab character.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param t the actual tab token
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of a configuration error
     */
    void tab(Context context, TokenSource source, Token t)
            throws TypesetterException,
                ConfigurationException;

}