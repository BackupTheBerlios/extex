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
package de.dante.extex.scanner;

import de.dante.util.UnicodeChar;

/**
 * This is the interface for the token factory.
 * The token factory is the only instance to deliver new tokens.
 * It is up to the implementation of the factory to create new tokens or to
 * cache some of them and deliver the same token several times.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public interface TokenFactory {

    /**
     * Get an instance of a token with a given Catcode and value.
     *
     * @param code the catcode
     * @param value the value
     *
     * @return the appropriate token
     */
    Token newInstance(Catcode code, String value) throws CatcodeException;

    /**
     * Get an instance of a token with a given Catcode and character value.
     *
     * @param code the catcode
     * @param c the character value
     *
     * @return the appropriate token
     */
    Token newInstance(Catcode code, char c) throws CatcodeException;

    /**
     * Get an instance of a token with a given Catcode and Unicode character
     * value.
     *
     * @param code the catcode
     * @param c the Unicode character value
     *
     * @return the appropriate token
     */
    Token newInstance(Catcode code, UnicodeChar c) throws CatcodeException;

}
