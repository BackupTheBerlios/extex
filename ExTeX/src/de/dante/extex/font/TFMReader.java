/*
 * Copyright (C) 2004 Michael Niedermair
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.jdom.Element;

import de.dante.extex.font.FontMetric;

/**
 * This class read a TFM-file.
 * <p>
 * See for more information TFtoPL  
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TFMReader implements FontMetric {

	/**
	 * InputStream for Reading
	 */
	private InputStream in;

	public TFMReader(InputStream in) throws TFMException, IOException {
		this.in = in;

		// read ...
		readLengths();
		readHeader();
		readTables();
		checkTables();
		makeLigTable();
		checkExtens();
		makeCharTable();

		// close stream
		in.close();
	}

	/** 
	 * Character information table in the final format. 
	 */
	private CharInfo[] charTable;

	/**
	 * Container for character information.
	 */
	private class CharInfo {

		/**
		 * init
		 * @param	w character width.
		 * @param	h character height.
		 * @param	d character depth.
		 * @param	i character italic correction.
		 */
		public CharInfo(FixWord w, FixWord h, FixWord d, FixWord i) {
			width = w;
			height = h;
			depth = d;
			italic = i;
		}

		/** 
		 * Character width 
		 */
		private FixWord width;

		/** 
		 * Character height 
		 */
		private FixWord height;

		/** 
		 * Character depth 
		 */
		private FixWord depth;

		/** 
		 * Character italic correction 
		 */
		private FixWord italic;

		/**
		 * @return	the character width.
		 */
		public FixWord getWidth() {
			return width;
		}

		/**
		 * @return	the character height.
		 */
		public FixWord getHeight() {
			return height;
		}

		/**
		 * @return	the character depth.
		 */
		public FixWord getDepth() {
			return depth;
		}

		/**
		 * @return	the character italic correction.
		 */
		public FixWord getItalic() {
			return italic;
		}

		/**
		 * @return	start index of lig/kern program or <code>NO_INDEX</code> if it has no lig/kern program associated.
		 */
		public int ligKernStart() {
			return NO_INDEX;
		}

		/**
		 * @return	next larger character code or <code>NO_CHAR_CODE</code> if there is no larger character.
		 */
		public short nextChar() {
			return NO_CHAR_CODE;
		}

		/**
		 * Gets the character code for top part of extensible character.
		 * @return	code of top part character or <code>NO_CHAR_CODE</code> if there is no top part or the character is not extensible.
		 */
		public short extTop() {
			return NO_CHAR_CODE;
		}

		/**
		 * Gets the character code for middle part of extensible character.
		 * @return	code of middle part character or <code>NO_CHAR_CODE</code> if there is no middle part or the character is not extensible.
		 */
		public short extMid() {
			return NO_CHAR_CODE;
		}

		/**
		 * Gets the character code for bottom part of extensible character.
		 * @return	code of bottom part character or <code>NO_CHAR_CODE</code> if there is no bottom part or the character is not extensible.
		 */
		public short extBot() {
			return NO_CHAR_CODE;
		}

		/**
		 * Gets the character code for repeatable part of extensible
		 * character.
		 * @return	code of repeatable part character or <code>NO_CHAR_CODE</code> if there is no repeatable part or the character is not extensible.
		 */
		public short extRep() {
			return NO_CHAR_CODE;
		}
	}

	/**
	 * Container for Character information for character with associated lig/kern program.
	 */
	private class LigCharInfo extends CharInfo {

		/**
		 * init
		 * @param	w character width.
		 * @param	h character height.
		 * @param	d character depth.
		 * @param	i character italic correction.
		 * @param	s lig/kern program starting index.
		 */
		public LigCharInfo(FixWord w, FixWord h, FixWord d, FixWord i, int s) {
			super(w, h, d, i);
			start = s;
		}

		/**
		 * Index of the starting instruction of lig/kern program in the <code>ligKernTable</code>.
		 */
		private int start;

		/**
		 * @return	start index of lig/kern program.
		 */
		public int ligKernStart() {
			return start;
		}
	}

	/**
	 * Character information for character which has next larger character
	 * associated.
	 */
	private class ListCharInfo extends CharInfo {

		/**
		 * init
		 * @param	w character width.
		 * @param	h character height.
		 * @param	d character depth.
		 * @param	i character italic correction.
		 * @param	n character code of the next larger character.
		 */
		public ListCharInfo(FixWord w, FixWord h, FixWord d, FixWord i, short n) {
			super(w, h, d, i);
			next = n;
		}

		/** 
		 * Next larger character code 
		 */
		private short next;

		/**
		 * @return	next larger character code.
		 */
		public short nextChar() {
			return next;
		}
	}

	/**
	 * Character information for character which has extensible recipe.
	 */
	private class ExtCharInfo extends CharInfo {

		/**
		 * init
		 * @param	w character width.
		 * @param	h character height.
		 * @param	d character depth.
		 * @param	i character italic correction.
		 * @param	t top part character code.
		 * @param	m middle part character code.
		 * @param	b bottom part character code.
		 * @param	r repeatable part character code.
		 */
		public ExtCharInfo(FixWord w, FixWord h, FixWord d, FixWord i, short t, short m, short b, short r) {
			super(w, h, d, i);
			top = t;
			mid = m;
			bot = b;
			rep = r;
		}

		/** 
		 * top part chracter code 
		 */
		private short top;

		/** 
		 * middle part chracter code 
		 */
		private short mid;

		/** 
		 * bottom part chracter code 
		 */
		private short bot;

		/** 
		 * repeatable part chracter code 
		 */
		private short rep;

		/**
		 * @return	code of top part character or <code>NO_CHAR_CODE</code> if there is no top part.
		 */
		public short extTop() {
			return top;
		}

		/**
		 * @return	code of middle part character or <code>NO_CHAR_CODE</code> if there is no middle part.
		 */
		public short extMid() {
			return mid;
		}

		/**
		 * @return	code of bottom part character or <code>NO_CHAR_CODE</code> if there is no bottom part.
		 */
		public short extBot() {
			return bot;
		}

		/**
		 * @return	code of repeatable part character or <code>NO_CHAR_CODE</code> if there is no repeatable part.
		 */
		public short extRep() {
			return rep;
		}
	}

	/**
	 * Converts the original tfm character infos to its final format and checks for validity.
	 * @exception	TFMException when some error stops loading.
	 */
	private void makeCharTable() throws TFMException {
		charTable = new CharInfo[charCount];
		for (int i = 0; i < charCount; i++) {
			charTable[i] = (charAuxTab[i].exists()) ? makeCharInfo(i) : null;
		}
	}

	/**
	 * Create one piece of character information in the final format for
	 * particular character.
	 * @param	pos the position of original character info in <code>charAuxTab</code>.
	 * @return	the final version of character information.
	 * @exception	TFMException when some error stops loading.
	 */
	private CharInfo makeCharInfo(int pos) throws TFMException {
		AuxCharInfo aci = charAuxTab[pos];
		FixWord wd = takeDimen(widthTable, aci.widthIndex(), pos, WD);
		FixWord ht = takeDimen(heightTable, aci.heightIndex(), pos, HT);
		FixWord dp = takeDimen(depthTable, aci.depthIndex(), pos, DP);
		FixWord ic = takeDimen(italicTable, aci.italicIndex(), pos, IC);
		switch (aci.tag()) {
			case LIG_TAG :
				return new LigCharInfo(wd, ht, dp, ic, aci.lig_kern_start);
			case LIST_TAG :
				if (validCharList(pos))
					return new ListCharInfo(wd, ht, dp, ic, aci.biggerChar());
				break;
			case EXT_TAG :
				if (aci.extenIndex() < extAuxCnt) {
					AuxExtRecipe aer = extAuxTab[aci.extenIndex()];
					return new ExtCharInfo(
						wd,
						ht,
						dp,
						ic,
						(aer.top != 0) ? aer.top : NO_CHAR_CODE,
						(aer.mid != 0) ? aer.mid : NO_CHAR_CODE,
						(aer.bot != 0) ? aer.bot : NO_CHAR_CODE,
						aer.rep);
				} else
					range_error(pos, "Extensible");
				break;
		}
		return new CharInfo(wd, ht, dp, ic);
	}

	/**
	 * Checks the consistency of larger character chain. It checks only the
	 * characters which have less position in |charTable| then the given
	 * character position and are supossed to have the corresponding
	 * <code>CharInfo</code> already created.
	 * @param	pos	position of currently processed character in <code>charTable</code>.
	 * @return	<code>true</code> if the associated chain is consistent.
	 * @exception	TFMException when some error stops loading.
	 */
	private boolean validCharList(int pos) throws TFMException {
		AuxCharInfo aci = charAuxTab[pos];
		short next = aci.biggerChar();
		if (!charExists(next)) {
			bad_char(next, "Character list link to");
			aci.resetTag();
			return false;
		}
		while ((next -= firstCharCode) < pos && (aci = charAuxTab[next]).tag() == LIST_TAG) {
			next = aci.biggerChar();
		}
		if (next == pos) {
			throw new TFMException("WARNING: Cycle in a character list!\nCharacter '" + octCharNum(pos) + " now ends the list.");
			// charAuxTab[pos].resetTag();
			// return false;
		}
		return true;
	}

	/**
	 * Gets referenced character dimension from apropriate table but checks
	 * for consistence first.
	 * @param	table	referenced table of dimensions.
	 * @param	i	referenced index to the dimension table.
	 * @param	pos	the position of character in <code>charTable</code> for error messages.
	 * @param	what	identification for error messages.
	 * @exception	TFMException when some error stops loading.
	 */
	private FixWord takeDimen(FixWord[] table, int i, int pos, String what) throws TFMException {
		if (i < table.length) {
			return table[i];
		}
		range_error(pos, what);
		return FixWord.ZERO;
	}

	/**
	 * Reports an inconsistent index of some character dimension in some table.
	 * @param	pos	the position of processed character info in <code>harTable</code>.
	 * @param	what	identification for error messages.
	 * @exception	TFMException if recoverable errors	stop loading.
	 */
	private void range_error(int pos, String what) throws TFMException {
		throw new TFMException("WARNING: " + what + " index for character '" + octCharNum(pos) + " is too large;\nso I reset it to zero.");
	}

	/**
	 * Checks the extensible recepies from tfm file for validity.
	 * @exception	TFMException when some error stops loading.
	 */
	private void checkExtens() throws TFMException {
		for (int i = 0; i < extAuxCnt; i++) {
			AuxExtRecipe aer = extAuxTab[i];
			if (aer.top != 0) {
				checkExt(aer.top);
			}
			if (aer.mid != 0) {
				checkExt(aer.mid);
			}
			if (aer.bot != 0) {
				checkExt(aer.bot);
			}
			checkExt(aer.rep);
		}
	}

	/**
	 * Checks one piece of extensible recipe for existence of used character.
	 * @param	the referenced character code.
	 * @exception	TFMException when some error stops loading.
	 */
	private void checkExt(short c) throws TFMException {
		if (!charExists(c)) {
			bad_char(c, "Extensible recipe involves the");
		}
	}

	/** 
	 * Size of coding scheme header information in 4 byte words 
	 */
	private static final int CODING_SIZE = 10;

	/** 
	 * Size of family header information in 4 byte words 
	 */
	private static final int FAMILY_SIZE = 5;

	/**
	 * The actual font metric type
	 * @see #VANILLA
	 * @see #MATHSY
	 * @see #MATHEX
	 */
	private byte fontType;

	/** 
	 * Normal TeX font metric type 
	 */
	private static final byte VANILLA = 0;

	/** 
	 * TeX Math Symbols font metric type 
	 */
	private static final byte MATHSY = 1;

	/** 
	 * TeX Math Extension font metric type 
	 */
	private static final byte MATHEX = 2;

	/** 
	 * The length of the whole tfm file (in words). 
	 */
	private int fileLength;

	/** 
	 * The length of the tfm file header. 
	 */
	private int headerLength;

	/** 
	 * Code of the first charactre present in this font 
	 */
	private short firstCharCode;

	/** 
	 * The number of character information structures in this tfm file 
	 */
	private int charCount;

	/** 
	 * The length of the width array (<code>widthTable</code>). 
	 */
	private int widthCount;

	/** 
	 * The length of the height array (<code>heightTable</code>). 
	 */
	private int heightCount;

	/** 
	 * The length of the depth array (<code>depthTable</code>). 
	 */
	private int depthCount;

	/** 
	 * The length of the italic correction array (<code>italicTable</code>). 
	 */
	private int italicCount;

	/** 
	 * The length of the raw lig/kern instruction table in tfm file 
	 */
	private int ligAuxLen;

	/** 
	 * The length of the kern amounts array (<code>kernTable</code>). 
	 */
	protected int kernCount;

	/** 
	 * The length of the raw extensible recipe table in tfm file 
	 */
	private int extAuxCnt;

	/** 
	 * The number of font dimension parameters (length of <code>paramTable</code>). 
	 */
	private int paramCount;

	/**
	 * Reads the lengths from tfm file.
	 * @exception	IOException if an I/O error ocurrs
	 */
	private void readLengths() throws TFMException, IOException {

		fileLength = readFileLength();
		headerLength = readLength();
		firstCharCode = readLength();
		short lastChar = readLength();
		widthCount = readLength();
		heightCount = readLength();
		depthCount = readLength();
		italicCount = readLength();
		ligAuxLen = readLength();
		kernCount = readLength();
		extAuxCnt = readLength();
		paramCount = readLength();

		if (headerLength < 2) {
			new TFMException("The header length is only " + headerLength + '!');
		}

		if (firstCharCode > lastChar + 1 || lastChar > 255) {
			throw new TFMException("The character code range " + firstCharCode + ".." + lastChar + "is illegal!");
		}

		charCount = lastChar + 1 - firstCharCode;

		if (charCount == 0)
			firstCharCode = 0;

		if (widthCount == 0 || heightCount == 0 || depthCount == 0 || italicCount == 0) {
			throw new TFMException("Incomplete subfiles for character dimensions!");
		}

		if (extAuxCnt > 256) {
			throw new TFMException("There are " + extAuxCnt + " extensible recipes!");
		}

		if (fileLength
			!= 6 + headerLength + charCount + widthCount + heightCount + depthCount + italicCount + ligAuxLen + kernCount + extAuxCnt + paramCount) {

			throw new TFMException("Subfile sizes don't add up to the stated total!");
		}
	}

	/**
	 * Reads the length of the whole tfm file (first 2 bytes).
	 * @exception	TFMException if an I/O error ocurrs
	 */
	private int readFileLength() throws TFMException, IOException {
		int i = readByte();
		if (i < 0) {
			throw new TFMException("The input file is empty!");
		}
		if (i > 127) {
			throw new TFMException("The first byte of the input file exceeds 127!");
		}

		int len = i << 8;
		i = readByte();
		if (i < 0) {
			throw new TFMException("The input file is only one byte long!");
		}
		len += i;
		if (len == 0) {
			throw new TFMException("The file claims to have length zero," + " but that's impossible!");
		}
		if (len < 6) {
			throw new TFMException("The file claims to have length " + len + " words, but it must be at least 6 words long!");
		}
		return len;
	}

	/**
	 * Reads on byte from the tfm file.
	 * @return		the positive value of the read byte.
	 * @exception	TFMException if an I/O error occurs 
	 */
	protected short readByte() throws TFMException, IOException {
		int i = in.read();
		if (i < 0) {
			throw new TFMException("The file has fewer bytes than it claims!");
		}
		return (short) i;
	}

	/**
	 * Reads 16 bit length value from the tfm file.
	 * @return	the lenght value
	 * @exception	TFMException if an I/O error occurs
	 */
	private short readLength() throws TFMException, IOException {
		short i = readByte();
		if ((i & 0x80) != 0) {
			throw new TFMException("One of the subfile sizes is negative!");
		}
		return (short) ((i << 8) + readByte());
	}

	/** 
	 * Design size of tfm-file 
	 */
	private FixWord designSize;

	/** 
	 * Font coding scheme 
	 */
	private String codingScheme = null;

	/** 
	 * Font family name
	 */
	private String family = null;

	/** 
	 * Font Xerox face code 
	 */
	private int face = -1;

	/** 
	 * True if only 7 bit character codes are used. 
	 */
	private boolean sevenBitSafe = false;

	/** 
	 * Uninterpreted rest of the header if any, <code>null</code> if there is not. 
	 */
	private int[] headerRest = null;

	/** 
	 * Starting index of uninterpreted rest of the header 
	 */
	private int restIndex = 0;

	/**
	 * Checksum
	 */
	private int checkSum = -1;

	/**
	 * Reads the header information and sets the actual font metric type.
	 * @exception	TFMException if an I/O error ocurrs
	 */
	private void readHeader() throws TFMException, IOException {

		int rest = headerLength;
		checkSum = readWord();
		FixWord dSize = readFixWord();

		if ((rest -= 2) >= CODING_SIZE) {
			codingScheme = readBCPL(4 * CODING_SIZE);
			fontType = getFontType(codingScheme);
			if ((rest -= CODING_SIZE) >= FAMILY_SIZE) {
				family = readBCPL(4 * FAMILY_SIZE);
				if ((rest -= FAMILY_SIZE) >= 1) {
					sevenBitSafe = (readByte() > 127);
					in.skip(2);
					face = readByte();
					if (--rest > 0) {
						headerRest = new int[rest];
						restIndex = headerLength - rest;
						for (int i = 0; i < rest; i++)
							headerRest[i] = readWord();
					}
				}
			}
		}

		if (dSize.lessThan(0)) {
			throw new TFMException("WARNING: Design size negative!\nI've set it to 10 points.");
			// dSize = FixWord.TEN;
		} else if (dSize.lessThan(1)) {
			throw new TFMException("WARNING: Design size too small!\nI've set it to 10 points.");
			//dSize = FixWord.TEN;
		}
		designSize = dSize;
	}

	/**
	 * Reads four bytes (32 bits) from the tfm file and returns them in an
	 * <code>int</code> in BigEndian order.
	 * @return	the integer value in BigEndian byte order.
	 * @exception	IOException if an I/O error occurs
	 */
	private int readWord() throws IOException {
		int i = readByte();
		i = (i << 8) + readByte();
		i = (i << 8) + readByte();
		return (i << 8) + readByte();
	}

	/**
	 * Reads four bytes from the tfm file and interpretes 
	 * them as a <code>FixWord</code> fraction.
	 * @return	the resulting fraction.
	 * @exception	IOException if an I/O error occurs or if the end of
	 *			file is reached.
	 */
	private final FixWord readFixWord() throws IOException {
		final int FIX_WORD_DENOMINATOR = 0x100000;
		return new FixWord(readWord(), FIX_WORD_DENOMINATOR);
	}

	/**
	 * Determines the actual font metric type based on character string value
	 * of coding scheme.
	 * @param	s the coding scheme.
	 * @return	the font metric type.
	 */
	private byte getFontType(String s) {
		if (s.startsWith("TEX MATH SY")) {
			return MATHSY;
		} else if (s.startsWith("TEX MATH EX")) {
			return MATHEX;
		}
		return VANILLA;
	}

	/**
	 * Reads a character string from the header given the size of appropriate
	 * area. The string is stored as its length in first byte then the string
	 * and the rest of area is not used.
	 * @param	size the size of string area in the header.
	 * @return	the read character string.
	 * @exception	IOException if an I/O error ocurrs
	 */
	private String readBCPL(int size) throws IOException {
		int len = readByte();
		if (len >= size) {
			throw new TFMException("WARNING: String is too long; I've shortened it drastically.");
			//len = 1;
		}
		size -= len + 1;
		StringBuffer buf = new StringBuffer(len);
		while (len-- > 0) {
			char c = (char) readByte();
			if (c == '(' || c == ')') {
				new TFMException("WARNING: Parenthesis in string has been changed to slash.");
				//c = '/';
			} else if (!(' ' <= c && c <= '~')) {
				throw new TFMException("WARNING: Nonstandard ASCII code has been blotted out.");
				//c = '?';
			} else
				c = Character.toUpperCase(c);
			buf.append(c);
		}
		in.skip(size);
		return buf.toString();
	}

	/** 
	 * Character information table in format close to tfm file 
	 */
	private AuxCharInfo[] charAuxTab;

	/** 
	 * The widths of characters in <code>charAuxTab</code> 
	 */
	private FixWord[] widthTable;

	/** 
	 * The heights of characters in <code>charAuxTab</code> 
	 */
	private FixWord[] heightTable;

	/** 
	 * The depths of characters in <code>charAuxTab</code> 
	 */
	private FixWord[] depthTable;

	/** 
	 * The italic corrections of characters in <code>charAuxTab<code> 
	 */
	private FixWord[] italicTable;

	/** 
	 * The instructions of lig/kern programs of characters in <code>charAuxTab</code> 
	 */
	private AuxLigKern[] ligAuxTab;

	/** 
	 * The kerning amounts of kerning instructions in <code>ligAuxTab</code> 
	 */
	private FixWord[] kernTable;

	/** 
	 * The extensible recipes of characters in <code>charAuxTab</code> 
	 */
	private AuxExtRecipe[] extAuxTab;

	/** 
	 * The font dimension parameters 
	 */
	private FixWord[] paramTable;

	/**
	 * Reads all the tables from tfm file.
	 * @exception	IOException if an I/O error ocurrs
	 */
	private void readTables() throws IOException {

		charAuxTab = new AuxCharInfo[charCount];
		for (int i = 0; i < charCount; i++) {
			charAuxTab[i] = new AuxCharInfo();
		}

		widthTable = readFixWords(widthCount);
		heightTable = readFixWords(heightCount);
		depthTable = readFixWords(depthCount);
		italicTable = readFixWords(italicCount);

		ligAuxTab = new AuxLigKern[ligAuxLen];
		for (int i = 0; i < ligAuxLen; i++) {
			ligAuxTab[i] = new AuxLigKern();
		}

		kernTable = readFixWords(kernCount);

		extAuxTab = new AuxExtRecipe[extAuxCnt];
		for (int i = 0; i < extAuxCnt; i++) {
			extAuxTab[i] = new AuxExtRecipe();
		}

		paramTable = readFixWords(paramCount);

		if (in.read() >= 0) {
			throw new TFMException("WARNING: There's some extra junk at the end of the TFM file,\n" + "but I'll proceed as if it weren't there.");
		}
	}

	/**
	 * Reads an array of <code>FixWords</code> from the tfm file.
	 * It reports fatal error message (and throws exception) if the end of file is reached.
	 * @param	count the number of fractions to be read.
	 * @return	the array of fractions.
	 * @exception	IOException if an I/O error occurs
	 */
	private FixWord[] readFixWords(int count) throws IOException {
		FixWord[] table = new FixWord[count];
		for (int i = 0; i < count; i++)
			table[i] = readFixWord();
		return table;
	}

	/**
	 * Data structure for raw character information from tfm file.
	 */
	private class AuxCharInfo {

		/**
		 * Creates <code>AuxCharInfo</code> by reading four bytes from the tfm file.
		 * @param	in the tfm byte input stream.
		 * @exception	IOException if an I/O error occures
		 */
		AuxCharInfo() throws IOException {
			_width_index = (byte) readByte();
			_height_depth_index = (byte) readByte();
			_italic_index_tag = (byte) readByte();
			_remainder = (byte) readByte();
		}

		/** 
		 * Index to the width table 
		 */
		private byte _width_index;

		/** 
		 * Indexes to the height and depth tables. 
		 */
		private byte _height_depth_index;

		/** 
		 * Index to the italic correction table and the tag 
		 */
		private byte _italic_index_tag;

		/** 
		 * Remainder which meaning is determined by value of tag 
		 */
		private byte _remainder;

		/**
		 * Index to newly created <code>ligKernTable</code> which is set during
		 * translation of the original raw lig/kern table in the tfm file.
		 */
		int lig_kern_start;

		/**
		 * Tells if the character of this <code>AuxCharInfo</code> exists in the font.
		 * @return	<code>true</code> if the character exists.
		 */
		boolean exists() {
			return _width_index != 0;
		}

		/**
		 * Gives the index to the width table from the tfm file.
		 * @return	the index to <code>widthTable</code>.
		 */
		int widthIndex() {
			return _width_index & 0xff;
		}

		/**
		 * Gives the index to the height table from the tfm file.
		 * @return	the index to <code>heightTable</code>.
		 */
		int heightIndex() {
			return _height_depth_index >> 4 & 0x0f;
		}

		/**
		 * Gives the index to the depth table from the tfm file.
		 * @return	the index to <code>depthTable</code>.
		 */
		int depthIndex() {
			return _height_depth_index & 0x0f;
		}

		/**
		 * Gives the index to the italic correction table from the tfm file.
		 * @return	the index to <code>italicTable</code>.
		 */
		int italicIndex() {
			return _italic_index_tag >> 2 & 0x3f;
		}

		/**
		 * Gives the tag field of the character information data.
		 * @return	the tag value.
		 */
		byte tag() {
			return (byte) (_italic_index_tag & 0x03);
		}

		/**
		 * Resets the tag field to NO_TAG (zero) value.
		 */
		void resetTag() {
			_italic_index_tag &= ~0x03;
		}

		/**
		 * Gives the value of remainder which meaning is dependent on the tag
		 * field value.
		 * @return	the uninterpreted tag.
		 */
		private short remainder() {
			return (short) (_remainder & 0xff);
		}

		/**
		 * Gives the remainder value interpreted as the index to the raw
		 * lig/kern table from tfm file.
		 * @return	starting of the lig/kern program in <code>ligAuxTab</code>.
		 */
		int ligStart() {
			return remainder();
		}

		/**
		 * Gives the remainder value interpreted as the code of next character
		 * in the chain of larger characters.
		 * @return	the next larger character.
		 */
		short biggerChar() {
			return remainder();
		}

		/**
		 * Gives the remainder value interpreted as the index to the table of
		 * extensible recipes from tfm file.
		 * @return	the index to the <code>extAuxTab</code>.
		 */
		int extenIndex() {
			return remainder();
		}
	}

	/**
	 * The data structure for lig/kern instruction from tfm file.
	 */
	private class AuxLigKern {

		/** 
		 * Value of <code>skip_byte()</code> which indicates the boundary information 
		 */
		private static final short BOUNDARY_FLAG = 255;

		/**
		 * Value of <code>skip_byte()</code> which indicates the last instruction in a
		 * lig/kern program.
		 */
		private static final short STOP_FLAG = 128;

		/** 
		 * Value of <code>op_byte()</code> which indicates the kerning instruction 
		 */
		private static final short KERN_FLAG = 128;

		/** 
		 * Amount of skip or a stop or boundary flag 
		 */
		private byte _skip_byte;

		/**
		 * Code of character which must be next to the current one to
		 * activate instruction.
		 */
		private byte _next_char;

		/** 
		 * Encoded ligature or kerning operation. 
		 */
		private byte _op_byte;

		/** 
		 * Remainder which meaning depends on the value of <code>op_byte</code>. 
		 */
		private byte _remainder;

		/**
		 * Gives the unsigned value of the <code>_skip_byte</code>.
		 * @return	the amount of skip or the stop or boundary flag.
		 */
		private short skip_byte() {
			return (short) (_skip_byte & 0xff);
		}

		/**
		 * Gives the unsigned value of the <code>_op_byte</code>.
		 * @return	the encoded ligature or kern operation.
		 */
		private short op_byte() {
			return (short) (_op_byte & 0xff);
		}

		/**
		 * Gives the unsigned value of uninterpreted remainder.
		 * @return	the remainder which meaning depends on the value
		 *		of <code>op_byte()</code>.
		 */
		private short remainder() {
			return (short) (_remainder & 0xff);
		}

		/**
		 * Tells whether this <code>AuxLigKern</code> contains information about boundary
		 * (it must be also first or last in the lig/kern table).
		 * @return	<code>true</code> if it contains boundary information.
		 */
		boolean meansBoundary() {
			return (skip_byte() == BOUNDARY_FLAG);
		}

		/**
		 * Tells whether this <code>AuxLigKern</code> redirects the actual start of
		 * a lig/kern program to some other instruction (it must be also the
		 * first instruction of some lig/kern program).
		 * @return	<code>true</code> if it is a restart instruction.
		 */
		boolean meansRestart() {
			return (skip_byte() > STOP_FLAG);
		}

		/**
		 * Tells whether this <code>AuxLigKern</code> is the last instruction of a
		 * lig/kern program.
		 * @return	<code>true</code> if this is the last instruction of a lig/kern	program.
		 */
		boolean meansStop() {
			return (skip_byte() >= STOP_FLAG);
		}

		/**
		 * Tells the position of the next lig/kern program instruction given
		 * the position of this <code>AuxLigKern</code> in the lig/kern table.
		 * @return	index to the <code>ligAuxTab</code> of the next lig/kern
		 *		instruction.
		 */
		int nextIndex(int pos) {
			return pos + skip_byte() + 1;
		}

		/**
		 * Forces this <code>AuxLigKern</code> to be the last instruction in a lig/kern program.
		 */
		void makeStop() {
			_skip_byte = (byte) STOP_FLAG;
		}

		/**
		 * Gives the code of the character which must be next to the current
		 * character if this instruction has to be activated.
		 * @return	the next character code.
		 */
		short nextChar() {
			return (short) (_next_char & 0xff);
		}

		/**
		 * Forces this <code>AuxLigKern</code> to have particular value of <code>nextChar()</code>.
		 * @param	c the forced value of <code>nextChar()</code>.
		 */
		void setNextChar(int c) {
			_next_char = (byte) c;
		}

		/**
		 * Gives actual starting index of the lig/kern program for restart
		 * instruction.
		 * @return	the actual start of lig/kern program.
		 */
		int restartIndex() {
			return (op_byte() << 8) + remainder();
		}

		/**
		 * Tells whether this <code>AuxLigKern</code> is a kerning instruction.
		 * @return	<code>true</code> for kerning instruction.
		 */
		boolean meansKern() {
			return op_byte() >= KERN_FLAG;
		}

		/**
		 * Gives the index to the kern table from tfm file for kerning
		 * instruction.
		 * @return	the index to the <code>kernTable</code>.
		 */
		int kernIndex() {
			return (op_byte() - KERN_FLAG << 8) + remainder();
		}

		/**
		 * Tells whether the current character should be left in place when
		 * executing this ligature instructions.
		 * @return	<code>true</code> if the current character should be left.
		 */
		boolean leaveLeft() {
			return (op_byte() & 0x02) != 0;
		}

		/**
		 * Tells whether the next character should be left in place when
		 * executing this ligature instructions.
		 * @return	<code>true</code> if the next character should be left.
		 */
		boolean leaveRight() {
			return (op_byte() & 0x01) != 0;
		}

		/**
		 * Tells how many character should be skipped over after executing
		 * this ligature instruction.
		 * @return the number of characters to be skipped.
		 */
		byte stepOver() {
			return (byte) (op_byte() >>> 2);
		}

		/**
		 * Gives the code of charcter which should be inserted between the
		 * current and the next characters when executing this ligature
		 * instruction.
		 * @return	the code of the character to be inserted.
		 */
		short ligChar() {
			return remainder();
		}

		/**
		 * Forces the <code>ligChar()</code> to have particular value.
		 * @param	c the forced value of <code>ligChar()</code>.
		 */
		void setLigChar(short c) {
			_remainder = (byte) c;
		}

		/**
		 * Creates <code>AuxLigKern</code> by reading four bytes from the tfm file.
		 * @exception	IOException if an I/O error occures
		 */
		AuxLigKern() throws IOException {
			_skip_byte = (byte) readByte();
			_next_char = (byte) readByte();
			_op_byte = (byte) readByte();
			_remainder = (byte) readByte();
		}

		/**
		 * The value of <code>activity</code> field which means that this lig/kern
		 * instruction is not a part of lig/kern program for any character.
		 */
		static final byte UNREACHABLE = 0;

		/**
		 * The value of <code>activity</code> field which means that this is restart
		 * instruction or the boundary information which was processed.
		 */
		static final byte PASS_THROUGH = 1;

		/**
		 * The value of <code>activity</code> field which means that this lig/kern
		 * instruction is a part of lig/kern program for some character.
		 */
		static final byte ACCESSIBLE = 2;

		/** 
		 * The flag determining the status of this lig/kern instruction. 
		 */
		byte activity = UNREACHABLE;

	}

	/**
	 * The data structure for extensible recipe from tfm file.
	 */
	private class AuxExtRecipe {

		/**
		 * Creates <code>AuxExtRecipe</code> by reading four bytes from the tfm file.
		 * @exception	IOException if an I/O error occures
		 */
		AuxExtRecipe() throws IOException {
			top = readByte();
			mid = readByte();
			bot = readByte();
			rep = readByte();
		}

		/** 
		 * Character code of the top part of extensible character. 
		 */
		short top;

		/** 
		 * Character code of the middle part of extensible character. 
		 */
		short mid;

		/** 
		 * Character code of the bottom part of extensible character. 
		 */
		short bot;

		/** 
		 * Character code of the repeatable part of extensible character. 
		 */
		short rep;
	}

	/**
	 * The following <code>String</code> constants are used in two places and we want
	 * them to be the same and they are also much shorter than the string
	 * literals.
	 */
	private static final String WD = "Width";
	private static final String HT = "Height";
	private static final String DP = "Depth";
	private static final String IC = "Italic correction";
	private static final String KR = "Kern";

	/**
	 * Checks all the tables readed from tfm file for inconsistency or malformation.
	 * @exception	TMFException when some error stops loading.
	 */
	private void checkTables() throws TFMException {

		checkParams();

		checkZeroDimen(widthTable, "width");
		checkZeroDimen(heightTable, "height");
		checkZeroDimen(depthTable, "depth");
		checkZeroDimen(italicTable, "italic");

		checkDimens(widthTable, 0, widthCount, WD);
		checkDimens(heightTable, 0, heightCount, HT);
		checkDimens(depthTable, 0, depthCount, DP);
		checkDimens(italicTable, 0, italicCount, IC);
		checkDimens(kernTable, 0, kernCount, KR);
	}

	/**
	 * Checks the font dimension parameter table for malformation.
	 * @exception	TFMException when some error stops loading.
	 */
	private void checkParams() throws TFMException {
		checkDimens(paramTable, 1, paramCount, "Parameter");

		switch (fontType) {
			case MATHSY :
				if (paramCount != 22) {
					throw new TFMException(
						"WARNING: Unusual number of fontdimen parameters" + " for a math symbols font (" + paramCount + " not 22).");
				}
				break;
			case MATHEX :
				if (paramCount != 13) {
					throw new TFMException("WARNING: Unusual number of fontdimen parameters" + " for an extension font (" + paramCount + " not 13).");
				}
				break;
		}
	}

	/**
	 * Checks a portion of a dimension table for malformation.
	 * Almost all dimension in tfm file must be less than 16 in its absolute
	 * value. The only two exceptions are <code>designSize</code> and <code>parmTable[0]</code>
	 *  --- the <code>slant</code> parameter. See TFtoPL[62].
	 * 
	 * @param	table the table of dimensions.
	 * @param	beg the starting index of checked dimensions.
	 * @param	end the index after the checked dimensions.
	 * @param	what identification for error messages.
	 * @exception	TFMException when some error stops loading.
	 */
	private void checkDimens(FixWord[] table, int beg, int end, String what) throws TFMException {
		for (; beg < end; beg++)
			if (!(table[beg].lessThan(16) && table[beg].moreThan(-16))) {
				throw new TFMException("WARNING: " + what + ' ' + beg + " is too big;\nI have set it to zero.");
				//table[beg] = FixWord.ZERO;
			}
	}

	/**
	 * Checks whether the first element of dimension table is zero.
	 * @param	table the checked dimension table.
	 * @param	what identification for error messages.
	 * @exception	TFMException when some error stops loading.
	 */
	private void checkZeroDimen(FixWord[] table, String what) throws TFMException {
		if (table[0].getValue() != 0) {
			throw new TFMException(what + "[0] should be zero.");
		} else {
			table[0] = FixWord.ZERO;
		}
	}

	/**
	 * Converts the lig/kern table information read from tfm file to a form
	 * suitable for metric object and check for errors.
	 * @exception	TFMxception when some error stops loading.
	 */
	private void makeLigTable() throws TFMException {
		if (ligAuxLen > 0) {
			setupBoundary();
		}
		buildLabels();
		promoteActivity();
		buildLigKernTable();
	}

	/**
	 * Fills in the blank <code>ligKernTable</code> by the final version of lig/kern instructions.
	 * @exception	TFMException when some error stops loading.
	 */
	private void buildLigKernTable() throws TFMException {
		int currIns = 0;
		for (int i = 0; i < ligAuxLen; i++) {
			setLigStarts(i, currIns);
			AuxLigKern alk = ligAuxTab[i];
			if (alk.activity != AuxLigKern.PASS_THROUGH) {
				if (!alk.meansRestart()) {
					checkLigKern(alk);
					int skip = getSkip(i);
					ligKernTable[currIns++] = (alk.meansKern()) ? makeKern(alk, skip) : makeLig(alk, skip);
				} else if (alk.restartIndex() > ligAuxLen) {
					throw new TFMException("WARNING: Ligature unconditional stop command" + " address is too big.");
				}
			}
		}
	}

	/**
	 * Creates a final version of ligature instruction after validity checks.
	 * @param	alk	the original version of lig/kern instruction.
	 * @param	skip	the offset of next lig/kern instruction in the final version of the lig/kern program.
	 * @exception	TFMException when some error stops loading.
	 */
	private LigKern makeLig(AuxLigKern alk, int skip) throws TFMException {
		if (!charExists(alk.ligChar())) {
			bad_char(alk.ligChar(), "Ligature step produces the");
			alk.setLigChar(firstCharCode);
		}
		boolean left = alk.leaveLeft();
		boolean right = alk.leaveRight();
		byte step = alk.stepOver();
		if (step > (left ? 1 : 0) + (right ? 1 : 0)) {
			throw new TFMException("WARNING: Ligature step with nonstandard code changed to LIG");
			// left = right = false;
			// step = 0;
		}
		return new Ligature(skip, alk.nextChar(), alk.ligChar(), left, right, step);
	}

	/**
	 * Creates a final version of kerning instruction after validity checks.
	 * @param	alk	the original version of lig/kern instruction.
	 * @param	skip	the offset of next lig/kern instruction in the final version of the lig/kern program.
	 * @exception	TFMException when some error stops loading.
	 */
	private LigKern makeKern(AuxLigKern alk, int skip) throws TFMException {
		int kernIdx = alk.kernIndex();
		FixWord kern;
		if (kernIdx < kernTable.length) {
			kern = kernTable[kernIdx];
		} else {
			throw new TFMException("WARNING: Kern index too large.");
			// kern = FixWord.ZERO;
		}
		return new Kerning(skip, alk.nextChar(), kern);
	}

	/**
	 * Gets the offset of next lig/kern instruction in a program based on
	 * counting only those intervene instructions which will be converted to
	 * final lig/kern program.
	 * @param	pos	the position of current lig/kern instruction in <code>ligAuxTable</code>.
	 * @return	the skip amount of the next instruction in the final version of lig/kern program.
	 */
	private int getSkip(int pos) {
		AuxLigKern alk = ligAuxTab[pos];
		if (alk.meansStop()) {
			return -1;
		}
		int skip = 0;
		int next = alk.nextIndex(pos);
		while (++pos < next) {
			if (ligAuxTab[pos].activity != AuxLigKern.PASS_THROUGH) {
				skip++;
			}
		}
		return skip;
	}

	/**
	 * Performs validity checks which are common to both (lig and kern) types
	 * of lig/kern instructions in tfm file.
	 * @param	alk the checked lig/kern instruction.
	 * @exception	TFMException when some error stops loading.
	 */
	private void checkLigKern(AuxLigKern alk) throws TFMException {
		if (!charExists(alk.nextChar()) && alk.nextChar() != boundaryChar) {
			bad_char(alk.nextChar(), ((alk.meansKern()) ? "Kern" : "Ligature") + " step for");
			alk.setNextChar(firstCharCode);
		}
	}

	/**
	 * Reports a reference to a nonexistent character.
	 * @param	c the checked character code.
	 * @param	s identification for error messages.
	 * @exception	TFMException if recoverable errors stop loading.
	 */
	private void bad_char(short c, String s) throws TFMException {
		throw new TFMException("WARNING: " + s + " nonexistent character '" + Integer.toOctalString(c) + '.');
	}

	/**
	 * Check the existence of particular character in the font.
	 * @param	c the checked character code.
	 * @return	<code>true</code> if the character is present.
	 */
	private boolean charExists(short c) {
		return ((c -= firstCharCode) >= 0 && c < charCount && charAuxTab[c].exists());
	}

	/**
	 * Records the starting indexes of final lig/kern program in
	 * <code>ligKernTable</code> to auxiliary character information field
	 * <code>lig_kern_start</code> of <code>AuxCharInfo</code>.
	 * @param	pos	the position of currently processed instruction in original tfm lig/kern table <code>ligAuxTab</code>.
	 * @param	start	the position of corresponding instruction in final lig/kern table <code>LigKernTable</code>.
	 */
	private void setLigStarts(int pos, int start) {
		IndexMultimap.Enum lab = labels.forKey(pos);
		while (lab.hasMore()) {
			int c = lab.next();
			if (c == BOUNDARY_LABEL)
				boundaryStart = start;
			else
				charAuxTab[c].lig_kern_start = start;
		}
	}

	/** 
	 * Lig/kern programs in the final format 
	 */
	private LigKern[] ligKernTable;

	/**
	 * Base class for <code>Ligature</code>/code>Kerning</code>| instructions.
	 * It handles the skip amount to the next instruction in the kern/lig
	 * program and the character code for the next character.
	 */
	private abstract class LigKern {

		/** 
		 * The skip amount 
		 */
		private int skip;

		/**
		 * Character code representing the character which must be next to the
		 * current one to activate this instruction.
		 */
		protected short nextChar;

		/**
		 * Constructs a lig/kern instruction with given skip amount to the
		 * next instruction in the lig/kern program.
		 * @param	skip the skip amount to the next instruction.
		 *		<code>0</code> means the folowing instruction is the next,
		 *		a number <code>< 0</code> means that there is no next instruction
		 *		(this is the last).
		 * @param	next the code of the next character.
		 */
		public LigKern(int skip, short next) {
			this.skip = skip;
			nextChar = next;
		}

		public FixWord getKern() {
			return FixWord.NULL;
		}

		/**
		 * Tells the index to the ligtable of the next instruction of lig/kern
		 * program for given index of this instruction.
		 * @param	pos the index of this instruction.
		 * @return	the index of the next instruction or <code>NO_INDEX</code>
		 *		if this is the last instruction of
		 *		the lig/kern program.
		 */
		public int nextIndex(int pos) {
			return (skip < 0) ? NO_INDEX : pos + skip + 1;
		}

	}

	/** 
	 * Ligature lig/kern instruction 
	 */
	private class Ligature extends LigKern {

		/**
		 * Makes new ligature instruction.
		 * See |LigKern| constructor for the details of the two first
		 * parameters.
		 * @param	skip the skip amount to the next instruction.
		 * @param	next the code of the next character.
		 * @param	a character code of ligature character to be inserted.
		 * @param	l indication that the current character should not be removed.
		 * @param	r indication that the next character should not be removed.
		 * @param	s number of characters from the current one to be stepped over after performing of this instruction.
		 */
		public Ligature(int skip, short next, short a, boolean l, boolean r, byte s) {
			super(skip, next);
			addingChar = a;
			keepLeft = l;
			keepRight = r;
			stepOver = s;
		}

		/**
		 * Character code representing the ligature character to be added
		 * between the current and next character in the text if this
		 * instruction is activated.
		 */
		private short addingChar;

		/**
		 * If some of the following flags are not set, the corresponding
		 * character in the text is removed after inserting the ligature
		 * character (in the process of constituing of ligatures).
		 */

		/** 
		 * Indication that the current character should not be removed 
		 */
		private boolean keepLeft;

		/** 
		 * Indication that the next character should not be removed 
		 */
		private boolean keepRight;

		/**
		 * Tells how many characters from the current position in the text
		 * should be skiped over after performing this instruction.
		 */
		private byte stepOver;

	}

	/** 
	 * Kerning lig/kern instruction 
	 */
	private class Kerning extends LigKern {

		/**
		 * Makes new ligature instruction.
		 * See <code>LigKern</code> constructor for the details of the two first parameters.
		 * @param	skip the skip amount to the next instruction.
		 * @param	next the code of the next character.
		 * @param	k the amount of kerning between the current and  the next characters.
		 */
		public Kerning(int skip, short next, FixWord k) {
			super(skip, next);
			kern = k;
		}

		/** 
		 * The amount of kerning 
		 */
		private FixWord kern;

		public FixWord getKern() {
			return kern;
		}
	}

	/**
	 * Marks the lig/kern instructions which are really a part of some
	 * lig/kern program (active), counts the final number of lig/kern
	 * instructions, creates the blank final lig/kern table and checks for
	 * errors. Uses <code>activity</code> field of <code>AuxLigKern</code> for marking the activity.
	 * It supposes that the first instructions of programs are already marked
	 * active.
	 * @exception	TFMException when some error stops loading.
	 */
	private void promoteActivity() throws TFMException {
		int ligKernLength = 0;
		for (int i = 0; i < ligAuxLen; i++) {
			AuxLigKern alk = ligAuxTab[i];
			if (alk.activity == AuxLigKern.ACCESSIBLE) {
				if (!alk.meansStop()) {
					int next = alk.nextIndex(i);
					if (next < ligAuxLen)
						ligAuxTab[next].activity = AuxLigKern.ACCESSIBLE;
					else {
						throw new TFMException("WARNING: Ligature/kern step " + i + " skips too far;\nI made it stop.");
						//alk.makeStop();
					}
				}
			}
			if (alk.activity != AuxLigKern.PASS_THROUGH)
				ligKernLength++;
		}
		ligKernTable = new LigKern[ligKernLength];
	}

	/** 
	 * Symbolic constant for nonexistent character code 
	 */
	private static final short NO_CHAR_CODE = -1;

	/** 
	 * Symbolic constant for index which is not valid 
	 */
	private static final int NO_INDEX = -1;

	/**
	 * Starting index of lig/kern program for invisible left boundary
	 * character or <code>NO_INDEX</code> if there is no such program.
	 */
	private int boundaryStart = NO_INDEX;

	/** 
	 * Invisible right boundary character code. 
	 */
	private short boundaryChar = NO_CHAR_CODE;

	/** 
	 * The associative table of lig/kern program starts in <code>ligAuxTab</code> 
	 */
	private IndexMultimap labels = new IndexMultimap();

	/** 
	 * Code for left boundary lig/kern program in <code>labels</code> table 
	 */
	private static final int BOUNDARY_LABEL = NO_CHAR_CODE;

	/**
	 * Tries to find the information about lig/kerns for word boundaries
	 * in tfm lig/kern table and checks for errors.
	 * @exception	TFMException when some error stops loading.
	 */
	private void setupBoundary() throws TFMException {

		AuxLigKern alk = ligAuxTab[0];
		if (alk.meansBoundary()) {
			boundaryChar = alk.nextChar();
			alk.activity = AuxLigKern.PASS_THROUGH;
		}

		alk = ligAuxTab[ligAuxLen - 1];
		if (alk.meansBoundary()) {
			int start = alk.restartIndex();
			alk.activity = AuxLigKern.PASS_THROUGH;
			if (start < ligAuxLen) {
				ligAuxTab[start].activity = AuxLigKern.ACCESSIBLE;
				labels.add(start, BOUNDARY_LABEL);
			} else {
				throw new TFMException("WARNING: Ligature/kern starting index for boundarychar " + "is too large;\nso I removed it.");
			}
		}
	}

	/** 
	 * Value of <code>AuxCharInfo.tag()</code> tagging normal character 
	 */
	private static final byte NO_TAG = 0;

	/**
	 * Value of <code>AuxCharInfo.tag()</code> tagging character with lig/kern program
	 */
	private static final byte LIG_TAG = 1;

	/**
	 * Value of <code>AuxCharInfo.tag()</code> tagging non last character in a chain
	 * of larger characters.
	 */
	private static final byte LIST_TAG = 2;

	/** 
	 * Value of <code>AuxCharInfo.tag()</code> which tagging with extensible recipe. 
	 */
	private static final byte EXT_TAG = 3;

	/**
	 * Builds associative table <code>labels</code> which maps the character codes to
	 * lig/kern program starting indexes in <code>ligAuxTab</code> for remapping later.
	 * It also marks the starting instructions of lig/kern programs as active
	 * (using the <code>ctivity</code> field of <code>AuxLigKern</code>).
	 * @exception	TFMException when some error stops loading.
	 */
	private void buildLabels() throws TFMException {
		for (int i = 0; i < charCount; i++) {
			if (charAuxTab[i].tag() == LIG_TAG) {
				int start = ligAuxStart(charAuxTab[i].ligStart());
				if (start < ligAuxLen) {
					labels.add(start, i);
					ligAuxTab[start].activity = AuxLigKern.ACCESSIBLE;
				} else {
					throw new TFMException(
						"WARNING: Ligature/kern starting index for character '" + octCharNum(i) + "\n is too large;\n" + "so I removed it.");
					//charAuxTab[i].resetTag();
				}
			}
		}
	}

	/**
	 * Finds out the actual starting index of lig/kern program in case there
	 * is a restart instructions and checks for validity.
	 * @param	start the starting index of lig/kern program given in a	character info.
	 * @return	the actual starting index.
	 */
	private int ligAuxStart(int start) {
		if (start < ligAuxLen) {
			AuxLigKern alk = ligAuxTab[start];
			if (alk.meansRestart()) {
				start = alk.restartIndex();
				if (start < ligAuxLen && alk.activity == AuxLigKern.UNREACHABLE)
					alk.activity = AuxLigKern.PASS_THROUGH;
			}
		}
		return start;
	}

	/**
	 * Error message identification of character given its position in
	 * <code>charTable</code>.
	 * @param	pos the position of referenced character info in <code>charTable</code>.
	 * @return	the string representation of character for error messages.
	 */
	private String octCharNum(int pos) {
		return Integer.toOctalString(pos + firstCharCode);
	}

	/**
	 * key-value-container
	 */
	private class KeyInt {

		int key;
		int val;

		/**
		 * init
		 */
		KeyInt(int k, int v) {
			key = k;
			val = v;
		}
	}

	/**
	 * <code>IndexMultimap</code> can store and retrieve <code>int</code> values associated to
	 * particular <code>int</code> key. There can be more values associated to the same key.
	 * This class can be replaced by any generic associative container which
	 * provides one to many mapping.
	 */
	private class IndexMultimap {

		/** 
		 * Internal storage of (key, value) pairs 
		 */
		private ArrayList data = new ArrayList();

		/**
		 * The number of (key, value) pairs kept.
		 * @return	the number of stored pairs.
		 */
		protected int size() {
			return data.size();
		}

		/**
		 * (key, value) pair at given position.
		 * @param	i the position of pair to be examined.
		 * @return	the pair at given position.
		 */
		protected KeyInt at(int i) {
			return (KeyInt) data.get(i);
		}

		/**
		 * Insert a (key, value) pair at the given position.
		 * @param	i the pair to be inserted.
		 * @param	i the position to be inserted to.
		 */
		protected void insert(KeyInt p, int i) {
			data.add(i, p);
		}

		/**
		 * Gives the position where a (key, value) pair with given key is stored
		 * or where it should be stored if there is no such pair.
		 * @param	key the key searched for.
		 * @return	the position.
		 */
		protected int search(int key) {
			int beg = 0;
			int end = size();
			while (beg < end) {
				int med = (beg + end) / 2;
				KeyInt p = at(med);
				if (key < p.key) {
					end = med;
				} else if (key > p.key) {
					beg = med + 1;
				} else {
					return med;
				}
			}
			return beg;
		}

		/**
		 * Adds a new (key, value) pair.
		 * @param	key the key of the new pair.
		 * @param	val the value of the new pair.
		 */
		public void add(int key, int val) {
			synchronized (data) {
				int pos = search(key);
				while (pos < size() && at(pos).key == key) {
					pos++;
				}
				insert(new KeyInt(key, val), pos);
			}
		}

		/**
		 * Class <code>Enum</code> provides the sequence of all values associated to
		 * particular key.
		 */
		public class Enum {

			/** 
			 * the current position in the sequence of pairs 
			 */
			private int pos;

			/** 
			 * the key for which the values are required 
			 */
			private final int key;

			/**
			 * Makes new |Enum| for given key.
			 * @param	k the key for which the values are required.
			 */
			/*
			 * The constructor is private so only the enclosing class can
			 * instantiate it.
			 */
			private Enum(int k) {
				synchronized (data) {
					key = k;
					pos = search(key);
					while (pos > 0 && at(pos - 1).key == key)
						pos--;
				}
			}

			/**
			 * Tests if there is another associated value.
			 * @return	<code>true</code> if next value is available, otherwise <code>false</code>.
			 */
			public boolean hasMore() {
				return (pos < size() && at(pos).key == key);
			}

			/**
			 * Gives the next value from the sequence of associated values.
			 * @return	the next value.
			 */
			public final int next() {
				return at(pos++).val;
			}
		}

		/**
		 * Gives the sequence of all keys associated to the given key.
		 * @param	key the given key.
		 * @return	the object representing the sequence of associated values.
		 */
		public Enum forKey(int key) {
			return new Enum(key);
		}
	}

	/**
	 * The dimensions are represented in the same way as in tfm files.
	 * Higher 12 bits is the whole part and lower 20 bits is the fractional
	 * part.
	 */
	private static class FixWord {

		public static final FixWord NULL = null;
		public static final FixWord ZERO = new FixWord(0);
		public static final FixWord UNITY = new FixWord(1);
		public static final FixWord TEN = new FixWord(10);

		/**
		 * init
		 * @param val	the values as int
		 */
		public FixWord(int val) {
			value = val << POINT_SHIFT;
		}

		/**
		 * init
		 * @param val	the values as num and den
		 */
		public FixWord(int num, int den) {
			value = ((long) num << POINT_SHIFT) / den;
		}

		/** 
		 * the internal value 
		 */
		private long value;

		/**
		 * POINT-SHIFT
		 */
		private static final int POINT_SHIFT = 20;

		/**
		 * Return the internal value 
		 * @return the internal value
		 */
		public long getValue() {
			return value;
		}

		/**
		 * less than 
		 * @param num  the value to compare
		 * @return	<code>true</code>, if the internal values is lesser, otherwise <code>false</code>
		 */
		public boolean lessThan(int num) {
			return (value < (num << POINT_SHIFT));
		}

		/**
		 * more than 
		 * @param num  the value to compare
		 * @return	<code>true</code>, if the internal values are more, otherwise <code>false</code>
		 */
		public boolean moreThan(int num) {
			return (value > (num << POINT_SHIFT));
		}

		/**
		 * Return the value as String in units.<p>
		 * It devide the value by 1000.
		 * @return the value as String in units
		 */
		public String toStringUnits() {
			if (value > 0) {
				return String.valueOf((value * 1000) >>> POINT_SHIFT);
			}
			return String.valueOf(- ((-value * 1000) >>> POINT_SHIFT));
		}

		/**
		 * Return the values as String
		 * @return the values as String
		 */
		public String toString() {
			StringBuffer buf = new StringBuffer();
			long v = value;
			final int UNITY = 1 << POINT_SHIFT;
			final int MASK = UNITY - 1;
			if (v < 0) {
				buf.append('-');
				v = -v;
			}
			buf.append(v >>> POINT_SHIFT);
			buf.append('.');
			v = 10 * (v & MASK) + 5;
			int delta = 10;
			do {
				if (delta > UNITY)
					v += UNITY / 2 - delta / 2;
				buf.append(Character.forDigit((int) (v >>> POINT_SHIFT), 10));
				v = 10 * (v & MASK);
			} while (v > (delta *= 10));
			return buf.toString();
		}
	}

	/**
	 * @see de.dante.util.font.FontMetric#getFontMetric()
	 */
	public Element getFontMetric() {

		// create efm-file
		Element root = new Element("fontgroup");
		root.setAttribute("name", family);
		root.setAttribute("id", family);
		root.setAttribute("default-size", designSize.toString());
		root.setAttribute("empr", "100");

		Element font = new Element("font");
		root.addContent(font);

		font.setAttribute("font-name", family);
		font.setAttribute("font-family", family);
		root.setAttribute("units-per-em", "1000");

		switch (fontType) {
			case MATHSY :
				font.setAttribute("type", "tfm-mathsyml");
				if (paramTable.length > 0) {
					final String[] paramLabel =
						{
							"SLANT",
							"SPACE",
							"STRETCH",
							"SHRINK",
							"XHEIGHT",
							"QUAD",
							"EXTRASPACE",
							"NUM1",
							"NUM2",
							"NUM3",
							"DENOM1",
							"DENOM2",
							"SUP1",
							"SUP2",
							"SUP3",
							"SUB1",
							"SUB2",
							"SUPDROP",
							"SUBDROP",
							"DELIM1",
							"DELIM2",
							"AXISHEIGHT" };
					for (int i = 0; i < paramTable.length; i++) {
						root.setAttribute(paramLabel[i], paramTable[i].toStringUnits());
					}
				}
				break;
			case MATHEX :
				font.setAttribute("type", "tfm-mathext");
				if (paramTable.length > 0) {
					final String[] paramLabel =
						{
							"SLANT",
							"SPACE",
							"STRETCH",
							"SHRINK",
							"XHEIGHT",
							"QUAD",
							"EXTRASPACE",
							"DEFAULTRULETHICKNESS",
							"BIGOPSPACING1",
							"BIGOPSPACING2",
							"BIGOPSPACING3",
							"BIGOPSPACING4",
							"BIGOPSPACING5",
							};
					for (int i = 0; i < paramTable.length; i++) {
						root.setAttribute(paramLabel[i], paramTable[i].toStringUnits());
					}
				}
				break;
			default :
				font.setAttribute("type", "tfm-normal");
				if (paramTable.length > 0) {

					final String[] paramLabel = { "SLANT", "SPACE", "STRETCH", "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE" };

					for (int i = 0; i < paramTable.length; i++) {
						root.setAttribute(paramLabel[i], paramTable[i].toStringUnits());
					}
				}
				break;
		}

		for (int i = 0; i < charTable.length; i++) {

			// get char
			CharInfo ci = getCharInfo((short) (i + firstCharCode));

			if (ci != null) {

				// create  glyph
				Element glyph = new Element("glyph");

				glyph.setAttribute("ID", String.valueOf(i));
				glyph.setAttribute("glyph-number", String.valueOf(i));
				String c = Character.toString((char) i);
				if (c != null && c.trim().length() > 0) {
					glyph.setAttribute("glyph-name", c);
				}

				glyph.setAttribute("width", ci.getWidth().toStringUnits());
				glyph.setAttribute("height", ci.getHeight().toStringUnits());
				glyph.setAttribute("depth", ci.getDepth().toStringUnits());
				glyph.setAttribute("italic", ci.getItalic().toStringUnits());

				// ligature
				int ligstart = ci.ligKernStart();
				if (ligstart != NO_INDEX) {

					for (int k = ligstart; k != NO_INDEX; k = ligKernTable[k].nextIndex(k)) {
						LigKern lk = ligKernTable[k];

						if (lk instanceof Ligature) {
							Ligature lig = (Ligature) lk;

							Element ligature = new Element("ligature");

							ligature.setAttribute("letter-id", String.valueOf(lig.nextChar));
							String sl = Character.toString((char) lig.nextChar);
							if (sl != null && sl.trim().length() > 0) {
								ligature.setAttribute("letter", sl.trim());
							}

							ligature.setAttribute("lig-id", String.valueOf(lig.addingChar));
							String slig = Character.toString((char) lig.addingChar);
							if (slig != null && slig.trim().length() > 0) {
								ligature.setAttribute("lig", slig.trim());
							}
							glyph.addContent(ligature);
						} else if (lk instanceof Kerning) {
							Kerning kern = (Kerning) lk;

							Element kerning = new Element("kerning");

							kerning.setAttribute("glyph-id", String.valueOf(kern.nextChar));
							String sk = Character.toString((char) kern.nextChar);
							if (sk != null && sk.trim().length() > 0) {
								kerning.setAttribute("glyph-name", sk.trim());
							}
							kerning.setAttribute("size", kern.getKern().toStringUnits());

							glyph.addContent(kerning);
						}
					}
				}
				font.addContent(glyph);
			}
		}
		return root;
	}

	/**
	 * Return the <code>CharInfo</code> for index <code>idx</code>.
	 * @param idx	the index
	 * @return	return the <code>CharInfo</code> or <code>null</code>, if not exists
	 */
	private CharInfo getCharInfo(short idx) {
		return (0 <= (idx -= firstCharCode) && idx < charTable.length) ? charTable[idx] : null;
	}
}
