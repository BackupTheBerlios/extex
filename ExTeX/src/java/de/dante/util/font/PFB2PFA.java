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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.dante.extex.unicodeFont.format.pfb.PfbParser;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Convert a PFB file to a PFA file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public final class PFB2PFA extends AbstractFontUtil {

    /**
     * Create a new object.
     *
     * @throws ConfigurationException if a config-error occurs.
     */
    private PFB2PFA() throws ConfigurationException {

        super();
    }

    /**
     * do it.
     *
     * @param args the command line
     * @throws Exception if an error occurs.
     */
    private void doIt(final String[] args) throws Exception {

        // pfb-file
        InputStream in = getFinder().findResource(args[0], "");

        if (in == null) {
            throw new FileNotFoundException(args[0]);
        }

        PfbParser parser = new PfbParser(in);

        String pfaname = args[0].replaceAll("\\.pfb|\\.PFB", "");

        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(pfaname + ".pfa"));

        parser.toPfa(out);

        out.close();
        in.close();

    }

    /**
     * parameter.
     */
    private static final int PARAMETER = 1;

    /**
     * main.
     * @param args      the command line arguments
     * @throws Exception if an error occurred.
     */
    public static void main(final String[] args) throws Exception {

        if (args.length != PARAMETER) {
            System.err.println("java de.dante.util.font.PFB2PFA <pfb-file>");
            System.exit(1);
        }

        PFB2PFA pfb2pfa = new PFB2PFA();
        pfb2pfa.doIt(args);
    }
}
