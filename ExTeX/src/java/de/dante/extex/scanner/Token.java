/*
 * Copyright (C) 2003-2004  Gerd Neugebauer, Michael Niedermair
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
 * This is the interface for a token. A token is a pair of catcode and value.
 * In most cases the value is a single character. Nevertheless it is also
 * possible to store a complete string in the value -- e.g. for control
 * sequence tokens.
 * <p>
 * Tokens are immutable. Thus no setters for the attributes are provided.
 * </p>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
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
    public abstract UnicodeChar getChar();
    
    /**
     * Compare the current token to another token. They are the same if the
     * catcode and the value are the same.
     * 
     * @param t the token to compare to
     * 
     * @return <code>true</code> iff the tokens are equal
     */
    public abstract boolean equals(Token t);

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
    public abstract boolean equals(Catcode cc, String s);

    /**
     * Compare the current token with a character value. They are the same if
     * the values are the same.
     * 
     * @param c the value
     * 
     * @return <code>true</code> iff the tokens are equal
     */
    public abstract boolean equals(char c);

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
    public abstract boolean equals(Catcode cc, char c);

    /**
     * Check if the current token has a specified catcode.
     * 
     * @param cc the catcode to compare against
     * 
     * @return <code>true</code> iff the catcodes coincide
     */
    public abstract boolean isa(Catcode cc);

    /**
     * This method retruns the textual representation for the Token.
     * This textual representation might not contain the full information but
     * can be used as an abbreviated form to be shown to the end user.
     * A representation with more complete information can be received with the
     * method {@link #toString() toString()}.
     * 
     * @return the textual representation
     */
    public abstract String toText();
}
