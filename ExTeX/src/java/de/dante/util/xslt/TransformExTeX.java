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

package de.dante.util.xslt;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import de.dante.extex.ExTeX;
import de.dante.extex.unicodeFont.FontFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationClassNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationInstantiationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.configuration.exception.ConfigurationNoSuchMethodException;
import de.dante.util.resource.EntityResolverRf;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.UriResolverRf;

/**
 * Transform a xml-file with a xslt-file and use a ExTeX-instance.
 *
 * <p>It use the default resource finder, to get the files.</p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public final class TransformExTeX {

    /**
     * private: no instance.
     */
    private TransformExTeX() {

    }

    /**
     * How much parameter.
     */
    private static final int PARAMETER = 3;

    /**
     * The buffer size.
     */
    private static final int BUFFERSIZE = 0xffff;

    /**
     * main method.
     * @param args      the command line arguments
     * @throws Exception  in case of an error
     */
    public static void main(final String[] args) throws Exception {

        if (args.length != PARAMETER) {
            System.err.println("java de.dante.util.xslt.TransformExTeX "
                    + "<xml-file> <xsl-file> <out-file>");
            System.exit(1);
        }

        // -----------------------------------------------
        Properties prop = System.getProperties();
        prop.setProperty("extex.launcher.verbose", "false");
        // prop.setProperty("extex.launcher.verbose", "true");
        prop.setProperty("extex.trace.input.files", "true");
        MyExTeX extex = new MyExTeX(prop, ".extex-test");
        ResourceFinder finder = extex.getResourceFinder();
        UriResolverRf resolver = new UriResolverRf(finder);
        EntityResolverRf entresolver = new EntityResolverRf(finder);

        // -----------------------------------------------

        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(args[2]), BUFFERSIZE);

        InputStream xmlin = finder.findResource(args[0], "");
        InputStream xslin = finder.findResource(args[1], "");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(entresolver);
        
        Document xmldoc = builder.parse(xmlin);

        Transform.transform(new DOMSource(xmldoc), new StreamSource(xslin),
                resolver, out);
    }

    /**
     * inner ExTeX class.
     */
    private static class MyExTeX extends ExTeX {

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
         * Creates a new object and supplies some properties for
         * those keys which are not contained in the properties already.
         * A detailed list of the properties supported can be found in section
         * <a href="#settings">Settings</a>.
         *
         * @param theProperties     The properties to start with.
         *                          This object is used and modified.
         *                          The caller should provide a new instance
         *                          if this is not desirable.
         *
         * @throws Exception in case of an error
         */
        public MyExTeX(final Properties theProperties) throws Exception {

            super(theProperties);
            makeConfig();
        }

        /**
         * The configuration.
         */
        private Configuration config;

        /**
         * Create the configuration.
         */
        private void makeConfig() throws ConfigurationException {

            config = new ConfigurationFactory().newInstance("extex.xml");

        }

        /**
         * The finder.
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
         * the font factroy
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

}
