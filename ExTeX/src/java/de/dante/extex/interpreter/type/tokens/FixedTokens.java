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

package de.dante.extex.interpreter.type.tokens;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.type.Token;
import de.dante.util.exception.GeneralException;

/**
 * This interface describes the features of a
 * {@link de.dante.extex.interpreter.type.tokens.Tokens Tokens}
 * which do not modify the value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface FixedTokens {

    /**
     * Get a specified token from the toks register.
     *
     * @param i the index for the token to get
     *
     * @return the i <sup>th</sup> token or <code>null</code> if i is out
     *         of bounds
     */
    Token get(int i);

    /**
     * Getter for the length of the toks register, this is the number of
     * elements contained.
     *
     * @return the number of elements in the toks register
     */
    int length();

    /**
     * Determine the printable representation of the object and append it to a
     * list of Tokens.
     *
     * @param context  the processor context
     * @param toks the tokens to add to
     *
     * @throws GeneralException in case of an error
     */
    void show(final Context context, final Tokens toks) throws GeneralException;

    /**
     * Return a String, which shows all tokens in the list.
     *
     * @return a String, which show all tokens in the list
     */
    String toString();

    /**
     * Return a String, which shows all tokens (in text format) in the list.
     *
     * @return a String, which show all tokens (in text format) in the list
     */
    String toText();

}