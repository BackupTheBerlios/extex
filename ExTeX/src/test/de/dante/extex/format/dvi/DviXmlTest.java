/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import junit.framework.TestCase;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import de.dante.extex.font.FontFactory;
import de.dante.util.file.random.RandomAccessInputFile;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationClassNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationInstantiationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.configuration.exception.ConfigurationNoSuchMethodException;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Test the DviXml class.
 * <p>
 * needs -Xms64m -Xmx512m
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.11 $
 */

public class DviXmlTest extends TestCase {

    /**
     * path.
     */
    private static final String PATH = "develop/test/data/dvi/";

    /**
     * files.
     */
    private static final String[] FILES = {"lettrine", "listings"};

    /**
     * write xml file.
     */
    private static final boolean WRITEXML = true;

    /**
     * test the dviXml interpreter.
     * @throws Exception if an error occurs
     */
    public void testlettrine() throws Exception {

        Element root = readXML(0);

        // -----------------------------------------
        JDOMXPath path = new JDOMXPath("/dvi/pre");
        Element el = (Element) path.selectSingleNode(root);

        assertEquals("2", el.getAttributeValue("identifies"));
        assertEquals("25400000", el.getAttributeValue("num"));
        assertEquals("473628672", el.getAttributeValue("den"));
        assertEquals("1000", el.getAttributeValue("mag"));

        // -----------------------------------------
        path = new JDOMXPath("/dvi/post_post");
        el = (Element) path.selectSingleNode(root);

        assertEquals("20510", el.getAttributeValue("q"));
        assertEquals("2", el.getAttributeValue("identifies"));

        // -----------------------------------------
        path = new JDOMXPath("/dvi/post/fntdef1[@font='15']");
        el = (Element) path.selectSingleNode(root);

        assertEquals("15", el.getAttributeValue("font"));
        assertEquals("cmbx12", el.getAttributeValue("name"));
        assertEquals("-1026142560", el.getAttributeValue("checksum"));
        assertEquals("943718", el.getAttributeValue("scalefactor"));
        assertEquals("1200", el.getAttributeValue("scaled"));

    }

    /**
     * test the dviXml interpreter.
     * @throws Exception if an error occurs
     */
    public void testlistings() throws Exception {

        /*Element root = */readXML(1);

    }

    /**
     * read the xml.
     * @param nr    the file-nr
     * @return Return the root element
     * @throws Exception if an error occurs
     */
    private Element readXML(final int nr) throws Exception {

        String file = PATH + FILES[nr] + ".dvi";
        RandomAccessInputFile rar = new RandomAccessInputFile(file);

        Element root = new Element("dvi");

        DviXml dvixml = new DviXml(root, makeFontFactory());
        // dvixml.setShowPT(true);

        dvixml.interpret(rar);
        rar.close();

        // write to efm-file
        if (WRITEXML) {
            XMLOutputter xmlout = new XMLOutputter("   ", true);
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(PATH + FILES[nr] + ".xml.tmp"));
            Document doc = new Document(root);
            xmlout.output(doc, out);
            out.close();
        }

        return root;
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
     * make a font factory.
     * @return  Return the font factory
     * @throws Exception if an error occurs.
     */

    private FontFactory makeFontFactory() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        FontFactory fontFactory = makeFontFactory(config
                .getConfiguration("Fonts"), config.getConfiguration("Resource"));
        ((PropertyConfigurable)fontFactory).setProperties(getProps());

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
    protected FontFactory makeFontFactory(final Configuration config,
            final Configuration finderCfg) throws ConfigurationException {

        FontFactory fontFactory;
        String fontClass = config.getAttribute("class");

        if (fontClass == null || fontClass.equals("")) {
            throw new ConfigurationMissingAttributeException("class", config);
        }

        ResourceFinder fontFinder = (new ResourceFinderFactory())
                .createResourceFinder(finderCfg, null, getProps());
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
                } catch (IOException e) {
                    // ignored on purpose
                }
            }
        }
        return (Properties) this.props.clone();
    }

    /**
     * test DviXml.
     * @param args  the commandline
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DviXmlTest.class);
    }
}
