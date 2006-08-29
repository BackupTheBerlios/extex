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

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.xtf.TtfTableNAME;
import de.dante.extex.unicodeFont.format.xtf.XtfReader;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Print information about a ttf/otf file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
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
     * Check if the glyphs in the encoding are exists.
     */
    private boolean encoding = false;

    /**
     * The name of the encoding file.
     */
    private String encname = "";

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
        //        if (listglyphs) {
        //            listGlyphs(parser);
        //        }
        //        if (glyphinfo) {
        //            glyphInfo(parser);
        //        }
        //
        //        if (encoding) {
        //            encoding(file, parser);
        //        }
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
        boolean encoding = false;
        String encname = "";
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
            } else if ("-e".equals(args[i])
                    || "--encodingcheck".equals(args[i])) {
                if (i + 1 < args.length) {
                    encoding = true;
                    encname = args[++i];
                }
            } else {
                file = args[i];
            }
            i++;
        } while (i < args.length);

        info.setGlyphinfo(glyphinfo);
        info.setGlyphname(glyphname);
        info.setListglyphs(listglyphs);
        info.setEncoding(encoding);
        info.setEncname(encname);

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
     * Returns the encoding.
     * @return Returns the encoding.
     */
    public boolean isEncoding() {

        return encoding;
    }

    /**
     * The encoding to set.
     * @param aencoding The encoding to set.
     */
    public void setEncoding(final boolean aencoding) {

        encoding = aencoding;
    }

    /**
     * Returns the encname.
     * @return Returns the encname.
     */
    public String getEncname() {

        return encname;
    }

    /**
     * The encname to set.
     * @param aencname The encname to set.
     */
    public void setEncname(final String aencname) {

        encname = aencname;
    }
}
