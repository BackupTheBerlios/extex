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
import com.ibm.icu.text.UTF16;

/**
 * UnicodeChar
 * 
 * @author <a href="gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class UnicodeChar {

	/**
	 * The codepoint of te unicode char (32 bit)
	 */
	private int code = 0;

	/**
	 * init with a 32 bit int-value
	 * 
	 * @param code the 32bit codepoint
	 */
	public UnicodeChar(int code) {
		super();
		this.code = code;
	}

	/**
	 * init with a 16 bit char-value
	 * 
	 * @param char16 16 bit character
	 */
	public UnicodeChar(char char16) {
		code = UCharacter.getCodePoint(char16);
	}

	/**
	 * init with two 16 bit-cahr-values
	 * 
	 * @param char16_1 first 16 bit character
	 * @param char16_2 second 16 bit character
	 */
	public UnicodeChar(char char16_1, char char16_2) {
		code = UCharacter.getCodePoint(char16_1, char16_2);
	}

	/**
	 * init with a unicodename
	 * 
	 * @param unicodename unicodename as String
	 */
	public UnicodeChar(String unicodename) {
		code = UCharacter.getCharFromName(unicodename);
	}

	/**
	 * Return the unicode-codepoint
	 * 
	 * @return the unicode-codepoint
	 */
	public int getCodePoint() {
		return code;
	}

	/**
	 * Return the count of char16 of the code
	 * 
	 * @return the count of char16 for this code
	 */
	public int getCharCount() {
		return UTF16.getCharCount(code);
	}

	/**
	 * Returns the unicodename of the code
	 * 
	 * @return unicodename of the code
	 */
	public String getUnicodeName() {
		return UCharacter.getName(code);
	}

	/**
	 * Returns the ISO-comment of the code
	 * 
	 * @return ISO-comment of the code
	 */
	public String getIsoComment() {
		return UCharacter.getISOComment(code);
	}

	/**
	 * Returns the lowercase-character of this object.
	 * <p>
	 * (this method do not use the TeX-lccode!)
	 * 
	 * @return character in lowercase
	 */
	public int toLowerCase() {
		return UCharacter.toLowerCase(code);
	}

	/**
	 * Returns the uppercase-character of this object.
	 * <p>
	 * (this method do not use the TeX-uccode!)
	 * 
	 * @return character in uppercase
	 */
	public int toUpperCase() {
		return UCharacter.toUpperCase(code);
	}

	/**
	 * Compares a <code>UnicodeChar</code> character with the value of this
	 * object.
	 * 
	 * @param texchar the character (\code{TeXChar}) to be compared with the stored one
	 * @return Are the characters equal (\code{true}) or not (\code{false})?
	 */
	public boolean equals(UnicodeChar unicodechar) {
		return (unicodechar.equals(code));
	}

	/**
	 * Compares a <code>char32</code> character with the value of this
	 * object.
	 * 
	 * @param char32 the character to be compared with the stored one
	 * @return Are the characters equal (<code>true</code>) or not (<code>false</code>)?
	 */
	public boolean equals(int char32) {
		return (char32 == code);
	}

	/**
	 * Compares a <code>char</code> character with the value of this object.
	 * 
	 * @param char16 the character to be compared with the stored one
	 * @return Are the characters equal (<code>true</code>) or not (<code>false</code>)?
	 */
	public boolean equals(char char16) {
		return (code == UCharacter.getCodePoint(char16));
	}

	/**
	 * Compares the stored character value with an object.
	 * 
	 * @param o object to be compared with the stored character
	 * @return Is the object a <code>UnicodeChar</code> with the same value (
	 *            <code>true</code>) or not (<code>false</code>)?
	 */
	public boolean equals(Object o) {
		return (o != null && o instanceof UnicodeChar && ((UnicodeChar) o).equals(code));
	}

	/**
	 * Returns the bidirection property of the character
	 * 
	 * @return the bidirection property
	 */
	public int getDirection() {
		return UCharacter.getDirection(code);
	}

	/**
	 * Return the character as a char-array
	 * 
	 * @return char-array of code
	 */
	public char[] toCharArray() {
		String tmp = toString();
		return tmp.toCharArray();
	}

	/**
	 * Returns a String of this object
	 * 
	 * @return String representation of the stored value.
	 */
	public String toString() {
		return UCharacter.toString(code);
	}

	/**
	 * Test, of the code is a letter
	 * 
	 * @return <code>true</code>, if the code is a letter, otherwise <code>false</code>
	 */
	public boolean isLetter() {
		return UCharacter.isLetter(code);
	}

	/**
	 * Test, of the code is a digit
	 * 
	 * @return <code>true</code>, if the code is a digit, otherwise <code>false</code>
	 */
	public boolean isDigit() {
		return UCharacter.isDigit(code);
	}

	/**
	 * Test, of the code is a letter or digit
	 * 
	 * @return <code>true</code>, if the code is a letter or digit,
	 *            otherwise <code>false</code>
	 */
	public boolean isLetterOrDigit() {
		return UCharacter.isLetterOrDigit(code);
	}

	/**
	 * Test, of the code is printable
	 * 
	 * @return <code>true</code>, if the code is printable, otherwise <code>false</code>
	 */
	public boolean isPrintable() {
		return UCharacter.isPrintable(code);
	}

	/**
	 * UnicodeChar with value -1
	 */
	public static final UnicodeChar NOT_DEFINED = new UnicodeChar(-1);

	/**
	 * UnicodeChar for '0'
	 */
	public static final UnicodeChar C_0 = new UnicodeChar('0');

	/**
	 * Unicode for '9'
	 */
	public static final UnicodeChar C_9 = new UnicodeChar('9');

	/**
	 * UnicodeChar for 'a'
	 */
	public static final UnicodeChar C_a = new UnicodeChar('a');

	/**
	 * UnicodeChar for 'f'
	 */
	public static final UnicodeChar C_f = new UnicodeChar('f');

	/**
	 * Check, if the character is a hexdigit (only lowercaseletter)
	 * 
	 * @return <code>true</code>, if the character is a hexdigit, otherwise
	 *            <code>false</code>
	 */
	public boolean isHexDigit() {
		return (C_0.getCodePoint() <= code && code <= C_9.getCodePoint() || C_a.getCodePoint() <= code && code <= C_f.getCodePoint());
	}

	/**
	 * Convert a hexdigit to a int-value.
	 * <p>
	 * (there is no chekc, if the character is a hexdigit, use <code>isHexDigit()</code>!)
	 * 
	 * @return int-value of the hexdigit
	 */
	public int getHexDigit() {
		return (code <= C_9.getCodePoint()) ? code - C_0.getCodePoint() : code - C_a.getCodePoint() + 10;
	}

	/**
	 * Return the ASCII-char of the character (if possible)
	 * 
	 * @return ASCII-char
	 */
	public char getASCIIChar() {
		return toString().charAt(0);
	}
}
