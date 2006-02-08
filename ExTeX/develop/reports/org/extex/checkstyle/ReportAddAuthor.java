/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package org.extex.checkstyle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.dante.util.xml.XMLStreamWriter;

/**
 * Add the author to the checkstyle xml report.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public final class ReportAddAuthor {

    /**
     * no instance.
     */
    private ReportAddAuthor() {

        // no instance
    }

    /**
     * How much parameter.
     */
    private static final int PARAMETER = 2;

    /**
     * output stream.
     */
    private static PrintStream err = System.err;

    /**
     * Encoding.
     */
    private static final String ENCODING = "UTF-8";

    /**
     * main.
     *
     * @param args  tThe command line.
     * @throws Exception if an error occurred.
     */
    public static void main(final String[] args) throws Exception {

        if (args.length != PARAMETER) {
            err.println("java org.extex.checkstyle.ReportAddAuthor "
                    + "<checkstyle-report.xml> <report-with-author.xml>");
            System.exit(1);
        }

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                args[0]));

        XMLStreamWriter out = new XMLStreamWriter(
                new FileOutputStream(args[1]), ENCODING);
        // out.setBeauty(true);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        /**
         * My handler class.
         */
        class MyHandler extends DefaultHandler {

            /**
             * The output.
             */
            private XMLStreamWriter xmlout;

            /**
             * Create a new object.
             *
             * @param xout  The output.
             */
            public MyHandler(final XMLStreamWriter xout) {

                xmlout = xout;
            }

            /**
             * @see org.xml.sax.helpers.DefaultHandler#startDocument()
             */
            public void startDocument() throws SAXException {

                super.startDocument();
                try {
                    xmlout.writeStartDocument();
                } catch (IOException e) {
                    throw new SAXException(e);
                }
            }

            /**
             * @see org.xml.sax.helpers.DefaultHandler#endDocument()
             */
            public void endDocument() throws SAXException {

                super.endDocument();

                try {
                    xmlout.writeEndDocument();
                } catch (IOException e) {
                    throw new SAXException(e);
                }
            }

            /**
             * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
             */
            public void startElement(final String uri, final String localName,
                    final String qName, final Attributes attributes)
                    throws SAXException {

                super.startElement(uri, localName, qName, attributes);
                try {

                    xmlout.writeStartElement(qName);

                    for (int i = 0, n = attributes.getLength(); i < n; i++) {
                        xmlout.writeAttribute(attributes.getQName(i),
                                attributes.getValue(i));
                    }

                    if ("file".equals(qName)) {
                        xmlout.writeStartElement("author");
                        xmlout.writeCharacters(getAuthor(attributes));
                        xmlout.writeEndElement();
                    }

                } catch (IOException e) {
                    throw new SAXException(e);
                }
            }

            /**
             * Get the author.
             *
             * @param attributes    the attributes of the element
             * @return Return the author from the java file.
             * @throws IOException in an IO-error occurred.
             */
            private String getAuthor(final Attributes attributes)
                    throws IOException {

                String file = attributes.getValue("name");
                String author = "Unknown";

                if (file != null && file.endsWith(".java")) {
                    BufferedReader injava = new BufferedReader(new FileReader(
                            file));
                    String line;
                    while ((line = injava.readLine()) != null) {
                        int pos = line.indexOf("@author");
                        if (pos >= 0) {
                            author = line.substring(pos + 7).trim();
                            // parse and extract only the name
                            // find '>'
                            pos = author.indexOf(">");
                            if (pos >= 0) {
                                author = author.substring(pos + 1).trim();
                                // find '<'
                                pos = author.indexOf("<");
                                if (pos >= 0) {
                                    author = author.substring(0, pos).trim();
                                }
                            }
                            break;
                        }
                    }
                    injava.close();
                }
                return author;
            }

            /**
             * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
             */
            public void endElement(final String uri, final String localName,
                    final String qName) throws SAXException {

                super.endElement(uri, localName, qName);
                try {
                    xmlout.writeEndElement();
                } catch (IOException e) {
                    throw new SAXException(e);
                }
            }

            /**
             * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
             */
            public void characters(final char[] ch, final int start,
                    final int length) throws SAXException {

                super.characters(ch, start, length);
                try {
                    xmlout.writeCharacters(new String(ch, start, length));
                } catch (IOException e) {
                    throw new SAXException(e);
                }

            }
        }

        parser.parse(in, new MyHandler(out));

        in.close();
        out.close();

    }
}
