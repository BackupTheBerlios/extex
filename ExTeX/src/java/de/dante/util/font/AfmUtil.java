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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.dante.extex.unicodeFont.format.afm.AfmParser;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Utilities for a afm file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public final class AfmUtil extends AbstractFontUtil {

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

            } else {
                file = args[i];
            }
            i++;
        } while (i < args.length);

        afm.setToxml(toxml);
        afm.setXmlname(xmlname);
        afm.setToefm(toefm);
        afm.setEfmname(efmname);

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
}
