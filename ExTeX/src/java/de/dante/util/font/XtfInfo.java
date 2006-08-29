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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.xtf.TtfTableCMAP;
import de.dante.extex.unicodeFont.format.xtf.TtfTableNAME;
import de.dante.extex.unicodeFont.format.xtf.TtfTablePOST;
import de.dante.extex.unicodeFont.format.xtf.XtfReader;
import de.dante.extex.unicodeFont.format.xtf.TtfTableCMAP.Format;
import de.dante.extex.unicodeFont.format.xtf.TtfTableCMAP.IndexEntry;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Print information about a ttf/otf file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public final class XtfInfo extends AbstractFontUtil {

    /**
     * List the glyphnames.
     */
    private boolean listglyphs = false;

    /**
     * Print a glyphinfo.
     */
    private boolean glyphinfo = false;

    /**
     * The glyphname to print.
     */
    private String glyphname = "";

    /**
     * Print the charinfo.
     */
    private boolean charinfo = false;

    /**
     * The char to print.
     */
    private int charcode;

    /**
     * Create a new object.
     *
     * @throws ConfigurationException if a configuration error occurs.
     */
    private XtfInfo() throws ConfigurationException {

        super(XtfInfo.class);
    }

    /**
     * Print the afm info.
     *
     * @param file  The afm file name.
     * @throws IOException  if a IO-error occurred.
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if a font error occurred.
     */
    private void doIt(final String file) throws IOException,
            ConfigurationException, FontException {

        InputStream xtfin = null;

        // find directly the ttf file.
        File ttffile = new File(file);

        if (ttffile.canRead()) {
            xtfin = new FileInputStream(ttffile);
        } else {
            // use the file finder
            xtfin = getFinder().findResource(ttffile.getName(), "");
        }

        if (xtfin == null) {
            throw new FileNotFoundException(file);
        }

        XtfReader parser = new XtfReader(xtfin);

        if (!listglyphs) {
            doHead(file, parser);
        }
        if (listglyphs) {
            listGlyphs(parser);
        }
        if (charinfo) {
            charInfo(parser);
        }
        if (glyphinfo) {
            glyphInfo(parser);
        }
    }

    /**
     * CharInfo.
     * @param parser    The ttf parser.
     */
    private void charInfo(final XtfReader parser) {

        TtfTableCMAP cmap = parser.getCmapTable();

        IndexEntry[] entries = cmap.getEntries();
        Format[] formats = cmap.getFormats();

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
     * @param parser    The ttf parser.
     */
    private void glyphInfo(final XtfReader parser) {

    }

    /**
     * List the glpyphs.
     * @param parser    The ttf parser
     */
    private void listGlyphs(final XtfReader parser) {

        TtfTablePOST post = parser.getPostTable();
        String[] glyphs = post.getPsGlyphName();
        Arrays.sort(glyphs);

        for (int i = 0; i < glyphs.length; i++) {
            getLogger().severe(
                    getLocalizer().format("XtfInfo.ListGlyph", glyphs[i]));
        }

    }

    /**
     * Head information.
     * @param file      The file name.
     * @param parser    The ttf parser.
     */
    private void doHead(final String file, final XtfReader parser) {

        getLogger().severe(getLocalizer().format("XtfInfo.Head"));
        getLogger().severe(getLocalizer().format("XtfInfo.Filename", file));
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
                        name.getPlatformName(pltid)));
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

        if (args.length < PARAMETER) {
            info.getLogger().severe(info.getLocalizer().format("XtfInfo.Call"));
            System.exit(1);
        }

        boolean listglyphs = false;
        boolean glyphinfo = false;
        String glyphname = "";
        boolean charinfo = false;
        int charcode = 0;
        String file = "";

        int i = 0;
        do {
            if ("-l".equals(args[i]) || "--listglyphs".equals(args[i])) {
                listglyphs = true;
            } else if ("-g".equals(args[i]) || "--glyphinfo".equals(args[i])) {
                if (i + 1 < args.length) {
                    glyphinfo = true;
                    glyphname = args[++i];
                }
            } else if ("-c".equals(args[i]) || "--charinfo".equals(args[i])) {
                if (i + 1 < args.length) {
                    charinfo = true;
                    try {
                        charcode = Integer.parseInt(args[++i]);
                    } catch (Exception e) {
                        charcode = 0;
                    }
                }
            } else {
                file = args[i];
            }
            i++;
        } while (i < args.length);

        info.setGlyphinfo(glyphinfo);
        info.setGlyphname(glyphname);
        info.setListglyphs(listglyphs);
        info.setCharinfo(charinfo);
        info.setCharCode(charcode);

        info.doIt(file);
    }

    /**
     * Returns the glyphinfo.
     * @return Returns the glyphinfo.
     */
    public boolean isGlyphinfo() {

        return glyphinfo;
    }

    /**
     * The glyphinfo to set.
     * @param aglyphinfo The glyphinfo to set.
     */
    public void setGlyphinfo(final boolean aglyphinfo) {

        glyphinfo = aglyphinfo;
    }

    /**
     * Returns the glyphname.
     * @return Returns the glyphname.
     */
    public String getGlyphname() {

        return glyphname;
    }

    /**
     * The glyphname to set.
     * @param aglyphname The glyphname to set.
     */
    public void setGlyphname(final String aglyphname) {

        glyphname = aglyphname;
    }

    /**
     * Returns the listglyphs.
     * @return Returns the listglyphs.
     */
    public boolean isListglyphs() {

        return listglyphs;
    }

    /**
     * The listglyphs to set.
     * @param alistglyphs The listglyphs to set.
     */
    public void setListglyphs(final boolean alistglyphs) {

        listglyphs = alistglyphs;
    }

    /**
     * Returns the charinfo.
     * @return Returns the charinfo.
     */
    public boolean isCharinfo() {

        return charinfo;
    }

    /**
     * The charinfo to set.
     * @param info The charinfo to set.
     */
    public void setCharinfo(final boolean info) {

        charinfo = info;
    }

    /**
     * Returns the charcode.
     * @return Returns the charcode.
     */
    public int getCharCode() {

        return charcode;
    }

    /**
     * The charcode to set.
     * @param cc The charcode to set.
     */
    public void setCharCode(final int cc) {

        charcode = cc;
    }

}
