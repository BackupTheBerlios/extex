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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import de.dante.extex.font.FontFactory;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationFactory;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;
import de.dante.util.file.random.RandomAccessInputFile;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Test the DviXml class.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class DviXmlTest extends TestCase {

    /**
     * write xml file
     */
    private static final boolean WRITEXML = true;

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

        DviXml dvixml = new DviXml(root, makeFontFactory());

        dvixml.interpret(rar);

        // write to efm-file
        if (WRITEXML) {
            XMLOutputter xmlout = new XMLOutputter("   ", true);
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream("dvi.xml.tmp"));
            Document doc = new Document(root);
            xmlout.output(doc, out);
            out.close();
        }
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

        assertEquals("15", findAttrElementNr("bop", "fntdef1", "font", 0));
        assertEquals("cmbx12", findAttrElementNr("bop", "fntdef1", "name", 0));
        assertEquals("-1026142560", findAttrElementNr("bop", "fntdef1",
                "checksum", 0));
        assertEquals("943718", findAttrElementNr("bop", "fntdef1",
                "scalefactor", 0));
        assertEquals("1200", findAttrElementNr("bop", "fntdef1", "scaled", 0));
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
     * @param pname     the parent element
     * @param ename     the element name
     * @param attrname  the attribute name
     * @param i         the index
     * @return Returns the value
     */
    private String findAttrElementNr(final String pname, final String ename,
            final String attrname, final int i) {

        String rt = null;

        Element parent = root.getChild(pname);
        if (parent != null) {

            List list = parent.getChildren(ename);
            Element e = (Element) list.get(i);
            if (e != null) {
                rt = e.getAttributeValue(attrname);
            }
        }
        return rt;
    }

    // --------------------------------------
    // --------------------------------------
    // --------------------------------------

    /**
     * The field <tt>props</tt> contains the merged properties from the
     * system properties and the properties loaded from <tt>.extex-test</tt>.
     */
    private Properties props = null;

    /**
     * make a font factroy
     * @return  Return the fontfactory
     * @throws Exception if an error occurs.
     */

    private FontFactory makeFontFactory() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        FontFactory fontFactory = makeFontFactory(config
                .getConfiguration("Fonts"));

        return fontFactory;
    }

    /**
     * Create a new font factory.
     *
     * @param config the configuration object for the font factory
     *
     * @return the new font factory
     *
     * @throws ConfigurationException in case that some kind of problems have
     * been detected in the configuration
     */
    protected FontFactory makeFontFactory(final Configuration config)
            throws ConfigurationException {

        FontFactory fontFactory;
        String fontClass = config.getAttribute("class");

        if (fontClass == null || fontClass.equals("")) {
            throw new ConfigurationMissingAttributeException("class", config);
        }

        ResourceFinder fontFinder = (new ResourceFinderFactory())
                .createResourceFinder(config.getConfiguration("Resource"),
                        null, getProps());
        if (Boolean.valueOf(getProps().getProperty("extex.trace.font.files"))
                .booleanValue()) {
            fontFinder.enableTracing(true);
        }

        try {
            fontFactory = (FontFactory) (Class.forName(fontClass)
                    .getConstructor(
                            new Class[]{Configuration.class,
                                    ResourceFinder.class})
                    .newInstance(new Object[]{config, fontFinder}));
        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(fontClass);
        }

        return fontFactory;
    }

    /**
     * Getter for props.
     *
     * @return the props
     */
    private Properties getProps() {

        if (props == null) {
            props = System.getProperties();

            File file = new File(".extex-test");
            if (file.canRead()) {
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    props.load(inputStream);
                    inputStream.close();
                } catch (FileNotFoundException e) {
                    // ignored on purpose
                } catch (IOException e) {
                    // ignored on purpose
                }
            }
        }
        return (Properties) this.props.clone();
    }

    /**
     * test DviXml
     * @param args  the commandline
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DviXmlTest.class);
    }
}
