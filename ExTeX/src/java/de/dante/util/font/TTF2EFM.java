/*
 * Copyright (C) 2004-2005 The ExTeX Group
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
import java.io.InputStream;

import de.dante.extex.unicodeFont.format.xtf.XtfReader;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Convert a TTF-file to a EFM-file
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.12 $
 */
public final class TTF2EFM {

    /**
     * filebuffer
     */
    private static final int FILEBUFFER = 0x8000;

    /**
     * private: no instance
     */
    private TTF2EFM() {

    }

    /**
     * max paramters
     */
    private static final int MAXPARAMS = 2;

    /**
     * main
     * @param args  the command line arguments
     * @throws Exception ...
     */
    public static void main(final String[] args) throws Exception {

        if (args.length != MAXPARAMS) {
            System.err
                    .println("java de.dante.util.font.TTF2EFM <ttf-file> <efm-file>");
            System.exit(1);
        }

        String fontname = args[0].replaceAll("\\.ttf\\.TTF", "");

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        //Configuration cfgfonts = config.getConfiguration("Fonts");

        ResourceFinder finder = (new ResourceFinderFactory())
                .createResourceFinder(config.getConfiguration("Resource"),
                        null, System.getProperties(), null);

        // ttf-file
        InputStream ttfin = finder.findResource(args[0], "");

        if (ttfin == null) {
            System.err.println(args[0] + " not found!");
            System.exit(1);
        }

        File efmfile = new File(args[1]);

        XtfReader ttfr = new XtfReader(ttfin);

        // MGN incomplete
        System.err.println("incomplete");

        // write to efm-file
        //        XMLOutputter xmlout = new XMLOutputter("   ", true);
        //        BufferedOutputStream out = new BufferedOutputStream(
        //                new FileOutputStream(efmfile), FILEBUFFER);
        //        Document doc = new Document(ttfr.getFontMetric());
        //        xmlout.output(doc, out);
        //        out.close();
    }
}