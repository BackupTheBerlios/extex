/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.dante.util.xslt;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Transform a xml-file with a xslt-file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */

public final class Transform {

    /**
     * private: no instance
     */
    private Transform() {

    }

    /**
     * How much parameter.
     */
    private static final int PARAMETER = 3;

    /**
     * The buffer size.
     */
    private static final int BUFFERSIZE = 0xffff;

    /**
     * main
     * @param args      the command line arguments
     * @throws Exception  in case of an error
     */
    public static void main(final String[] args) throws Exception {

        if (args.length != PARAMETER) {
            System.err.println("java de.dante.util.xslt.Transform "
                    + "<xml-file> <xsl-file> <out-file>");
            System.exit(1);
        }

        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(args[2]), BUFFERSIZE);

        transform(new StreamSource(args[0]), new StreamSource(args[1]), null,
                out);

    }

    /**
     * transform a xml file with xslt.
     * @param xml       The xml source.
     * @param xsl       The xsl source.
     * @param resolver  The URIResolver, to get stream (xsl:include)
     * @param out       The output.
     * @throws TransformerException if a transformer error occurred.
     * @throws IOException if a IO error occurred.
     */
    public static void transform(final Source xml, final Source xsl,
            final URIResolver resolver, final OutputStream out)
            throws TransformerException, IOException {

        StreamResult result = new StreamResult(out);
        TransformerFactory factory = TransformerFactory.newInstance();
        if (resolver != null) {
            factory.setURIResolver(resolver);
        }
        Transformer transformer = factory.newTransformer(xsl);
        transformer.transform(xml, result);
        out.close();
    }

    /**
     * transform a xml file with xslt.
     * @param xml       The xml source.
     * @param xsl       The xsl source.
     * @param resolver  The URIResolver, to get stream (xsl:include)
     * @param out       The output.
     * @throws TransformerException if a transformer error occurred.
     * @throws IOException if a IO error occurred.
     */
    public static void transform(final Source xml, final Source xsl,
            final URIResolver resolver, final Writer out)
            throws TransformerException, IOException {

        StreamResult result = new StreamResult(out);
        TransformerFactory factory = TransformerFactory.newInstance();
        if (resolver != null) {
            factory.setURIResolver(resolver);
        }
        Transformer transformer = factory.newTransformer(xsl);
        transformer.transform(xml, result);
        out.close();
    }
}
