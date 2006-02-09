/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.type;

import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * Interface for the extex font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public interface ExtexFont extends Font {

    /**
     * Returns the width of the char.
     *
     * @param uc     The Unicode char.
     * @return Returns the width of the char.
     */
    Glue getWidth(UnicodeChar uc);

    /**
     * Returns the height of the char.
     *
     * @param uc     The Unicode char.
     * @return Returns the height of the char.
     */
    Glue getHeight(UnicodeChar uc);

    /**
     * Returns the depth of the char.
     *
     * @param uc     The Unicode char.
     * @return Returns the depth of the char.
     */
    Glue getDepth(UnicodeChar uc);

    /**
     * Returns the italic correction of the char.
     *
     * @param uc     The Unicode char.
     * @return Returns the italic correction of the char.
     */
    Dimen getItalicCorrection(UnicodeChar uc);

    /**
     * Returns the size of 'x'.
     *
     * @return Returns the size of 'x'.
     */
    Dimen getEx();

    /**
     * Returns the size of 'M'.
     *
     * @return Returns the size of 'M'.
     */
    Dimen getEm();

    /**
     * Returns the size of the 'space'.
     *
     * @return Returns the size of the 'space'.
     */
    Glue getSpace();

    /**
     * Returns the name of the font.
     *
     * @return Returns the name of the font.
     */
    String getFontName();

    /**
     * Returns the size of the parameter with the name 'name'.
     * <p>
     * The size are multiples of the design size!
     * </p>
     *
     * @param name  The name of the parameter.
     * @return Returns the size of the parameter with the name 'name'.
     */
    Dimen getFontDimen(String name);

    /**
     * Returns the design size of the font.
     *
     * @return Returns the design size of the font.
     */
    Dimen getDesignsize();

    /**
     * Returns the actual size of the font.
     *
     * @return Returns the actual size of the font.
     */
    Dimen getActualsize();

    /**
     * Returns the scale factor of the font.
     *
     * @return Returns the scale factor of the font.
     */
    Count getScalefactor();

    /**
     * Returns the kerning between two chars.
     *
     * @param uc1     The Unicode char (first one).
     * @param uc2     The Unicode char (second one).
     * @return Returns the kerning between two chars.
     */
    Dimen getKerning(UnicodeChar uc1, UnicodeChar uc2);

    /**
     * Returns the ligature for two chars.
     *
     * @param uc1     The Unicode char (first one).
     * @param uc2     The Unicode char (second one).
     * @return Returns the ligature for two chars.
     */
    UnicodeChar getLigature(UnicodeChar uc1, UnicodeChar uc2);
}
