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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Test for XMLStreamWriter.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class TestXMLStreamWriter extends TestCase {

    /**
     * Test the document header.
     * @throws IOException if an error occurs.
     */
    public void testHeader() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartDocument();
        writer.writeEndDocument();
        writer.close();

        String xml = (new String(out.toByteArray())).trim();

        assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>", xml);

    }

    /**
     * Test the document header.
     * @throws IOException if an error occurs.
     */
    public void testHeaderRoot() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartDocument();
        writer.writeStartElement("root");
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();

        String xml = (new String(out.toByteArray())).trim();

        assertEquals(
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<root/>", xml);

    }

    /**
     * Test the document header.
     * @throws IOException if an error occurs.
     */
    public void testHeaderRootE1() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        try {
        writer.writeStartDocument();
        writer.writeStartElement("root");
        // writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();

        } catch (IOException e) {

            if (e.getMessage().startsWith("invalid struktur")) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
            return;
        }
        assertTrue(false);
    }

    /**
     * Test the element.
     * @throws IOException if an error occurs.
     */
    public void testElement1() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartElement("root");
        writer.writeStartElement("xxx");
        writer.writeAttribute("key", "value");
        writer.writeAttribute("key2", "value2");
        writer.writeAttribute("key3", "value3");
        writer.writeEndElement();
        writer.close();

        String xml = (new String(out.toByteArray())).trim();
        assertEquals(
                "<root><xxx key=\"value\" key2=\"value2\" key3=\"value3\"/>", xml);

    }

    /**
     * Test the element.
     * @throws IOException if an error occurs.
     */
    public void testText1() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartElement("root");
        writer.writeCharacters("Dies ist ein Text!");
        writer.writeEndElement();
        writer.close();
        String xml = (new String(out.toByteArray())).trim();
        assertEquals(
                "<root>Dies ist ein Text!</root>", xml);

    }

    /**
     * Test the element.
     * @throws IOException if an error occurs.
     */
    public void testText2() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartElement("root");
        writer.writeCharacters("Sonderzeichen <");
        writer.writeEndElement();
        writer.close();
        String xml = (new String(out.toByteArray())).trim();
        assertEquals(
                "<root>Sonderzeichen &lt;</root>", xml);

    }

    /**
     * Test the element.
     * @throws IOException if an error occurs.
     */
    public void testText3() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartElement("root");
        writer.writeCharacters("Sonderzeichen >");
        writer.writeEndElement();
        writer.close();
        String xml = (new String(out.toByteArray())).trim();
        assertEquals(
                "<root>Sonderzeichen &gt;</root>", xml);

    }

    /**
     * Test the element.
     * @throws IOException if an error occurs.
     */
    public void testText4() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartElement("root");
        writer.writeCharacters("Sonderzeichen &");
        writer.writeEndElement();
        writer.close();
        String xml = (new String(out.toByteArray())).trim();
        assertEquals(
                "<root>Sonderzeichen &amp;</root>", xml);

    }

    /**
     * Test the element.
     * @throws IOException if an error occurs.
     */
    public void testText5() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartElement("root");
        writer.writeCharacters("Sonderzeichen \"");
        writer.writeEndElement();
        writer.close();
        String xml = (new String(out.toByteArray())).trim();
        assertEquals(
                "<root>Sonderzeichen &quot;</root>", xml);

    }

    /**
     * Test the element.
     * @throws IOException if an error occurs.
     */
    public void testText6() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writer = new XMLStreamWriter(out, "ISO-8859-1");

        writer.writeStartElement("root");
        writer.writeCharacters("Sonderzeichen '");
        writer.writeEndElement();
        writer.close();
        String xml = (new String(out.toByteArray())).trim();
        assertEquals(
                "<root>Sonderzeichen &apos;</root>", xml);

    }

    /**
     * main
     * @param args the commandline
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(TestXMLStreamWriter.class);
    }

}
