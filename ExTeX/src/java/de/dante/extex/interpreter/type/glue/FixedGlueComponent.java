/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.glue;

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.TokenFactory;
import de.dante.util.GeneralException;

/**
 * This interface describes the features of a
 * {@link de.dante.extex.interpreter.type.glue.GlueComponent GlueComponent}
 * which do not modify the value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface FixedGlueComponent {

    /**
     * Getter for the value in scaled points (sp).
     *
     * @return the value in internal units of scaled points (sp)
     */
    long getValue();

    /**
     * Getter for order.
     *
     * @return the order.
     */
    int getOrder();

    /**
     * Create a copy of this instance with the same order and value.
     *
     * @return a new copy of this instance
     */
    GlueComponent copy();

    /**
     * Determine the printable representation of the object.
     *
     * @return the printable representation
     *
     * @see #toString(StringBuffer)
     * @see #toToks(TokenFactory)
     */
    String toString();

    /**
     * Determine the printable representation of the object and append it to
     * the given StringBuffer.
     *
     * @param sb the output string buffer
     *
     * @see #toString()
     */
    void toString(final StringBuffer sb);

    /**
     * Determine the printable representation of the object and return it as a
     * list of Tokens.
     * The value returned is exactely the string which would be produced by
     * TeX to print the Dimen. This means the result is expressed in pt and
     * properly rounded to be read back in again without loss of information.
     *
     * @param factory the token factory to get the required tokens from
     *
     * @return the printable representation
     *
     * @throws GeneralException in case of an error
     *
     * @see "TeX -- The Program [103]"
     * @see #toToks(Tokens, TokenFactory)
     * @see #toString()
     * @see #toString(StringBuffer)
     */
    Tokens toToks(final TokenFactory factory)
            throws GeneralException;

    /**
     * Determine the printable representation of the object and return it as a
     * list of Tokens.
     * The value returned is exactely the string which would be produced by
     * TeX to print the Dimen. This means the result is expressed in pt and
     * properly rounded to be read back in again without loss of information.
     *
     * @param toks the tokens to append to
     * @param factory the token factory to get the required tokens from
     *
     * @throws GeneralException in case of an error
     *
     * @see "TeX -- The Program [103]"
     * @see #toToks(TokenFactory)
     * @see #toString()
     * @see #toString(StringBuffer)
     */
    void toToks(final Tokens toks, final TokenFactory factory)
            throws GeneralException;
}