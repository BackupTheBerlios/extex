/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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

import com.ibm.icu.lang.UCharacter;

/**
 * UnicodeChar
 *
 * @author <a href="gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class UnicodeChar {

    /**
     * The codepoint of the unicode char (32 bit)
     */
    private int code;

    /**
     * init with a 32 bit int value
     *
     * @param code the 32 bit codepoint
     */
    public UnicodeChar(final int code) {
        super();
        this.code = code;
    }

    /**
     * init with a 16 bit char value
     *
     * @param char16 16 bit character
     */
    public UnicodeChar(final char char16) {
        code = UCharacter.getCodePoint(char16);
    }

    /**
     * init with two 16 bit char values
     *
     * @param char1 first 16 bit character
     * @param char2 second 16 bit character
     */
    public UnicodeChar(final char char1, final char char2) {
        code = UCharacter.getCodePoint(char1, char2);
    }

    /**
     * Init with a Unicode name.
     *
     * @param unicodename Unicode name as String
     */
    public UnicodeChar(final String unicodename) {
        code = UCharacter.getCharFromName(unicodename);
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
     * @return unicodename of the code
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
     * object.
     * <p>
     * The general signature for comparison to an arbitray object is required
     * for the implementation of HahshMap and friends.
     * </p>
     * 
     * @param unicodeChar the character to compare
     * @return <code>true</code>, if the characters are equals, otherwise
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
     *             MGN das muss drinbleiben,
     *             da es Zeichen gibt, die die Schreibrichtung umstellen.
     *             Wird vom TypeSetter benötigt, wenn die die etex bzw.
     *             Omega Funktionen einbauen.
     */
    public int getDirection() {
        return UCharacter.getDirection(code);
    }

    /**
     * Return the character as a char array.
     *
     * @return char array of code
     */
    public char[] toCharArray() {
        String tmp = toString();
        return tmp.toCharArray();
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
     * Test, of the code is printable.
     *
     * @return <code>true</code>, if the code is printable,
     * otherwise <code>false</code>
     */
    public boolean isPrintable() {
        return UCharacter.isPrintable(code);
    }

}
