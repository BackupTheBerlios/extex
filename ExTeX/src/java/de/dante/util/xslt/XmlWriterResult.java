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
 */

package de.dante.util.xslt;

import javax.xml.transform.sax.SAXResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.dante.util.xml.XMLStreamWriter;

/**
 * Transformer result, which use the <code>XMLStreamWriter</code>.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 *
 */
public class XmlWriterResult extends SAXResult {

    /**
     * Create a new object.
     *
     * @param writer    The xml stream writer.
     */
    public XmlWriterResult(final XMLStreamWriter writer) {

        super.setHandler(new RemoveSingleNewline(writer));

    }

    /**
     * The RemoveSingleNewline handler removes single NL or CR from the characters.
     */
    public static class RemoveSingleNewline extends DefaultHandler {

        /**
         * The xml writer.
         */
        private XMLStreamWriter writer;

        /**
         * Create a new object.
         *
         * @param w The xml writer.
         */
        public RemoveSingleNewline(final XMLStreamWriter w) {

            writer = w;
        }

        /**
         * @see org.xml.sax.ContentHandler#characters(char[], int, int)
         */
        public void characters(final char[] ch, final int start,
                final int length) throws SAXException {

            try {
                String text = new String(ch, start, length);
                if (!(text.equals("\n") || text.equals("\r") || text
                        .equals("\r\n"))) {
                    writer.writeCharacters(text);
                }
            } catch (Exception e) {
                new SAXException(e);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endDocument()
         */
        public void endDocument() throws SAXException {

            try {
                writer.writeEndDocument();
            } catch (Exception e) {
                new SAXException(e);
            }
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
         *      java.lang.String, java.lang.String)
         */
        public void endElement(final String uri, final String localName,
                final String qName) throws SAXException {

            try {
                writer.writeEndElement();
            } catch (Exception e) {
                new SAXException(e);
            }

        }

        /**
         * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
         */
        public void ignorableWhitespace(final char[] ch, final int start,
                final int length) throws SAXException {

            // TODO incomplete
            throw new SAXException("not implemented");
        }

        /**
         * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
         *      java.lang.String)
         */
        public void processingInstruction(final String target, final String data)
                throws SAXException {

            // TODO incomplete
            throw new SAXException("not implemented");
        }

        /**
         * @see org.xml.sax.ContentHandler#startDocument()
         */
        public void startDocument() throws SAXException {

            try {
                writer.writeStartDocument();
            } catch (Exception e) {
                new SAXException(e);
            }

        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
         *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(final String uri, final String localName,
                final String qName, final Attributes atts) throws SAXException {

            try {
                writer.writeStartElement(qName);
                for (int i = 0, n = atts.getLength(); i < n; i++) {
                    String qname = atts.getQName(i);
                    String val = atts.getValue(i);
                    writer.writeAttribute(qname, val);
                }
            } catch (Exception e) {
                new SAXException(e);
            }

        }

    }
}
