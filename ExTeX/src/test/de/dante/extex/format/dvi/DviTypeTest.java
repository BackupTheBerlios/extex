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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import junit.framework.TestCase;
import de.dante.extex.font.FontFactory;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationFactory;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;
import de.dante.util.file.random.RandomAccessInputFile;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Test the DviType class.
 * <p>
 * needs -Xms64m -Xmx127m
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class DviTypeTest extends TestCase {

    /**
     * path
     */
    private static final String PATH = "src/test/data/dvi/";

    /**
     * files
     */
    private static final String[] FILES = {"lettrine", "listings", "andre"};

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        for (int i = 0; i < FILES.length; i++) {

            String file = PATH + FILES[i] + ".dvi";
            RandomAccessInputFile rar = new RandomAccessInputFile(file);

            PrintWriter writer = new PrintWriter(new BufferedOutputStream(
                    new FileOutputStream(PATH + FILES[i] + ".tmp")));
            DviType dvitype = new DviType(writer, makeFontFactory());

            dvitype.interpret(rar);
            rar.close();
            writer.close();
        }
    }

    /**
     * test the dviXml interpreter
     * @throws IOException if a IO-error occurs
     */
    public void testlettrine() throws IOException {

        checkFile(FILES[0]);
    }

    /**
     * test the dviXml interpreter
     * @throws IOException if a IO-error occurs
     */
    public void testlistings() throws IOException {

        checkFile(FILES[1]);
    }

    /**
     * test the dviXml interpreter
     * @throws IOException if a IO-error occurs
     */
    public void testandre() throws IOException {

        checkFile(FILES[2]);
    }

    /**
     * @param file  the file to test
     * @throws IOException if an io-error occurs
     */
    private void checkFile(final String file) throws IOException {

        LineNumberReader inorg = new LineNumberReader(new FileReader(PATH
                + file + ".dvitype"));
        LineNumberReader innew = new LineNumberReader(new FileReader(PATH
                + file + ".tmp"));

        Map maporg = new TreeMap();
        Map mapnew = new TreeMap();

        readFile(inorg, maporg);
        readFile(innew, mapnew);

        inorg.close();
        innew.close();

        Iterator it = maporg.keySet().iterator();
        while (it.hasNext()) {
            Integer key = (Integer) it.next();
            String lineorg = killVVHH((String) maporg.get(key));
            String linenew = killVVHH((String) mapnew.get(key));
            // String lineorg = (String) maporg.get(key);
            // String linenew = (String) mapnew.get(key);
            assertEquals(lineorg, linenew);
        }
    }

    /**
     * remove the vv- or hh-value
     * @param s the String
     * @return Returns the String witout vv.. / hh..
     */
    private String killVVHH(final String s) {

        int i = s.indexOf("vv");
        if (i >= 0) {
            return s.substring(0, i);
        }
        i = s.indexOf("hh");
        if (i >= 0) {
            return s.substring(0, i);
        }
        return s;
    }

    // --------------------------------------
    // --------------------------------------
    // --------------------------------------

    /**
     * @param in    the input
     * @param map   the map
     * @throws IOException if an io-error occurs
     */
    private void readFile(final LineNumberReader in, final Map map)
            throws IOException {

        // read all line with start with a number "111:..."
        String line;
        while ((line = in.readLine()) != null) {
            if (line.matches("^[0-9]*:.*")) {
                int pos = line.indexOf(":");
                Integer key = new Integer(line.substring(0, pos));
                map.put(key, line.trim());
            }
        }
    }

    /**
     * The field <tt>props</tt> contains the merged properties from the
     * system properties and the properties loaded from <tt>.extex-test</tt>.
     */
    private Properties props = null;

    /**
     * finder
     */
    private ResourceFinder finder;

    /**
     * make a font factroy
     * @return  Return the fontfactory
     * @throws Exception if an error occurs.
     */

    private FontFactory makeFontFactory() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        props = getProps();

        finder = (new ResourceFinderFactory()).createResourceFinder(config
                .getConfiguration("Resource"), null, props);

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

        //        ResourceFinder fontFinder = (new ResourceFinderFactory())
        //                .createResourceFinder(config.getConfiguration("Resource"),
        //                        null, getProps());
        //        if (Boolean.valueOf(getProps().getProperty("extex.trace.font.files"))
        //                .booleanValue()) {
        //            fontFinder.enableTracing(true);
        //        }

        try {
            fontFactory = (FontFactory) (Class.forName(fontClass)
                    .getConstructor(
                            new Class[]{Configuration.class,
                                    ResourceFinder.class})
                    .newInstance(new Object[]{config, finder}));
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

        if (fontFactory instanceof PropertyConfigurable) {
            ((PropertyConfigurable) fontFactory).setProperties(props);
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

        junit.textui.TestRunner.run(DviTypeTest.class);
    }
}
