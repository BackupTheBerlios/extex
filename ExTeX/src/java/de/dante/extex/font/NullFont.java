/*
 * Copyright (C) 2003-2004  Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.font;

import java.io.File;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Font;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.Glyph;
import de.dante.util.UnicodeChar;

/**
 * This class implements a dummy font which does not contain any characters.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.11 $
 */
public class NullFont implements Font {

    /**
     * The field <tt>hyphen</tt> contains the ...
     */
    private UnicodeChar hyphen = new UnicodeChar('-');

    /**
     * The field <tt>skew</tt> contains the ...
     */
    private UnicodeChar skew = new UnicodeChar('-');

    /**
     * Creates a new object.
     */
    public NullFont() {
        super();
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getHyphenChar()
     */
    public UnicodeChar getHyphenChar() {
        return hyphen;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getSkewChar()
     */
    public UnicodeChar getSkewChar() {
        return skew;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#setHyphenChar(de.dante.util.UnicodeChar)
     */
    public void setHyphenChar(final UnicodeChar hyphen) {

        this.hyphen = hyphen;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#setSkewChar(de.dante.util.UnicodeChar)
     */
    public void setSkewChar(final UnicodeChar skew) {

        this.skew = skew;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getSpace()
     */
    public Glue getSpace() {
        return new Glue(0);
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getEm()
     */
    public Dimen getEm() {
        return new Dimen(0);
    }
	
	/**
	 * @see de.dante.extex.interpreter.type.Font#getFontType()
	 */
	public String getFontType() {
		return null;
	}

	/**
	 * @see de.dante.extex.interpreter.type.Font#setFontDimen(java.lang.String, de.dante.extex.interpreter.type.Dimen)
	 */
	public void setFontDimen(String key, Dimen value) {

    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getEx()
     */
    public Dimen getEx() {

        return new Dimen(0);
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getFontDimen(java.lang.String)
     */
    public Dimen getFontDimen(final String key) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getFontName()
     */
    public String getFontName() {
        return "nullFont";
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#isDefined(de.dante.util.UnicodeChar)
     */
    public boolean isDefined(final UnicodeChar c) {
        return false;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#kern(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public Dimen kern(final UnicodeChar c1, final UnicodeChar c2) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#ligature(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public UnicodeChar ligature(final UnicodeChar c1, final UnicodeChar c2) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getExternalFile()
     */
    public File getExternalFile() {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getExternalID(UnicodeChar)
     */
    public String getExternalID(final UnicodeChar c) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getGlyph(de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {
        return null;
    }

}
