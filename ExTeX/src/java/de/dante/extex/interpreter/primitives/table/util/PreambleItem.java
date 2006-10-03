/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.table.util;

import de.dante.extex.interpreter.type.tokens.Tokens;

/**
 * This class provides a building block for the preamble of an alignment.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class PreambleItem {

    /**
     * The field <tt>post</tt> contains the tokens before the insertion text.
     */
    private Tokens post;

    /**
     * The field <tt>pre</tt> contains the tokens after the insertion text.
     */
    private Tokens pre;

    /**
     * Creates a new object.
     *
     * @param preTokens the tokens before the insertion text
     * @param postTokens the tokens after the insertion text
     */
    public PreambleItem(final Tokens preTokens, final Tokens postTokens) {

        super();
        this.pre = preTokens;
        this.post = postTokens;
    }

    /**
     * Getter for the tokens after the insertion text.
     *
     * @return the tokens after the insertion text
     */
    public Tokens getPost() {

        return this.post;
    }

    /**
     * Getter for the tokens before the insertion text.
     *
     * @return the tokens before the insertion text
     */
    public Tokens getPre() {

        return this.pre;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "\npre = " + pre.toText() + "\npost = " + post.toText() + "\n";
    }

}
