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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessInputFile;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.file.random.RandomAccessR;

/**
 * The TrueType font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class TTFFont implements XMLConvertible {

    /* 51 tag types: from java.awt.font.OpenType */

    /**
     * Character to glyph mapping.
     * Table tag "cmap" in the Open Type Specification.
     */
    public static final int CMAP = 0x636d6170;

    /**
     * Font header.
     * Table tag "head" in the Open Type Specification.
     */
    public static final int HEAD = 0x68656164;

    /**
     * Naming table.
     * Table tag "name" in the Open Type Specification.
     */
    public static final int NAME = 0x6e616d65;

    /**
     * Glyph data.
     * Table tag "glyf" in the Open Type Specification.
     */
    public static final int GLYF = 0x676c7966;

    /**
     * Maximum profile.
     * Table tag "maxp" in the Open Type Specification.
     */
    public static final int MAXP = 0x6d617870;

    /**
     * CVT preprogram.
     * Table tag "prep" in the Open Type Specification.
     */
    public static final int PREP = 0x70726570;

    /**
     * Horizontal metrics.
     * Table tag "hmtx" in the Open Type Specification.
     */
    public static final int HMTX = 0x686d7478;

    /**
     * Kerning.
     * Table tag "kern" in the Open Type Specification.
     */
    public static final int KERN = 0x6b65726e;

    /**
     * Horizontal device metrics.
     * Table tag "hdmx" in the Open Type Specification.
     */
    public static final int HDMX = 0x68646d78;

    /**
     * Index to location.
     * Table tag "loca" in the Open Type Specification.
     */
    public static final int LOCA = 0x6c6f6361;

    /**
     * PostScript Information.
     * Table tag "post" in the Open Type Specification.
     */
    public static final int POST = 0x706f7374;

    /**
     * OS/2 and Windows specific metrics.
     * Table tag "OS/2" in the Open Type Specification.
     */
    public static final int OS_2 = 0x4f532f32;

    /**
     * Control value table.
     * Table tag "cvt " in the Open Type Specification.
     */
    public static final int CVT = 0x63767420;

    /**
     * Grid-fitting and scan conversion procedure.
     * Table tag "gasp" in the Open Type Specification.
     */
    public static final int GASP = 0x67617370;

    /**
     * Vertical device metrics.
     * Table tag "VDMX" in the Open Type Specification.
     */
    public static final int VDMX = 0x56444d58;

    /**
     * Vertical metrics.
     * Table tag "vmtx" in the Open Type Specification.
     */
    public static final int VMTX = 0x766d7478;

    /**
     * Vertical metrics header.
     * Table tag "vhea" in the Open Type Specification.
     */
    public static final int VHEA = 0x76686561;

    /**
     * Horizontal metrics header.
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
     * Font program.
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
     * Embedded bitmap data.
     * Table tag "EBDT" in the Open Type Specification.
     */
    public static final int EBDT = 0x45424454;

    /**
     * Embedded bitmap location.
     * Table tag "EBLC" in the Open Type Specification.
     */
    public static final int EBLC = 0x45424c43;

    /**
     * Embedded bitmap scaling.
     * Table tag "EBSC" in the Open Type Specification.
     */
    public static final int EBSC = 0x45425343;

    /**
     * Linear threshold.
     * Table tag "LTSH" in the Open Type Specification.
     */
    public static final int LTSH = 0x4c545348;

    /**
     * PCL 5 data.
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
    private TableDirectory tableDirectory = null;

    /**
     * tabel os2
     */
    private TTFTableOS2 os2;

    /**
     * tabel cmap
     */
    private TTFTableCMAP cmap;

    /**
     * tabel glyf
     */
    private TTFTableGLYF glyf;

    /**
     * tabel ohead
     */
    private TTFTableHEAD head;

    /**
     * tabel ohhea
     */
    private TTFTableHHEA hhea;

    /**
     * tabel hmtx
     */
    private TTFTableHMTX hmtx;

    /**
     * tabel loca
     */
    private TTFTableLOCA loca;

    /**
     * tabel maxp
     */
    private TTFTableMAXP maxp;

    /**
     * tabel name
     */
    private TTFTableNAME name;

    /**
     * tabel post
     */
    private TTFTablePOST post;

    /**
     * Create a new object.
     * @param   rar     input
     * @throws IOException if an IO-error occurs
     */
    public TTFFont(final RandomAccessR rar) throws IOException {

        super();
        tablemap = new TableMap();
        read(rar);
    }

    /**
     * Create a new object.
     * @param   filename     filename for input
     * @throws IOException if an IO-error occurs
     */
    public TTFFont(final String filename) throws IOException {

        this(new RandomAccessInputFile(filename));
    }

    /**
     * Create a new object.
     * @param   file     file for input
     * @throws IOException if an IO-error occurs
     */
    public TTFFont(final File file) throws IOException {

        this(new RandomAccessInputFile(file));
    }

    /**
     * Create a new object.
     * @param   iostream    stream for input
     * @throws IOException if an IO-error occurs
     */
    public TTFFont(final InputStream iostream) throws IOException {

        this(new RandomAccessInputStream(iostream));
    }

    /**
     * Map for all tables
     */
    private TableMap tablemap;

    /**
     * Return the table with the spezial type
     * @param type the tabletype
     * @return Returns the table
     */
    public TTFTable getTable(final int type) {

        return tablemap.get(type);
    }

    /**
     * Returns the os2 table
     * @return Returns the os2 table
     */
    public TTFTableOS2 getOS2Table() {

        return os2;
    }

    /**
     * Returns the cmap table
     * @return Returns the cmap table
     */
    public TTFTableCMAP getCmapTable() {

        return cmap;
    }

    /**
     * Returns the head table
     * @return Returns the head table
     */
    public TTFTableHEAD getHeadTable() {

        return head;
    }

    /**
     * Returns the hhea table
     * @return Returns the hhea table
     */
    public TTFTableHHEA getHheaTable() {

        return hhea;
    }

    /**
     * Returns the hmtx table
     * @return Returns the hmtx table
     */
    public TTFTableHMTX getHmtxTable() {

        return hmtx;
    }

    /**
     * Returns the loca table
     * @return Returns the loca table
     */
    public TTFTableLOCA getLocaTable() {

        return loca;
    }

    /**
     * Returns the os2 table
     * @return Returns the os2 table
     */
    public TTFTableMAXP getMaxpTable() {

        return maxp;
    }

    /**
     * Returns the maxp table
     * @return Returns the maxp table
     */
    public TTFTableNAME getNameTable() {

        return name;
    }

    /**
     * Returns the font family name.
     * @return Returns the font family name.
     */
    public String getFontFamilyName() {

        return getNameTable().getRecord(TTFTableNAME.FONTFAMILYNAME);
    }

    /**
     * Returns the post table
     * @return Returns the post table
     */
    public TTFTablePOST getPostTable() {

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
     * Returns the numbers of glyphs
     * @return Returns the the numbers of glyphs
     */
    public int getNumGlyphs() {

        return maxp.getNumGlyphs();
    }

    /**
     * Returns the glyph
     * @param   i   glyph number
     * @return Returns the glyph
     */
    public TTFGlyph getGlyph(final int i) {

        return (glyf.getDescription(i) != null) ? new TTFGlyph(glyf
                .getDescription(i), hmtx.getLeftSideBearing(i), hmtx
                .getAdvanceWidth(i)) : null;
    }

    /**
     * Returns the table directory
     * @return Returns the table directory
     */
    public TableDirectory getTableDirectory() {

        return tableDirectory;
    }

    /**
     * Read the TTF
     * @param rar   input
     * @throws IOException if an IO-error occurs.
     */
    private void read(final RandomAccessR rar) throws IOException {

        tableDirectory = new TableDirectory(rar);

        // Load each of the tables
        for (int i = 0; i < tableDirectory.getNumTables(); i++) {
            TTFTable t = create(tableDirectory.getEntry(i), rar);
            if (t != null) {
                tablemap.put(t.getType(), t);
            }
        }
        rar.close();

        // Get references to commonly used tables
        os2 = (TTFTableOS2) getTable(OS_2);
        cmap = (TTFTableCMAP) getTable(CMAP);
        glyf = (TTFTableGLYF) getTable(GLYF);
        head = (TTFTableHEAD) getTable(HEAD);
        hhea = (TTFTableHHEA) getTable(HHEA);
        hmtx = (TTFTableHMTX) getTable(HMTX);
        loca = (TTFTableLOCA) getTable(LOCA);
        maxp = (TTFTableMAXP) getTable(MAXP);
        name = (TTFTableNAME) getTable(NAME);
        post = (TTFTablePOST) getTable(POST);

        // Initialize the tables that require it
        hmtx.init(hhea.getNumberOfHMetrics(), maxp.getNumGlyphs()
                - hhea.getNumberOfHMetrics());
        loca.init(maxp.getNumGlyphs(), head.getIndexToLocFormat() == 0);
        glyf.init(maxp.getNumGlyphs(), loca);
    }

    /**
     * Create the table
     * @param de        directory entry
     * @param rar       input
     * @return Returns the table
     * @throws IOException if an IO-error occurs
     */
    private TTFTable create(final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        TTFTable t = null;
        switch (de.getTag()) {
            case GPOS :
                t = new TTFTableGPOS(de, rar);
                break;
            case GSUB :
                t = new TTFTableGSUB(de, rar);
                break;
            case OS_2 :
                t = new TTFTableOS2(de, rar);
                break;
            case CMAP :
                t = new TTFTableCMAP(de, rar);
                break;
            case CVT :
                t = new TTFTableCVT(de, rar);
                break;
            case FPGM :
                t = new TTFTableFPGM(de, rar);
                break;
            case GLYF :
                t = new TTFTableGLYF(de, rar);
                break;
            case HEAD :
                t = new TTFTableHEAD(de, rar);
                break;
            case HHEA :
                t = new TTFTableHHEA(de, rar);
                break;
            case HMTX :
                t = new TTFTableHMTX(de, rar);
                break;
            case KERN :
                t = new TTFTableKERN(de, rar);
                break;
            case LOCA :
                t = new TTFTableLOCA(de, rar);
                break;
            case MAXP :
                t = new TTFTableMAXP(de, rar);
                break;
            case NAME :
                t = new TTFTableNAME(de, rar);
                break;
            case PREP :
                t = new TTFTablePREP(de, rar);
                break;
            case POST :
                t = new TTFTablePOST(de, rar);
                break;
            case HDMX :
                t = new TTFTableHDMX(de, rar);
                break;
            case GASP :
                t = new TTFTableGASP(de, rar);
                break;
            case VDMX :
                t = new TTFTableVDMX(de, rar);
                break;
            case VMTX :
                t = new TTFTableVMTX(de, rar);
                break;
            case VHEA :
                t = new TTFTableVHEA(de, rar);
                break;
            case TYP1 :
                t = new TTFTableTYP1(de, rar);
                break;
            case BSLN :
                t = new TTFTableBSLN(de, rar);
                break;
            case DSIG :
                t = new TTFTableDSIG(de, rar);
                break;
            case FVAR :
                t = new TTFTableFVAR(de, rar);
                break;
            case GVAR :
                t = new TTFTableGVAR(de, rar);
                break;
            case CFF :
                t = new TTFTableCFF(de, rar);
                break;
            case MMSD :
                t = new TTFTableMMSD(de, rar);
                break;
            case MMFX :
                t = new TTFTableMMFX(de, rar);
                break;
            case GDEF :
                t = new TTFTableGDEF(de, rar);
                break;
            case JSTF :
                t = new TTFTableJSTF(de, rar);
                break;
            case EBDT :
                t = new TTFTableEBDT(de, rar);
                break;
            case EBLC :
                t = new TTFTableEBLC(de, rar);
                break;
            case EBSC :
                t = new TTFTableEBSC(de, rar);
                break;
            case LTSH :
                t = new TTFTableLTSH(de, rar);
                break;
            case PCLT :
                t = new TTFTablePCLT(de, rar);
                break;
            case ACNT :
                t = new TTFTableACNT(de, rar);
                break;
            case AVAR :
                t = new TTFTableAVAR(de, rar);
                break;
            case BDAT :
                t = new TTFTableBDAT(de, rar);
                break;
            case BLOC :
                t = new TTFTableBLOC(de, rar);
                break;
            case CVAR :
                t = new TTFTableCVAR(de, rar);
                break;
            case FEAT :
                t = new TTFTableFEAT(de, rar);
                break;
            case FDSC :
                t = new TTFTableFDSC(de, rar);
                break;
            case FMTX :
                t = new TTFTableFMTX(de, rar);
                break;
            case JUST :
                t = new TTFTableJUST(de, rar);
                break;
            case LCAR :
                t = new TTFTableLCAR(de, rar);
                break;
            case MORT :
                t = new TTFTableMORT(de, rar);
                break;
            //            case OPBD :
            //                t = new TTFTableOPBD(de, rar);
            //                break;
            case PROP :
                t = new TTFTablePROP(de, rar);
                break;
            case TRAK :
                t = new TTFTableTRAK(de, rar);
                break;
            default :
                t = null;
        }
        return t;
    }

    /**
     * SHIFT 0x10
     */
    private static final int SHIFTX10 = 0x10;

    /**
     * Convert a Fixed value (Version)
     *
     * @param value the fixed value
     * @return Returns the float-value
     */
    static float convertVersion(final int value) {

        int v1 = value >> SHIFTX10;
        return v1;
    }

    /**
     * Returns the info for this class
     * @return Returns the info for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();

        buf.append(tableDirectory.toString());

        int[] keys = tablemap.getKeys();
        for (int i = 0; i < keys.length; i++) {
            TTFTable t = tablemap.get(keys[i]);
            if (t != null) {
                buf.append(t.toString());
            }
        }

        return buf.toString();

    }

    /**
     * only for test
     * @param args  commandline
     * @throws IOException if an IO-error occurs
     */
    public static void main(final String[] args) throws IOException {

        TTFFont f = new TTFFont("src/font/Gara.ttf");
        // write to xml-file
        XMLOutputter xmlout = new XMLOutputter();
        xmlout.setEncoding("ISO-8859-1");
        xmlout.setIndent("   ");
        xmlout.setNewlines(true);
        xmlout.setTrimAllWhite(true);
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(f.getFontFamilyName() + ".xml"));
        Document doc = new Document(f.toXML());
        xmlout.output(doc, out);
        out.close();

    }

    /**
     * the xml-tag for the font
     */
    private static final String TAG_FONT = "ttffont";

    /**
     * the attrinbut for the fontname
     */
    private static final String ATTR_FONTNAME = "fontname";

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element font = new Element(TAG_FONT);

        // name
        if (getFontFamilyName() != null) {
            font.setAttribute(ATTR_FONTNAME, getFontFamilyName());
        }

        // table directory
        if (tableDirectory instanceof XMLConvertible) {
            font.addContent(tableDirectory.toXML());
        }

        // tables
        int[] keys = tablemap.getKeys();
        for (int i = 0; i < keys.length; i++) {
            TTFTable t = tablemap.get(keys[i]);
            if (t != null && t instanceof XMLConvertible) {
                XMLConvertible xml = (XMLConvertible) t;
                font.addContent(xml.toXML());
            }
        }
        return font;
    }
}