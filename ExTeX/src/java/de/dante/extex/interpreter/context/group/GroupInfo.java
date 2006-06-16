/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context.group;

import de.dante.extex.scanner.type.token.Token;
import de.dante.util.Locator;

/**
 * This interface provides access to the info for some group.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface GroupInfo {

    /**
     * Getter for the starting token of the group.
     * This value is null for the global group.
     *
     * @return the token which started the group
     */
    Token getGroupStart();

    /**
     * Getter for the group type.
     *
     * @return the group type
     */
    GroupType getGroupType();

    /**
     * Getter for the locator describing where the group started.
     * This value can be null for the global group.
     *
     * @return the locator
     */
    Locator getLocator();

}
