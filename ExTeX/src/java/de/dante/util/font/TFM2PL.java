/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.exception.FontMapNotFoundException;
import de.dante.extex.font.type.PlWriter;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.font.type.tfm.enc.EncFactory;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Convert a TFM-file to a PL-file
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public final class TFM2PL {

    /**
     * private: no instance
     */
    private TFM2PL() {

    }

    /**
     * parameter
     */
    private static final int PARAMETER = 2;

    /**
     * main
     * @param args      the comandlinearguments
     * @throws FontException if a font error occured.
     * @throws IOException if an IO-error occured.
     * @throws ConfigurationException from the resourcefinder.
     */
    public static void main(final String[] args) throws FontException,
            IOException, ConfigurationException {

        if (args.length != PARAMETER) {
            System.err
                    .println("java de.dante.util.font.TFM2EFM <tfm-file> <pl-file>");
            System.exit(1);
        }

        File plfile = new File(args[1]);
        String fontname = args[0].replaceAll("\\.tfm|\\.TFM", "");

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        //Configuration cfgfonts = config.getConfiguration("Fonts");

        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(".extex");
            prop.load(in);
        } catch (Exception e) {
            prop.setProperty("extex.fonts", "src/font");
        }

        ResourceFinder finder = (new ResourceFinderFactory())
                .createResourceFinder(config.getConfiguration("Resource"),
                        null, prop, null);

        EncFactory ef = new EncFactory(finder);

        // tfm-file
        InputStream tfmin = finder.findResource(args[0], "");

        if (tfmin == null) {
            throw new FileNotFoundException(args[0]);
        }

        // psfonts.map
        InputStream psin = finder.findResource("psfonts.map", "");

        if (psin == null) {
            throw new FontMapNotFoundException();
        }
        PSFontsMapReader psfm = new PSFontsMapReader(psin);

        TFMFont font = new TFMFont(new RandomAccessInputStream(tfmin), fontname);

        font.setFontMapEncoding(psfm, ef);

        PlWriter out = new PlWriter(new BufferedOutputStream(
                new FileOutputStream(plfile)));
        font.toPL(out);
        out.close();
    }
}