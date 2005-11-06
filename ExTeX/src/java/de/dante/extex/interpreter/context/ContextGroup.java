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

package de.dante.extex.interpreter.context;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.observer.group.AfterGroupObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public interface ContextGroup {

    /**
     * Register a observer to be called at the end of the group.
     *
     * @param observer the observer to register
     */
    void afterGroup(AfterGroupObserver observer);

    /**
     * Add a token to the tokens inserted after the group has been closed.
     *
     * @param t the token to add
     *
     * @throws InterpreterException in case of an error
     */
    void afterGroup(Token t) throws InterpreterException;

    /**
     * Perform all actions required upon the closing of a group.
     *
     * @param typesetter the typesetter to invoke if needed
     * @param source the source to get Tokens from if needed
     *
     * @throws InterpreterException in case of an error
     */
    void closeGroup(Typesetter typesetter, TokenSource source)
            throws InterpreterException;

    /**
     * Getter for the group level. The group level is the number of groups which
     * are currently open. Thus this number of groups can be closed.
     *
     * @return the group level
     */
    long getGroupLevel();

    /**
     * Test whether this group is the first one, which means that there is no
     * group before and closing this group would fail.
     *
     * @return <code>true</code> iff this is the first group
     */
    boolean isGlobalGroup();

    /**
     * This method can be used to open another group. The current group is
     * pushed onto the stack to be reactivated when the new group will be
     * closed.
     *
     * @throws ConfigurationException in case of an error in the configuration,
     *             e.g. the class for the group can not be determined.
     */
    void openGroup() throws ConfigurationException, InterpreterException;

}