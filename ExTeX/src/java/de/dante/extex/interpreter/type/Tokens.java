/*
 * Copyright (C) 2003 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.interpreter.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.util.GeneralException;

/**
 * A Buffer for Tokens.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class Tokens implements Serializable {

	/**
	 * This constant is the empty toks register
	 */
	public static final Tokens EMPTY = new Tokens();

	/**
	 * The internal list of tokens
	 */
	private List tokens = new ArrayList();

	/**
	 * Creates a new object which does not contain any elements.
	 */
	public Tokens() {
		super();
	}

	/**
	 * Creates a new object
	 * <p>
	 * Each character of the string a convert in a <code>OtherToken</code>
	 * and add to the internal list
	 * 
	 * @param context
	 *                 the Context
	 * @param s
	 *                 the <code>String</code> to add
	 * @throws GeneralException,
	 *                 if a error throws in the factory
	 */
	public Tokens(Context context, String s) throws GeneralException {
		this();
		if (s != null && s.length() > 0) {
			TokenFactory factory = context.getTokenFactory();
			for (int i = 0; i < s.length(); i++) {
				add(factory.newInstance(Catcode.OTHER, s.charAt(i)));
			}
		}
	}

	/**
	 * Add another token to the end of the Tokens.
	 * 
	 * @param t
	 *                 The token to add
	 */
	public void add(Token t) {
		tokens.add(t);
	}

	/**
	 * Get a specified token from the toks register.
	 * 
	 * @param i
	 *                 the index for the token to get
	 * 
	 * @return the i <sup>th</sup> token or <code>null</code> if i is out
	 *            of bounds
	 */
	public Token get(int i) {
		return (i >= 0 && i < tokens.size() ? (Token) (tokens.get(i)) : null);
	}

	/**
	 * Getter for the length of the toks register, this is the number of
	 * elements contained.
	 * 
	 * @return the number of elements in the toks register
	 */
	public int length() {
		return tokens.size();
	}

	/**
	 * Return a String, which show all tokens in the list.
	 * 
	 * @return a String, which show all tokens in the list
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < tokens.size(); i++) {
			sb.append(((Token) tokens.get(i)).toString());
		}

		return sb.toString();
	}
}
