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
package de.dante.extex.interpreter.type;

import de.dante.extex.scanner.Token;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Tokens implements Serializable {
    /** this constant is the empty toks register */
    public static final Tokens EMPTY = new Tokens();

    /** the internal list of tokens */
    private List tokens = new ArrayList();

    /**
     * Creates a new object which does not contain any elements.
     */
    public Tokens() {
        super();
    }

    /**
     * ...
     *
     * @param t ...
     */
    public void add(Token t) {
        tokens.add(t);
    }

    /**
     * Get a specified token from the toks register.
     *
     * @param i the index for the token to get
     *
     * @return the i<sup>th</sup> token or <code>null</code> if i is out of
     * bounds
     */
    public Token get(int i) {
        return (i >= 0 && i < tokens.size() ? (Token) (tokens.get(i))
                : null);
    }

    /**
     * Getter for the length of the toks register, i.e. the number of elements.
     *
     * @return the number of elements in the toks register
     */
    public int length() {
        return tokens.size();
    }

    /**
     * ...
     *
     * @return ...
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < tokens.size(); i++) {
            sb.append(((Token) tokens.get(i)).getValue());
        }

        return sb.toString();
    }
}
