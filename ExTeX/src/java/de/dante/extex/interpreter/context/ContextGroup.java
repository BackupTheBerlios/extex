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

package de.dante.extex.interpreter.context;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.group.GroupInfo;
import de.dante.extex.interpreter.context.group.GroupType;
import de.dante.extex.interpreter.context.observer.group.AfterGroupObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.Locator;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This interface describes the container for group-related of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public interface ContextGroup {

    /**
     * Register a observer to be called at the end of the group. The end of the
     * group is reached when the group is closed.
     *
     * @param observer the observer to register
     *
     * @see #closeGroup(Typesetter, TokenSource)
     */
    void afterGroup(AfterGroupObserver observer);

    /**
     * Add a token to the tokens inserted after the group has been closed.
     *
     * @param t the token to add
     *
     * @throws InterpreterException in case of an error
     *
     * @see #closeGroup(Typesetter, TokenSource)
     */
    void afterGroup(Token t) throws InterpreterException;

    /**
     * Perform all actions required upon the closing of a group.
     *
     * @param typesetter the typesetter to invoke if needed
     * @param source the source to get Tokens from if needed
     *
     * @throws InterpreterException in case of an error
     *
     * @see #openGroup(GroupType, Locator, Token)
     */
    void closeGroup(Typesetter typesetter, TokenSource source)
            throws InterpreterException;

    /**
     * Getter for the array of group information describing the currently open
     * groups. The elements represent the groups in ascending order. Thus the
     * element 0 always represents the global group. This one is guaranteed to be
     * present. This means that the arras has always at least one element.
     *
     * @return the array of group infos
     *
     * @see #getGroupLevel()
     */
    GroupInfo[] getGroupInfos();

    /**
     * Getter for the group level. The group level is the number of groups which
     * are currently open. Thus this number of groups can be closed.
     *
     * @return the group level
     *
     * @see #getGroupInfos()
     */
    long getGroupLevel();

    /**
     * Getter for the group type.
     *
     * @return the group type
     *
     * @see #openGroup(GroupType, Locator, Token)
     */
    GroupType getGroupType();

    /**
     * Test whether this group is the first one, which means that there is no
     * group before and closing this group would fail.
     *
     * @return <code>true</code> iff this is the first group
     *
     * @see #openGroup(GroupType, Locator, Token)
     */
    boolean isGlobalGroup();

    /**
     * This method can be used to open another group. The current group is
     * pushed onto the stack to be reactivated when the new group will be
     * closed.
     *
     * @param id the type of the group
     * @param locator the locator for the start
     * @param start the token which started the group
     *
     * @throws ConfigurationException in case of an error in the configuration,
     *             e.g. the class for the group can not be determined.
     * @throws InterpreterException in case of an error
     *
     * @see #closeGroup(Typesetter, TokenSource)
     */
    void openGroup(GroupType id, Locator locator, Token start)
            throws ConfigurationException,
                InterpreterException;

}
