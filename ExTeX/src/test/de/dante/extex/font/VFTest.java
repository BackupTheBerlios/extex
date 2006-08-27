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

package de.dante.extex.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import junit.framework.TestCase;
import de.dante.extex.font.type.vf.VFFont;
import de.dante.util.file.random.RandomAccessInputStream;
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
import de.dante.util.xml.XMLStreamWriter;

/**
 * Test the VFFont class.
 * <p>
 * needs -Xms64m -Xmx127m
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */

public class VFTest extends TestCase {

    /**
     * path
     */
    private static final String PATH = "develop/test/data/font/";

    /**
     * files
     */
    private static final String[] FILES = {"aer12"};

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Configuration resource = new ConfigurationFactory()
                .newInstance("config/path/fileFinder.xml");

        Configuration cfgfonts = config.getConfiguration("Fonts");

        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(".extex-test");
            prop.load(in);
        } catch (Exception e) {
            prop.setProperty("extex.fonts", "src/font");
        }

        ResourceFinder finder = (new ResourceFinderFactory())
                .createResourceFinder(resource, null, prop);

        //       EncFactory ef = new EncFactory(finder);

        // create pl-files
        for (int i = 0; i < FILES.length; i++) {

            // vf-file
            InputStream vfin = finder.findResource(FILES[i], "vf");

            if (vfin == null) {
                throw new FileNotFoundException(FILES[i]);
            }

            //            // psfonts.map
            //            InputStream psin = finder.findResource("psfonts.map", "");
            //
            //            if (psin == null) {
            //                throw new FontMapNotFoundException();
            //            }
            //            PSFontsMapReader psfm = new PSFontsMapReader(psin);

            VFFont font = new VFFont(new RandomAccessInputStream(vfin),
                    FILES[i], makeFontFactory());

            //           font.setFontMapEncoding(psfm, ef);

            // write to xml-file
            XMLStreamWriter writer = new XMLStreamWriter(new FileOutputStream(
                    PATH + FILES[i] + ".xml.tmp"), "ISO-8859-1");
            writer.setBeauty(true);
            writer.writeStartDocument();
            font.writeXML(writer);
            writer.writeEndDocument();
            writer.close();
        }

    }

    /**
     * test the dviXml interpreter
     * @throws IOException if a IO-error occurs
     */
    public void testaer12() throws IOException {

        assertTrue(true);
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
        Configuration resource = new ConfigurationFactory()
                .newInstance("config/path/fileFinder.xml");

        if (fontClass == null || fontClass.equals("")) {
            throw new ConfigurationMissingAttributeException("class", config);
        }

        ResourceFinder fontFinder = (new ResourceFinderFactory())
                .createResourceFinder(resource, null, getProps());
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

        ((PropertyConfigurable)fontFactory).setProperties(getProps());
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
                } catch (Exception e) {
                    // ignored on purpose
                    e.printStackTrace();
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

        junit.textui.TestRunner.run(VFTest.class);
    }
}
