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

import de.dante.util.file.random.RandomAccessR;

/**
 * The 'OS/2' table consists of a set of metrics
 * that are required by OS/2 and Windows.
 * It is not used by the Mac OS.
 *
 * <table BORDER="1">
 *   <tbody>
 *      <tr><td><b>Type</b></td><td><b>Name of Entry</b></td><td><b>Comments</b></td></tr>
 *   </tbody>
 *   <tbody>
 *   <tr><td>USHORT</td><td>version</td><td>
 *          table version number (set to 0)</td></tr>
 *   <tr><td>SHORT</td><td>xAvgCharWidth;</td><td>
 *          average weighted advance width of lower case letters and space</td></tr>
 *   <tr><td>USHORT</td><td>usWeightClass;</td><td>
 *          visual weight (degree of blackness or thickness) of stroke in glyphs</td></tr>
 *   <tr><td>USHORT</td><td>usWidthClass;</td><td>
 *          relative change from the normal aspect
 *          ratio (width to height ratio) as specified
 *          by a font designer for the glyphs in the font</td></tr>
 *   <tr><td>SHORT</td><td>fsType;</td><td>
 *          characteristics and properties of this font
 *          (set undefined bits to zero)</td></tr>
 *   <tr><td>SHORT</td><td>ySubscriptXSize;</td><td>
 *          recommended horizontal size in pixels for subscripts</td></tr>
 *   <tr><td>SHORT</td><td>ySubscriptYSize;</td><td>
 *          recommended vertical size in pixels for subscripts</td></tr>
 *   <tr><td>SHORT</td><td>ySubscriptXOffset;</td><td>
 *          recommended horizontal offset for subscripts</td></tr>
 *   <tr><td>SHORT</td><td>ySubscriptYOffset;</td><td>
 *          recommended vertical offset form the baseline for subscripts</td></tr>
 *   <tr><td>SHORT</td><td>ySuperscriptXSize;</td><td>
 *          recommended horizontal size in pixels for superscripts</td></tr>
 *   <tr><td>SHORT</td><td>ySuperscriptYSize;</td><td>
 *          recommended vertical size in pixels for superscripts</td></tr>
 *   <tr><td>SHORT</td><td>ySuperscriptXOffset;</td><td>
 *          recommended horizontal offset for superscripts</td></tr>
 *   <tr><td>SHORT</td><td>ySuperscriptYOffset;</td><td>
 *          recommended vertical offset from the baseline for superscripts</td></tr>
 *   <tr><td>SHORT</td><td>yStrikeoutSize;</td><td>
 *          width of the strikeout stroke</td></tr>
 *   <tr><td>SHORT</td><td>yStrikeoutPosition;</td><td>
 *          position of the strikeout stroke relative to the baseline</td></tr>
 *   <tr><td>SHORT</td><td>sFamilyClass;</td><td>
 *          classification of font-family design.</td></tr>
 *   <tr><td>PANOSE</td><td>panose;</td><td>
 *          10 byte series of number used to describe the
 *          visual characteristics of a given typeface</td></tr>
 *   <tr><td>ULONG</td><td>ulUnicodeRange1</td><td>
 *          Field is split into two bit fields of 96 and 36 bits each.
 *          The low 96 bits are used to specify the Unicode blocks
 *          encompassed by the font file. The high 32 bits are
 *          used to specify the character or script sets covered
 *          by the font file. Bit assignments are pending. Set to 0<br/>
 *          Bits 0&ndash;31</td></tr>
 *   <tr><td>ULONG</td><td>ulUnicodeRange2</td><td>Bits 32&ndash;63</td></tr>
 *   <tr><td>ULONG</td><td>ulUnicodeRange3</td><td>Bits 64&ndash;95</td></tr>
 *   <tr><td>ULONG</td><td>ulUnicodeRange4</td><td>Bits 96&ndash;127</td></tr>
 *   <tr><td>CHAR</td><td>achVendID[4];</td><td>
 *          four character identifier for the font vendor</td></tr>
 *   <tr><td>USHORT</td><td>fsSelection;</td><td>
 *          2-byte bit field containing information concerning
 *          the nature of the font patterns</td></tr>
 *   <tr><td>USHORT</td><td>usFirstCharIndex</td><td>
 *          The minimum Unicode index in this font.</td></tr>
 *   <tr><td>USHORT</td><td>usLastCharIndex</td><td>
 *          The maximum Unicode index in this font.</td></tr>
 *   <tr><td>USHORT</td><td>sTypoAscender</td><td></td></tr>
 *   <tr><td>USHORT</td><td>sTypoDescender</td><td></td></tr>
 *   <tr><td>USHORT</td><td>sTypoLineGap</td><td></td></tr>
 *   <tr><td>USHORT</td><td>usWinAscent</td><td></td></tr>
 *   <tr><td>USHORT</td><td>usWinDescent</td><td></td></tr>
 *   <tr><td>ULONG</td><td>ulCodePageRange1</td><td>Bits 0-31</td></tr>
 *   <tr><td>ULONG</td><td>ulCodePageRange2</td><td>Bits 32-63</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class TTFTableOS2 extends AbstractTTFTable implements TTFTable {

    /**
     * version
     */
    private int version;

    /**
     * xAvgCharWidth
     */
    private short xAvgCharWidth;

    /**
     * usWeightClass
     */
    private int usWeightClass;

    /**
     * usWidthClass
     */
    private int usWidthClass;

    /**
     * fsType
     */
    private short fsType;

    /**
     * ySubscriptXSize
     */
    private short ySubscriptXSize;

    /**
     * ySubscriptYSize
     */
    private short ySubscriptYSize;

    /**
     * ySubscriptXOffset
     */
    private short ySubscriptXOffset;

    /**
     * ySubscriptYOffset
     */
    private short ySubscriptYOffset;

    /**
     * ySuperscriptXSize
     */
    private short ySuperscriptXSize;

    /**
     * ySuperscriptYSize
     */
    private short ySuperscriptYSize;

    /**
     * ySuperscriptXOffset
     */
    private short ySuperscriptXOffset;

    /**
     * ySuperscriptYOffset
     */
    private short ySuperscriptYOffset;

    /**
     * yStrikeoutSize
     */
    private short yStrikeoutSize;

    /**
     * yStrikeoutPosition
     */
    private short yStrikeoutPosition;

    /**
     * sFamilyClass
     */
    private short sFamilyClass;

    /**
     * panose
     */
    private Panose panose;

    /**
     * ulUnicodeRange1
     */
    private int ulUnicodeRange1;

    /**
     * ulUnicodeRange2
     */
    private int ulUnicodeRange2;

    /**
     * ulUnicodeRange3
     */
    private int ulUnicodeRange3;

    /**
     * ulUnicodeRange4
     */
    private int ulUnicodeRange4;

    /**
     * achVendorID
     */
    private int achVendorID;

    /**
     * fsSelection
     */
    private short fsSelection;

    /**
     * usFirstCharIndex
     */
    private int usFirstCharIndex;

    /**
     * usLastCharIndex
     */
    private int usLastCharIndex;

    /**
     * sTypoAscender
     */
    private short sTypoAscender;

    /**
     * sTypoDescender
     */
    private short sTypoDescender;

    /**
     * sTypoLineGap
     */
    private short sTypoLineGap;

    /**
     * usWinAscent
     */
    private int usWinAscent;

    /**
     * usWinDescent
     */
    private int usWinDescent;

    /**
     * ulCodePageRange1
     */
    private int ulCodePageRange1;

    /**
     * ulCodePageRange2
     */
    private int ulCodePageRange2;

    /**
     * Create a new object.
     *
     * @param tablemap  the tablemap
     * @param de        directory entry
     * @param rar       the RandomAccessInput
     * @throws IOException if an error occured
     */
    TTFTableOS2(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        version = rar.readUnsignedShort();
        xAvgCharWidth = rar.readShort();
        usWeightClass = rar.readUnsignedShort();
        usWidthClass = rar.readUnsignedShort();
        fsType = rar.readShort();
        ySubscriptXSize = rar.readShort();
        ySubscriptYSize = rar.readShort();
        ySubscriptXOffset = rar.readShort();
        ySubscriptYOffset = rar.readShort();
        ySuperscriptXSize = rar.readShort();
        ySuperscriptYSize = rar.readShort();
        ySuperscriptXOffset = rar.readShort();
        ySuperscriptYOffset = rar.readShort();
        yStrikeoutSize = rar.readShort();
        yStrikeoutPosition = rar.readShort();
        sFamilyClass = rar.readShort();
        byte[] buf = new byte[10];
        rar.readFully(buf); // mgn rar.read(buf)
        panose = new Panose(buf);
        ulUnicodeRange1 = rar.readInt();
        ulUnicodeRange2 = rar.readInt();
        ulUnicodeRange3 = rar.readInt();
        ulUnicodeRange4 = rar.readInt();
        achVendorID = rar.readInt();
        fsSelection = rar.readShort();
        usFirstCharIndex = rar.readUnsignedShort();
        usLastCharIndex = rar.readUnsignedShort();
        sTypoAscender = rar.readShort();
        sTypoDescender = rar.readShort();
        sTypoLineGap = rar.readShort();
        usWinAscent = rar.readUnsignedShort();
        usWinDescent = rar.readUnsignedShort();
        ulCodePageRange1 = rar.readInt();
        ulCodePageRange2 = rar.readInt();
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.OS_2;
    }

    /**
     * Returns the achVendorID.
     * @return Returns the achVendorID.
     */
    public int getAchVendorID() {

        return achVendorID;
    }

    /**
     * Returns the fsSelection.
     * @return Returns the fsSelection.
     */
    public short getFsSelection() {

        return fsSelection;
    }

    /**
     * Returns the fsType.
     * @return Returns the fsType.
     */
    public short getFsType() {

        return fsType;
    }

    /**
     * Returns the panose.
     * @return Returns the panose.
     */
    public Panose getPanose() {

        return panose;
    }

    /**
     * Returns the sFamilyClass.
     * @return Returns the sFamilyClass.
     */
    public short getSFamilyClass() {

        return sFamilyClass;
    }

    /**
     * Returns the sTypoAscender.
     * @return Returns the sTypoAscender.
     */
    public short getSTypoAscender() {

        return sTypoAscender;
    }

    /**
     * Returns the sTypoDescender.
     * @return Returns the sTypoDescender.
     */
    public short getSTypoDescender() {

        return sTypoDescender;
    }

    /**
     * Returns the sTypoLineGap.
     * @return Returns the sTypoLineGap.
     */
    public short getSTypoLineGap() {

        return sTypoLineGap;
    }

    /**
     * Returns the ulCodePageRange1.
     * @return Returns the ulCodePageRange1.
     */
    public int getUlCodePageRange1() {

        return ulCodePageRange1;
    }

    /**
     * Returns the ulCodePageRange2.
     * @return Returns the ulCodePageRange2.
     */
    public int getUlCodePageRange2() {

        return ulCodePageRange2;
    }

    /**
     * Returns the ulUnicodeRange1.
     * @return Returns the ulUnicodeRange1.
     */
    public int getUlUnicodeRange1() {

        return ulUnicodeRange1;
    }

    /**
     * Returns the ulUnicodeRange2.
     * @return Returns the ulUnicodeRange2.
     */
    public int getUlUnicodeRange2() {

        return ulUnicodeRange2;
    }

    /**
     * Returns the ulUnicodeRange3.
     * @return Returns the ulUnicodeRange3.
     */
    public int getUlUnicodeRange3() {

        return ulUnicodeRange3;
    }

    /**
     * Returns the ulUnicodeRange4.
     * @return Returns the ulUnicodeRange4.
     */
    public int getUlUnicodeRange4() {

        return ulUnicodeRange4;
    }

    /**
     * Returns the usFirstCharIndex.
     * @return Returns the usFirstCharIndex.
     */
    public int getUsFirstCharIndex() {

        return usFirstCharIndex;
    }

    /**
     * Returns the usLastCharIndex.
     * @return Returns the usLastCharIndex.
     */
    public int getUsLastCharIndex() {

        return usLastCharIndex;
    }

    /**
     * Returns the usWeightClass.
     * @return Returns the usWeightClass.
     */
    public int getUsWeightClass() {

        return usWeightClass;
    }

    /**
     * Returns the usWidthClass.
     * @return Returns the usWidthClass.
     */
    public int getUsWidthClass() {

        return usWidthClass;
    }

    /**
     * Returns the usWinAscent.
     * @return Returns the usWinAscent.
     */
    public int getUsWinAscent() {

        return usWinAscent;
    }

    /**
     * Returns the usWinDescent.
     * @return Returns the usWinDescent.
     */
    public int getUsWinDescent() {

        return usWinDescent;
    }

    /**
     * Returns the version.
     * @return Returns the version.
     */
    public int getVersion() {

        return version;
    }

    /**
     * Returns the xAvgCharWidth.
     * @return Returns the xAvgCharWidth.
     */
    public short getXAvgCharWidth() {

        return xAvgCharWidth;
    }

    /**
     * Returns the yStrikeoutPosition.
     * @return Returns the yStrikeoutPosition.
     */
    public short getYStrikeoutPosition() {

        return yStrikeoutPosition;
    }

    /**
     * Returns the yStrikeoutSize.
     * @return Returns the yStrikeoutSize.
     */
    public short getYStrikeoutSize() {

        return yStrikeoutSize;
    }

    /**
     * Returns the ySubscriptXOffset.
     * @return Returns the ySubscriptXOffset.
     */
    public short getYSubscriptXOffset() {

        return ySubscriptXOffset;
    }

    /**
     * Returns the ySubscriptXSize.
     * @return Returns the ySubscriptXSize.
     */
    public short getYSubscriptXSize() {

        return ySubscriptXSize;
    }

    /**
     * Returns the ySubscriptYOffset.
     * @return Returns the ySubscriptYOffset.
     */
    public short getYSubscriptYOffset() {

        return ySubscriptYOffset;
    }

    /**
     * Returns the ySubscriptYSize.
     * @return Returns the ySubscriptYSize.
     */
    public short getYSubscriptYSize() {

        return ySubscriptYSize;
    }

    /**
     * Returns the ySuperscriptXOffset.
     * @return Returns the ySuperscriptXOffset.
     */
    public short getYSuperscriptXOffset() {

        return ySuperscriptXOffset;
    }

    /**
     * Returns the ySuperscriptXSize.
     * @return Returns the ySuperscriptXSize.
     */
    public short getYSuperscriptXSize() {

        return ySuperscriptXSize;
    }

    /**
     * Returns the ySuperscriptYOffset.
     * @return Returns the ySuperscriptYOffset.
     */
    public short getYSuperscriptYOffset() {

        return ySuperscriptYOffset;
    }

    /**
     * Returns the ySuperscriptYSize.
     * @return Returns the ySuperscriptYSize.
     */
    public short getYSuperscriptYSize() {

        return ySuperscriptYSize;
    }

    /**
     * Returns the info for this class
     * @return Returns the info for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("Table OS2\n");
        buf.append("   Version     : " + String.valueOf(version) + '\n');
        return buf.toString();
    }

    /**
     * panose
     */
    public class Panose {

        /**
         * familyType
         */
        private byte familyType = 0;

        /**
         * serifStyle
         */
        private byte serifStyle = 0;

        /**
         * weight
         */
        private byte weight = 0;

        /**
         * proportion
         */
        private byte proportion = 0;

        /**
         * contrast
         */
        private byte contrast = 0;

        /**
         * strokeVariation
         */
        private byte strokeVariation = 0;

        /**
         * armStyle
         */
        private byte armStyle = 0;

        /**
         * letterform
         */
        private byte letterform = 0;

        /**
         * midline
         */
        private byte midline = 0;

        /**
         * xHeight
         */
        private byte xHeight = 0;

        /**
         * Position in array: FAMILYTYPE
         */
        private static final int FAMILYTYPE = 0;

        /**
         * Position in array: SERIFSTYLE
         */
        private static final int SERIFSTYLE = 1;

        /**
         * Position in array: WEIGHT
         */
        private static final int WEIGHT = 2;

        /**
         * Position in array: PROPORTION
         */
        private static final int PROPORTION = 3;

        /**
         * Position in array: CONTRAST
         */
        private static final int CONTRAST = 4;

        /**
         * Position in array: STROKEVARIATION
         */
        private static final int STROKEVARIATION = 5;

        /**
         * Position in array: ARMSTYLE
         */
        private static final int ARMSTYLE = 6;

        /**
         * Position in array: LETTERFORM
         */
        private static final int LETTERFORM = 7;

        /**
         * Position in array: MIDLINE
         */
        private static final int MIDLINE = 8;

        /**
         * Position in array: XHEIGHT
         */
        private static final int XHEIGHT = 9;

        /**
         * Create a new object.
         *
         * @param panosearray    the panose
         */
        public Panose(final byte[] panosearray) {

            familyType = panosearray[FAMILYTYPE];
            serifStyle = panosearray[SERIFSTYLE];
            weight = panosearray[WEIGHT];
            proportion = panosearray[PROPORTION];
            contrast = panosearray[CONTRAST];
            strokeVariation = panosearray[STROKEVARIATION];
            armStyle = panosearray[ARMSTYLE];
            letterform = panosearray[LETTERFORM];
            midline = panosearray[MIDLINE];
            xHeight = panosearray[XHEIGHT];
        }

        /**
         * Returns the info for this class
         * @return Returns the info for this class
         */
        public String toString() {

            StringBuffer buf = new StringBuffer();
            buf.append(String.valueOf(familyType)).append(" ").append(
                    String.valueOf(serifStyle)).append(" ").append(
                    String.valueOf(weight)).append(" ").append(
                    String.valueOf(proportion)).append(" ").append(
                    String.valueOf(contrast)).append(" ").append(
                    String.valueOf(strokeVariation)).append(" ").append(
                    String.valueOf(armStyle)).append(" ").append(
                    String.valueOf(letterform)).append(" ").append(
                    String.valueOf(midline)).append(" ").append(
                    String.valueOf(xHeight));
            return buf.toString();
        }

        /**
         * Returns the armStyle.
         * @return Returns the armStyle.
         */
        public byte getArmStyle() {

            return armStyle;
        }

        /**
         * Returns the contrast.
         * @return Returns the contrast.
         */
        public byte getContrast() {

            return contrast;
        }

        /**
         * Returns the familyType.
         * @return Returns the familyType.
         */
        public byte getFamilyType() {

            return familyType;
        }

        /**
         * Returns the ferifStyle.
         * @return Returns the ferifStyle.
         */
        public byte getFerifStyle() {

            return serifStyle;
        }

        /**
         * Returns the letterform.
         * @return Returns the letterform.
         */
        public byte getLetterform() {

            return letterform;
        }

        /**
         * Returns the midline.
         * @return Returns the midline.
         */
        public byte getMidline() {

            return midline;
        }

        /**
         * Returns the proportion.
         * @return Returns the proportion.
         */
        public byte getProportion() {

            return proportion;
        }

        /**
         * Returns the strokeVariation.
         * @return Returns the strokeVariation.
         */
        public byte getStrokeVariation() {

            return strokeVariation;
        }

        /**
         * Returns the weight.
         * @return Returns the weight.
         */
        public byte getWeight() {

            return weight;
        }

        /**
         * Returns the xHeight.
         * @return Returns the xHeight.
         */
        public byte getXHeight() {

            return xHeight;
        }
    }

}