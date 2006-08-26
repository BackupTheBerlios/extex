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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.afm.AfmCharMetric;
import de.dante.extex.unicodeFont.format.afm.AfmKernPairs;
import de.dante.extex.unicodeFont.format.afm.AfmParser;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.EncReader;
import de.dante.extex.unicodeFont.glyphname.GlyphName;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Print information about a afm file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public final class AfmInfo extends AbstractFontUtil {

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
    private AfmInfo() throws ConfigurationException {

        super(AfmInfo.class);
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

        InputStream afmin = null;

        // find directly the afm file.
        File afmfile = new File(file);

        if (afmfile.canRead()) {
            afmin = new FileInputStream(afmfile);
        } else {
            // use the file finder
            afmin = getFinder().findResource(afmfile.getName(), "");
        }

        if (afmin == null) {
            throw new FileNotFoundException(file);
        }

        AfmParser parser = new AfmParser(afmin);

        if (!listglyphs) {
            getLogger().severe(getLocalizer().format("AfmInfo.Head"));
            getLogger().severe(getLocalizer().format("AfmInfo.Filename", file));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Fontname",
                            parser.getHeader().getFontname()));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Fullname",
                            parser.getHeader().getFullname()));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Family",
                            parser.getHeader().getFamilyname()));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Weight",
                            parser.getHeader().getWeight()));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Encoding",
                            parser.getHeader().getEncodingscheme()));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.NumGlyphs",
                            String.valueOf(parser.getAfmCharMetrics().size())));

            getLogger().severe(
                    getLocalizer().format("AfmInfo.FontBBoxllx",
                            String.valueOf(parser.getHeader().getLlx())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.FontBBoxlly",
                            String.valueOf(parser.getHeader().getLly())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.FontBBoxurx",
                            String.valueOf(parser.getHeader().getUrx())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.FontBBoxury",
                            String.valueOf(parser.getHeader().getUry())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.IsFixedPitch",
                            String.valueOf(parser.getHeader().isFixedpitch())));
            getLogger().severe(
                    getLocalizer()
                            .format(
                                    "AfmInfo.ItalicAngle",
                                    String.valueOf(parser.getHeader()
                                            .getItalicangle())));
            getLogger().severe(
                    getLocalizer().format(
                            "AfmInfo.UnderlinePosition",
                            String.valueOf(parser.getHeader()
                                    .getUnderlineposition())));
            getLogger().severe(
                    getLocalizer().format(
                            "AfmInfo.UnderlineThickness",
                            String.valueOf(parser.getHeader()
                                    .getUnderlinethickness())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.CapHeight",
                            String.valueOf(parser.getHeader().getCapheight())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.XHeight",
                            String.valueOf(parser.getHeader().getXheight())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Ascender",
                            String.valueOf(parser.getHeader().getAscender())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Descender",
                            String.valueOf(parser.getHeader().getDescender())));

        }
        if (listglyphs) {
            ArrayList list = parser.getAfmCharMetrics();
            for (int i = 0, n = list.size(); i < n; i++) {
                AfmCharMetric metric = (AfmCharMetric) list.get(i);
                getLogger().severe(
                        getLocalizer().format("AfmInfo.ListGlyph",
                                metric.getN()));
            }
        }
        if (glyphinfo) {
            AfmCharMetric metric = parser.getAfmCharMetric(glyphname);
            if (metric == null) {
                getLogger().severe(
                        getLocalizer().format("AfmInfo.GlyphNotFound",
                                glyphname));
            }
            getLogger().severe(
                    getLocalizer().format("AfmInfo.GlyphInfo", glyphname));
            int c = metric.getC();
            String cc = c < 0 ? "-" : String.valueOf(c);
            String hex = c < 0 ? "-" : Integer.toHexString(c);
            String oct = c < 0 ? "-" : Integer.toOctalString(c);
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Number", cc, hex, oct));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.Width",
                            String.valueOf(metric.getWx())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.GlyphBBoxllx",
                            String.valueOf(metric.getBllx())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.GlyphBBoxlly",
                            String.valueOf(metric.getBlly())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.GlyphBBoxurx",
                            String.valueOf(metric.getBurx())));
            getLogger().severe(
                    getLocalizer().format("AfmInfo.GlyphBBoxury",
                            String.valueOf(metric.getBury())));

            HashMap ligmap = metric.getL();
            if (ligmap != null) {
                Iterator it = ligmap.keySet().iterator();
                while (it.hasNext()) {
                    Object letter = it.next();
                    Object lig = ligmap.get(letter);
                    getLogger().severe(
                            getLocalizer().format("AfmInfo.Ligature",
                                    letter.toString(), lig.toString()));

                }
            }
            List kerning = metric.getK();
            if (kerning != null) {
                for (int i = 0, n = kerning.size(); i < n; i++) {
                    AfmKernPairs kp = (AfmKernPairs) kerning.get(i);
                    getLogger().severe(
                            getLocalizer().format("AfmInfo.Kerning",
                                    kp.getCharpre(), kp.getCharpost(),
                                    String.valueOf(kp.getKerningsize())));

                }
            }
        }

        if (encoding) {
            InputStream encin = null;
            File encfile = new File(encname);
            if (encfile.canRead()) {
                encin = new FileInputStream(encfile);
            } else {
                // use the file finder
                encin = getFinder().findResource(encfile.getName(), "");
            }

            if (encin == null) {
                throw new FileNotFoundException(file);
            }

            EncReader enc = new EncReader(encin);

            GlyphName glyphname = GlyphName.getInstance();

            String[] table = enc.getTable();

            for (int i = 0; i < table.length; i++) {
                String name = table[i].replaceAll("/", "");
                boolean found = true;
                String ucname = "";

                if (!".notdef".equals(name)) {
                    AfmCharMetric cm = parser.getAfmCharMetric(name);
                    if (cm == null) {
                        found = false;
                        UnicodeChar uc = glyphname.getUnicode(name);
                        if (uc != null) {
                            int cp = uc.getCodePoint();
                            String snr = "0000" + Integer.toHexString(cp);
                            ucname = "uni" + snr.substring(snr.length() - 4);
                            cm = parser.getAfmCharMetric(ucname);
                            if (cm != null) {
                                ucname = "";
                            }
                        }
                    }
                }
                String sfound = found ? getLocalizer().format(
                        "AfmInfo.GlyphFound") : getLocalizer().format(
                        "AfmInfo.GlyphNotFound");
                String rename = "".equals(ucname) ? ucname : getLocalizer()
                        .format("AfmInfo.GlyphRename", ucname);

                getLogger().severe(
                        getLocalizer().format("AfmInfo.GlyphTest", name,
                                sfound, rename));

            }

        }
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

        AfmInfo info = new AfmInfo();

        if (args.length < PARAMETER) {
            info.getLogger().severe(info.getLocalizer().format("AfmInfo.Call"));
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
