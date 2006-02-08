/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import junit.framework.TestCase;
import de.dante.extex.ExTeX;
import de.dante.extex.unicodeFont.format.pfb.PfbParser;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationClassNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationInstantiationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.configuration.exception.ConfigurationNoSuchMethodException;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceFinder;

/**
 * Test the pdf parser.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class PfbParserTest extends TestCase {

    /**
     * Which configuration file.
     */
    private static final String CONFIG_EXTEX = "config/extex-font.xml";

    /**
     * my extex.
     */
    private MyExTeX extex;

    /**
     * test 01.
     * lmr12.pfb
     * @throws Exception if an error occurs.
     */
    public void test01() throws Exception {

        String[] enc = parser.getEncoding();

        assertEquals(enc[32], "space");
        assertEquals(enc[50], "two");
        assertEquals(enc[95], "underscore");
        assertEquals(enc[117], "u");
        assertEquals(enc[138], "Scaron");
        assertEquals(enc[255], "ydieresis");
        assertEquals(enc[0], null);
    }

    /**
     * test 02.
     * Check, if lmr12.pfa is correct.
     *
     * @throws Exception if an error occurs.
     */
    public void test02() throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream(parser.size());
        parser.toPfa(out);
        byte[] pfa = out.toByteArray();
        out.close();

        InputStream in = extex.getResourceFinder()
                .findResource("lmr12.pfa", "");
        assertNotNull(in);

        int c;
        int pos = 0;
        while ((c = in.read()) != -1) {
            assertEquals(c, pfa[pos++]);
        }
    }

    /**
     * the parser.
     */
    private PfbParser parser;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {

        if (extex == null) {
            extex = new MyExTeX(System.getProperties(), ".extex-test");

            InputStream in = extex.getResourceFinder().findResource(
                    "lmr12.pfb", "");
            parser = new PfbParser(in);
        }
    }

    // -----------------------------------------------------------
    // -----------------------------------------------------------
    // -----------------------------------------------------------
    // -----------------------------------------------------------
    // -----------------------------------------------------------
    // -----------------------------------------------------------
    // -----------------------------------------------------------

    /**
     * inner ExTeX class.
     */
    public class MyExTeX extends ExTeX {

        /**
         * Creates a new object and initializes the properties from given
         * properties and possibly from a user's properties in the file
         * <tt>.extex</tt>.
         * The user properties are loaded from the users home directory and the
         * current directory.
         *
         * @param theProperties the properties to consider
         * @param dotFile the name of the local configuration file. In the case
         *            that this value is <code>null</code> no user properties
         *            will be considered.
         *
         * @throws Exception in case of an error
         */
        public MyExTeX(final Properties theProperties, final String dotFile)
                throws Exception {

            super(theProperties, dotFile);
            makeConfig();
        }

        /**
         * Creates a new object and supplies some properties for those keys which
         * are not contained in the properties already.
         * A detailed list of the properties supported can be found in section
         * <a href="#settings">Settings</a>.
         *
         * @param theProperties the properties to start with. This object is
         *  used and modified. The caller should provide a new instance if this is
         *  not desirable.
         *
         * @throws Exception in case of an error
         */
        public MyExTeX(final Properties theProperties) throws Exception {

            super(theProperties);
            makeConfig();
        }

        /**
         * the config.
         */
        private Configuration config;

        /**
         * create the config.
         * @throws ConfigurationException from the config system.
         */
        private void makeConfig() throws ConfigurationException {

            config = new ConfigurationFactory().newInstance(CONFIG_EXTEX);

        }

        /**
         * the finder.
         */
        private ResourceFinder finder;

        /**
         * Returns the finder.
         * @return Returns the finder.
         * @throws ConfigurationException if an error occurs.
         */
        public ResourceFinder getResourceFinder() throws ConfigurationException {

            if (finder == null) {
                finder = makeResourceFinder(config);
            }
            return finder;
        }

        /**
         * the font factroy.
         */
        private FontFactory fontFactory;

        /**
         * Returns the font factory.
         * @return Returns the font factory.
         * @throws ConfigurationException if an error occurs
         */
        public FontFactory getFontFactory() throws ConfigurationException {

            if (fontFactory == null) {
                fontFactory = makemyFontFactory(config
                        .getConfiguration("Fonts"), getResourceFinder());
            }
            return fontFactory;
        }

        /**
         * Create a new font factory.
         * @param cfg the configuration object for the font factory
         * @param rfinder the resource finder to use
         *
         * @return the new font factory
         *
         * @throws ConfigurationException in case that some kind of problems have
         * been detected in the configuration
         */
        protected FontFactory makemyFontFactory(final Configuration cfg,
                final ResourceFinder rfinder) throws ConfigurationException {

            FontFactory foFactory;
            String fontClass = cfg.getAttribute("class");

            if (fontClass == null || fontClass.equals("")) {
                throw new ConfigurationMissingAttributeException("class", cfg);
            }

            try {
                foFactory = (FontFactory) (Class.forName(fontClass)
                        .getConstructor(
                                new Class[]{Configuration.class,
                                        ResourceFinder.class})
                        .newInstance(new Object[]{cfg, rfinder}));
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

            if (foFactory instanceof PropertyConfigurable) {
                ((PropertyConfigurable) foFactory)
                        .setProperties(getProperties());
            }

            return foFactory;
        }

    }

    // --------------------------------------------

    /**
     * The main method.
     * @param args  The command line.
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(PfbParserTest.class);
    }

}
