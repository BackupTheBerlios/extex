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
import java.io.InputStream;

import de.dante.extex.unicodeFont.format.afm.AfmParser;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Convert a AFM-file to a XML-file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public final class Afm2Xml extends AbstractFontUtil {

    /**
     * Create a new object.
     *
     * @throws ConfigurationException if a config-error occurs.
     */
    private Afm2Xml() throws ConfigurationException {

        super(Afm2Xml.class);
    }

    /**
     * do it.
     *
     * @param args the command line arguments.
     * @throws Exception if an error occurs.
     */
    private void doIt(final String[] args) throws Exception {

        File xmlfile = new File(args[1]);
        InputStream afmin = null;

        // find directly the afm file.
        File afmfile = new File(args[0]);

        if (afmfile.canRead()) {
            afmin = new FileInputStream(afmfile);
        } else {
            // use the file finder
            afmin = getFinder().findResource(afmfile.getName(), "");
        }

        if (afmin == null) {
            throw new FileNotFoundException(args[0]);
        }

        AfmParser parser = new AfmParser(afmin);

        // write to xml-file
        XMLStreamWriter writer = new XMLStreamWriter(new FileOutputStream(
                xmlfile), "ISO-8859-1");
        writer.setBeauty(true);
        writer.writeStartDocument();
        writer.writeComment("created with ExTEX ...");
        parser.writeXML(writer);
        writer.writeEndDocument();
        writer.close();
    }

    /**
     * parameter.
     */
    private static final int PARAMETER = 2;

    /**
     * main.
     * @param args the command line arguments.
     * @throws Exception if a error occurs.
     */
    public static void main(final String[] args) throws Exception {

        Afm2Xml afm2xml = new Afm2Xml();

        if (args.length != PARAMETER) {
            afm2xml.getLogger().severe(
                    afm2xml.getLocalizer().format("Afm2Xml.Call"));
            System.exit(1);
        }

        afm2xml.doIt(args);
    }
}
