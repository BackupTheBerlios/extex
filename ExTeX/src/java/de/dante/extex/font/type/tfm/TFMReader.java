/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.tfm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jdom.Element;

import de.dante.extex.font.type.FontMetric;
import de.dante.extex.font.type.tfm.enc.EncFactory;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontEncoding;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.extex.i18n.HelpingException;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class read a TFM-file.
 * 
 * @see <a href="package-summary.html#TFMformat">TFM-Format</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class TFMReader implements FontMetric {

    /**
     * InputStream for Reading
     */
    private InputStream in;

    /**
     * fontname
     */
    private String fontname;

    /**
     * psfontmap
     */
    private PSFontsMapReader psfontmap;

    /**
     * Encoderfactory
     */
    private EncFactory encfactory;

    /**
     * Create e new object.
     *
     * @see <a href="package-summary.html#TFMformat">TFM-Format</a> 
     *
     * @param ins the InputStream for reading
     * @param afontname fontname
     * @param apsfontmap psfontmap
     * @param encf encoderfactroy
     * @throws IOException if a IO-error occured
     * @throws HelpingException if a error occured
     */
    public TFMReader(final InputStream ins, final String afontname,
            final PSFontsMapReader apsfontmap, final EncFactory encf)
            throws IOException, HelpingException {

        in = new BufferedInputStream(ins);
        fontname = afontname;
        psfontmap = apsfontmap;
        encfactory = encf;

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
    private TFMCharInfo[] charTable;

    /**
     * Converts the original tfm character infos to its final format and checks
     * for validity.
     *
     * @throws IOException if a IO-error occured
     * @throws HelpingException if e error occured
     */
    private void makeCharTable() throws IOException, HelpingException {

        charTable = new TFMCharInfo[charCount];
        for (int i = 0; i < charCount; i++) {
            charTable[i] = (charAuxTab[i].exists()) ? makeCharInfo(i) : null;
        }
    }

    /**
     * Create one piece of character information in the final format for
     * particular character.
     *
     * @param pos the position of original character info in <code>charAuxTab</code>.
     * @return the final version of character information.
     * @throws IOException if a IO-error occured
     * @throws HelpingException if a error occured
     */
    private TFMCharInfo makeCharInfo(final int pos) throws IOException,
            HelpingException {

        TFMAuxCharInfo aci = charAuxTab[pos];
        TFMFixWord wd = takeDimen(widthTable, aci.widthIndex(), pos, WD);
        TFMFixWord ht = takeDimen(heightTable, aci.heightIndex(), pos, HT);
        TFMFixWord dp = takeDimen(depthTable, aci.depthIndex(), pos, DP);
        TFMFixWord ic = takeDimen(italicTable, aci.italicIndex(), pos, IC);
        switch (aci.tag()) {
            case LIGTAG :
                return new TFMLigCharInfo(wd, ht, dp, ic, aci.getLigkernstart());
            case LISTTAG :
                if (validCharList(pos)) {
                    return new TFMListCharInfo(wd, ht, dp, ic, aci.biggerChar());
                }
                break;
            case EXTTAG :
                if (aci.extenIndex() < extAuxCnt) {
                    TFMAuxExtRecipe aer = extAuxTab[aci.extenIndex()];
                    return new TFMExtCharInfo(wd, ht, dp, ic,
                            (aer.getTop() != 0)
                                    ? aer.getTop()
                                    : TFMCharInfo.NOCHARCODE,
                            (aer.getMid() != 0)
                                    ? aer.getMid()
                                    : TFMCharInfo.NOCHARCODE,
                            (aer.getBot() != 0)
                                    ? aer.getBot()
                                    : TFMCharInfo.NOCHARCODE, aer.getRep());
                } else {
                    rangeerror(pos, "Extensible");
                }
                break;
        }
        return new TFMCharInfo(wd, ht, dp, ic);
    }

    /**
     * Checks the consistency of larger character chain. It checks only the
     * characters which have less position in |charTable| then the given
     * character position and are supossed to have the corresponding <code>CharInfo</code>
     * already created.
     *
     * @param pos position of currently processed character in <code>charTable</code>.
     * @return <code>true</code> if the associated chain is consistent.
     * @throws HelpingException when some error stops loading.
     */
    private boolean validCharList(final int pos) throws HelpingException {

        TFMAuxCharInfo aci = charAuxTab[pos];
        short next = aci.biggerChar();
        if (!charExists(next)) {
            badchar(next, "Character list link to");
            aci.resetTag();
            return false;
        }
        while ((next -= firstCharCode) < pos
                && (aci = charAuxTab[next]).tag() == LISTTAG) {
            next = aci.biggerChar();
        }
        if (next == pos) {
            throw new HelpingException("TFM.cycleinchar", String
                    .valueOf(octCharNum(pos)));
        }
        return true;
    }

    /**
     * Gets referenced character dimension from apropriate table but checks for
     * consistence first.
     *
     * @param table referenced table of dimensions.
     * @param i referenced index to the dimension table.
     * @param pos the position of character in <code>charTable</code> for
     * error messages.
     * @param what identification for error messages.
     * @return the FixWord
     * @throws HelpingException when some error stops loading.
     */
    private TFMFixWord takeDimen(final TFMFixWord[] table, final int i,
            final int pos, final String what) throws HelpingException {

        if (i < table.length) {
            return table[i];
        }
        rangeerror(pos, what);
        return TFMFixWord.ZERO;
    }

    /**
     * Reports an inconsistent index of some character dimension in some table.
     *
     * @param pos the position of processed character info in <code>harTable</code>.
     * @param what identification for error messages.
     * @throws HelpingException if recoverable errors stop loading.
     */
    private void rangeerror(final int pos, final String what)
            throws HelpingException {

        throw new HelpingException("TFM.charindexerror", what, String
                .valueOf(octCharNum(pos)));
    }

    /**
     * Checks the extensible recepies from tfm file for validity.
     *
     * @throws HelpingException when some error stops loading.
     */
    private void checkExtens() throws HelpingException {

        for (int i = 0; i < extAuxCnt; i++) {
            TFMAuxExtRecipe aer = extAuxTab[i];
            if (aer.getTop() != 0) {
                checkExt(aer.getTop());
            }
            if (aer.getMid() != 0) {
                checkExt(aer.getMid());
            }
            if (aer.getBot() != 0) {
                checkExt(aer.getBot());
            }
            checkExt(aer.getRep());
        }
    }

    /**
     * Checks one piece of extensible recipe for existence of used character.
     *
     * @param c the referenced character code.
     * @throws HelpingException when some error stops loading.
     */
    private void checkExt(final short c) throws HelpingException {

        if (!charExists(c)) {
            badchar(c, "Extensible recipe involves the");
        }
    }

    /**
     * Size of coding scheme header information in 4 byte words
     */
    private static final int CODINGSIZE = 10;

    /**
     * Size of family header information in 4 byte words
     */
    private static final int FAMILYSIZE = 5;

    /**
     * The actual font metric type
     *
     * @see #VANILLA
     * @see #MATHSY
     * @see #MATHEX
     */
    private FontMetricType fontType;

    /**
     * Normal TeX font metric type
     */
    private static final FontMetricType VANILLA = new FontMetricType();

    /**
     * TeX Math Symbols font metric type
     */
    private static final FontMetricType MATHSY = new FontMetricType();

    /**
     * TeX Math Extension font metric type
     */
    private static final FontMetricType MATHEX = new FontMetricType();

    /**
     * This is a type-safe class to represent fontmetrictype information.
     */
    private static final class FontMetricType {

        /**
         * Creates a new object.
         */
        public FontMetricType() {

            super();
        }
    }

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
    private int kernCount;

    /**
     * The length of the raw extensible recipe table in tfm file
     */
    private int extAuxCnt;

    /**
     * The number of font dimension parameters (length of <code>paramTable</code>).
     */
    private int paramCount;

    /**
     * max chars
     */
    private static final int MAXCHARS = 255;

    /**
     * Bytes in the Header of the TFM-file
     */
    private static final int HEADERBYTES = 6;

    /**
     * Reads the lengths from tfm file.
     *
     * @throws IOException if an I/O error ocurrs
     * @throws HelpingException in an TFM error ocurrs
     */
    private void readLengths() throws IOException, HelpingException {

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
            throw new HelpingException("TFM.wrongheaderlength", String
                    .valueOf(headerLength));
        }

        if (firstCharCode > lastChar + 1 || lastChar > MAXCHARS) {
            throw new HelpingException("TFM.wrongcoderange", String
                    .valueOf(firstCharCode), String.valueOf(lastChar));
        }

        charCount = lastChar + 1 - firstCharCode;

        if (charCount == 0) {
            firstCharCode = 0;
        }

        if (widthCount == 0 || heightCount == 0 || depthCount == 0
                || italicCount == 0) {
            throw new HelpingException("TFM.incompletesubfiles");
        }

        if (extAuxCnt > MAXCHARS + 1) {
            throw new HelpingException("TFM.extensiblerecipes", String
                    .valueOf(extAuxCnt));
        }

        if (fileLength != HEADERBYTES + headerLength + charCount + widthCount
                + heightCount + depthCount + italicCount + ligAuxLen
                + kernCount + extAuxCnt + paramCount) {

            throw new HelpingException("TFM.subfileswrongsize");
        }
    }

    /**
     * Reads the length of the whole tfm file (first 2 bytes).
     *
     * @return Returns the length of the whole tfm file.
     * @throws IOException if an IO-error occured
     * @throws HelpingException if an error occured
     */
    private int readFileLength() throws IOException, HelpingException {

        int i = readByte();
        if (i < 0) {
            throw new HelpingException("TFM.emptyinputfile");
        }
        if (i > 127) {
            throw new HelpingException("TFM.firstbyteerror");
        }

        int len = i << 8;
        i = readByte();
        if (i < 0) {
            throw new HelpingException("TFM.onlyonebyte");
        }
        len += i;
        if (len == 0) {
            throw new HelpingException("TFM.lengthzero");
        }
        if (len < 6) {
            throw new HelpingException("TFM.wrongwordsize", String.valueOf(len));
        }
        return len;
    }

    /**
     * Reads on byte from the tfm file.
     *
     * @return the positive value of the read byte.
     * @throws IOException if an IO-error occured
     * @throws HelpingException if an error occured
     */
    private short readByte() throws IOException, HelpingException {

        int i = in.read();
        if (i < 0) {
            throw new HelpingException("TFM.filefewerbytes=");
        }
        return (short) i;
    }

    /**
     * Reads 16 bit length value from the tfm file.
     *
     * @return the lenght value
     * @throws IOException if an IO-error occured
     * @throws HelpingException if an error occured
     */
    private short readLength() throws IOException, HelpingException {

        short i = readByte();
        if ((i & 0x80) != 0) {
            throw new IOException("One of the subfile sizes is negative!");
        }
        return (short) ((i << 8) + readByte());
    }

    /**
     * Design size of tfm-file
     */
    private TFMFixWord designSize;

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
     * Uninterpreted rest of the header if any, <code>null</code> if there is
     * not.
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
     *
     * @throws IOException if an I/O error occured
     * @throws HelpingException if an error occured
     */
    private void readHeader() throws IOException, HelpingException {

        int rest = headerLength;
        checkSum = readWord();
        TFMFixWord dSize = readFixWord();

        if ((rest -= 2) >= CODINGSIZE) {
            codingScheme = readBCPL(4 * CODINGSIZE);
            fontType = getFontType(codingScheme);
            if ((rest -= CODINGSIZE) >= FAMILYSIZE) {
                family = readBCPL(4 * FAMILYSIZE);
                if ((rest -= FAMILYSIZE) >= 1) {
                    sevenBitSafe = (readByte() > 127);
                    in.skip(2);
                    face = readByte();
                    if (--rest > 0) {
                        headerRest = new int[rest];
                        restIndex = headerLength - rest;
                        for (int i = 0; i < rest; i++) {
                            headerRest[i] = readWord();
                        }
                    }
                }
            }
        }

        if (dSize.lessThan(0)) {
            throw new HelpingException("TFM.designsizenegative");
        } else if (dSize.lessThan(1)) {
            throw new HelpingException("Design size too small");
        }
        designSize = dSize;
    }

    /**
     * Reads four bytes (32 bits) from the tfm file and returns them in an
     * <code>int</code> in BigEndian order.
     *
     * @return the integer value in BigEndian byte order.
     * @throws IOException if an I/O error occured
     * @throws HelpingException if an error occured
     */
    private int readWord() throws IOException, HelpingException {

        int i = readByte();
        i = (i << 8) + readByte();
        i = (i << 8) + readByte();
        return (i << 8) + readByte();
    }

    /**
     * fixdominator
     */
    private static final int FIXWORDDENOMINATOR = 0x100000;

    /**
     * Reads four bytes from the tfm file and interpretes them as a <code>FixWord</code>
     * fraction.
     *
     * @return the resulting fraction.
     * @throws IOException if an I/O error occurs or 
     *         if the end of file is reached.
     * @throws HelpingException if an error occured.
     */
    private TFMFixWord readFixWord() throws IOException, HelpingException {

        return new TFMFixWord(readWord(), FIXWORDDENOMINATOR);
    }

    /**
     * Determines the actual font metric type based on character string value
     * of coding scheme.
     *
     * @param s the coding scheme.
     * @return the font metric type.
     */
    private FontMetricType getFontType(final String s) {

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
     *
     * @param size the size of string area in the header.
     * @return the read character string.
     * @throws IOException if an I/O error occured
     * @throws HelpingException if an error occured
     */
    private String readBCPL(int size) throws IOException, HelpingException {

        int len = readByte();
        if (len >= size) {
            throw new HelpingException("TFM.stringtoolong");
        }
        size -= len + 1;
        StringBuffer buf = new StringBuffer(len);
        while (len-- > 0) {
            char c = (char) readByte();
            if (c == '(' || c == ')') {
                throw new HelpingException("TFM.parenthiesinstring");
            } else if (!(' ' <= c && c <= '~')) {
                throw new HelpingException("TFM.nonstandardcode");
            } else {
                c = Character.toUpperCase(c);
            }
            buf.append(c);
        }
        in.skip(size);
        return buf.toString();
    }

    /**
     * Character information table in format close to tfm file
     */
    private TFMAuxCharInfo[] charAuxTab;

    /**
     * The widths of characters in <code>charAuxTab</code>
     */
    private TFMFixWord[] widthTable;

    /**
     * The heights of characters in <code>charAuxTab</code>
     */
    private TFMFixWord[] heightTable;

    /**
     * The depths of characters in <code>charAuxTab</code>
     */
    private TFMFixWord[] depthTable;

    /**
     * The italic corrections of characters in <code>charAuxTab</code>
     */
    private TFMFixWord[] italicTable;

    /**
     * The instructions of lig/kern programs of characters in <code>charAuxTab</code>
     */
    private TFMAuxLigKern[] ligAuxTab;

    /**
     * The kerning amounts of kerning instructions in <code>ligAuxTab</code>
     */
    private TFMFixWord[] kernTable;

    /**
     * The extensible recipes of characters in <code>charAuxTab</code>
     */
    private TFMAuxExtRecipe[] extAuxTab;

    /**
     * The font dimension parameters
     */
    private TFMFixWord[] paramTable;

    /**
     * Reads all the tables from tfm file.
     *
     * @throws IOException if an I/O error ocurrs
     * @throws HelpingException if an I/O error ocurrs 
     */
    private void readTables() throws IOException, HelpingException {

        charAuxTab = new TFMAuxCharInfo[charCount];
        for (int i = 0; i < charCount; i++) {
            byte widthindex = (byte) readByte();
            byte heightdepthindex = (byte) readByte();
            byte italicindextag = (byte) readByte();
            byte remainder = (byte) readByte();

            charAuxTab[i] = new TFMAuxCharInfo(widthindex, heightdepthindex,
                    italicindextag, remainder);
        }

        widthTable = readFixWords(widthCount);
        heightTable = readFixWords(heightCount);
        depthTable = readFixWords(depthCount);
        italicTable = readFixWords(italicCount);

        ligAuxTab = new TFMAuxLigKern[ligAuxLen];
        for (int i = 0; i < ligAuxLen; i++) {
            byte skipbyte = (byte) readByte();
            byte nextchar = (byte) readByte();
            byte opbyte = (byte) readByte();
            byte remainder = (byte) readByte();

            ligAuxTab[i] = new TFMAuxLigKern(skipbyte, nextchar, opbyte,
                    remainder);
        }

        kernTable = readFixWords(kernCount);

        extAuxTab = new TFMAuxExtRecipe[extAuxCnt];
        for (int i = 0; i < extAuxCnt; i++) {
            short top = readByte();
            short mid = readByte();
            short bot = readByte();
            short rep = readByte();

            extAuxTab[i] = new TFMAuxExtRecipe(top, mid, bot, rep);
        }

        paramTable = readFixWords(paramCount);

        if (in.read() >= 0) {
            throw new HelpingException("TFM.extrajunk");
        }
    }

    /**
     * Reads an array of <code>FixWords</code> from the tfm file. It reports
     * fatal error message (and throws exception) if the end of file is
     * reached.
     *
     * @param count the number of fractions to be read.
     * @return the array of fractions.
     * @throws IOException if an I/O error occurs
     * @throws HelpingException if an error occurs
     */
    private TFMFixWord[] readFixWords(final int count) throws IOException,
            HelpingException {

        TFMFixWord[] table = new TFMFixWord[count];
        for (int i = 0; i < count; i++) {
            table[i] = readFixWord();
        }
        return table;
    }

    /*
     * The following <code>String</code> constants are used in two places and
     * we want them to be the same and they are also much shorter than the
     * string literals.
     */
    /**
     * width
     */
    private static final String WD = "Width";

    /**
     * height
     */
    private static final String HT = "Height";

    /**
     * depth
     */
    private static final String DP = "Depth";

    /**
     * italic correction
     */
    private static final String IC = "Italic correction";

    /**
     * kern
     */
    private static final String KR = "Kern";

    /**
     * Checks all the tables readed from tfm file for inconsistency or
     * malformation.
     *
     * @throws IOException when some error stops loading.
     * @throws HelpingException if an error occured
     */
    private void checkTables() throws IOException, HelpingException {

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
     *
     * @throws IOException when some error stops loading.
     * @throws HelpingException if an error occured
     */
    private void checkParams() throws IOException, HelpingException {

        checkDimens(paramTable, 1, paramCount, "Parameter");

        if (fontType == MATHSY) {
            if (paramCount != 22) {
                throw new HelpingException("TFM.unusualnumber", String
                        .valueOf(paramCount));
            }
        } else if (fontType == MATHEX) {
            if (paramCount != 13) {
                throw new HelpingException("TFM.unusualnumberext", String
                        .valueOf(paramCount));
            }
        }
    }

    /**
     * Checks a portion of a dimension table for malformation. Almost all
     * dimension in tfm file must be less than 16 in its absolute value. The
     * only two exceptions are <code>designSize</code> and <code>parmTable[0]</code>
     * --- the <code>slant</code> parameter. See TFtoPL[62].
     *
     * @param table the table of dimensions.
     * @param beg the starting index of checked dimensions.
     * @param end the index after the checked dimensions.
     * @param what identification for error messages.
     * @throws HelpingException when some error stops loading.
     */
    private void checkDimens(final TFMFixWord[] table, int beg, int end,
            final String what) throws HelpingException {

        for (; beg < end; beg++) {
            if (!(table[beg].lessThan(16) && table[beg].moreThan(-16))) {
                throw new HelpingException("TFM.xxxtoobig", what, String
                        .valueOf(beg));
            }
        }
    }

    /**
     * Checks whether the first element of dimension table is zero.
     *
     * @param table the checked dimension table.
     * @param what identification for error messages.
     * @throws HelpingException when some error stops loading.
     */
    private void checkZeroDimen(final TFMFixWord[] table, final String what)
            throws HelpingException {

        if (table[0].getValue() != 0) {
            throw new HelpingException("TFM.dimenzero", what);
        } else {
            table[0] = TFMFixWord.ZERO;
        }
    }

    /**
     * Converts the lig/kern table information read from tfm file to a form
     * suitable for metric object and check for errors.
     *
     * @throws HelpingException when some error stops loading.
     */
    private void makeLigTable() throws HelpingException {

        if (ligAuxLen > 0) {
            setupBoundary();
        }
        buildLabels();
        promoteActivity();
        buildLigKernTable();
    }

    /**
     * Fills in the blank <code>ligKernTable</code> by the final version of
     * lig/kern instructions.
     *
     * @throws HelpingException when some error stops loading.
     */
    private void buildLigKernTable() throws HelpingException {

        int currIns = 0;
        for (int i = 0; i < ligAuxLen; i++) {
            setLigStarts(i, currIns);
            TFMAuxLigKern alk = ligAuxTab[i];
            if (alk.getActivity() != TFMAuxLigKern.PASSTHROUGH) {
                if (!alk.meansRestart()) {
                    checkLigKern(alk);
                    int skip = getSkip(i);
                    ligKernTable[currIns++] = (alk.meansKern()) ? makeKern(alk,
                            skip) : makeLig(alk, skip);
                } else if (alk.restartIndex() > ligAuxLen) {
                    throw new HelpingException("TFM.ligadresstoobig");

                }
            }
        }
    }

    /**
     * Creates a final version of ligature instruction after validity checks.
     *
     * @param alk the original version of lig/kern instruction.
     * @param skip the offset of next lig/kern instruction in the final version
     * of the lig/kern program.
     * @return ligkern
     * @throws HelpingException when some error stops loading.
     */
    private TFMLigKern makeLig(final TFMAuxLigKern alk, final int skip)
            throws HelpingException {

        if (!charExists(alk.ligChar())) {
            badchar(alk.ligChar(), "Ligature step produces the");
            alk.setLigChar(firstCharCode);
        }
        boolean left = alk.leaveLeft();
        boolean right = alk.leaveRight();
        byte step = alk.stepOver();
        if (step > (left ? 1 : 0) + (right ? 1 : 0)) {
            throw new HelpingException("TFM.nonstandardcode");
        }
        return new TFMLigature(skip, alk.nextChar(), alk.ligChar(), left,
                right, step);
    }

    /**
     * Creates a final version of kerning instruction after validity checks.
     *
     * @param alk the original version of lig/kern instruction.
     * @param skip the offset of next lig/kern instruction in the final version
     * of the lig/kern program.
     * @return Return the ligkern
     * @throws HelpingException when some error stops loading.
     */
    private TFMLigKern makeKern(final TFMAuxLigKern alk, final int skip)
            throws HelpingException {

        int kernIdx = alk.kernIndex();
        TFMFixWord kern;
        if (kernIdx < kernTable.length) {
            kern = kernTable[kernIdx];
        } else {
            throw new HelpingException("TFM.kernindextoolarge");
        }
        return new TFMKerning(skip, alk.nextChar(), kern);
    }

    /**
     * Gets the offset of next lig/kern instruction in a program based on
     * counting only those intervene instructions which will be converted to
     * final lig/kern program.
     *
     * @param pos the position of current lig/kern instruction in <code>ligAuxTable</code>.
     * @return the skip amount of the next instruction in the final version of
     * lig/kern program.
     */
    private int getSkip(int pos) {

        TFMAuxLigKern alk = ligAuxTab[pos];
        if (alk.meansStop()) {
            return -1;
        }
        int skip = 0;
        int next = alk.nextIndex(pos);
        while (++pos < next) {
            if (ligAuxTab[pos].getActivity() != TFMAuxLigKern.PASSTHROUGH) {
                skip++;
            }
        }
        return skip;
    }

    /**
     * Performs validity checks which are common to both (lig and kern) types
     * of lig/kern instructions in tfm file.
     *
     * @param alk the checked lig/kern instruction.
     * @throws HelpingException when some error stops loading.
     */
    private void checkLigKern(final TFMAuxLigKern alk) throws HelpingException {

        if (!charExists(alk.nextChar()) && alk.nextChar() != boundaryChar) {
            badchar(alk.nextChar(), ((alk.meansKern()) ? "Kern" : "Ligature")
                    + " step for");
            alk.setNextChar(firstCharCode);
        }
    }

    /**
     * Reports a reference to a nonexistent character.
     *
     * @param c the checked character code.
     * @param s identification for error messages.
     * @throws HelpingException if recoverable errors stop loading.
     */
    private void badchar(final short c, final String s) throws HelpingException {

        throw new HelpingException("TFM.nonexistentchar", s, Integer
                .toOctalString(c));
    }

    /**
     * Check the existence of particular character in the font.
     *
     * @param c the checked character code.
     * @return <code>true</code> if the character is present.
     */
    private boolean charExists(short c) {

        return ((c -= firstCharCode) >= 0 && c < charCount && charAuxTab[c]
                .exists());
    }

    /**
     * Records the starting indexes of final lig/kern program in <code>ligKernTable</code>
     * to auxiliary character information field <code>ligkernstart</code>
     * of <code>AuxCharInfo</code>.
     *
     * @param pos the position of currently processed instruction in original
     * tfm lig/kern table <code>ligAuxTab</code>.
     * @param start the position of corresponding instruction in final lig/kern
     * table <code>LigKernTable</code>.
     */
    private void setLigStarts(final int pos, final int start) {

        TFMIndexMultimap.Enum lab = labels.forKey(pos);
        while (lab.hasMore()) {
            int c = lab.next();
            if (c == BOUNDARYLABEL) {
                boundaryStart = start;
            } else {
                charAuxTab[c].setLigkernstart(start);
            }
        }
    }

    /**
     * Lig/kern programs in the final format
     */
    private TFMLigKern[] ligKernTable;

    /**
     * Marks the lig/kern instructions which are really a part of some lig/kern
     * program (active), counts the final number of lig/kern instructions,
     * creates the blank final lig/kern table and checks for errors. Uses
     * <code>activity</code> field of <code>AuxLigKern</code> for marking
     * the activity. It supposes that the first instructions of programs are
     * already marked active.
     *
     * @throws HelpingException when some error stops loading.
     */
    private void promoteActivity() throws HelpingException {

        int ligKernLength = 0;
        for (int i = 0; i < ligAuxLen; i++) {
            TFMAuxLigKern alk = ligAuxTab[i];
            if (alk.getActivity() == TFMAuxLigKern.ACCESSIBLE) {
                if (!alk.meansStop()) {
                    int next = alk.nextIndex(i);
                    if (next < ligAuxLen) {
                        ligAuxTab[next].setActivity(TFMAuxLigKern.ACCESSIBLE);
                    } else {
                        throw new HelpingException("TFM.skipstoofar", String
                                .valueOf(i));
                    }
                }
            }
            if (alk.getActivity() != TFMAuxLigKern.PASSTHROUGH) {
                ligKernLength++;
            }
        }
        ligKernTable = new TFMLigKern[ligKernLength];
    }

    /**
     * Starting index of lig/kern program for invisible left boundary character
     * or <code>NOINDEX</code> if there is no such program.
     */
    private int boundaryStart = TFMCharInfo.NOINDEX;

    /**
     * Invisible right boundary character code.
     */
    private short boundaryChar = TFMCharInfo.NOCHARCODE;

    /**
     * The associative table of lig/kern program starts in <code>ligAuxTab</code>
     */
    private TFMIndexMultimap labels = new TFMIndexMultimap();

    /**
     * Code for left boundary lig/kern program in <code>labels</code> table
     */
    private static final int BOUNDARYLABEL = TFMCharInfo.NOCHARCODE;

    /**
     * Tries to find the information about lig/kerns for word boundaries in tfm
     * lig/kern table and checks for errors.
     *
     * @throws HelpingException when some error stops loading.
     */
    private void setupBoundary() throws HelpingException {

        TFMAuxLigKern alk = ligAuxTab[0];
        if (alk.meansBoundary()) {
            boundaryChar = alk.nextChar();
            alk.setActivity(TFMAuxLigKern.PASSTHROUGH);
        }

        alk = ligAuxTab[ligAuxLen - 1];
        if (alk.meansBoundary()) {
            int start = alk.restartIndex();
            alk.setActivity(TFMAuxLigKern.PASSTHROUGH);
            if (start < ligAuxLen) {
                ligAuxTab[start].setActivity(TFMAuxLigKern.ACCESSIBLE);
                labels.add(start, BOUNDARYLABEL);
            } else {
                throw new HelpingException("TFM.boundarychartoolarge");
            }
        }
    }

    /**
     * Value of <code>AuxCharInfo.tag()</code> tagging normal character
     */
    private static final byte NOTAG = 0;

    /**
     * Value of <code>AuxCharInfo.tag()</code> tagging character with
     * lig/kern program
     */
    private static final byte LIGTAG = 1;

    /**
     * Value of <code>AuxCharInfo.tag()</code> tagging non last character in
     * a chain of larger characters.
     */
    private static final byte LISTTAG = 2;

    /**
     * Value of <code>AuxCharInfo.tag()</code> which tagging with extensible
     * recipe.
     */
    private static final byte EXTTAG = 3;

    /**
     * Builds associative table <code>labels</code> which maps the character
     * codes to lig/kern program starting indexes in <code>ligAuxTab</code>
     * for remapping later. It also marks the starting instructions of lig/kern
     * programs as active (using the <code>ctivity</code> field of <code>AuxLigKern</code>).
     *
     * @throws HelpingException when some error stops loading.
     */
    private void buildLabels() throws HelpingException {

        for (int i = 0; i < charCount; i++) {
            if (charAuxTab[i].tag() == LIGTAG) {
                int start = ligAuxStart(charAuxTab[i].ligStart());
                if (start < ligAuxLen) {
                    labels.add(start, i);
                    ligAuxTab[start].setActivity(TFMAuxLigKern.ACCESSIBLE);
                } else {
                    throw new HelpingException("TFM.startingindextoolarge",
                            String.valueOf(octCharNum(i)));
                }
            }
        }
    }

    /**
     * Finds out the actual starting index of lig/kern program in case there is
     * a restart instructions and checks for validity.
     *
     * @param start the starting index of lig/kern program given in a character
     * info.
     * @return the actual starting index.
     */
    private int ligAuxStart(int start) {

        if (start < ligAuxLen) {
            TFMAuxLigKern alk = ligAuxTab[start];
            if (alk.meansRestart()) {
                start = alk.restartIndex();
                if (start < ligAuxLen
                        && alk.getActivity() == TFMAuxLigKern.UNREACHABLE) {
                    alk.setActivity(TFMAuxLigKern.PASSTHROUGH);
                }
            }
        }
        return start;
    }

    /**
     * Error message identification of character given its position in <code>charTable</code>.
     *
     * @param pos the position of referenced character info in <code>charTable</code>.
     * @return the string representation of character for error messages.
     */
    private String octCharNum(final int pos) {

        return Integer.toOctalString(pos + firstCharCode);
    }

    /**
     * @see de.dante.util.font.type.FontMetric#getFontMetric()
     */
    public Element getFontMetric() throws IOException, ConfigurationException,
            HelpingException {

        // read psfonts.map
        PSFontEncoding psfenc = psfontmap.getPSFontEncoding(fontname);

        // encoding
        String[] enctable = null;
        if (!psfenc.getEncfile().equals("")) {
            enctable = encfactory.getEncodingTable(psfenc.getEncfile());
        }

        // create efm-file
        Element root = new Element("fontgroup");
        root.setAttribute("name", family);
        root.setAttribute("id", family);
        root.setAttribute("default-size", designSize.toString());
        root.setAttribute("empr", "100");

        Element fontdimen = new Element("fontdimen");
        root.addContent(fontdimen);

        Element font = new Element("font");
        root.addContent(font);

        font.setAttribute("font-name", family);
        font.setAttribute("font-family", family);
        root.setAttribute("units-per-em", "1000");

        if (fontType == MATHSY) {
            font.setAttribute("type", "tfm-mathsyml");
            if (paramTable.length > 0) {
                final String[] paramLabel = {"SLANT", "SPACE", "STRETCH",
                        "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE", "NUM1",
                        "NUM2", "NUM3", "DENOM1", "DENOM2", "SUP1", "SUP2",
                        "SUP3", "SUB1", "SUB2", "SUPDROP", "SUBDROP", "DELIM1",
                        "DELIM2", "AXISHEIGHT"};
                for (int i = 0; i < paramTable.length; i++) {
                    if (i < paramLabel.length) {
                        fontdimen.setAttribute(paramLabel[i], paramTable[i]
                                .toStringUnits());
                    }
                }
            }
        } else if (fontType == MATHEX) {
            font.setAttribute("type", "tfm-mathext");
            if (paramTable.length > 0) {
                final String[] paramLabel = {"SLANT", "SPACE", "STRETCH",
                        "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE",
                        "DEFAULTRULETHICKNESS", "BIGOPSPACING1",
                        "BIGOPSPACING2", "BIGOPSPACING3", "BIGOPSPACING4",
                        "BIGOPSPACING5"};
                for (int i = 0; i < paramTable.length; i++) {
                    if (i < paramLabel.length) {
                        fontdimen.setAttribute(paramLabel[i], paramTable[i]
                                .toStringUnits());
                    }
                }
            }
        } else {
            font.setAttribute("type", "tfm-normal");
            if (paramTable.length > 0) {

                final String[] paramLabel = {"SLANT", "SPACE", "STRETCH",
                        "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE"};

                for (int i = 0; i < paramTable.length; i++) {
                    if (i < paramLabel.length) {
                        fontdimen.setAttribute(paramLabel[i], paramTable[i]
                                .toStringUnits());
                    }

                }
            }
        }

        // filename
        font.setAttribute("filename", filenameWithoutPath(psfenc.getPfbfile()));

        for (int i = 0; i < charTable.length; i++) {

            // get char
            TFMCharInfo ci = getCharInfo((short) (i + firstCharCode));

            if (ci != null) {

                // create glyph
                Element glyph = new Element("glyph");

                glyph.setAttribute("ID", String.valueOf(i));
                glyph.setAttribute("glyph-number", String.valueOf(i));
                String c = Character.toString((char) i);
                if (c != null && c.trim().length() > 0) {
                    glyph.setAttribute("char", c);
                }

                if (enctable != null && i < enctable.length) {
                    glyph.setAttribute("glyph-name", enctable[i].substring(1));
                }

                glyph.setAttribute("width", ci.getWidth().toStringUnits());
                glyph.setAttribute("height", ci.getHeight().toStringUnits());
                glyph.setAttribute("depth", ci.getDepth().toStringUnits());
                glyph.setAttribute("italic", ci.getItalic().toStringUnits());

                // ligature
                int ligstart = ci.ligKernStart();
                if (ligstart != TFMCharInfo.NOINDEX) {

                    for (int k = ligstart; k != TFMCharInfo.NOINDEX; k = ligKernTable[k]
                            .nextIndex(k)) {
                        TFMLigKern lk = ligKernTable[k];

                        if (lk instanceof TFMLigature) {
                            TFMLigature lig = (TFMLigature) lk;

                            Element ligature = new Element("ligature");

                            ligature.setAttribute("letter-id", String
                                    .valueOf(lig.getNextChar()));
                            String sl = Character.toString((char) lig
                                    .getNextChar());
                            if (sl != null && sl.trim().length() > 0) {
                                ligature.setAttribute("letter", sl.trim());
                            }

                            ligature.setAttribute("lig-id", String.valueOf(lig
                                    .getAddingChar()));
                            String slig = Character.toString((char) lig
                                    .getAddingChar());
                            if (slig != null && slig.trim().length() > 0) {
                                ligature.setAttribute("lig", slig.trim());
                            }
                            glyph.addContent(ligature);
                        } else if (lk instanceof TFMKerning) {
                            TFMKerning kern = (TFMKerning) lk;

                            Element kerning = new Element("kerning");

                            kerning.setAttribute("glyph-id", String
                                    .valueOf(kern.getNextChar()));
                            String sk = Character.toString((char) kern
                                    .getNextChar());
                            if (sk != null && sk.trim().length() > 0) {
                                kerning.setAttribute("char", sk.trim());
                            }
                            kerning.setAttribute("size", kern.getKern()
                                    .toStringUnits());

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
     * remove the path, if exists
     * @param  file the filename
     * @return  the filename without the path
     */
    private String filenameWithoutPath(final String file) {

        String rt = file;
        int i = rt.lastIndexOf(File.separator);
        if (i > 0) {
            rt = rt.substring(i + 1);
        }
        return rt;
    }

    /**
     * Return the <code>CharInfo</code> for index <code>idx</code>.
     *
     * @param i the index
     * @return Returns the <code>CharInfo</code> or <code>null</code>, 
     *         if not exists
     */
    private TFMCharInfo getCharInfo(final short i) {

        short idx = i;
        return (0 <= (idx -= firstCharCode) && idx < charTable.length)
                ? charTable[idx]
                : null;
    }
}