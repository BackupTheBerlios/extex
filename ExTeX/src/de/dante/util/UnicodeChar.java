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
package de.dante.util;

import java.nio.CharBuffer;

import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.UTF16;

/**
 * UnicodeChar
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.15 $
 */
public class UnicodeChar {

    /**
     * The constant <tt>NULL</tt> contains the Unicode character with the
     * code point 0.
     */
    public static final UnicodeChar NULL = new UnicodeChar(0);

    /**
     * The code point of the Unicode char (32 bit)
     */
    private int code;

    /**
     * Creates a new object.
     *
     * @param codePoint the 32 bit code point
     */
    public UnicodeChar(final int codePoint) {

        super();
        this.code = codePoint;
    }

    /**
     * Creates a new object.
     *
     * @param char16 16 bit character
     */
    public UnicodeChar(final char char16) {

        super();
        this.code = UCharacter.getCodePoint(char16);
    }

    /**
     * init with two 16 bit char values
     *
     * @param char1 first 16 bit character
     * @param char2 second 16 bit character
     */
    public UnicodeChar(final char char1, final char char2) {

        super();
        this.code = UCharacter.getCodePoint(char1, char2);
    }

    /**
     * Init with a char32 from a String at position idx.
     *
     * @param s the <code>String</code>
     * @param idx the position in the string
     */
    public UnicodeChar(final String s, final int idx) {

        super();
        this.code = UTF16.charAt(s, idx);
    }

    /**
     * Init with a char32 from a <code>CharBuffer</code> at position idx.
     * <p>
     * This use the code from <code>UTF16.charAt(String,int)</code> and
     * change String to Charbuffer.
     *
     * @param cb     the <code>CharBuffer</code>
     * @param idx     the position in the charbuffer
     */
    public UnicodeChar(final CharBuffer cb, final int idx) {

        super();

        if (idx < 0 || idx >= cb.length()) {
            throw new StringIndexOutOfBoundsException(idx);
        }

        char single = cb.charAt(idx);
        if (single < UTF16.LEAD_SURROGATE_MIN_VALUE
            || single > UTF16.TRAIL_SURROGATE_MAX_VALUE) {
            code = single;
        } else {

            code = single;

            // Convert the UTF-16 surrogate pair if necessary.
            // For simplicity in usage, and because the frequency of pairs is
            // low, look both directions.
            if (single <= UTF16.LEAD_SURROGATE_MAX_VALUE) {
                if (cb.length() > (idx + 1)) {
                    char trail = cb.charAt(idx + 1);
                    if (trail >= UTF16.TRAIL_SURROGATE_MIN_VALUE
                        && trail <= UTF16.TRAIL_SURROGATE_MAX_VALUE) {
                        code = UCharacterProperty.getRawSupplementary(single,
                                                                      trail);
                    } else {
                        throw new RuntimeException("This can't happen???");
                    }
                } else {
                    throw new RuntimeException("This can't happen???");
                }
            } else if (idx > 0) {
                char lead = cb.charAt(idx - 1);
                if (lead >= UTF16.LEAD_SURROGATE_MIN_VALUE
                        && lead <= UTF16.LEAD_SURROGATE_MAX_VALUE) {
                    code = UCharacterProperty.getRawSupplementary(lead,
                            single);
                } else {
                    throw new RuntimeException("This can't happen???");
                }
            } else {
                throw new RuntimeException("This can't happen???");
            }
        }
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
     * Return the unicode codepoint.
     *
     * @return the unicode codepoint
     */
    public int getCodePoint() {

        return code;
    }

    /**
     * Returns the Unicode name of the code.
     *
     * @return Unicode name of the code
     */
    public String getUnicodeName() {

        return UCharacter.getName(code);
    }

    /**
     * Returns the lowercase character of this object.
     * <p>
     * (this method does not use the TeX lccode!)
     * </p>
     *
     * @return character in lowercase
     */
    public int toLowerCase() {

        return UCharacter.toLowerCase(code);
    }

    /**
     * Returns the uppercase character of this object.
     * <p>
     * (this method does not use the TeX uccode!)
     * </p>
     *
     * @return character in uppercase
     */
    public int toUpperCase() {

        return UCharacter.toUpperCase(code);
    }

    /**
     * Compares a <code>UnicodeChar</code> character with the value of this
     * object. They are considered equal if the are both UnicodeChars and have
     * the same code.
     * <p>
     * The general signature for comparison to an arbitray object is required
     * for the implementation of {@link java.util.HashMap HashMap} and friends.
     * </p>
     *
     * @param unicodeChar the character to compare
     * @return <code>true</code> if the characters are equal, otherwise
     *         <code>false</code>
     */
    public boolean equals(final Object unicodeChar) {

        return ((unicodeChar instanceof UnicodeChar) && //
        code == ((UnicodeChar) unicodeChar).getCodePoint());
    }

    /**
     * Computes the hash code for the character. The hash code of equal objects
     * must be equal, but the hash code of different object need not to be
     * different. This is needed for the implementations of HashMap and friends.
     *
     * @return the hash code
     */
    public int hashCode() {

        return code;
    }

    /**
     * Returns the bidirection property of the character.
     *
     * @return the bidirection property
     */
    public int getDirection() {

        return UCharacter.getDirection(code);
    }

    /**
     * Returns a String of this object.
     *
     * @return String representation of the stored value.
     */
    public String toString() {

        return UCharacter.toString(code);
    }

    /**
     * Test, of the code is a letter
     *
     * @return <code>true</code>, if the code is a letter,
     * otherwise <code>false</code>
     */
    public boolean isLetter() {

        return UCharacter.isLetter(code);
    }

    /**
     * Test, of the code is a digit.
     *
     * @return <code>true</code>, if the code is a digit,
     * otherwise <code>false</code>
     */
    public boolean isDigit() {

        return UCharacter.isDigit(code);
    }

    /**
     * Test, of the code is a letter or digit.
     *
     * @return <code>true</code>, if the code is a letter or digit,
     *            otherwise <code>false</code>
     * @deprecated
     */
    public boolean isLetterOrDigit() {

        return UCharacter.isLetterOrDigit(code);
    }

    /**
     * Test, if the code is printable.
     *
     * @return <code>true</code>, if the code is printable,
     * otherwise <code>false</code>
     */
    public boolean isPrintable() {

        return UCharacter.isPrintable(code);
    }

    /**
     * Return the count of char16 of the code
     * @return the count of char16 for this code
     */
    public int getChar16Count() {

        return UTF16.getCharCount(code);
    }

}
