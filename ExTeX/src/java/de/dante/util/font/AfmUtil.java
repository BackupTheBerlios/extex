/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.afm.AfmCharMetric;
import de.dante.extex.unicodeFont.format.afm.AfmParser;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.EncReader;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Utilities for a afm file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public final class AfmUtil extends AbstractFontUtil {

    /**
     * Number of glyphs.
     */
    private static final int NUMBEROFGLYPHS = 256;

    /**
     * Create a xml file.
     */
    private boolean toxml = false;

    /**
     * The xml file name.
     */
    private String xmlname = "";

    /**
     * Create a efm file.
     */
    private boolean toefm = false;

    /**
     * The efm file name.
     */
    private String efmname = "";

    /**
     * The list for the encoding vectors.
     */
    private ArrayList enclist = new ArrayList();

    /**
     * Create encoding vectors.
     */
    private boolean toenc = false;

    /**
     * The name for the encoding vectors.
     */
    private String encname = "";

    /**
     * Create a new object.
     *
     * @throws ConfigurationException if a config-error occurs.
     */
    private AfmUtil() throws ConfigurationException {

        super(AfmUtil.class);
    }

    /**
     * do it.
     *
     * @param file  The afm file name.
     * @throws Exception if an error occurs.
     */
    private void doIt(final String file) throws Exception {

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

        if (toxml) {
            toXml(parser);
        }

        if (toefm) {
            toEfm(parser);
        }

        if (toenc) {
            toEnc(parser);
        }
    }

    /**
     * Create encoding vectors.
     * @param parser    The afm parser.
     * @throws IOException if an IO-error occurred.
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if a font error occurred.
     */
    private void toEnc(final AfmParser parser) throws IOException,
            ConfigurationException, FontException {

        // read all glyphs from the encoding vectors
        ArrayList readenc = new ArrayList();
        readAllGlyphName(readenc);

        // get the glpyh names form the afm file.
        ArrayList names = readGlyphNames(parser);
        Collections.sort(names);

        // remove all names from readenc in names
        removeExistingNames(readenc, names);

        createEncFiles(names);

    }

    /**
     * Create the encoding files.
     * @param names     The names for the files.
     * @throws IOException if a IO-error occurred.
     */
    private void createEncFiles(final ArrayList names) throws IOException {

        int cnt = 0;
        int filecnt = 0;
        BufferedWriter out = null;
        for (int i = 0, n = names.size(); i < n; i++) {
            if (cnt == 0) {
                String na = encname + filecnt;
                File newenc = new File(na + ".enc");
                out = new BufferedWriter(new FileWriter(newenc));
                out.write("% created with ExTeX....\n");
                out.write("/" + na + "Encoding [\n");
            }
            out.write("% " + cnt++ + "\n");
            out.write("/" + names.get(i) + "\n");
            if (cnt == NUMBEROFGLYPHS) {
                out.write("] def\n");
                out.close();
                cnt = 0;
                filecnt++;
            }
        }
        if (cnt != 0) {
            for (int i = cnt; i < NUMBEROFGLYPHS; i++) {
                out.write("% " + i + "\n");
                out.write("/.notdef\n");
            }
            out.write("] def\n");
            out.close();
        }
        getLogger().severe(
                getLocalizer().format("AfmUtil.EncCreate",
                        String.valueOf(names.size()), String.valueOf(filecnt)));

    }

    /**
     * Remove existing glyph name from the list.
     * @param readenc   The existing glyph names.
     * @param names     The glyph names from the afm file.
     */
    private void removeExistingNames(final ArrayList readenc,
            final ArrayList names) {

        for (int i = 0, n = readenc.size(); i < n; i++) {
            String name = (String) readenc.get(i);
            names.remove(name);
        }
    }

    /**
     * Read the glyph names.
     * @param parser    The afm parser.
     * @return Returns the glyph name list.
     */
    private ArrayList readGlyphNames(final AfmParser parser) {

        ArrayList cmlist = parser.getAfmCharMetrics();
        ArrayList names = new ArrayList(cmlist.size());

        for (int i = 0, n = cmlist.size(); i < n; i++) {
            AfmCharMetric cm = (AfmCharMetric) cmlist.get(i);
            names.add(cm.getN());
        }
        return names;
    }

    /**
     * Read all glyph names from the encoding vectors.
     *
     * @param readenc   The list for the names.
     * @throws IOException if an IO-error occurred.
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if a font error occurred.
     */
    private void readAllGlyphName(final ArrayList readenc) throws IOException,
            ConfigurationException, FontException {

        for (int i = 0, n = enclist.size(); i < n; i++) {

            String encv = (String) enclist.get(i);

            InputStream encin = null;
            File encfile = new File(encv);
            if (encfile.canRead()) {
                encin = new FileInputStream(encfile);
            } else {
                // use the file finder
                encin = getFinder().findResource(encfile.getName(), "");
            }

            if (encin == null) {
                throw new FileNotFoundException(encv);
            }

            EncReader enc = new EncReader(encin);

            String[] table = enc.getTable();

            for (int k = 0; k < table.length; k++) {
                String name = table[k].replaceAll("/", "");
                readenc.add(name);
            }
        }
    }

    /**
     * Export to xml.
     * @param parser    The afm parser.
     * @throws IOException  if a IO-error occurred.
     */
    private void toXml(final AfmParser parser) throws IOException {

        File xmlfile = new File(xmlname);

        // write to xml-file
        XMLStreamWriter writer = new XMLStreamWriter(new FileOutputStream(
                xmlfile), "ISO-8859-1");
        writer.setBeauty(true);
        writer.writeStartDocument();
        writer.writeComment("created with ExTEX ...");
        parser.writeXML(writer);
        writer.writeEndDocument();
        writer.close();

        getLogger().severe(getLocalizer().format("AfmUtil.XmlCreate", xmlname));
    }

    /**
     * Export to efm.
     * @param parser    The afm parser.
     * @throws IOException  if a IO-error occurred.
     */
    private void toEfm(final AfmParser parser) throws IOException {

        File efmfile = new File(efmname);

        // write to efm-file
        XMLStreamWriter writer = new XMLStreamWriter(new FileOutputStream(
                efmfile), "ISO-8859-1");
        writer.setBeauty(true);
        writer.writeStartDocument();
        writer.writeComment("created with ExTEX ...");
        parser.writeEFM(writer);
        writer.writeEndDocument();
        writer.close();

        getLogger().severe(getLocalizer().format("AfmUtil.EfmCreate", efmname));
    }

    /**
     * parameter.
     */
    private static final int PARAMETER = 1;

    /**
     * main.
     * @param args the command line arguments.
     * @throws Exception if a error occurs.
     */
    public static void main(final String[] args) throws Exception {

        AfmUtil afm = new AfmUtil();

        if (args.length < PARAMETER) {
            afm.getLogger().severe(afm.getLocalizer().format("AfmUtil.Call"));
            System.exit(1);
        }

        boolean toxml = false;
        String xmlname = "";
        boolean toefm = false;
        String efmname = "";
        ArrayList enclist = new ArrayList();
        boolean toenc = false;
        String encname = "";
        String file = "";

        int i = 0;
        do {
            if ("-x".equals(args[i]) || "--xml".equals(args[i])) {
                if (i + 1 < args.length) {
                    toxml = true;
                    xmlname = args[++i];
                }
            } else if ("-e".equals(args[i]) || "--efm".equals(args[i])) {
                if (i + 1 < args.length) {
                    toefm = true;
                    efmname = args[++i];
                }

            } else if ("-c".equals(args[i]) || "--createenc".equals(args[i])) {
                if (i + 1 < args.length) {
                    toenc = true;
                    encname = args[++i];
                }
            } else if ("-v".equals(args[i]) || "--encvector".equals(args[i])) {
                if (i + 1 < args.length) {
                    enclist.add(args[++i]);
                }
            } else {
                file = args[i];
            }
            i++;
        } while (i < args.length);

        afm.setToxml(toxml);
        afm.setXmlname(xmlname);
        afm.setToefm(toefm);
        afm.setEfmname(efmname);
        afm.setEnclist(enclist);
        afm.setToenc(toenc);
        afm.setEncname(encname);

        afm.doIt(file);
    }

    /**
     * Returns the toxml.
     * @return Returns the toxml.
     */
    public boolean isToxml() {

        return toxml;
    }

    /**
     * The toxml to set.
     * @param xml The toxml to set.
     */
    public void setToxml(final boolean xml) {

        toxml = xml;
    }

    /**
     * Returns the xmlname.
     * @return Returns the xmlname.
     */
    public String getXmlname() {

        return xmlname;
    }

    /**
     * The xmlname to set.
     * @param name The xmlname to set.
     */
    public void setXmlname(final String name) {

        xmlname = name;
    }

    /**
     * Returns the efmname.
     * @return Returns the efmname.
     */
    public String getEfmname() {

        return efmname;
    }

    /**
     * The efmname to set.
     * @param name The efmname to set.
     */
    public void setEfmname(final String name) {

        efmname = name;
    }

    /**
     * Returns the toefm.
     * @return Returns the toefm.
     */
    public boolean isToefm() {

        return toefm;
    }

    /**
     * The toefm to set.
     * @param efm The toefm to set.
     */
    public void setToefm(final boolean efm) {

        toefm = efm;
    }

    /**
     * Returns the enclist.
     * @return Returns the enclist.
     */
    public ArrayList getEnclist() {

        return enclist;
    }

    /**
     * The enclist to set.
     * @param list The enclist to set.
     */
    public void setEnclist(final ArrayList list) {

        enclist = list;
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
     * @param name The encname to set.
     */
    public void setEncname(final String name) {

        encname = name;
    }

    /**
     * Returns the toenc.
     * @return Returns the toenc.
     */
    public boolean isToenc() {

        return toenc;
    }

    /**
     * The toenc to set.
     * @param enc The toenc to set.
     */
    public void setToenc(final boolean enc) {

        toenc = enc;
    }
}
