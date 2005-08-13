/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.xtf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessInputFile;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Reader for a TrueType / OpenType font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class XtfReader implements XMLWriterConvertible {

    /* 51 tag types: from java.awt.font.OpenType */

    /**
     * Character to glyph mapping. (TTF required)
     * Table tag "cmap" in the Open Type Specification.
     */
    public static final int CMAP = 0x636d6170;

    /**
     * Font header. (TTF required)
     * Table tag "head" in the Open Type Specification.
     */
    public static final int HEAD = 0x68656164;

    /**
     * Naming table. (TTF required)
     * Table tag "name" in the Open Type Specification.
     */
    public static final int NAME = 0x6e616d65;

    /**
     * Glyph data. (TTF required)
     * Table tag "glyf" in the Open Type Specification.
     */
    public static final int GLYF = 0x676c7966;

    /**
     * Maximum profile. (TTF required)
     * Table tag "maxp" in the Open Type Specification.
     */
    public static final int MAXP = 0x6d617870;

    /**
     * CVT preprogram. (TTF optional)
     * Table tag "prep" in the Open Type Specification.
     */
    public static final int PREP = 0x70726570;

    /**
     * Horizontal metrics. (TTF required)
     * Table tag "hmtx" in the Open Type Specification.
     */
    public static final int HMTX = 0x686d7478;

    /**
     * Kerning. (TTF optional)
     * Table tag "kern" in the Open Type Specification.
     */
    public static final int KERN = 0x6b65726e;

    /**
     * Horizontal device metrics. (TTF optional)
     * Table tag "hdmx" in the Open Type Specification.
     */
    public static final int HDMX = 0x68646d78;

    /**
     * Index to location. (TTF required)
     * Table tag "loca" in the Open Type Specification.
     */
    public static final int LOCA = 0x6c6f6361;

    /**
     * PostScript Information. (TTF required)
     * Table tag "post" in the Open Type Specification.
     */
    public static final int POST = 0x706f7374;

    /**
     * OS/2 and Windows specific metrics. (TTF required)
     * Table tag "OS/2" in the Open Type Specification.
     */
    public static final int OS_2 = 0x4f532f32;

    /**
     * Control value table. (TTF optional)
     * Table tag "cvt " in the Open Type Specification.
     */
    public static final int CVT = 0x63767420;

    /**
     * Grid-fitting and scan conversion procedure. (TTF optional)
     * Table tag "gasp" in the Open Type Specification.
     */
    public static final int GASP = 0x67617370;

    /**
     * Vertical device metrics. (TTF optional)
     * Table tag "VDMX" in the Open Type Specification.
     */
    public static final int VDMX = 0x56444d58;

    /**
     * Vertical metrics. (TTF optional)
     * Table tag "vmtx" in the Open Type Specification.
     */
    public static final int VMTX = 0x766d7478;

    /**
     * Vertical metrics header. (TTF optional)
     * Table tag "vhea" in the Open Type Specification.
     */
    public static final int VHEA = 0x76686561;

    /**
     * Horizontal metrics header. (TTF required)
     * Table tag "hhea" in the Open Type Specification.
     */
    public static final int HHEA = 0x68686561;

    /**
     * Adobe Type 1 font data.
     * Table tag "typ1" in the Open Type Specification.
     */
    public static final int TYP1 = 0x74797031;

    /**
     * Baseline table.
     * Table tag "bsln" in the Open Type Specification.
     */
    public static final int BSLN = 0x62736c6e;

    /**
     * Glyph substitution.
     * Table tag "GSUB" in the Open Type Specification.
     */
    public static final int GSUB = 0x47535542;

    /**
     * Digital signature.
     * Table tag "DSIG" in the Open Type Specification.
     */
    public static final int DSIG = 0x44534947;

    /**
     * Font program. (TTF optional)
     * Table tag "fpgm" in the Open Type Specification.
     */
    public static final int FPGM = 0x6670676d;

    /**
     * Font variation.
     * Table tag "fvar" in the Open Type Specification.
     */
    public static final int FVAR = 0x66766172;

    /**
     * Glyph variation.
     * Table tag "gvar" in the Open Type Specification.
     */
    public static final int GVAR = 0x67766172;

    /**
     * Compact font format (Type1 font).
     * Table tag "CFF " in the Open Type Specification.
     */
    public static final int CFF = 0x43464620;

    /**
     * Multiple master supplementary data.
     * Table tag "MMSD" in the Open Type Specification.
     */
    public static final int MMSD = 0x4d4d5344;

    /**
     * Multiple master font metrics.
     * Table tag "MMFX" in the Open Type Specification.
     */
    public static final int MMFX = 0x4d4d4658;

    /**
     * Baseline data.
     * Table tag "BASE" in the Open Type Specification.
     */
    public static final int BASE = 0x42415345;

    /**
     * Glyph definition.
     * Table tag "GDEF" in the Open Type Specification.
     */
    public static final int GDEF = 0x47444546;

    /**
     * Glyph positioning.
     * Table tag "GPOS" in the Open Type Specification.
     */
    public static final int GPOS = 0x47504f53;

    /**
     * Justification.
     * Table tag "JSTF" in the Open Type Specification.
     */
    public static final int JSTF = 0x4a535446;

    /**
     * Embedded bitmap data. (TTF optional)
     * Table tag "EBDT" in the Open Type Specification.
     */
    public static final int EBDT = 0x45424454;

    /**
     * Embedded bitmap location. (TTF optional)
     * Table tag "EBLC" in the Open Type Specification.
     */
    public static final int EBLC = 0x45424c43;

    /**
     * Embedded bitmap scaling. (TTF optional)
     * Table tag "EBSC" in the Open Type Specification.
     */
    public static final int EBSC = 0x45425343;

    /**
     * Linear threshold. (TTF optional)
     * Table tag "LTSH" in the Open Type Specification.
     */
    public static final int LTSH = 0x4c545348;

    /**
     * PCL 5 data. (TTF optional)
     * Table tag "PCLT" in the Open Type Specification.
     */
    public static final int PCLT = 0x50434c54;

    /**
     * Accent attachment.
     * Table tag "acnt" in the Open Type Specification.
     */
    public static final int ACNT = 0x61636e74;

    /**
     * Axis variaiton.
     * Table tag "avar" in the Open Type Specification.
     */
    public static final int AVAR = 0x61766172;

    /**
     * Bitmap data.
     * Table tag "bdat" in the Open Type Specification.
     */
    public static final int BDAT = 0x62646174;

    /**
     * Bitmap location.
     * Table tag "bloc" in the Open Type Specification.
     */
    public static final int BLOC = 0x626c6f63;

    /**
     * CVT variation.
     * Table tag "cvar" in the Open Type Specification.
     */
    public static final int CVAR = 0x63766172;

    /**
     * Feature name.
     * Table tag "feat" in the Open Type Specification.
     */
    public static final int FEAT = 0x66656174;

    /**
     * Font descriptors.
     * Table tag "fdsc" in the Open Type Specification.
     */
    public static final int FDSC = 0x66647363;

    /**
     * Font metrics.
     * Table tag "fmtx" in the Open Type Specification.
     */
    public static final int FMTX = 0x666d7478;

    /**
     * Justification.
     * Table tag "just" in the Open Type Specification.
     */
    public static final int JUST = 0x6a757374;

    /**
     * Ligature caret.
     * Table tag "lcar" in the Open Type Specification.
     */
    public static final int LCAR = 0x6c636172;

    /**
     * Glyph metamorphosis.
     * Table tag "mort" in the Open Type Specification.
     */
    public static final int MORT = 0x6d6f7274;

    /**
     * Optical bounds.
     * Table tag "opbd" in the Open Type Specification.
     */
    //   public static final int OPBD = 0x6d6f7274;
    // entspricht mort
    /**
     * Glyph properties.
     * Table tag "prop" in the Open Type Specification.
     */
    public static final int PROP = 0x70726f70;

    /**
     * Tracking.
     * Table tag "trak" in the Open Type Specification.
     */
    public static final int TRAK = 0x7472616b;

    /**
     * TableDirectory
     */
    private XtfTableDirectory tableDirectory = null;

    /**
     * Table os2 (required)
     */
    private TtfTableOS2 os2;

    /**
     * Table cmap (required)
     */
    private TtfTableCMAP cmap;

    /**
     * Table glyf (required)
     */
    private TtfTableGLYF glyf;

    /**
     * Table head (required)
     */
    private TtfTableHEAD head;

    /**
     * Table hhea (required)
     */
    private TtfTableHHEA hhea;

    /**
     * Table hmtx (required)
     */
    private TtfTableHMTX hmtx;

    /**
     * Table loca (required)
     */
    private TtfTableLOCA loca;

    /**
     * Table maxp (required)
     */
    private TtfTableMAXP maxp;

    /**
     * Table name (required)
     */
    private TtfTableNAME name;

    /**
     * Table post (required)
     */
    private TtfTablePOST post;

    /**
     * Create a new object.
     * @param   rar     input
     * @throws IOException if an IO-error occurs
     */
    public XtfReader(final RandomAccessR rar) throws IOException {

        super();
        type = TTF;
        tablemap = new XtfTableMap();
        read(rar);
    }

    /**
     * Create a new object.
     * @param   filename     file name for input
     * @throws IOException if an IO-error occurs
     */
    public XtfReader(final String filename) throws IOException {

        this(new RandomAccessInputFile(filename));
    }

    /**
     * Create a new object.
     * @param   file     file for input
     * @throws IOException if an IO-error occurs
     */
    public XtfReader(final File file) throws IOException {

        this(new RandomAccessInputFile(file));
    }

    /**
     * Create a new object.
     * @param   iostream    stream for input
     * @throws IOException if an IO-error occurs
     */
    public XtfReader(final InputStream iostream) throws IOException {

        this(new RandomAccessInputStream(iostream));
    }

    /**
     * Map for all tables
     */
    private XtfTableMap tablemap;

    /**
     * Return the table with the special type
     * @param tabletype the table type
     * @return Returns the table
     */
    public XtfTable getTable(final int tabletype) {

        return tablemap.get(tabletype);
    }

    /**
     * Returns the os2 table
     * @return Returns the os2 table
     */
    public TtfTableOS2 getOS2Table() {

        return os2;
    }

    /**
     * Returns the cmap table
     * @return Returns the cmap table
     */
    public TtfTableCMAP getCmapTable() {

        return cmap;
    }

    /**
     * Returns the head table
     * @return Returns the head table
     */
    public TtfTableHEAD getHeadTable() {

        return head;
    }

    /**
     * Returns the hhea table
     * @return Returns the hhea table
     */
    public TtfTableHHEA getHheaTable() {

        return hhea;
    }

    /**
     * Returns the hmtx table
     * @return Returns the hmtx table
     */
    public TtfTableHMTX getHmtxTable() {

        return hmtx;
    }

    /**
     * Returns the loca table
     * @return Returns the loca table
     */
    public TtfTableLOCA getLocaTable() {

        return loca;
    }

    /**
     * Returns the os2 table
     * @return Returns the os2 table
     */
    public TtfTableMAXP getMaxpTable() {

        return maxp;
    }

    /**
     * Returns the maxp table
     * @return Returns the maxp table
     */
    public TtfTableNAME getNameTable() {

        return name;
    }

    /**
     * Returns the font family name.
     * @return Returns the font family name.
     */
    public String getFontFamilyName() {

        return getNameTable().getRecord(TtfTableNAME.FONTFAMILYNAME);
    }

    /**
     * Returns the post table
     * @return Returns the post table
     */
    public TtfTablePOST getPostTable() {

        return post;
    }

    /**
     * Returns the ascent
     * @return Returns the ascent
     */
    public int getAscent() {

        return hhea.getAscender();
    }

    /**
     * Returns the descent
     * @return Returns the descent
     */
    public int getDescent() {

        return hhea.getDescender();
    }

    /**
     * Returns the units per em.
     * @return Returns the units per em.
     */
    public int getUntisPerEm() {

        return head.getUnitsPerEm();
    }

    /**
     * Returns the BoundingBox.
     * <p>0 xmin</p>
     * <p>1 ymin</p>
     * <p>2 xmax</p>
     * <p>3 ymax</p>
     * @return Returns the BoundingBox.
     */
    public int[] getBoundingBox() {

        int[] bb = new int[4];
        bb[0] = head.getXMin();
        bb[1] = head.getYMin();
        bb[2] = head.getXMax();
        bb[3] = head.getYMax();
        return bb;
    }

    /**
     * Returns the number of glyphs.
     * @return Returns the number of glyphs.
     */
    public int getNumberOfGlyphs() {

        return maxp.getNumGlyphs();
    }

    /**
     * Returns the italic angle.
     * @return Returns the italic angle.
     */
    public int getItalicAngle() {

        return post.getItalicAngle();
    }

    /**
     * Return the underline position.
     * @return Return the underline position.
     */
    public int getUnderlinePos() {

        return post.getUnderlinePosition();
    }

    /**
     * Return the underline thickness.
     * @return Return the underline thickness.
     */
    public int getUnderlineThickness() {

        return post.getUnderlineThickness();
    }

    /**
     * Returns the glyph
     * @param   i   glyph number
     * @return Returns the glyph
     */
    public XtfGlyph getGlyph(final int i) {

        return (glyf.getDescription(i) != null) ? new XtfGlyph(glyf
                .getDescription(i), hmtx.getLeftSideBearing(i), hmtx
                .getAdvanceWidth(i)) : null;
    }

    /**
     * Returns the table directory
     * @return Returns the table directory
     */
    public XtfTableDirectory getTableDirectory() {

        return tableDirectory;
    }

    /**
     * Read the TTF
     * @param rar   input
     * @throws IOException if an IO-error occurs.
     */
    private void read(final RandomAccessR rar) throws IOException {

        tableDirectory = new XtfTableDirectory(rar);

        // Load each of the tables
        for (int i = 0; i < tableDirectory.getNumTables(); i++) {
            XtfTable t = create(tableDirectory.getEntry(i), rar);
            if (t != null) {
                tablemap.put(t.getType(), t);
            }
        }

        // Get references to required tables
        os2 = (TtfTableOS2) getTable(OS_2);
        cmap = (TtfTableCMAP) getTable(CMAP);
        glyf = (TtfTableGLYF) getTable(GLYF);
        head = (TtfTableHEAD) getTable(HEAD);
        hhea = (TtfTableHHEA) getTable(HHEA);
        hmtx = (TtfTableHMTX) getTable(HMTX);
        loca = (TtfTableLOCA) getTable(LOCA);
        maxp = (TtfTableMAXP) getTable(MAXP);
        name = (TtfTableNAME) getTable(NAME);
        post = (TtfTablePOST) getTable(POST);

        // sort tables for init
        XtfTable[] tabs = tablemap.getTables();

        Arrays.sort(tabs, new Comparator() {

            /**
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            public int compare(final Object arg0, final Object arg1) {

                XtfTable t0 = (XtfTable) arg0;
                XtfTable t1 = (XtfTable) arg1;
                if (t0.getInitOrder() > t1.getInitOrder()) {
                    return 1;
                }
                return 0;
            }
        });

        // init tables
        for (int i = 0; i < tabs.length; i++) {
            tabs[i].init();
        }

        // close
        rar.close();
    }

    /**
     * Create the table
     * @param de        directory entry
     * @param rar       input
     * @return Returns the table
     * @throws IOException if an IO-error occurs
     */
    private XtfTable create(final XtfTableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        XtfTable t = null;
        switch (de.getTag()) {
            case GPOS :
                t = new OtfTableGPOS(tablemap, de, rar);
                type = OTF;
                break;
            case GSUB :
                t = new OtfTableGSUB(tablemap, de, rar);
                type = OTF;
                break;
            case OS_2 :
                t = new TtfTableOS2(tablemap, de, rar);
                break;
            case CMAP :
                t = new TtfTableCMAP(tablemap, de, rar);
                break;
            case CVT :
                t = new TtfTableCVT(tablemap, de, rar);
                break;
            case FPGM :
                t = new TtfTableFPGM(tablemap, de, rar);
                break;
            case GLYF :
                t = new TtfTableGLYF(tablemap, de, rar);
                break;
            case HEAD :
                t = new TtfTableHEAD(tablemap, de, rar);
                break;
            case HHEA :
                t = new TtfTableHHEA(tablemap, de, rar);
                break;
            case HMTX :
                t = new TtfTableHMTX(tablemap, de, rar);
                break;
            case KERN :
                t = new TtfTableKERN(tablemap, de, rar);
                break;
            case LOCA :
                t = new TtfTableLOCA(tablemap, de, rar);
                break;
            case MAXP :
                t = new TtfTableMAXP(tablemap, de, rar);
                break;
            case NAME :
                t = new TtfTableNAME(tablemap, de, rar);
                break;
            case PREP :
                t = new TtfTablePREP(tablemap, de, rar);
                break;
            case POST :
                t = new TtfTablePOST(tablemap, de, rar);
                break;
            case HDMX :
                t = new TtfTableHDMX(tablemap, de, rar);
                break;
            case GASP :
                t = new TtfTableGASP(tablemap, de, rar);
                break;
            case VDMX :
                t = new TtfTableVDMX(tablemap, de, rar);
                break;
            case VMTX :
                t = new TtfTableVMTX(tablemap, de, rar);
                break;
            case VHEA :
                t = new TtfTableVHEA(tablemap, de, rar);
                break;
            case TYP1 :
                t = new OtfTableTYP1(tablemap, de, rar);
                type = OTF;
                break;
            case BSLN :
                t = new OtfTableBSLN(tablemap, de, rar);
                type = OTF;
                break;
            case DSIG :
                t = new OtfTableDSIG(tablemap, de, rar);
                type = OTF;
                break;
            case FVAR :
                t = new OtfTableFVAR(tablemap, de, rar);
                type = OTF;
                break;
            case GVAR :
                t = new OtfTableGVAR(tablemap, de, rar);
                type = OTF;
                break;
            case CFF :
                t = new OtfTableCFF(tablemap, de, rar);
                type = OTF;
                break;
            case MMSD :
                t = new OtfTableMMSD(tablemap, de, rar);
                type = OTF;
                break;
            case MMFX :
                t = new OtfTableMMFX(tablemap, de, rar);
                type = OTF;
                break;
            case GDEF :
                t = new OtfTableGDEF(tablemap, de, rar);
                type = OTF;
                break;
            case JSTF :
                t = new OtfTableJSTF(tablemap, de, rar);
                type = OTF;
                break;
            case EBDT :
                t = new TtfTableEBDT(tablemap, de, rar);
                break;
            case EBLC :
                t = new TtfTableEBLC(tablemap, de, rar);
                break;
            case EBSC :
                t = new TtfTableEBSC(tablemap, de, rar);
                break;
            case LTSH :
                t = new TtfTableLTSH(tablemap, de, rar);
                break;
            case PCLT :
                t = new TtfTablePCLT(tablemap, de, rar);
                break;
            case ACNT :
                t = new OtfTableACNT(tablemap, de, rar);
                type = OTF;
                break;
            case AVAR :
                t = new OtfTableAVAR(tablemap, de, rar);
                break;
            case BDAT :
                t = new OtfTableBDAT(tablemap, de, rar);
                type = OTF;
                break;
            case BLOC :
                t = new OtfTableBLOC(tablemap, de, rar);
                type = OTF;
                break;
            case CVAR :
                t = new OtfTableCVAR(tablemap, de, rar);
                type = OTF;
                break;
            case FEAT :
                t = new OtfTableFEAT(tablemap, de, rar);
                type = OTF;
                break;
            case FDSC :
                t = new OtfTableFDSC(tablemap, de, rar);
                type = OTF;
                break;
            case FMTX :
                t = new OtfTableFMTX(tablemap, de, rar);
                type = OTF;
                break;
            case JUST :
                t = new OtfTableJUST(tablemap, de, rar);
                type = OTF;
                break;
            case LCAR :
                t = new OtfTableLCAR(tablemap, de, rar);
                type = OTF;
                break;
            case MORT :
                t = new OtfTableMORT(tablemap, de, rar);
                type = OTF;
                break;
            //            case OPBD :
            //                t = new TTFTableOPBD(de, rar);
            //                break;
            case PROP :
                t = new OtfTablePROP(tablemap, de, rar);
                type = OTF;
                break;
            case TRAK :
                t = new OtfTableTRAK(tablemap, de, rar);
                type = OTF;
                break;
            case BASE :
                t = new OtfTableBASE(tablemap, de, rar);
                type = OTF;
                break;
            default :
                t = null;
        }
        return t;
    }

    /**
     * Convert a Fixed value (Version)
     *
     * @param value the fixed value
     * @return Returns the float-value
     */
    static float convertVersion(final int value) {

        int v1 = value >> XtfConstants.SHIFTX10;
        return v1;
    }

    /**
     * Convert the internal long date format to a Date.
     * <p>
     * Calculate the difference between the original Mac epoch (1904)
     * to the epoch on this machine.
     * </p>
     * @param value the long format (mac)
     * @return Returns the calculate Date
     */
    static Date convertDate(final long value) {

        // TODO wrong!!!
        return new Date(value);
    }

    /**
     * seconds from 1904 to 1971
     */
    //private static final long SECONDS1904TO1971 = 60 * 60 * 24 * (365 * (1971 - 1904) + 17);
    /**
     * Convert a int value in a binary string
     * @param value the int value
     * @return Returns the binary string
     */
    static String convertIntToBinaryString(final int value) {

        StringBuffer buf = new StringBuffer("00000000000000000000000000000000");
        buf.append(Integer.toBinaryString(value));
        return buf.substring(buf.length() - XtfConstants.SHIFT32).toString();
    }

    /**
     * Convert a int value in a hex string
     * @param value the int value
     * @return Returns the hex string
     */
    static String convertIntToHexString(final int value) {

        StringBuffer buf = new StringBuffer("00000000");
        buf.append(Integer.toHexString(value));
        return "0x"
                + buf.substring(buf.length() - XtfConstants.SHIFT8).toString();

    }

    /**
     * the type
     */
    private Type type = TTF;

    /**
     * Returns the font type.
     * @return Returns the font type
     */
    public Type getType() {

        return type;
    }

    /**
     * TTF
     */
    public static final Type TTF = new Type();

    /**
     * OTF
     */
    public static final Type OTF = new Type();

    /**
     * Font-Type
     */
    private static class Type {

        /**
         * Create a new object.
         */
        public Type() {

            super();
        }
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(
     *      de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("font");
        writer.writeAttribute("type", type == TTF ? "ttf" : "otf");

        // name
        if (getFontFamilyName() != null) {
            writer.writeAttribute("name", getFontFamilyName());
        }

        // table directory
        tableDirectory.writeXML(writer);

        // tables
        int[] keys = tablemap.getKeys();
        for (int i = 0; i < keys.length; i++) {
            XtfTable t = tablemap.get(keys[i]);

            if (t != null && t instanceof XMLWriterConvertible) {
                XMLWriterConvertible xml = (XMLWriterConvertible) t;
                xml.writeXML(writer);
            } else {
                System.out.println(t.getShortcut());
            }
        }
        writer.writeEndElement();
    }

    /**
     * only for test
     * @param args  command line
     * @throws IOException if an IO-error occurs
     */
    public static void main(final String[] args) throws IOException {

        XtfReader f = new XtfReader("src/font/Gara.ttf");
        // XtfReader f = new XtfReader("src/font/Oregon LDO.otf");

        // write to xml-file
        XMLStreamWriter writer = new XMLStreamWriter(new FileOutputStream(
                "x.tmp"), "ISO-8859-1");
        //writer.setBeauty(true);
        writer.writeStartDocument();
        f.writeXML(writer);
        writer.writeEndDocument();
        writer.close();
        System.out.println("x.tmp erstellt");
    }
}