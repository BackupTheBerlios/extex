/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import de.dante.extex.font.TFMReader;

/**
 * Convert a TFM-file to a EFM-file
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class TFM2EFM {

    /**
     * filebuffer
     */
    private static final int FILEBUFFER = 0x8000;

    /**
     * main
     * @param args      the comandlinearguments
     * @throws IOException ...
     */
    public static void main(final String[] args) throws IOException {

        if (args.length != 3) {
            System.err
                    .println("java de.dante.util.font.TFM2EFM <tfm-file> <pfb-file> <efm-file>");
            System.exit(1);
        }

        File tfmfile = new File(args[0]);
        File efmfile = new File(args[2]);

        TFMReader tfmr = new TFMReader(new BufferedInputStream(
                new FileInputStream(tfmfile), FILEBUFFER), args[1]);

        // write to efm-file
        XMLOutputter xmlout = new XMLOutputter("   ", true);
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(efmfile), FILEBUFFER);
        Document doc = new Document(tfmr.getFontMetric());
        xmlout.output(doc, out);
        out.close();
    }
}
