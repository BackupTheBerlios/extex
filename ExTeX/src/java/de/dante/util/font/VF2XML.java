/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.vf.VFFont;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationFactory;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Convert a VF-file to a XML-file
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public final class VF2XML {

    /**
     * private: no instance
     */
    private VF2XML() {

    }

    /**
     * parameter
     */
    private static final int PARAMETER = 2;

    /**
     * main
     * @param args      the comandlinearguments
     * @throws IOException ...
     * @throws HelpingException ...
     * @throws ConfigurationException ...
     * @throws FontException ...
     */
    public static void main(final String[] args) throws IOException,
            HelpingException, ConfigurationException, FontException {

        if (args.length != PARAMETER) {
            System.err
                    .println("java de.dante.util.font.VF2XML <vf-file> <xml-file>");
            System.exit(1);
        }

        File xmlfile = new File(args[1]);
        String fontname = args[0].replaceAll("\\.vf|\\.VF", "");

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        Configuration cfgfonts = config.getConfiguration("Fonts");

        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(".extex");
            prop.load(in);
        } catch (Exception e) {
            prop.setProperty("extex.fonts", "src/font");
        }

        ResourceFinder finder = (new ResourceFinderFactory())
                .createResourceFinder(cfgfonts.getConfiguration("Resource"),
                        null, prop);

        // EncFactory ef = new EncFactory(finder);

        // vf-file
        InputStream vfin = finder.findResource(args[0], "");

        if (vfin == null) {
            System.err.println(args[0] + " not found!");
            System.exit(1);
        }

        // psfonts.map
        InputStream psin = finder.findResource("psfonts.map", "");

        if (psin == null) {
            System.err.println("psfonts.map not found!");
            System.exit(1);
        }
        // PSFontsMapReader psfm = new PSFontsMapReader(psin);

        // RandomAccessInputFileDebug rar = new RandomAccessInputFileDebug("src/font/aer12.vf");

        VFFont font = new VFFont(new RandomAccessInputStream(vfin), fontname);
        //        VFFont font = new VFFont(rar, fontname);

        //font.setFontMapEncoding(psfm, ef);

        // write to efm-file
        XMLOutputter xmlout = new XMLOutputter("   ", true);
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(xmlfile));
        Document doc = new Document(font.toXML());
        xmlout.output(doc, out);
        out.close();
    }
}