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

package de.dante.extex.format.dvi;

import java.util.List;

import junit.framework.TestCase;

import org.jdom.Element;

import de.dante.util.file.random.RandomAccessInputFile;

/**
 * Test the DviXml class.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviXmlTest extends TestCase {

    /**
     * the root
     */
    private Element root;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        String file = "src/test/data/lettrine.dvi";

        root = new Element("dvi");
        RandomAccessInputFile rar = new RandomAccessInputFile(file);

        DviXml dvixml = new DviXml(root);

        dvixml.interpret(rar);

        // write to efm-file
        //        XMLOutputter xmlout = new XMLOutputter("   ", true);
        //        BufferedOutputStream out = new BufferedOutputStream(
        //                new FileOutputStream("dvi.tmp"));
        //        Document doc = new Document(root);
        //        xmlout.output(doc, out);
        //        out.close();

    }

    /**
     * test the dviXml interpreter
     */
    public void testInterpretpre() {

        assertEquals("2", findAttrElement("pre", "identifies"));
        assertEquals("25400000", findAttrElement("pre", "num"));
        assertEquals("473628672", findAttrElement("pre", "den"));
        assertEquals("1000", findAttrElement("pre", "mag"));
    }

    /**
     * test the dviXml interpreter
     */
    public void testInterpretpostpost() {

        assertEquals("20510", findAttrElement("postpost", "q"));
        assertEquals("2", findAttrElement("postpost", "identifies"));
    }

    /**
     * test the dviXml interpreter
     */
    public void testInterpretfont() {

        assertEquals("15", findAttrElementNr("fntdef1", "font", 0));
        assertEquals("cmbx12", findAttrElementNr("fntdef1", "name", 0));
        assertEquals("-1026142560", findAttrElementNr("fntdef1", "checksum", 0));
    }

    /**
     * find a Attribute in the first element with the name
     * @param ename the element name
     * @param attrname  the attribute name
     * @return Returns the value
     */
    private String findAttrElement(final String ename, final String attrname) {

        String rt = null;
        Element e = root.getChild(ename);
        if (e != null) {
            rt = e.getAttributeValue(attrname);
        }
        return rt;
    }

    /**
     * find a Attribute in the element i with the name
     * @param ename     the element name
     * @param attrname  the attribute name
     * @param i         the index
     * @return Returns the value
     */
    private String findAttrElementNr(final String ename, final String attrname,
            final int i) {

        String rt = null;

        List list = root.getChildren(ename);
        Element e = (Element) list.get(i);
        if (e != null) {
            rt = e.getAttributeValue(attrname);
        }
        return rt;
    }

    /**
     * test DviXml
     * @param args  the commandline
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DviXmlTest.class);
    }
}
