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

package de.dante.util.xml;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Create LaTeX-Macros from the XML file (only structure).
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class Xml2LaTeXMacros {

    /**
     * the buffer size
     */
    private static final int BUFFERSIZE = 0xffff;

    /**
     * The output
     */
    private PrintStream out;

    /**
     * Array with spaces
     */
    private static final String[] INSERT = {"", "  ", "    ", "      ",
            "        ", "          ", "            ", "              ",
            "                ", "                  ", "                    "};

    /**
     * Create a new object.
     * @param output   The Output
     */
    public Xml2LaTeXMacros(final PrintStream output) {

        out = output;
    }

    /**
     * Print the LaTeX-Macros
     * @param in    The input.
     * @throws IOException if an IO-error occurs.
     */
    public void printLaTeXMacros(final InputStream in) throws IOException {

        try {

            BufferedInputStream input = new BufferedInputStream(in, BUFFERSIZE);

            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser parser = factory.newSAXParser();

            MyDefaultHandler defaulthandler = new MyDefaultHandler();

            parser.parse(input, defaulthandler);

            input.close();

        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

    }

    /**
     * Default handler
     */
    private class MyDefaultHandler extends DefaultHandler {

        /**
         * Create a new object.
         */
        public MyDefaultHandler() {

            super();
        }

        /**
         * the element level
         */
        private int level = 0;

        /**
         * how much space for each level
         */
        private static final int LEVELSPACEFACTOR = 2;

        /**
         * Returns spaces for inserting.
         * @return Returns spaces for inserting.
         */
        private String insertSpace() {

            if (level < INSERT.length) {
                return INSERT[level];
            }
            StringBuffer buf = new StringBuffer(level * LEVELSPACEFACTOR);
            for (int i = 0, n = level * LEVELSPACEFACTOR; i < n; i++) {
                buf.append(INSERT[1]);
            }
            return buf.toString();
        }

        /**
         * @see org.xml.sax.helpers.DefaultHandler#startElement(
         *      java.lang.String, java.lang.String, java.lang.String,
         *      org.xml.sax.Attributes)
         */
        public void startElement(final String uri, final String localName,
                final String qName, final Attributes attributes)
                throws SAXException {

            super.startElement(uri, localName, qName, attributes);
            out.print(insertSpace());
            out.print("\\begin{element}{");
            out.print(qName);
            out.print("}{");
            out.print(level);
            out.println("}%");
            level++;
            printAttributes(attributes);

        }

        /**
         * @see org.xml.sax.helpers.DefaultHandler#endElement(
         *      java.lang.String, java.lang.String, java.lang.String)
         */
        public void endElement(final String uri, final String localName,
                final String qName) throws SAXException {

            super.endElement(uri, localName, qName);
            level--;
            out.print(insertSpace());
            out.println("\\end{element}%");

        }

        /**
         * Prints the Attributes
         * @param attributes    The attributes
         */
        private void printAttributes(final Attributes attributes) {

            if (attributes.getLength() > 0) {
                for (int i = 0, n = attributes.getLength(); i < n; i++) {
                    String name = attributes.getQName(i);
                    String value = attributes.getValue(i);
                    out.print(insertSpace());
                    out.print("\\attribut{");
                    out.print(name);
                    out.print("}{");
                    out.print(value);
                    out.println("}%");
                }
            }
        }

        /**
         * @see org.xml.sax.helpers.DefaultHandler#endDocument()
         */
        public void endDocument() throws SAXException {

            super.endDocument();
            level--;
            out.println("\\end{xmldocument}%");

        }

        /**
         * @see org.xml.sax.helpers.DefaultHandler#startDocument()
         */
        public void startDocument() throws SAXException {

            super.startDocument();
            out.println("\\begin{xmldocument}%");
            level++;

        }
    }

    /**
     * main.
     *
     * @param args  The command line
     * @throws IOException if an IO-error occurs.
     */
    public static void main(final String[] args) throws IOException {

        if (args.length != 1) {
            System.err
                    .println("java de.dante.util.xml.Xml2LaTeXMacros <file.xml>");
            System.exit(1);
        }
        Xml2LaTeXMacros x = new Xml2LaTeXMacros(System.out);

        x.printLaTeXMacros(new FileInputStream(args[0]));
    }

}
