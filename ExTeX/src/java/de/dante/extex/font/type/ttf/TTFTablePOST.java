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

package de.dante.extex.font.type.ttf;

import java.io.IOException;

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * The 'post' table contains information needed to use a
 * TrueType font on a PostScript printer.
 * It contains the data needed for the FontInfo
 * dictionary entry as well as the PostScript
 * names for all of the glyphs in the font.
 * It also contains memory usage information
 * needed by the PostScript driver for memory management.
 *
 * <table BORDER="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>Fixed</td><td>Format Type</td><td>
 *              0x00010000 for format 1.0, 0x00020000 for format
 *              2.0, and so on...</td></tr>
 *   <tr><td>Fixed</td><td>italicAngle</td><td>
 *              Italic angle in counter-clockwise degrees from the
 *               vertical. Zero for upright text, negative for text that leans to
 *              the right (forward)</td></tr>
 *   <tr><td>FWord</td><td>underlinePosition</td><td>
 *              Suggested values for the underline position
 *              (negative values indicate below baseline).</td></tr>
 *   <tr><td>FWord</td><td>underlineThickness</td><td>
 *          Suggested values for the underline thickness.</td></tr>
 *   <tr><td>ULONG</td><td>isFixedPitch</td><td>
 *              Set to 0 if the font is proportionally spaced,
 *              non-zero if the font is not proportionally spaced (i.e.
 *              monospaced).</td></tr>
 *   <tr><td>ULONG</td><td>minMemType42</td><td>
 *              Minimum memory usage when a TrueType font is
 *              downloaded.</td></tr>
 *   <tr><td>ULONG</td><td>maxMemType42</td><td>
 *              Maximum memory usage when a TrueType font is
 *              downloaded.</td><tr>
 *   <tr><td>ULONG</td><td>minMemType1</td><td>
 *              Minimum memory usage when a TrueType font is
 *              downloaded as a Type 1 font.</td></tr>
 *   <tr><td>ULONG</td><td>maxMemType1</td><td>
 *              Maximum memory usage when a TrueType font is
 *              downloaded as a Type 1 font.</td><tr>
 * </table>
 *
 * <p>Format 2</p>
 * <table border="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>uint16</td><td>numberOfGlyphs</td><td>number of glyphs</td></tr>
 *   <tr><td>uint16</td><td>glyphNameIndex[numberOfGlyphs]</td><td>
 *          Ordinal number of this glyph in <code>'post'</code> string tables.
 *          This is not an offset.</td></tr>
 *   <tr><td>Pascal string</td><td>names[numberNewGlyphs]</td><td>
 *          glyph names with length bytes [variable] (a Pascal string)</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class TTFTablePOST extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * Format 1
     */
    public static final int FORMAT1 = 0x00010000;

    /**
     * Format 2
     */
    public static final int FORMAT2 = 0x00020000;

    /**
     * Format 2.5
     */
    public static final int FORMAT25 = 0x00020005;

    /**
     * Format 3
     */
    public static final int FORMAT3 = 0x00030000;

    /**
     * Format 4
     */
    public static final int FORMAT4 = 0x00040000;

    /**
     * 'post' Format 1
     * <p>The order in which glyphs are placed in a font
     * is at the convenience of the font developer To use format 1,
     * a font must contain exactly the 258 glyphs
     * in the standard Macintosh ordering.
     * For such fonts, the glyph names are taken from the system.
     * As a result, this format does not require a special subtable.
     * </p>
     * <p>The names for these 258 glyphs are, in order:</p>
     */
    private static final String[] FORMAT1NAME = {".notdef", // 0
            ".null", // 1
            "nonmarkingreturn", // 2
            "space", // 3
            "exclam", // 4
            "quotedbl", // 5
            "numbersign", // 6
            "dollar", // 7
            "percent", // 8
            "ampersand", // 9
            "quotesingle", // 10
            "parenleft", // 11
            "parenright", // 12
            "asterisk", // 13
            "plus", // 14
            "comma", // 15
            "hyphen", // 16
            "period", // 17
            "slash", // 18
            "zero", // 19
            "one", // 20
            "two", // 21
            "three", // 22
            "four", // 23
            "five", // 24
            "six", // 25
            "seven", // 26
            "eight", // 27
            "nine", // 28
            "colon", // 29
            "semicolon", // 30
            "less", // 31
            "equal", // 32
            "greater", // 33
            "question", // 34
            "at", // 35
            "A", // 36
            "B", // 37
            "C", // 38
            "D", // 39
            "E", // 40
            "F", // 41
            "G", // 42
            "H", // 43
            "I", // 44
            "J", // 45
            "K", // 46
            "L", // 47
            "M", // 48
            "N", // 49
            "O", // 50
            "P", // 51
            "Q", // 52
            "R", // 53
            "S", // 54
            "T", // 55
            "U", // 56
            "V", // 57
            "W", // 58
            "X", // 59
            "Y", // 60
            "Z", // 61
            "bracketleft", // 62
            "backslash", // 63
            "bracketright", // 64
            "asciicircum", // 65
            "underscore", // 66
            "grave", // 67
            "a", // 68
            "b", // 69
            "c", // 70
            "d", // 71
            "e", // 72
            "f", // 73
            "g", // 74
            "h", // 75
            "i", // 76
            "j", // 77
            "k", // 78
            "l", // 79
            "m", // 80
            "n", // 81
            "o", // 82
            "p", // 83
            "q", // 84
            "r", // 85
            "s", // 86
            "t", // 87
            "u", // 88
            "v", // 89
            "w", // 90
            "x", // 91
            "y", // 92
            "z", // 93
            "braceleft", // 94
            "bar", // 95
            "braceright", // 96
            "asciitilde", // 97
            "Adieresis", // 98
            "Aring", // 99
            "Ccedilla", // 100
            "Eacute", // 101
            "Ntilde", // 102
            "Odieresis", // 103
            "Udieresis", // 104
            "aacute", // 105
            "agrave", // 106
            "acircumflex", // 107
            "adieresis", // 108
            "atilde", // 109
            "aring", // 110
            "ccedilla", // 111
            "eacute", // 112
            "egrave", // 113
            "ecircumflex", // 114
            "edieresis", // 115
            "iacute", // 116
            "igrave", // 117
            "icircumflex", // 118
            "idieresis", // 119
            "ntilde", // 120
            "oacute", // 121
            "ograve", // 122
            "ocircumflex", // 123
            "odieresis", // 124
            "otilde", // 125
            "uacute", // 126
            "ugrave", // 127
            "ucircumflex", // 128
            "udieresis", // 129
            "dagger", // 130
            "degree", // 131
            "cent", // 132
            "sterling", // 133
            "section", // 134
            "bullet", // 135
            "paragraph", // 136
            "germandbls", // 137
            "registered", // 138
            "copyright", // 139
            "trademark", // 140
            "acute", // 141
            "dieresis", // 142
            "notequal", // 143
            "AE", // 144
            "Oslash", // 145
            "infinity", // 146
            "plusminus", // 147
            "lessequal", // 148
            "greaterequal", // 149
            "yen", // 150
            "mu", // 151
            "partialdiff", // 152
            "summation", // 153
            "product", // 154
            "pi", // 155
            "integral'", // 156
            "ordfeminine", // 157
            "ordmasculine", // 158
            "Omega", // 159
            "ae", // 160
            "oslash", // 161
            "questiondown", // 162
            "exclamdown", // 163
            "logicalnot", // 164
            "radical", // 165
            "florin", // 166
            "approxequal", // 167
            "increment", // 168
            "guillemotleft", // 169
            "guillemotright", //170
            "ellipsis", // 171
            "nbspace", // 172
            "Agrave", // 173
            "Atilde", // 174
            "Otilde", // 175
            "OE", // 176
            "oe", // 177
            "endash", // 178
            "emdash", // 179
            "quotedblleft", // 180
            "quotedblright", // 181
            "quoteleft", // 182
            "quoteright", // 183
            "divide", // 184
            "lozenge", // 185
            "ydieresis", // 186
            "Ydieresis", // 187
            "fraction", // 188
            "currency", // 189
            "guilsinglleft", // 190
            "guilsinglright", //191
            "fi", // 192
            "fl", // 193
            "daggerdbl", // 194
            "middot", // 195
            "quotesinglbase", //196
            "quotedblbase", // 197
            "perthousand", // 198
            "Acircumflex", // 199
            "Ecircumflex", // 200
            "Aacute", // 201
            "Edieresis", // 202
            "Egrave", // 203
            "Iacute", // 204
            "Icircumflex", // 205
            "Idieresis", // 206
            "Igrave", // 207
            "Oacute", // 208
            "Ocircumflex", // 209
            "", // 210
            "Ograve", // 211
            "Uacute", // 212
            "Ucircumflex", // 213
            "Ugrave", // 214
            "dotlessi", // 215
            "circumflex", // 216
            "tilde", // 217
            "overscore", // 218
            "breve", // 219
            "dotaccent", // 220
            "ring", // 221
            "cedilla", // 222
            "hungarumlaut", // 223
            "ogonek", // 224
            "caron", // 225
            "Lslash", // 226
            "lslash", // 227
            "Scaron", // 228
            "scaron", // 229
            "Zcaron", // 230
            "zcaron", // 231
            "brokenbar", // 232
            "Eth", // 233
            "eth", // 234
            "Yacute", // 235
            "yacute", // 236
            "Thorn", // 237
            "thorn", // 238
            "minus", // 239
            "multiply", // 240
            "onesuperior", // 241
            "twosuperior", // 242
            "threesuperior", // 243
            "onehalf", // 244
            "onequarter", // 245
            "threequarters", // 246
            "franc", // 247
            "Gbreve", // 248
            "gbreve", // 249
            "Idot", // 250
            "Scedilla", // 251
            "scedilla", // 252
            "Cacute", // 253
            "cacute", // 254
            "Ccaron", // 255
            "ccaron", // 256
            "dcroat" // 257
    };

    /**
     * version
     */
    private int version;

    /**
     * italicAngle
     */
    private int italicAngle;

    /**
     * underlinePosition
     */
    private short underlinePosition;

    /**
     * underlineThickness
     */
    private short underlineThickness;

    /**
     * isFixedPitch
     */
    private int isFixedPitch;

    /**
     * minMemType42
     */
    private int minMemType42;

    /**
     * maxMemType42
     */
    private int maxMemType42;

    /**
     * minMemType1
     */
    private int minMemType1;

    /**
     * maxMemType1
     */
    private int maxMemType1;

    /**
     * numGlyphs
     */
    private int numGlyphs;

    /**
     * glyphNameIndex
     */
    private int[] glyphNameIndex;

    /**
     * psGlyphName
     */
    private String[] psGlyphName;

    /**
     * Create a new object.
     *
     * @param tablemap  the tablemap
     * @param de        directory entry
     * @param rar       input
     * @throws IOException if an IO-error occured
     */
    TTFTablePOST(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        version = rar.readInt();
        italicAngle = rar.readInt();
        underlinePosition = rar.readShort();
        underlineThickness = rar.readShort();
        isFixedPitch = rar.readInt();
        minMemType42 = rar.readInt();
        maxMemType42 = rar.readInt();
        minMemType1 = rar.readInt();
        maxMemType1 = rar.readInt();

        switch (version) {
            case FORMAT1 :
                // see FORMAT1NAME
                break;
            case FORMAT2 :
                // This TrueType-based font file contains glyphs
                // not in the standard Macintosh set or the ordering
                // of the glyphs in the TrueType font file is non-standard
                // (again, for the Macintosh). The glyph name array maps
                // the glyphs in this font to name index. If the name index
                // is between 0 and 257, treat the name index as a glyph
                // index in the Macintosh standard order. If the name index
                // is between 258 and 32767, then subtract 258 and use that
                // to index into the list of Pascal strings at the end of
                // the table. Thus a given font may map some of its glyphs
                // to the standard glyph names, and some to its own names.
                numGlyphs = rar.readUnsignedShort();
                glyphNameIndex = new int[numGlyphs];
                for (int i = 0; i < numGlyphs; i++) {
                    glyphNameIndex[i] = rar.readUnsignedShort();
                }
                int h = highestGlyphNameIndex();
                if (h > FORMAT1NAME.length - 1) {
                    h -= (FORMAT1NAME.length - 1);
                    psGlyphName = new String[h];
                    for (int i = 0; i < h; i++) {
                        int len = rar.readUnsignedByte();
                        byte[] buf = new byte[len];
                        rar.readFully(buf);
                        psGlyphName[i] = new String(buf);
                    }
                }
                break;
            case FORMAT25 :
                break;
            case FORMAT3 :
                break;
            case FORMAT4 :
                break;
            default :
                break;
        }
    }

    /**
     * Returns the highest glyph index
     * @return Returns the highest glyph index
     */
    private int highestGlyphNameIndex() {

        int high = 0;
        for (int i = 0; i < numGlyphs; i++) {
            if (high < glyphNameIndex[i]) {
                high = glyphNameIndex[i];
            }
        }
        return high;
    }

    /**
     * Returns the glyph name
     * @param i index
     * @return Returns the glyph name
     */
    public String getGlyphName(final int i) {

        switch (version) {
            case FORMAT1 :
                return (i > FORMAT1NAME.length - 1)
                        ? ".notdef"
                        : FORMAT1NAME[i];
            case FORMAT2 :
                return (glyphNameIndex[i] > FORMAT1NAME.length - 1)
                        ? psGlyphName[glyphNameIndex[i] - FORMAT1NAME.length]
                        : FORMAT1NAME[glyphNameIndex[i]];
            default :
                break;
        }
        return null;
    }

    /**
     * Get the table type, as a table directory value.
     * @return The table type
     */
    public int getType() {

        return TTFFont.POST;
    }

    /**
     * Returns the glyphNameIndex.
     * @return Returns the glyphNameIndex.
     */
    public int[] getGlyphNameIndex() {

        return glyphNameIndex;
    }

    /**
     * Returns the isFixedPitch.
     * @return Returns the isFixedPitch.
     */
    public int getIsFixedPitch() {

        return isFixedPitch;
    }

    /**
     * Returns the italicAngle.
     * @return Returns the italicAngle.
     */
    public int getItalicAngle() {

        return italicAngle;
    }

    /**
     * Returns the maxMemType1.
     * @return Returns the maxMemType1.
     */
    public int getMaxMemType1() {

        return maxMemType1;
    }

    /**
     * Returns the maxMemType42.
     * @return Returns the maxMemType42.
     */
    public int getMaxMemType42() {

        return maxMemType42;
    }

    /**
     * Returns the minMemType1.
     * @return Returns the minMemType1.
     */
    public int getMinMemType1() {

        return minMemType1;
    }

    /**
     * Returns the minMemType42.
     * @return Returns the minMemType42.
     */
    public int getMinMemType42() {

        return minMemType42;
    }

    /**
     * Returns the numGlyphs.
     * @return Returns the numGlyphs.
     */
    public int getNumGlyphs() {

        return numGlyphs;
    }

    /**
     * Returns the psGlyphName.
     * @return Returns the psGlyphName.
     */
    public String[] getPsGlyphName() {

        return psGlyphName;
    }

    /**
     * Returns the underlinePosition.
     * @return Returns the underlinePosition.
     */
    public short getUnderlinePosition() {

        return underlinePosition;
    }

    /**
     * Returns the underlineThickness.
     * @return Returns the underlineThickness.
     */
    public short getUnderlineThickness() {

        return underlineThickness;
    }

    /**
     * Returns the version.
     * @return Returns the version.
     */
    public int getVersion() {

        return version;
    }

    /**
     * Returns the int-value for a glyph.
     * @param id    the id for the glyph
     * @return Returns the int-value for a glyph.
     */
    public int getGlyphValue(final int id) {

        if (glyphNameIndex != null && id >= 0 && id < glyphNameIndex.length) {

            return glyphNameIndex[id];
        }
        return -1;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("post");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        table.setAttribute("version", "0x" + Integer.toHexString(version));
        table.setAttribute("isfixedpitch", String.valueOf(isFixedPitch));
        table.setAttribute("italicangle", String.valueOf(italicAngle));
        table.setAttribute("maxmemtype1", String.valueOf(maxMemType1));
        table.setAttribute("maxmemtype42", String.valueOf(maxMemType42));
        table.setAttribute("minmemtype1", String.valueOf(minMemType1));
        table.setAttribute("minmentype42", String.valueOf(minMemType42));
        table.setAttribute("numglyphs", String.valueOf(numGlyphs));
        table.setAttribute("underlinepos", String.valueOf(underlinePosition));
        table.setAttribute("underlinethickness", String
                .valueOf(underlineThickness));

        if (glyphNameIndex != null) {
            for (int i = 0; i < glyphNameIndex.length; i++) {
                Element name = new Element("glyphname");
                name.setAttribute("id", String.valueOf(i));
                name.setAttribute("value", String.valueOf(glyphNameIndex[i]));
                name.setAttribute("name", getGlyphName(i));
                table.addContent(name);
            }
        }
        return table;
    }
}