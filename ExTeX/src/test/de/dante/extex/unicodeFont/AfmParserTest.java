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

package de.dante.extex.unicodeFont;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import junit.framework.TestCase;
import de.dante.extex.ExTeX;
import de.dante.extex.unicodeFont.format.afm.AfmCharMetric;
import de.dante.extex.unicodeFont.format.afm.AfmHeader;
import de.dante.extex.unicodeFont.format.afm.AfmKernPairs;
import de.dante.extex.unicodeFont.format.afm.AfmParser;
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
 * Test the afm parser.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class AfmParserTest extends TestCase {

    /**
     * rounding error.
     */
    private static final float C0001F = 0.0001f;

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
     * lmr12.afm header
     * @throws Exception if an error occurs.
     */
    public void test01() throws Exception {

        AfmHeader header = parser.getHeader();
        assertEquals("LMRoman12-Regular", header.getFontname());
        assertEquals("LMRoman12-Regular", header.getFullname());
        assertEquals("LMRoman12", header.getFamilyname());
        assertEquals("Normal", header.getWeight());
        assertEquals(0, header.getItalicangle(), 0f);
        assertEquals(false, header.isFixedpitch());
        assertEquals(-175, header.getUnderlineposition(), 0f);
        assertEquals(44, header.getUnderlinethickness(), 0f);
        assertEquals("FontSpecific", header.getEncodingscheme());
        assertEquals(-185, header.getLlx(), 0f);
        assertEquals(-280, header.getLly(), 0f);
        assertEquals(1394, header.getUrx(), 0f);
        assertEquals(968, header.getUry(), 0f);
        assertEquals(683.33333f, header.getCapheight(), C0001F);
        assertEquals(430.55556f, header.getXheight(), C0001F);
        assertEquals(-194.44444, header.getDescender(), C0001F);
        assertEquals(694.44444, header.getAscender(), C0001F);

    }

    /**
     * test 02.
     * lmr12.afm charmetric
     * @throws Exception if an error occurs.
     */
    public void test02() throws Exception {

        // C 32 ; WX 326.38461 ; N space ; B 0 0 0 0 ;
        AfmCharMetric cm = parser.getAfmCharMetric(32);

        assertEquals(32, cm.getC());
        assertEquals(326.38461f, cm.getWx(), C0001F);
        assertEquals("space", cm.getN());
        assertEquals(0, cm.getBllx(), 0);
        assertEquals(0, cm.getBlly(), 0);
        assertEquals(0, cm.getBurx(), 0);
        assertEquals(0, cm.getBury(), 0);

    }

    /**
     * test 03.
     * lmr12.afm charmetric
     * @throws Exception if an error occurs.
     */
    public void test03() throws Exception {

        // C 33 ; WX 272 ; N exclam ; B 86 0 184 715 ; 
        // L quoteleft exclamdown ; L quoteleft.dup exclamdown ;

        AfmCharMetric cm = parser.getAfmCharMetric(33);

        assertEquals(33, cm.getC());
        assertEquals(272f, cm.getWx(), C0001F);
        assertEquals("exclam", cm.getN());
        assertEquals(86, cm.getBllx(), 0);
        assertEquals(0, cm.getBlly(), 0);
        assertEquals(184, cm.getBurx(), 0);
        assertEquals(715, cm.getBury(), 0);

        assertEquals("exclamdown", cm.getLigature("quoteleft"));
        assertEquals("exclamdown", cm.getLigature("quoteleft.dup"));
        assertEquals(null, cm.getLigature("gibts nicht"));
    }

    /**
     * test 04.
     * lmr12.afm charmetric
     * @throws Exception if an error occurs.
     */
    public void test04() throws Exception {

        // C -1 ; WX 0 ; N sfthyphen ; B 11 189 271 243 ;
        AfmCharMetric cm = parser.getAfmCharMetric("sfthyphen");

        assertEquals(-1, cm.getC());
        assertEquals(0, cm.getWx(), C0001F);
        assertEquals("sfthyphen", cm.getN());
        assertEquals(11, cm.getBllx(), 0);
        assertEquals(189, cm.getBlly(), 0);
        assertEquals(271, cm.getBurx(), 0);
        assertEquals(243, cm.getBury(), 0);

    }

    /**
     * test 05.
     * lmr12.afm charmetric
     * @throws Exception if an error occurs.
     */
    public void test05() throws Exception {

        // C -1 ; WX 272 ; N quoteleft.dup ; B 73 403 184 693 ;
        // L quoteleft.dup quotedblleft ;

        AfmCharMetric cm = parser.getAfmCharMetric("quoteleft.dup");

        assertEquals(-1, cm.getC());
        assertEquals(272, cm.getWx(), C0001F);
        assertEquals("quoteleft.dup", cm.getN());
        assertEquals(73, cm.getBllx(), 0);
        assertEquals(403, cm.getBlly(), 0);
        assertEquals(184, cm.getBurx(), 0);
        assertEquals(693, cm.getBury(), 0);

        assertEquals("quotedblleft", cm.getLigature("quoteleft.dup"));
        assertEquals(null, cm.getLigature("gibts nicht"));

    }

    /**
     * test 06.
     * lmr12.afm kerning
     * @throws Exception if an error occurs.
     */
    public void test06() throws Exception {

        // KPX A C -27.192
        AfmCharMetric cm = parser.getAfmCharMetric("A");
        AfmKernPairs kp = cm.getAfmKernPair("C");
        assertNotNull(kp);
        assertEquals("A", kp.getCharpre());
        assertEquals("C", kp.getCharpost());
        assertEquals(-27.192f, kp.getKerningsize(), C0001F);

        // KPX A Ccircumflex -27.192
        kp = cm.getAfmKernPair("Ccircumflex");
        assertNotNull(kp);
        assertEquals("A", kp.getCharpre());
        assertEquals("Ccircumflex", kp.getCharpost());
        assertEquals(-27.192f, kp.getKerningsize(), C0001F);

        // KPX A T -81.577
        kp = cm.getAfmKernPair("T");
        assertNotNull(kp);
        assertEquals("A", kp.getCharpre());
        assertEquals("T", kp.getCharpost());
        assertEquals(-81.577f, kp.getKerningsize(), C0001F);

    }

    /**
     * test 07.
     * lmr12.afm kerning
     * @throws Exception if an error occurs.
     */
    public void test07() throws Exception {

        // KPX ygrave oacute -27.192
        AfmCharMetric cm = parser.getAfmCharMetric("ygrave");
        AfmKernPairs kp = cm.getAfmKernPair("oacute");
        assertNotNull("kern missing", kp);
        assertEquals("ygrave", kp.getCharpre());
        assertEquals("oacute", kp.getCharpost());
        assertEquals(-27.192f, kp.getKerningsize(), C0001F);

        kp = cm.getAfmKernPair("gibts nicht");
        assertNull(kp);
    }

    /**
     * the parser.
     */
    private AfmParser parser;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {

        if (extex == null) {
            extex = new MyExTeX(System.getProperties(), ".extex-test");

            InputStream in = extex.getResourceFinder().findResource(
                    "lmr12", "afm");
            assertNotNull("font not found", in);
            parser = new AfmParser(in);
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
                finder = makeResourceFinder(config.getConfiguration("Resource"));
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
         * @param config the configuration object for the font factory
         * @param finder the resource finder to use
         *
         * @return the new font factory
         *
         * @throws ConfigurationException in case that some kind of problems have
         * been detected in the configuration
         */
        protected FontFactory makemyFontFactory(final Configuration config,
                final ResourceFinder finder) throws ConfigurationException {

            FontFactory fontFactory;
            String fontClass = config.getAttribute("class");

            if (fontClass == null || fontClass.equals("")) {
                throw new ConfigurationMissingAttributeException("class",
                        config);
            }

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
                ((PropertyConfigurable) fontFactory)
                        .setProperties(getProperties());
            }

            return fontFactory;
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
