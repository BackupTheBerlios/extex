/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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
 *
 */

package de.dante.util.format.dvi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import de.dante.extex.font.FontFactory;
import de.dante.extex.format.dvi.DviXml;
import de.dante.util.file.random.RandomAccessInputFile;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationClassNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationInstantiationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.configuration.exception.ConfigurationNoSuchMethodException;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Convert a Dvi-File to to a XML-file
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public final class Dvi2Xml {

    /**
     * private: no instance
     */
    private Dvi2Xml() {

    }

    /**
     * parameter
     */
    private static final int PARAMETER = 2;

    /**
     * the encoding
     */
    private static final String ENCODING = "ISO-8859-1";

    /**
     * the buffer for the output
     */
    private static final int BUFFER = 0xffff;

    /**
     * main
     * @param args      the comandlinearguments
     * @throws Exception  in case of an error
     */
    public static void main(final String[] args) throws Exception {

        if (args.length != PARAMETER) {
            System.err
                    .println("java de.dante.util.format.dvi.Dvi2Xml <dvi-file> <xml-file>");
            System.exit(1);
        }

        // write to efm-file
        XMLOutputter xmlout = new XMLOutputter("   ", true);
        xmlout.setEncoding(ENCODING);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(args[1]), ENCODING), BUFFER);

        printDeclaration(out, "1.0", ENCODING);

        Element edvi = new Element("dvi");
        printElement(edvi, out, true);

        RandomAccessInputFile rar = new RandomAccessInputFile(args[0]);
        DviXml dvi = new DviXml(null, makeFontFactory());

        Element e = null;
        do {
            e = dvi.readNextElement(rar);
            if (e != null) {
                xmlout.output(e, out);
                out.newLine();
            }
        } while (e != null);

        printElement(edvi, out, false);
        out.close();
    }

    /**
     * This will print the stattag without attributes or the endtag.
     *
     * @param element   <code>Element</code> to output.
     * @param out       <code>BufferedWriter</code> to use.
     * @param start     <code>boolean</code> print the starttag or the enttag
     * @throws IOException if an IO-error occurs
     */
    private static void printElement(final Element element,
            final BufferedWriter out, final boolean start) throws IOException {

        if (!start) {
            out.write("</");
            out.write(element.getQualifiedName());
            out.write(">");
            out.newLine();
        } else {
            out.write("<");
            out.write(element.getQualifiedName());
            out.write(">");
            out.newLine();
        }
    }

    /**
     * Print the xml-Declaration
     *
     * @param out           the writer
     * @param version       the xml versiuon
     * @param encoding      the xml encoding
     * @throws IOException if an IO-error occurs
     */
    private static void printDeclaration(final BufferedWriter out,
            final String version, final String encoding) throws IOException {

        out.write("<?xml version=\"" + version + "\"");
        out.write(" encoding=\"" + encoding + "\"");
        out.write("?>");
        out.newLine();
    }

    // ------------------------------------------
    // ------------------------------------------
    // ------------------------------------------
    // ------------------------------------------
    // ------------------------------------------

    /**
     * make a font factroy
     * @return  Return the fontfactory
     * @throws Exception if an error occurs.
     */

    private static FontFactory makeFontFactory() throws Exception {

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
    protected static FontFactory makeFontFactory(final Configuration config)
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
    private static Properties getProps() {

        if (props == null) {
            props = System.getProperties();

            File file = new File(".extex");
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
        return (Properties) props.clone();
    }

    /**
     * The field <tt>props</tt> contains the merged properties from the
     * system properties and the properties loaded from <tt>.extex-test</tt>.
     */
    private static Properties props = null;

}