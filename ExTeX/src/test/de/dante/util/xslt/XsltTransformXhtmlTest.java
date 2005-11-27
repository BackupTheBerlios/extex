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

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.xml.transform.stream.StreamSource;

import de.dante.extex.ExTeX;
import de.dante.extex.unicodeFont.FontFactory;
import de.dante.test.ExTeXLauncher;
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
 * Testcase for the xslt transformer.
 *
 * <p> !!!!!!!!!!!!!!!!!!
 * Die Datei xhtml2latex.xsl wird über xsl:import nachgeladen
 * und muss im Moment unter extex-root sein!!!!
 * <p>
 * cd <extex>
 * ln -s src/xslt/xhtml2latex.xsl .
 * </p>
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class XsltTransformXhtmlTest extends ExTeXLauncher {

    /**
     * Getter for the configuration name.
     *
     * @return the name of the configuration
     */
    protected String getConfig() {

        return "extex-jx.xml";
    }

    /**
     * Create a new object.
     */
    public XsltTransformXhtmlTest() {

        super("XhtmlTest");
    }

    /**
     * my extex
     */
    private MyExTeX extex;

    /**
     * ResourceFinder
     */
    private ResourceFinder finder;

    /**
     * xsl stream
     */
    private StreamSource xslstream;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        Properties prop = System.getProperties();
        prop.setProperty("extex.launcher.verbose", "false");
        extex = new MyExTeX(prop, ".extex-test");
        finder = extex.getResourceFinder();

        InputStream xsl = finder.findResource("xhtml2latex_noheader.xsl", "");
        xslstream = new StreamSource(xsl);

    }

    /**
     * html head
     */
    private static final String HEAD = "<html><body>";

    /**
     * html foot
     */
    private static final String FOOT = "</body></html>";

    /**
     * test section 01
     * @throws Exception if an error occurred.
     */
    public void testSection01() throws Exception {

        String text = "Dies ist eine Überschrift";
        String html = HEAD + "<h1>" + text + "</h1>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals(out.toString().trim(), "\\section{" + text + "}");
    }

    /**
     * test section 02
     * @throws Exception if an error occurred.
     */
    public void testSection02() throws Exception {

        String text = "Dies ist eine Überschrift";
        String html = HEAD + "<h2>" + text + "</h2>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals(out.toString().trim(), "\\subsection{" + text + "}");
    }

    /**
     * test section 03
     * @throws Exception if an error occurred.
     */
    public void testSection03() throws Exception {

        String text = "Dies ist eine Überschrift";
        String html = HEAD + "<h3>" + text + "</h3>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals(out.toString().trim(), "\\subsubsection{" + text + "}");
    }

    /**
     * test section 04
     * @throws Exception if an error occurred.
     */
    public void testSection04() throws Exception {

        String text = "Dies ist eine Überschrift";
        String html = HEAD + "<h4>" + text + "</h4>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals(out.toString().trim(), "\\paragraph{" + text + "}");
    }

    /**
     * test section 05
     * @throws Exception if an error occurred.
     */
    public void testSection05() throws Exception {

        String text = "Dies ist eine Überschrift";
        String html = HEAD + "<h5>" + text + "</h5>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals(out.toString().trim(), "\\subparagraph{" + text + "}");
    }

    /**
     * test section 06
     * @throws Exception if an error occurred.
     */
    public void testSection06() throws Exception {

        String text = "Dies ist eine Überschrift";
        String html = HEAD + "<h6>" + text + "</h6>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals(out.toString().trim(), "\\textbf{" + text + "}\\newline");
    }

    /**
     * test quote 01
     * @throws Exception if an error occurred.
     */
    public void testQuote01() throws Exception {

        String text = "Text";
        String html = HEAD + "<quote>" + text + "</quote>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("``Text''", out.toString().trim());
    }

    /**
     * test code 01
     * @throws Exception if an error occurred.
     */
    public void testCode01() throws Exception {

        String text = "Text";
        String html = HEAD + "<code>" + text + "</code>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("\\texttt{Text}", out.toString().trim());
    }

    /**
     * test b 01
     * @throws Exception if an error occurred.
     */
    public void testB01() throws Exception {

        String text = "Text";
        String html = HEAD + "<b>" + text + "</b>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("\\textbf{Text}", out.toString().trim());
    }

    /**
     * test strong 01
     * @throws Exception if an error occurred.
     */
    public void testStrong01() throws Exception {

        String text = "Text";
        String html = HEAD + "<strong>" + text + "</strong>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("\\textbf{Text}", out.toString().trim());
    }

    /**
     * test it 01
     * @throws Exception if an error occurred.
     */
    public void testIt01() throws Exception {

        String text = "Text";
        String html = HEAD + "<it>" + text + "</it>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("\\emph{Text}", out.toString().trim());
    }

    /**
     * test em 01
     * @throws Exception if an error occurred.
     */
    public void testEm01() throws Exception {

        String text = "Text";
        String html = HEAD + "<em>" + text + "</em>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("\\emph{Text}", out.toString().trim());
    }

    /**
     * test tt 01
     * @throws Exception if an error occurred.
     */
    public void testTt01() throws Exception {

        String text = "Text";
        String html = HEAD + "<tt>" + text + "</tt>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("\\texttt{Text}", out.toString().trim());
    }

    /**
     * test big 01
     * @throws Exception if an error occurred.
     */
    public void testBig01() throws Exception {

        String text = "Text";
        String html = HEAD + "<big>" + text + "</big>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("{\\Large Text}", out.toString().trim());
    }

    /**
     * test small 01
     * @throws Exception if an error occurred.
     */
    public void testSmall01() throws Exception {

        String text = "Text";
        String html = HEAD + "<small>" + text + "</small>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals("{\\footnotesize Text}", out.toString().trim());
    }

    /**
     * test par 01
     * @throws Exception if an error occurred.
     */
    public void testPar01() throws Exception {

        String text = "Dies ist ein Text.";
        String html = HEAD + "<p>" + text + "</p>" + FOOT;

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(html);

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertEquals(text + "\n\n\n", removeRightSpace(out.toString()));
    }

    /**
     * item number
     */
    private static final int ITEMNUMBER = 6;

    /**
     * test item 01
     * @throws Exception if an error occurred.
     */
    public void testItem01() throws Exception {

        String text = "Dies ist der Text mit der Nummer ";
        StringBuffer buf = new StringBuffer();
        StringBuffer result = new StringBuffer();

        result.append("/begin (itemize) /par ");

        buf.append(HEAD);
        buf.append("<ul>\n");
        for (int i = 1; i < ITEMNUMBER; i++) {
            buf.append("<li>").append(text).append(i).append("</li>");
            result.append("/item ").append(text).append(i).append(" /par ");
        }
        buf.append("</ul>\n");
        buf.append(FOOT);
        result.append("/end (itemize) /par /par ");
        result.append("\n\n");

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(buf.toString());

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertSuccess(
                //--- input code ---
                DEFINE_BRACES
                        + "\\javadef\\t{"
                        + "de.dante.extex.interpreter.primitives.dynamic.ReadTokenUntilNull}"
                        + "\\t " + out.toString(),
                //--- log message ---
                result.toString());
    }

    /**
     * test item 02
     * @throws Exception if an error occurred.
     */
    public void testItem02() throws Exception {

        String text = "Dies ist der Text mit der Nummer ";
        StringBuffer buf = new StringBuffer();
        StringBuffer result = new StringBuffer();

        result.append("/begin (enumerate) /par ");

        buf.append(HEAD);
        buf.append("<ol>\n");
        for (int i = 1; i < ITEMNUMBER; i++) {
            buf.append("<li>").append(text).append(i).append("</li>");
            result.append("/item ").append(text).append(i).append(" /par ");
        }
        buf.append("</ol>\n");
        buf.append(FOOT);
        result.append("/end (enumerate) /par /par ");
        result.append("\n\n");

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(buf.toString());

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertSuccess(
                //--- input code ---
                DEFINE_BRACES
                        + "\\javadef\\t{"
                        + "de.dante.extex.interpreter.primitives.dynamic.ReadTokenUntilNull}"
                        + "\\t " + out.toString(),
                //--- log message ---
                result.toString());
    }

    /**
     * test item 03
     * @throws Exception if an error occurred.
     */
    public void testItem03() throws Exception {

        String text = "Dies ist der Text mit der Nummer ";
        StringBuffer buf = new StringBuffer();
        StringBuffer result = new StringBuffer();

        result.append("/begin (description) /par ");

        buf.append(HEAD);
        buf.append("<dl>\n");
        for (int i = 1; i < ITEMNUMBER; i++) {
            buf.append("<li>").append(text).append(i).append("</li>");
            result.append("/item ").append(text).append(i).append(" /par ");
        }
        buf.append("</dl>\n");
        buf.append(FOOT);
        result.append("/end (description) /par /par ");
        result.append("\n\n");

        StringWriter out = new StringWriter();
        StringReader xml = new StringReader(buf.toString());

        Transform.transform(new StreamSource(xml), xslstream, out);

        assertSuccess(
                //--- input code ---
                DEFINE_BRACES
                        + "\\javadef\\t{"
                        + "de.dante.extex.interpreter.primitives.dynamic.ReadTokenUntilNull}"
                        + "\\t " + out.toString(),
                //--- log message ---
                result.toString());
    }

    // ----------------------------------------------------
    // ----------------------------------------------------
    // ----------------------------------------------------
    // ----------------------------------------------------
    // ----------------------------------------------------

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
         * the config
         */
        private Configuration config;

        /**
         * create the config
         */
        private void makeConfig() throws ConfigurationException {

            config = new ConfigurationFactory().newInstance(getConfig());

        }

        /**
         * the finder
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

    // --------------------------------------------

    /**
     * main
     * @param args  the command line.
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(XsltTransformXhtmlTest.class);
    }

    /**
     * Remove the right space of a string.
     * @param text  The String
     * @return Returns the string without right spaces.
     */
    private String removeRightSpace(final String text) {

        StringBuffer buf = new StringBuffer(text);

        while (true) {
            char ch = buf.charAt(buf.length() - 1);
            if (ch == ' ') {
                buf.deleteCharAt(buf.length() - 1);
            } else {
                break;
            }
        }

        return buf.toString();
    }
}
