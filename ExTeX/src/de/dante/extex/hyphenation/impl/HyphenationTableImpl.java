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
package de.dante.extex.hyphenation.impl;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.hyphenation.HyphenationTable;

/**
 * This class store the values for hyphenations.
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class HyphenationTableImpl implements HyphenationTable {

    /**
     * Creates a new object.
     */
    public HyphenationTableImpl() {
        super();
    }

    /**
     * Map for hyphenation
     */
    private Map hypmap = new HashMap();

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#addHyphenation(java.lang.String, java.lang.String)
     */
    public void addHyphenation(final String word, final String hyphword) {
        hypmap.put(word, hyphword);
    }

    /**
     * Map for hyphenation-pattern
     */
    private Map patmap = new HashMap();

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#addPattern(java.lang.String, java.lang.String)
     */
    public void addPattern(final String word, final String pattern) {
        patmap.put(word, pattern);
    }

    /**
     * lefthyphenmin
     */
    private int lefthyphnemin;

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#getLeftHyphenmin()
     */
    public int getLeftHyphenmin() {
        return lefthyphnemin;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setLeftHyphenmin(int)
     */
    public void setLeftHyphenmin(final int left) {
        lefthyphnemin = left;
    }

    /**
     * righthyphenmin
     */
    private int righthyphenmin;

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#getRightHyphenmin()
     */
    public int getRightHyphenmin() {
        return righthyphenmin;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setRightHyphenmin(int)
     */
    public void setRightHyphenmin(final int right) {
        righthyphenmin = right;
    }

    /**
     * hyphenactive
     */
    private boolean hyphenactive = true;

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#isHyphenActive()
     */
    public boolean isHyphenActive() {
        return hyphenactive;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setHyphenActive(boolean)
     */
    public void setHyphenActive(final boolean active) {
        hyphenactive = active;
    }

}
