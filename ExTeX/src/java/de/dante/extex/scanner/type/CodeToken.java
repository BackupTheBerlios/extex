/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.scanner.type;

/**
 * The code token extends the {@link de.dante.extex.scanner.type.Token Token}
 * with the ability to retrieve a namespace.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface CodeToken extends Token {

    /**
     * Getter for the namespace.
     *
     * @return the namespace
     */
    String getNamespace();

    /**
     * Create a new instance of the token where the namespace is the default
     * namespace and the other attributes are the same as for the current token.
     *
     * @return the new token
     */
    CodeToken cloneInDefaultNamespace();

    /**
     * Create a new instance of the token where the namespace is the given one
     * and the other attributes are the same as for the current token.
     *
     * @param namespace the namespace to use
     *
     * @return the new token
     */
    CodeToken cloneInNamespace(final String namespace);

    /**
     * Getter for the name.
     * The name is the string representation without the escape character
     * in front.
     *
     * @return the name of the token
     */
    public String getName();

}