/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.noad;

import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.UnicodeChar;

/**
 * This class provides a container for a mathematical glyph.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MathGlyph implements Noad {

    /**
     * The constant <tt>FAMILY_MASK</tt> contains the mask for the family in the
     * TeX encoding.
     */
    private static final int FAMILY_MASK = 0xf;

    /**
     * The constan <tt>CHARACTER_MASK</tt> contains the mask for the character
     * value in the TeX encoding.
     */
    private static final int CHARACTER_MASK = 0xff;

    /**
     * The field <tt>character</tt> contains the character of this glyph.
     */
    private UnicodeChar character;

    /**
     * The field <tt>family</tt> contains the math family.
     */
    private int family;

    /**
     * Creates a new object from a TeX encoded number.
     *
     * @param code the TeX code
     */
    public MathGlyph(final int code) {

        this((code >> 8) & FAMILY_MASK, new UnicodeChar(
                (int) (code & CHARACTER_MASK)));
    }

    /**
     * Creates a new object.
     *
     * @param family the math family of the glyph
     * @param character the character in the font
     */
    public MathGlyph(final int family, final UnicodeChar character) {

        super();
        this.family = family;
        this.character = character;

    }

    /**
     * Getter for character.
     *
     * @return the character.
     */
    public UnicodeChar getCharacter() {

        return this.character;
    }

    /**
     * Getter for family.
     *
     * @return the family.
     */
    public int getFamily() {

        return this.family;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSubscript()
     */
    public Noad getSubscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSuperscript()
     */
    public Noad getSuperscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSubscript(
     *       de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSubscript(final Noad subscript) {

        throw new UnsupportedOperationException("subscript");
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSuperscript(
     *       de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSuperscript(final Noad superscript) {

        throw new UnsupportedOperationException("superscript");
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *       java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        toString(sb, 1);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *       java.lang.StringBuffer, int)
     */
    public void toString(final StringBuffer sb, final int depth) {

        if (depth >= 0) {
            sb.append("\"");
            sb.append(Integer.toHexString(family));
            sb.append(Integer.toHexString(character.getCodePoint()));
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

}