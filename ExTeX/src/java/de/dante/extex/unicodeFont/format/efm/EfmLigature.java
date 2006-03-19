/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.efm;

import de.dante.util.UnicodeChar;

/**
 * Class for a efm ligature.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EfmLigature {

    /**
     * The letter uc.
     */
    private UnicodeChar letterUc;

    /**
     * The lig uc.
     */
    private UnicodeChar ligUc;

    /**
     * Create a new object.
     */
    public EfmLigature() {

        super();
    }

    /**
     * Returns the letterUc.
     * @return Returns the letterUc.
     */
    public UnicodeChar getLetterUc() {

        return letterUc;
    }

    /**
     * Set the letterUc.
     * @param aletterUc The letterUc to set.
     */
    public void setLetterUc(final UnicodeChar aletterUc) {

        letterUc = aletterUc;
    }

    /**
     * Returns the ligUc.
     * @return Returns the ligUc.
     */
    public UnicodeChar getLigUc() {

        return ligUc;
    }

    /**
     * Set the ligUc.
     * @param aligUc The ligUc to set.
     */
    public void setLigUc(final UnicodeChar aligUc) {

        ligUc = aligUc;
    }

}
