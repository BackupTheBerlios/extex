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

package de.dante.util;

import java.io.Serializable;
import java.nio.CharBuffer;

import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.UTF16;

/**
 * This class represents a 32-bit Unicode character.
 *
 * Java 1.4 defines 16-bit characters only. Thus we are forced to roll our own
 * version. As soon as Java supports 32-bit characters this class is obsolete
 * and should be eliminated.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.26 $
 */
public class UnicodeChar implements Serializable {

    /**
     * The constant <tt>NULL</tt> contains the Unicode character with the
     * code point 0.
     */
    public static final UnicodeChar NULL = new UnicodeChar(0);

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * The field <tt>code</tt> contains the code point of the Unicode character
     * (32 bit).
     */
    private int code;

    /**
     * Creates a new object from two 16-bit characters.
     *
     * @param char1 first 16-bit character
     * @param char2 second 16-bit character
     */
    public UnicodeChar(final char char1, final char char2) {

        super();
        this.code = UCharacter.getCodePoint(char1, char2);
    }

    /**
     * Create a new instance with a char32 from a <code>CharBuffer</code>
     * at position index.
     * <p>
     *  This use the code from <code>UTF16.charAt(String,int)</code> and
     *  change String to CharBuffer.
     * </p>
     *
     * @param buffer the <code>CharBuffer</code>
     * @param index the position in the char buffer
     */
    public UnicodeChar(final CharBuffer buffer, final int index) {

        super();

        if (index < 0 || index >= buffer.length()) {
            throw new StringIndexOutOfBoundsException(index);
        }

        char single = buffer.charAt(index);
        if (single < UTF16.LEAD_SURROGATE_MIN_VALUE
                || single > UTF16.TRAIL_SURROGATE_MAX_VALUE) {
            this.code = single;
        } else {

            this.code = single;

            // Convert the UTF-16 surrogate pair if necessary.
            // For simplicity in usage, and because the frequency of pairs is
            // low, look both directions.
            if (single <= UTF16.LEAD_SURROGATE_MAX_VALUE) {
                if (buffer.length() > (index + 1)) {
                    char trail = buffer.charAt(index + 1);
                    if (trail >= UTF16.TRAIL_SURROGATE_MIN_VALUE
                            && trail <= UTF16.TRAIL_SURROGATE_MAX_VALUE) {
                        this.code = UCharacterProperty.getRawSupplementary(
                                single, trail);
                    } else {
                        throw new RuntimeException("This can't happen?");
                    }
                } else {
                    throw new RuntimeException("This can't happen?");
                }
            } else if (index > 0) {
                char lead = buffer.charAt(index - 1);
                if (lead >= UTF16.LEAD_SURROGATE_MIN_VALUE
                        && lead <= UTF16.LEAD_SURROGATE_MAX_VALUE) {
                    this.code = UCharacterProperty.getRawSupplementary(lead,
                            single);
                } else {
                    throw new RuntimeException("This can't happen?");
                }
            } else {
                throw new RuntimeException("This can't happen?");
            }
        }
    }

    /**
     * Creates a new object from an integer code point.
     *
     * @param codePoint the 32-bit code point
     */
    public UnicodeChar(final int codePoint) {

        super();
        if (codePoint < UCharacter.MIN_VALUE
                || codePoint > UCharacter.MAX_VALUE) {
            throw new IllegalArgumentException("Codepoint out of bounds");
        }
        this.code = codePoint;
    }

    /**
     * Creates a new object from a Unicode name.
     *
     * @param unicodeName Unicode name as String
     */
    public UnicodeChar(final String unicodeName) {

        super();
        this.code = UCharacter.getCharFromName(unicodeName);
    }

    /**
     * Compares a <code>UnicodeChar</code> character with the value of this
     * object. They are considered equal if the are both UnicodeChars and have
     * the same code.
     * <p>
     * The general signature for comparison to an arbitrary object is required
     * for the implementation of {@link java.util.HashMap HashMap} and friends.
     * </p>
     *
     * @param unicodeChar the character to compare
     *
     * @return <code>true</code> if the characters are equal, otherwise
     *         <code>false</code>
     */
    public boolean equals(final Object unicodeChar) {

        return ((unicodeChar instanceof UnicodeChar) && //
        this.code == ((UnicodeChar) unicodeChar).getCodePoint());
    }

    /**
     * Return the count of char16 of the code.
     *
     * @return the count of char16 for this code
     */
    public int getChar16Count() {

        return UTF16.getCharCount(this.code);
    }

    /**
     * Return the Unicode code point.
     *
     * @return the Unicode code point
     */
    public int getCodePoint() {

        return this.code;
    }

    /**
     * Returns the bi-direction property of the character.
     *
     * @return the bi-direction property
     */
    public int getDirection() {

        return UCharacter.getDirection(this.code);
    }

    /**
     * Returns the Unicode name of the code.
     *
     * @return Unicode name of the code
     */
    public String getUnicodeName() {

        return UCharacter.getName(this.code);
    }

    /**
     * Computes the hash code for the character. The hash code of equal objects
     * must be equal, but the hash code of different object need not to be
     * different. This is needed for the implementations of HashMap and friends.
     *
     * @return the hash code
     */
    public int hashCode() {

        return this.code;
    }

    /**
     * Test, if the code is a digit.
     *
     * @return <code>true</code>, if the code is a digit,
     * otherwise <code>false</code>
     */
    public boolean isDigit() {

        return UCharacter.isDigit(this.code);
    }

    /**
     * Test, if the character is a letter.
     *
     * @return <code>true</code>, if the code is a letter,
     * otherwise <code>false</code>
     */
    public boolean isLetter() {

        return UCharacter.isLetter(this.code);
    }

    /**
     * Test, if the code is printable.
     *
     * @return <code>true</code>, if the code is printable,
     * otherwise <code>false</code>
     */
    public boolean isPrintable() {

        return UCharacter.isPrintable(this.code);
    }

    /**
     * Returns the lowercase character of this object.
     * <p>
     * (this method does not use the <logo>TeX</logo> lccode!)
     * </p>
     *
     * @return character in lowercase
     */
    public UnicodeChar lower() {

        int lc = UCharacter.toLowerCase(this.code);
        return (lc == code ? this : new UnicodeChar(lc));
    }

    /**
     * Returns the lowercase character of this object.
     * <p>
     * (this method does not use the <logo>TeX</logo> lccode!)
     * </p>
     *
     * @return character in lowercase
     */
    public int toLowerCase() {

        return UCharacter.toLowerCase(this.code);
    }

    /**
     * Returns a String of this object.
     *
     * @return String representation of the stored value.
     */
    public String toString() {

        return UCharacter.toString(this.code);
    }

    /**
     * Returns the uppercase character of this object.
     * <p>
     * (this method does not use the <logo>TeX</logo> uccode!)
     * </p>
     *
     * @return character in uppercase
     */
    public int toUpperCase() {

        return UCharacter.toUpperCase(this.code);
    }

}
