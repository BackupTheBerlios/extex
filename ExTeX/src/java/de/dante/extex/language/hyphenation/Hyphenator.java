/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.language.hyphenation;

import java.io.Serializable;

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.node.HorizontalListNode;

/**
 * Interface for the <code>HyphenationTable</code>.
 * <p>
 * In the table the hyphenation patterns (see <code>\patterns</code>
 * and the user hyphenations (see <code>\hyphenation</code>) are stored.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public interface Hyphenator extends Serializable {

    /**
     * Add a user hyphenation.
     *
     * @param word the word
     * @param context the iterpreter context
     *
     * @throws HyphenationException in case of an error
     */
    void addHyphenation(Tokens word, TypesetterOptions context)
            throws HyphenationException;

    /**
     * Add a pattern to the hyphenation table.
     *
     * @param pattern the pattern word with numbers
     *
     * @throws HyphenationException in case of an error
     */
    void addPattern(Tokens pattern) throws HyphenationException;

    /**
     * Return the value for lefthyphenmin
     * @return  lefthyphenmin
     *
     * @throws HyphenationException in case of an error
     */
    long getLeftHyphenmin() throws HyphenationException;

    /**
     * Return the value for righthyphenmin
     * @return  righthyphenmin
     *
     * @throws HyphenationException in case of an error
     */
    long getRightHyphenmin() throws HyphenationException;

    /**
     * Insert the hyphenation marks for a horizontal list of nodes.
     *
     * @param nodelist the horizonzal node list
     * @param context the context
     * @param hyphen the tokens to be inserted for hyphens
     *
     * @return a nodelist with hyphenation marks inserted
     *
     * @throws HyphenationException in case of an error
     */
    HorizontalListNode hyphenate(HorizontalListNode nodelist,
            TypesetterOptions context, Token hyphen)
            throws HyphenationException;

    /**
     * Return <code>true</code>, if hyphenation is active,
     * otherwise <code>false</code>;
     *
     * @return hyphenactive
     *
     * @throws HyphenationException in case of an error
     */
    boolean isHyphenActive() throws HyphenationException;

    /**
     * Set the value for hyphenactive
     * @param active the new value
     *
     * @throws HyphenationException in case of an error
     */
    void setHyphenActive(boolean active) throws HyphenationException;

    /**
     * Set the value for lefthyphenmin
     * @param left the new value
     *
     * @throws HyphenationException in case of an error
     */
    void setLeftHyphenmin(long left) throws HyphenationException;

    /**
     * Set the value for righthyphenmin
     * @param right the new value
     *
     * @throws HyphenationException in case of an error
     */
    void setRightHyphenmin(long right) throws HyphenationException;
}