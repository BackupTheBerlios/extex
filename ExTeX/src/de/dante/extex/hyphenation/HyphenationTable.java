/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.hyphenation;


/**
 * Interface for the <code>HyphenationTable</code>.
 * <p>
 * In the table are stored the hyphenations-pattern (see <code>\patterns</code>
 * and the user-hyphenations (see <code>\hyphenation</code>). 
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface HyphenationTable {

    /**
     * Add a pattern
     * 
     * @param word 		the pattern-word
     * @param pattern 	the pattern-word with numbers
     */
    void addPattern(final String word, final String pattern);

    /**
     * Add a user-hyphenation
     * 
     * @param word 		the word
     * @param pattern 	the word with hyphenation-chars
     */
    void addHyphenation(final String word, final String hyphword);

    /**
     * Set hte value for lefthyphenmin
     * @param left	the new value
     */
    void setLeftHyphenmin(final int left);

    /**
     * Return the value for lefthyphenmin
     * @return	lefthyphenmin
     */
    int getLeftHyphenmin();

    /**
     * Set hte value for righthyphenmin
     * @param right	the new value
     */
    void setRightHyphenmin(final int right);

    /**
     * Return the value for righthyphenmin
     * @return	righthyphenmin
     */
    int getRightHyphenmin();
 
    /**
     * Set hte value for hyphenactive
     * @param active	the new value
     */
    void setHyphenActive(final boolean active);

    /**
     * Return <code>true</code>, if hyphenation is active, 
     * otherwise <code>false</code>;
     * 
     * @return	hyphenactive
     */
    boolean isHyphenActive();
    
}
