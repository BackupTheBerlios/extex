/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.hyphenation;

import java.io.Serializable;

import de.dante.extex.hyphenation.exception.HyphenationException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.GeneralException;

/**
 * Interface for the <code>HyphenationTable</code>.
 * <p>
 * In the table the hyphenation patterns (see <code>\patterns</code>
 * and the user-hyphenations (see <code>\hyphenation</code>) are stored.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public interface HyphenationTable extends Serializable {

    /**
     * Add a pattern
     *
     * @param word      the pattern-word
     * @param pattern   the pattern-word with numbers
     */
    void addPattern(String word, String pattern);

    /**
     * Add a user hyphenation.
     *
     * @param word the word
     * @param context the iterpreter context
     *
     * @throws HyphenationException in case of an error
     */
    void addHyphenation(Tokens word, Context context) throws HyphenationException;

    /**
     * Set hte value for lefthyphenmin
     * @param left  the new value
     */
    void setLeftHyphenmin(long left);

    /**
     * Return the value for lefthyphenmin
     * @return  lefthyphenmin
     */
    long getLeftHyphenmin();

    /**
     * Set hte value for righthyphenmin
     * @param   right   the new value
     */
    void setRightHyphenmin(long right);

    /**
     * Return the value for righthyphenmin
     * @return  righthyphenmin
     */
    long getRightHyphenmin();

    /**
     * Set hte value for hyphenactive
     * @param active the new value
     */
    void setHyphenActive(boolean active);

    /**
     * Return <code>true</code>, if hyphenation is active,
     * otherwise <code>false</code>;
     *
     * @return hyphenactive
     */
    boolean isHyphenActive();

    /**
     * Insert the hyphenation marks for a horizontal list of nodes.
     * @param nodelist the horizonzal nodelist
     * @param context the context
     * @return a nodelist with hyphenation marks inserted
     *
     * @throws GeneralException in case of an error
     */
    HorizontalListNode hyphenate(HorizontalListNode nodelist, Context context)
            throws GeneralException;

}
