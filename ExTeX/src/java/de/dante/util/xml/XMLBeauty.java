/*
 * Copyright (C) 2004 The ExTeX Group
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

package de.dante.util.xml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Format XML-files.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public final class XMLBeauty {

    /**
     * private: no instance
     */
    private XMLBeauty() {

    }

    /**
     * filebuffe
     */
    private static final int FILEBUFFER = 0x8000;

    /**
     * Read a xml-file (check it) and store it with new encoding.
     * @param src       the source
     * @param dest      the destination
     * @param encoding  the encoding
     * @throws JDOMException    jdom-exception
     * @throws IOException      io-exception
     */
    public static void beauty(final File src, final File dest,
            final String encoding) throws JDOMException, IOException {

        // create a document with SAXBuilder (without validate)
        SAXBuilder builder = new SAXBuilder(false);
        Document doc = builder.build(new BufferedInputStream(
                new FileInputStream(src)));

        // write to xml-file
        XMLOutputter xmlout = new XMLOutputter("   ", true, encoding);
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(dest), FILEBUFFER);
        xmlout.output(doc, out);
        out.close();
    }

    /**
     * main
     * @param args  the commandline
     */
    public static void main(final String[] args) {

        if (args.length < 2 || args.length > 3) {
            System.err
                    .println("java de.dante.util.xml.XMLBeauty <src-file> <dest-file> [encoding]");
            System.exit(1);
        }

        try {

            File src = new File(args[0]);
            File dest = new File(args[1]);

            String encoding = "UTF-8";
            if (args.length == 3) {
                encoding = args[2];
            }

            beauty(src, dest, encoding);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}