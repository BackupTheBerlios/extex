/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.util.font;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.dante.extex.ExTeX;
import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.xtf.TtfTableCMAP;
import de.dante.extex.unicodeFont.format.xtf.TtfTableHMTX;
import de.dante.extex.unicodeFont.format.xtf.TtfTableNAME;
import de.dante.extex.unicodeFont.format.xtf.TtfTablePOST;
import de.dante.extex.unicodeFont.format.xtf.XtfReader;
import de.dante.extex.unicodeFont.format.xtf.TtfTableCMAP.IndexEntry;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Print information about a ttf/otf file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public final class XtfInfo extends AbstractFontUtil {

    /**
     * The properties.
     */
    private Properties props = System.getProperties();

    /**
     * Create a new object.
     *
     * @throws ConfigurationException if a configuration error occurs.
     */
    private XtfInfo() throws ConfigurationException {

        super(XtfInfo.class);

    }

    /**
     * The xtf file name.
     */
    private String xtfFileName;

    /**
     * The xtf parser.
     */
    private XtfReader parser;

    /**
     * The xtf file object.
     */
    private File xtffile;

    /**
     * Print the xtf info and something else.
     *
     * @throws IOException  if a IO-error occurred.
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if a font error occurred.
     * @throws DocumentException from iText
     */
    private void doIt() throws IOException, ConfigurationException,
            FontException, DocumentException {

        if (props.getProperty("xtf.ttfinfo", "false").equals("true")) {
            printttfindo();
        }

        if (props.getProperty("xtf.ttfcopy", "false").equals("true")) {
            copyttf();
        }

        // read the xtf file
        InputStream xtfin = null;

        // find directly the ttf file.
        xtfFileName = props.getProperty("xtf.file", "null");
        if (!xtfFileName.equals("null")) {
            xtffile = new File(xtfFileName);

            if (xtffile.canRead()) {
                xtfin = new FileInputStream(xtffile);
            } else {
                // use the file finder
                xtfin = getFinder().findResource(xtffile.getName(), "");
            }

            if (xtfin == null) {
                throw new FileNotFoundException(xtfFileName);
            }

            parser = new XtfReader(xtfin);

            if (props.getProperty("xtf.printhead", "true").equals("true")) {
                doHead();
            }

            if (props.getProperty("xtf.listglyphs", "false").equals("true")) {
                listGlyphs();
            }

            if (props.getProperty("xtf.charinfo", "false").equals("true")) {
                charInfo();
            }

            if (props.getProperty("xtf.glyphinfo", "false").equals("true")) {
                glyphInfo();
            }

            if (props.getProperty("xtf.printglyphs", "false").equals("true")) {
                printGlpyhs(props.getProperty("xtf.outdir", "."));
            }
        }
    }

    /**
     * Copy the ttf file.
     * @throws IOException if an IO-error occurred.
     * @throws DocumentException from the iText system.
     */
    private void copyttf() throws IOException, DocumentException {

        boolean group = Boolean
                .valueOf(props.getProperty("xtf.group", "false"))
                .booleanValue();

        boolean printglyphs = Boolean.valueOf(
                props.getProperty("xtf.printglyphs", "false")).booleanValue();

        boolean printfamily = Boolean.valueOf(
                props.getProperty("xtf.printfamily", "false")).booleanValue();

        ArrayList family = new ArrayList();

        File dir = new File(props.getProperty("xtf.ttfdir", "."));
        String outdir = props.getProperty("xtf.outdir", ".");

        if (dir.isDirectory()) {
            String[] lists = dir.list(new FilenameFilter() {

                /**
                 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
                 */
                public boolean accept(final File directory, final String name) {

                    return name.matches(".*\\.[tToO][tT][fF]");
                }
            });

            Arrays.sort(lists);

            for (int i = 0; i < lists.length; i++) {

                File src = new File(dir.getAbsolutePath() + File.separator
                        + lists[i]);
                if (src.canRead()) {
                    try {
                        parser = new XtfReader(new FileInputStream(src));
                        TtfTableNAME name = parser.getNameTable();
                        int[] platformids = name.getPlatformIDs();

                        int pltid = TtfTableNAME.MICROSOFT;
                        if (!name.existsPlatfrom(TtfTableNAME.MICROSOFT)) {
                            // use the last one
                            pltid = platformids[platformids.length - 1];
                        }
                        String psname = name.getRecordString(pltid,
                                TtfTableNAME.POSTSCRIPTNAME);
                        String familyname = name.getRecordString(pltid,
                                TtfTableNAME.FONTFAMILYNAME);

                        String ext = "ttf";
                        if (parser.getType() == XtfReader.TTF) {
                            ext = "ttf";
                        } else {
                            ext = "otf";
                        }

                        File dst;
                        if (group) {
                            File newdir = new File(outdir + File.separator
                                    + familyname);
                            newdir.mkdir();
                            dst = new File(newdir.getAbsolutePath()
                                    + File.separator + psname + "." + ext);
                        } else {

                            dst = new File(outdir + File.separator + psname
                                    + "." + ext);
                        }

                        getLogger().severe(
                                getLocalizer().format("XtfInfo.TtfCopy",
                                        src.getAbsoluteFile(),
                                        dst.getAbsoluteFile()));

                        copy(src, dst);
                        xtffile = dst;
                        String parentdir = dst.getParent();
                        if (!family.contains(parentdir)) {
                            family.add(parentdir);
                        }
                        if (printglyphs) {
                            printGlpyhs(parentdir);
                        }
                    } catch (Exception e) {
                        // Error in ttf
                        System.err.println("Font: " + src);
                        e.printStackTrace();
                    }

                }
            }
        }
        //        if (printfamily) {
        //            for (int i = 0, n = family.size(); i < n; i++) {
        //                String familydir = (String) family.get(i);
        //                printFamily(familydir);
        //            }
        //        }
    }

    /**
     * Print the font family with all glyphs.
     *
     * @param familydir The directory.
     * @throws IOException if an IO-error occurred.
     */
    private void printFamily(final String familydir) throws IOException {

        Document document = new Document();

        File dir = new File(familydir);

        if (dir.isDirectory()) {
            String[] lists = dir.list(new FilenameFilter() {

                /**
                 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
                 */
                public boolean accept(final File directory, final String name) {

                    return name.matches(".*\\.[tToO][tT][fF]");
                }
            });

            Arrays.sort(lists);

            XtfReader[] reader = new XtfReader[lists.length];
            for (int i = 0; i < lists.length; i++) {
                reader[i] = new XtfReader(lists[i]);
            }

            String pdfname = dir.getAbsolutePath() + File.separator
                    + dir.getName() + ".pdf";

            System.out.println("##### " + pdfname);
            System.exit(1);
            //                    "");
            //            PdfWriter.getInstance(document, new FileOutputStream(outdir
            //                    + File.separator + pdfname + ".pdf"));
            //            document.open();
            //
            //            Font hex = new Font(Font.HELVETICA, FONT_6);
            //
            //            BaseFont basefont = BaseFont.createFont(xtffile.getAbsolutePath(),
            //                    BaseFont.IDENTITY_H, true);
            //
            //            Font font = new Font(basefont, FONT_18, Font.NORMAL);
            //
            //            // first page
            //
            //            PdfPTable table1 = new PdfPTable(2);
            //            table1.getDefaultCell().setBorderWidth(0);
            //            table1.addCell("XtfInfo:    (ExTeX " + ExTeX.getVersion() + ")");
            //            table1.addCell("");
            //            table1.addCell("Filename");
            //            table1.addCell(xtffile.getName());
            //            table1.addCell("Fullname");
            //            table1.addCell(parser.getFontFamilyName());
            //
            //            // name
            //            TtfTableNAME name = parser.getNameTable();
            //            int[] platformids = name.getPlatformIDs();
            //
            //            int pltid = TtfTableNAME.MICROSOFT;
            //            if (!name.existsPlatfrom(TtfTableNAME.MICROSOFT)) {
            //                // use the last one
            //                pltid = platformids[platformids.length - 1];
            //            }
            //
            //            table1.addCell("Platform");
            //            table1.addCell(TtfTableNAME.getPlatformName(pltid));
            //            table1.addCell("PSFontname");
            //            table1.addCell(name.getRecordString(pltid,
            //                    TtfTableNAME.POSTSCRIPTNAME));
            //            table1.addCell("Subfamily");
            //            table1.addCell(name.getRecordString(pltid,
            //                    TtfTableNAME.FONTSUBFAMILYNAME));
            //            table1.addCell("Familyname");
            //            table1.addCell(name.getRecordString(pltid,
            //                    TtfTableNAME.FONTFAMILYNAME));
            //            table1.addCell("Version");
            //            table1.addCell(name.getRecordString(pltid,
            //                    TtfTableNAME.VERSIONSTRING));
            //            table1.addCell("Number of Glpyhs");
            //            table1.addCell(String.valueOf(parser.getNumberOfGlyphs()));
            //            table1.addCell("Numbers");
            //            table1.addCell("01234567890");
            //            table1.addCell("Umlaute");
            //            table1.addCell("� � � �  � � �");
            //            table1.addCell("Ligaturen");
            //            table1.addCell("fi ff ffl ffi");
            //
            //            document.add(table1);
            //
            //            // add dummy text
            //            URL dummytxt = getClass().getResource("XtfInfo.txt");
            //            if (dummytxt != null) {
            //
            //                InputStream dummyin = dummytxt.openStream();
            //                if (dummyin != null) {
            //                    BufferedReader reader = new BufferedReader(
            //                            new InputStreamReader(dummyin));
            //
            //                    String zeile;
            //                    while ((zeile = reader.readLine()) != null) {
            //                        Phrase dummy = new Phrase(zeile, font);
            //                        document.add(new Paragraph(dummy));
            //                    }
            //                }
            //            }
            //            document.newPage();
            //
            //            // glyph table
            //            PdfPTable table = new PdfPTable(BLOCK);
            //            table.setWidthPercentage(100);
            //            table.getDefaultCell().setBorderWidth(1);
            //            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            //            for (int k = 0; k < MAXGLYPHS; k += BLOCK) {
            //
            //                boolean glyphexists = false;
            //                for (int b = 0; b < BLOCK; b++) {
            //                    if (basefont.charExists((char) (k + b))) {
            //                        glyphexists = true;
            //                        break;
            //                    }
            //                }
            //
            //                if (glyphexists) {
            //                    for (int b = 0; b < BLOCK; b++) {
            //
            //                        int kk = k + b;
            //                        char c = (char) kk;
            //
            //                        if (basefont.charExists(c)) {
            //                            Phrase ph = new Phrase(FONT_12, new String(
            //                                    new char[]{c}), font);
            //                            ph.add(new Phrase(FONT_12, "\n\n" + cst(kk) + "  ("
            //                                    + kk + ")", hex));
            //
            //                            String namettf = parser.mapCharCodeToGlyphname(kk,
            //                                    TtfTableCMAP.PLATFORM_MICROSOFT,
            //                                    TtfTableCMAP.ENCODING_ISO_ISO10646);
            //
            //                            if (namettf == null) {
            //                                namettf = "-xxx-";
            //                            }
            //                            Phrase glyphttf = new Phrase("\n" + namettf, hex);
            //
            //                            ph.add(glyphttf);
            //                            table.addCell(ph);
            //                        } else {
            //                            Phrase ph = new Phrase("\u00a0");
            //                            table.addCell(ph);
            //                        }
            //                    }
            //                }
            //            }
            // document.add(table);

        }
        document.close();

    }

    /**
     * The buffer size.
     */
    private static final int BUFFERSIZE = 0xffff;

    /**
     * Copy a source file to destination file.
     *
     * @param src    The source file.
     * @param dst    The destination file.
     * @throws IOException if an IO-error occurred.
     */
    private void copy(final File src, final File dst) throws IOException {

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                src), BUFFERSIZE);
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(dst), BUFFERSIZE);

        byte[] buf = new byte[BUFFERSIZE];
        int size;
        while ((size = in.read(buf)) != -1) {
            out.write(buf, 0, size);
        }

        in.close();
        out.close();
    }

    /**
     * Print the ttf info.
     * @throws IOException if an IO-error occurred.
     */
    private void printttfindo() throws IOException {

        File dir = new File(props.getProperty("xtf.ttfdir", "."));
        String outdir = props.getProperty("xtf.outdir", ".");

        if (dir.isDirectory()) {
            String[] lists = dir.list(new FilenameFilter() {

                /**
                 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
                 */
                public boolean accept(final File directory, final String name) {

                    return name.matches(".*\\.[tToO][tT][fF]");
                }
            });

            Arrays.sort(lists);

            BufferedWriter out = new BufferedWriter(new FileWriter(outdir
                    + File.separator + "xtf.info"));

            for (int i = 0; i < lists.length; i++) {

                File file = new File(dir.getAbsolutePath() + File.separator
                        + lists[i]);
                if (file.canRead()) {
                    try {
                        parser = new XtfReader(new FileInputStream(file));
                        TtfTableNAME name = parser.getNameTable();
                        int[] platformids = name.getPlatformIDs();

                        int pltid = TtfTableNAME.MICROSOFT;
                        if (!name.existsPlatfrom(TtfTableNAME.MICROSOFT)) {
                            // use the last one
                            pltid = platformids[platformids.length - 1];
                        }
                        out.write(file.getName());
                        out.write("\t;\t");
                        out.write(name.getRecordString(pltid,
                                TtfTableNAME.POSTSCRIPTNAME));
                        out.newLine();
                        out.flush();
                        getLogger().severe(
                                getLocalizer().format(
                                        "XtfInfo.TtfInfo",
                                        file.getName(),
                                        name.getRecordString(pltid,
                                                TtfTableNAME.POSTSCRIPTNAME)));
                    } catch (Exception e) {
                        // ignore error
                        e.printStackTrace();
                    }
                }
            }
            out.close();
        }

    }

    /**
     * font size 6.
     */
    private static final int FONT_6 = 6;

    /**
     * font size 12.
     */
    private static final int FONT_12 = 12;

    /**
     * font size 18.
     */
    private static final int FONT_18 = 18;

    /**
     * max. glyphs in a ttf font.
     */
    private static final int MAXGLYPHS = 0xffff;

    /**
     * How much glyphs in one table row.
     */
    private static final int BLOCK = 8;

    /**
     * Print the glyphs in a pdf file.
     * @param outdir    The directory for the output.
     * @throws DocumentException from iText
     * @throws IOException if an IO-error occurred.
     */
    private void printGlpyhs(final String outdir) throws IOException,
            DocumentException {

        Document document = new Document();

        if (!xtffile.canRead()) {
            getLogger().severe(getLocalizer().format("XtfInfo.ErrPG"));

        } else {

            String pdfname = xtffile.getName().replaceAll("\\.[tToO][tT][fF]",
                    "");
            PdfWriter.getInstance(document, new FileOutputStream(outdir
                    + File.separator + pdfname + ".pdf"));
            document.open();

            Font hex = new Font(Font.HELVETICA, FONT_6);

            BaseFont basefont = BaseFont.createFont(xtffile.getAbsolutePath(),
                    BaseFont.IDENTITY_H, true);

            Font font = new Font(basefont, FONT_18, Font.NORMAL);

            // first page

            PdfPTable table1 = new PdfPTable(2);
            table1.getDefaultCell().setBorderWidth(0);
            table1.addCell("XtfInfo:    (ExTeX " + ExTeX.getVersion() + ")");
            table1.addCell("");
            table1.addCell("Filename");
            table1.addCell(xtffile.getName());
            table1.addCell("Fullname");
            table1.addCell(parser.getFontFamilyName());

            // name
            TtfTableNAME name = parser.getNameTable();
            int[] platformids = name.getPlatformIDs();

            int pltid = TtfTableNAME.MICROSOFT;
            if (!name.existsPlatfrom(TtfTableNAME.MICROSOFT)) {
                // use the last one
                pltid = platformids[platformids.length - 1];
            }

            table1.addCell("Platform");
            table1.addCell(TtfTableNAME.getPlatformName(pltid));
            table1.addCell("PSFontname");
            table1.addCell(name.getRecordString(pltid,
                    TtfTableNAME.POSTSCRIPTNAME));
            table1.addCell("Subfamily");
            table1.addCell(name.getRecordString(pltid,
                    TtfTableNAME.FONTSUBFAMILYNAME));
            table1.addCell("Familyname");
            table1.addCell(name.getRecordString(pltid,
                    TtfTableNAME.FONTFAMILYNAME));
            table1.addCell("Version");
            table1.addCell(name.getRecordString(pltid,
                    TtfTableNAME.VERSIONSTRING));
            table1.addCell("Number of Glpyhs");
            table1.addCell(String.valueOf(parser.getNumberOfGlyphs()));
            table1.addCell("Numbers");
            table1.addCell("01234567890");
            table1.addCell("Umlaute");
            table1.addCell("Ö Ä Ü  ö ä ü ß");
            table1.addCell("Ligaturen");
            table1.addCell("fi ff ffl ffi");

            document.add(table1);

            // add dummy text
            URL dummytxt = getClass().getResource("XtfInfo.txt");
            if (dummytxt != null) {

                InputStream dummyin = dummytxt.openStream();
                if (dummyin != null) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(dummyin));

                    String zeile;
                    while ((zeile = reader.readLine()) != null) {
                        Phrase dummy = new Phrase(zeile, font);
                        document.add(new Paragraph(dummy));
                    }
                }
            }
            document.newPage();

            // glyph table
            PdfPTable table = new PdfPTable(BLOCK);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorderWidth(1);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            for (int k = 0; k < MAXGLYPHS; k += BLOCK) {

                boolean glyphexists = false;
                for (int b = 0; b < BLOCK; b++) {
                    if (basefont.charExists((char) (k + b))) {
                        glyphexists = true;
                        break;
                    }
                }

                if (glyphexists) {
                    for (int b = 0; b < BLOCK; b++) {

                        int kk = k + b;
                        char c = (char) kk;

                        if (basefont.charExists(c)) {
                            Phrase ph = new Phrase(FONT_12, new String(
                                    new char[]{c}), font);
                            ph.add(new Phrase(FONT_12, "\n\n" + cst(kk) + "  ("
                                    + kk + ")", hex));

                            String namettf = parser.mapCharCodeToGlyphname(kk,
                                    TtfTableCMAP.PLATFORM_MICROSOFT,
                                    TtfTableCMAP.ENCODING_ISO_ISO10646);

                            if (namettf == null) {
                                namettf = "-xxx-";
                            }
                            Phrase glyphttf = new Phrase("\n" + namettf, hex);

                            ph.add(glyphttf);
                            table.addCell(ph);
                        } else {
                            Phrase ph = new Phrase("\u00a0");
                            table.addCell(ph);
                        }
                    }
                }
            }
            document.add(table);

        }
        document.close();

    }

    /**
     * Convert a int to a tow digit hex string.
     * @param c The int.
     * @return Returns the hex value.
     */
    private String cst(final int c) {

        String s = "0000" + Integer.toHexString(c).toUpperCase();
        return "0x" + s.substring(s.length() - 4);
    }

    /**
     * CharInfo.
     */
    private void charInfo() {

        TtfTableCMAP cmap = parser.getCmapTable();
        IndexEntry[] entries = cmap.getEntries();

        int charcode = 1;
        try {
            charcode = Integer.parseInt(props.getProperty("xtf.charcode", "1"));
        } catch (NumberFormatException e) {
            // ignore
            charcode = -1;
        }

        for (int i = 0; i < entries.length; i++) {
            int pid = entries[i].getPlatformId();
            int eid = entries[i].getEncodingId();
            String platformname = entries[i].getPlatformName();
            String encname = entries[i].getEncodingName();
            getLogger().severe(
                    getLocalizer()
                            .format("XtfInfo.CMAP", platformname, encname));
            String gn = parser.mapCharCodeToGlyphname(charcode, (short) pid,
                    (short) eid);
            String cchex = "0x" + Integer.toHexString(charcode);
            if (gn == null) {
                gn = "-";
            }
            getLogger().severe(
                    getLocalizer().format("XtfInfo.CHARMAP",
                            String.valueOf(charcode), cchex, gn));

        }

    }

    /**
     * GlyphInfo.
     */
    private void glyphInfo() {

        TtfTablePOST post = parser.getPostTable();
        String glyphName = props.getProperty("xtf.glyphname", "A");
        int gpos = post.getGlyphNamePosition(glyphName);

        getLogger()
                .severe(
                        getLocalizer().format("XtfInfo.GlyphPOS", glyphName,
                                String.valueOf(gpos),
                                "0x" + Integer.toHexString(gpos)));

        if (gpos >= 0) {
            TtfTableHMTX hmtx = parser.getHmtxTable();

            int width = hmtx.getAdvanceWidth(gpos);
            getLogger().severe(
                    getLocalizer().format("XtfInfo.GlyphWidth",
                            String.valueOf(width)));

        }

    }

    /**
     * List the glyphs.
     */
    private void listGlyphs() {

        TtfTablePOST post = parser.getPostTable();
        String[] glyphs = post.getPsGlyphName();
        Arrays.sort(glyphs);

        for (int i = 0; i < glyphs.length; i++) {
            getLogger().severe(
                    getLocalizer().format("XtfInfo.ListGlyph", glyphs[i]));
        }

    }

    /**
     * Print the head information.
     */
    private void doHead() {

        getLogger().severe(
                getLocalizer().format("XtfInfo.Head", ExTeX.getVersion()));
        getLogger().severe(
                getLocalizer().format("XtfInfo.Filename", xtfFileName));
        getLogger().severe(
                getLocalizer().format("XtfInfo.Fullname",
                        parser.getFontFamilyName()));

        // name
        TtfTableNAME name = parser.getNameTable();
        int[] platformids = name.getPlatformIDs();

        int pltid = TtfTableNAME.MICROSOFT;
        if (!name.existsPlatfrom(TtfTableNAME.MICROSOFT)) {
            // use the last one
            pltid = platformids[platformids.length - 1];
        }

        getLogger().severe(
                getLocalizer().format("XtfInfo.Platform",
                        TtfTableNAME.getPlatformName(pltid)));
        getLogger().severe(
                getLocalizer().format(
                        "XtfInfo.PSName",
                        name
                                .getRecordString(pltid,
                                        TtfTableNAME.POSTSCRIPTNAME)));
        getLogger().severe(
                getLocalizer().format(
                        "XtfInfo.SubFamily",
                        name.getRecordString(pltid,
                                TtfTableNAME.FONTSUBFAMILYNAME)));
        getLogger().severe(
                getLocalizer()
                        .format(
                                "XtfInfo.Version",
                                name.getRecordString(pltid,
                                        TtfTableNAME.VERSIONSTRING)));
        getLogger().severe(
                getLocalizer().format("XtfInfo.NumGlyphs",
                        String.valueOf(parser.getNumberOfGlyphs())));

    }

    /**
     * Returns the properties.
     * @return Returns the props.
     */
    public Properties getProperties() {

        return props;
    }

    /**
     * Set the properties.
     * @param pro The properties to set.
     */
    public void setProperties(final Properties pro) {

        props = pro;
    }

    /**
     * parameter.
     */
    private static final int PARAMETER = 1;

    /**
     * main.
     * @param args  The command line arguments.
     * @throws Exception if a error occurs.
     */
    public static void main(final String[] args) throws Exception {

        XtfInfo info = new XtfInfo();
        Properties p = info.getProperties();

        if (!(p.getProperty("xtf.ttfinfo", "false").equals("true") || p
                .getProperty("xtf.ttfcopy", "false").equals("true"))) {

            if (args.length < PARAMETER) {
                info.getLogger().severe(
                        info.getLocalizer().format("XtfInfo.Call"));
                System.exit(1);
            }

            int i = 0;
            do {
                if ("-p".equals(args[i]) || "--props".equals(args[i])) {
                    if (i + 1 < args.length) {
                        File propsfile = new File(args[++i]);
                        if (propsfile.canRead()) {
                            p.load(new FileInputStream(propsfile));
                        }
                    }

                } else if ("-o".equals(args[i]) || "--outdir".equals(args[i])) {
                    if (i + 1 < args.length) {
                        p.setProperty("xtf.outdir", args[++i]);
                    }
                } else {
                    p.setProperty("xtf.file", args[i]);
                }
                i++;
            } while (i < args.length);
        }
        info.doIt();
    }

}
