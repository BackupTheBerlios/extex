/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
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

package de.dante.extex.scanner.type.token;

import java.io.Serializable;

import de.dante.extex.scanner.type.Catcode;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This is the abstract base class for all Tokens.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public abstract class AbstractToken implements Token, Serializable {

    /**
     * The constant <tt>HASH_FACTOR</tt> contains the factor used to construct
     * the hash code.
     */
    private static final int HASH_FACTOR = 17;

    /**
     * The field <tt>uniCode</tt> contains the Unicode character assigned to
     * this token. Note that <code>null</code> is a legal value.
     */
    private UnicodeChar character = null;

    /**
     * Creates a new object for a Unicode character.
     *
     * @param uc the value of the token
     */
    protected AbstractToken(final UnicodeChar uc) {

        super();
        this.character = uc;
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#equals(
     *      de.dante.extex.scanner.type.Catcode,
     *      char)
     */
    public boolean equals(final Catcode cc, final char c) {

        return getCatcode() == cc && equals(c);
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#equals(
     *      de.dante.extex.scanner.type.Catcode,
     *      java.lang.String)
     */
    public boolean equals(final Catcode cc, final String s) {

        return getCatcode() == cc && s.length() == 1
                && character.getCodePoint() == s.charAt(0);
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#equals(char)
     */
    public boolean equals(final char c) {

        UnicodeChar uc = getChar();

        return (uc != null && uc.getCodePoint() == c);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object t) {

        return this == t
                || (t instanceof Token
                        && getCatcode() == ((Token) t).getCatcode() && getChar()
                        .equals(((Token) t).getChar()));
    }

    /**
     * This is the getter for the catcode of this token.
     *
     * @see de.dante.extex.scanner.type.token.Token#getCatcode()
     */
    public abstract Catcode getCatcode();

    /**
     * @see de.dante.extex.scanner.type.token.Token#getChar()
     */
    public final UnicodeChar getChar() {

        return character;
    }

    /**
     * Getter for localizer.
     *
     * @return the localizer.
     */
    protected Localizer getLocalizer() {

        return LocalizerFactory.getLocalizer(Token.class);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return getCatcode().hashCode() + HASH_FACTOR * character.hashCode();
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#isa(
     *      de.dante.extex.scanner.type.Catcode)
     */
    public boolean isa(final Catcode cc) {

        return getCatcode() == cc;
    }

    /**
     * Return the printable representation of this object.
     *
     * @return the printable representation
     */
    public abstract String toString();

    /**
     * Return the text representation of this object.
     *
     * @return the text representation
     */
    public String toText() {

        return character.toString();
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#toText(
     *      de.dante.util.UnicodeChar)
     */
    public String toText(final UnicodeChar esc) {

        return character.toString();
    }

}