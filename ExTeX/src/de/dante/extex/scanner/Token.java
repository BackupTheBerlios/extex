/*
 * Copyright (C) 2003  Gerd Neugebauer
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


/**
 * This is the interface for a token. A token is a pair of catcode and value.
 * In most cases the value is a single character. Nevertheless it is also
 * possible to store a complete string in the value -- e.g. for control sequence
 * tokens.
 * <p>
 * Tokens are immutable. Thus no setters for the attributes are provided.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface Token {
    /**
     * Getter for the catcode.
     *
     * @return the catcode
     */
    public abstract Catcode getCatcode();

    /**
     * Getter for the value.
     *
     * @return the value
     */
    public abstract String getValue();

	/**
	 * Getter for the value.
	 *
	 * @return the value
	 */
	//public abstract UnicodeChar getCharValue();

    /**
     * Compare the current token to another token.
     * They are the same if the catcode and the value are the same.
     *
     * @param t the token to compare to
     *
     * @return <code>true</code> iff the tokens are equal
     */
    public abstract boolean equals(Token t);

    /**
     * Compare the current token with a pair of catcode and String value.
     * This pair constitutes a vitual token.
     * They are the same if the catcode and the value are the same.
     *
     * @param cc the catcode
     * @param s the value
     *
     * @return <code>true</code> iff the tokens are equal
     */
    public abstract boolean equals(Catcode cc, String s);

    /**
     * Compare the current token with a pair of catcode and character value.
     * This pair constitutes a vitual token.
     * They are the same if the catcode and the value are the same.
     *
     * @param cc the catcode
     * @param c the value
     *
     * @return <code>true</code> iff the tokens are equal
     */
    public abstract boolean equals(Catcode cc, char c);

    /**
     * Check if the current token has a specified catcode.
     *
     * @param cc the catcode to compare against
     *
     * @return <code>true</code> iff the catcodes coincide
     */
    public abstract boolean isa(Catcode cc);
}
