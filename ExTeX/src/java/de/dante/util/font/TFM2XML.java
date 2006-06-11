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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.dante.extex.font.exception.FontMapNotFoundException;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.font.type.tfm.enc.EncFactory;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.extex.unicodeFont.format.pfb.PfbParser;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Convert a TFM-file to a XML-file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.8 $
 */
public final class TFM2XML extends AbstractFontUtil {

    /**
     * Create a new object.
     *
     * @throws ConfigurationException if a config-error occurs.
     */
    private TFM2XML() throws ConfigurationException {

        super();
    }

    /**
     * do it.
     *
     * @param args the command line.
     * @throws Exception if an error occurs.
     */
    private void doIt(final String[] args) throws Exception {

        File xmlfile = new File(args[1]);
        String fontname = args[0].replaceAll("\\.tfm|\\.TFM", "");

        EncFactory ef = new EncFactory(getFinder());

        // tfm-file
        InputStream tfmin = getFinder().findResource(args[0], "");

        if (tfmin == null) {
            throw new FileNotFoundException(args[0]);
        }

        // psfonts.map
        InputStream psin = getFinder().findResource("psfonts.map", "");

        if (psin == null) {
            throw new FontMapNotFoundException();
        }
        PSFontsMapReader psfm = new PSFontsMapReader(psin);

        TFMFont font = new TFMFont(new RandomAccessInputStream(tfmin), fontname);

        font.setFontMapEncoding(psfm, ef);

        String pfbfile = font.getPfbfilename();
        if (pfbfile != null) {
            // pfb file
            InputStream pfbin = getFinder().findResource(pfbfile, "");
            if (pfbin == null) {
                // throw new FileNotFoundException(pfbfile);
                System.err.println("Warning: file " + pfbfile + " not found!");
            } else {
                font.setPfbParser(new PfbParser(pfbin));
            }
        }

        // write to xml-file
        XMLStreamWriter writer = new XMLStreamWriter(new FileOutputStream(
                xmlfile), "ISO-8859-1");
        writer.setBeauty(true);
        writer.writeStartDocument();
        font.writeXML(writer);
        writer.writeEndDocument();
        writer.close();
    }

    /**
     * how much parameter.
     */
    private static final int PARAMETER = 2;

    /**
     * main.
     * @param args      the command line arguments
     * @throws Exception if an error occurred.
     */
    public static void main(final String[] args) throws Exception {

        if (args.length != PARAMETER) {
            System.err
                    .println("java de.dante.util.font.TFM2XML <tfm-file> <xml-file>");
            System.exit(1);
        }

        TFM2XML tfm2xml = new TFM2XML();
        tfm2xml.doIt(args);
    }
}
