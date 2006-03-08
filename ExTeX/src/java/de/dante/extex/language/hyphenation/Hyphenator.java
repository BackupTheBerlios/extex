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

package de.dante.extex.language.hyphenation;

import java.io.Serializable;

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.UnicodeChar;
import de.dante.util.UnicodeCharList;

/**
 * Interface for the <code>HyphenationTable</code>.
 * <p>
 * In the table the hyphenation patterns (see <code>\patterns</code>
 * and the user hyphenations (see <code>\hyphenation</code>) are stored.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public interface Hyphenator extends Serializable {

    /**
     * Add a user-defined hyphenation.
     * <p>
     *  The hyphenation template consists of a word of characters.
     *  The Unicode soft hyphenation character has a special meaning. This
     *  character is used to indicate places where a hyphenation is permitted.
     *  The other characters &ndash; i.e. normal Unicode characters &ndash; are
     *  used as-is.
     * </p>
     *
     * @param word the word with the hyphenation marks
     * @param context the interpreter context
     *
     * @throws HyphenationException in case of an error
     */
    void addHyphenation(UnicodeCharList word, TypesetterOptions context)
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
     * Return the value for the minimum number of characters before a
     * hyphenation on the left hand side of a word.
     *
     * @return the value \lefthyphenmin
     *
     * @throws HyphenationException in case of an error
     */
    long getLeftHyphenmin() throws HyphenationException;

    /**
     * Return the value for the minimum number of characters before a
     * hyphenation on the right hand side of a word.
     *
     * @return the value \righthyphenmin
     *
     * @throws HyphenationException in case of an error
     */
    long getRightHyphenmin() throws HyphenationException;

    /**
     * Insert the hyphenation marks for a horizontal list of nodes. The
     * hyphenation marks are made up of discretionary nodes.
     *
     * @param nodelist the horizontal node list
     * @param context the context
     * @param hyphen the tokens to be inserted for hyphens
     * @param start the start index
     * @param forall the indicator that all words to the end should be
     *  processed. if <code>false</code> then only the next word is hyphenated.
     * @param nodeFactory the node factory
     *
     * @return <code>true</code> iff the hyphenator is responsible for this
     *  word. Usually this means that some hyphenation marks have been inserted.
     *
     * @throws HyphenationException in case of an error
     */
    boolean hyphenate(NodeList nodelist, TypesetterOptions context,
            UnicodeChar hyphen, int start, boolean forall,
            NodeFactory nodeFactory) throws HyphenationException;

    /**
     * Return <code>true</code>, if hyphenation is active,
     * otherwise <code>false</code>;
     *
     * @return <code>true</code> iff the hyphenation for this language is
     *  enabled
     *
     * @throws HyphenationException in case of an error
     */
    boolean isHyphenActive() throws HyphenationException;

    /**
     * Activate or deactivate the hyphenation for this language.
     * If the hyphenation is deactivated then no hyphenation should be added
     * automatically.
     *
     * @param active the indicator that the hyphenation is activated
     *
     * @throws HyphenationException in case of an error
     */
    void setHyphenActive(boolean active) throws HyphenationException;

    /**
     * Set the value for the minimum number of characters before a
     * hyphenation on the left hand side of a word.
     *
     * @param left the new value
     *
     * @throws HyphenationException in case of an error
     */
    void setLeftHyphenmin(long left) throws HyphenationException;

    /**
     * Set the value for the minimum number of characters before a
     * hyphenation on the right hand side of a word.
     *
     * @param right the new value
     *
     * @throws HyphenationException in case of an error
     */
    void setRightHyphenmin(long right) throws HyphenationException;

}
