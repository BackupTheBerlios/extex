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

package de.dante.extex.scanner.type.token;

import de.dante.extex.scanner.type.Catcode;
import de.dante.util.UnicodeChar;

/**
 * This is the interface for a token. A token is a pair of catcode and value.
 * In most cases the value is a single character. Nevertheless it is also
 * possible to store a complete string in the value &ndash; e.g. for control
 * sequence tokens.
 * <p>
 * Tokens are immutable. Thus no setters for the attributes are provided.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public interface Token {

    /**
     * Compare the current token with a pair of catcode and character value.
     * This pair constitutes a vitual token. They are the same if the catcode
     * and the value are the same.
     *
     * @param cc the catcode
     * @param c the value
     *
     * @return <code>true</code> iff the tokens are equal
     */
    boolean equals(Catcode cc, char c);

    /**
     * Compare the current token with a pair of catcode and String value. This
     * pair constitutes a vitual token. They are the same if the catcode and
     * the value are the same.
     *
     * @param cc the catcode
     * @param s the value
     *
     * @return <code>true</code> iff the tokens are equal
     */
    boolean equals(Catcode cc, String s);

    /**
     * Compare the current token with a character value. They are the same if
     * the values are the same.
     *
     * @param c the value
     *
     * @return <code>true</code> iff the tokens are equal
     */
    boolean equals(char c);

    /**
     * Compare the current token to another token. They are the same if the
     * catcode and the value are the same.
     *
     * @param t the token to compare to
     *
     * @return <code>true</code> iff the tokens are equal
     */
    boolean equals(Object t);

    /**
     * Getter for the catcode.
     *
     * @return the catcode
     */
    Catcode getCatcode();

    /**
     * Getter for the value.
     *
     * @return the value
     */
    UnicodeChar getChar();

    /**
     * Check if the current token has a specified catcode.
     *
     * @param cc the catcode to compare against
     *
     * @return <code>true</code> iff the catcodes coincide
     */
    boolean isa(Catcode cc);

    /**
     * Print the token into a StringBuffer.
     *
     * @param sb the target string buffer
     */
    void toString(StringBuffer sb);

    /**
     * This method returns the textual representation for the Token.
     * This textual representation might not contain the full information but
     * can be used as an abbreviated form to be shown to the end user.
     * A representation with more complete information can be received with the
     * method {@link java.lang.Object#toString() toString()}.
     *
     * @return the textual representation
     */
    String toText();

    /**
     * Return the printable representation of this token as it can be read back
     * in.
     *
     * @param esc the escape character
     *
     * @return the printable representation
     */
    String toText(UnicodeChar esc);

    /**
     * Invoke the appropriate visit method for the current class.
     * @param visitor the calling visitor
     * @param arg1 the first argument to pass
     *
     * @return the result object
     *
     * @throws Exception in case of an error
     */
    Object visit(TokenVisitor visitor, Object arg1) throws Exception;

}
