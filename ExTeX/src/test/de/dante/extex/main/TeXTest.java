/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;
import de.dante.extex.ExTeX;
import de.dante.extex.interpreter.exception.InterpreterException;

/**
 * This class contains test cases for the command line interface of
 * <logo>ExTeX</logo>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class TeXTest extends TestCase {

    /**
     * The field <tt>BANNER</tt> contains the default banner.
     */
    public static final String BANNER = "This is ExTeX, Version "
            + ExTeX.getVersion() + " (" + System.getProperty("java.version")
            + ")\n";

    /**
     * The command line interface.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(TeXTest.class);
    }

    /**
     * Create a new instance of properties pre-filled with the java.version.
     *
     * @return the new properties
     */
    private Properties makeProperties() {

        Properties properties = new Properties();
        properties.put("java.version", System.getProperty("java.version"));
        return properties;
    }

    /**
     * Run a test through the command line.
     *
     * @param args the array of command line arguments
     * @param properties the properties to use
     * @param expect the expected result on the error stream or
     *  <code>null</code>
     * @param exit the expected exit code
     *
     * @return the result on the error stream
     *
     * @throws InterpreterException in case of an interpreter error
     * @throws IOException in case of an io error
     */
    public static String runTest(final String[] args,
            final Properties properties, final String expect, final int exit)
            throws InterpreterException,
                IOException {

        TeX tex;
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        PrintStream stdout = System.out;
        PrintStream stderr = System.err;
        String result = null;
        try {
            System.setOut(new PrintStream(outBuffer));
            System.setErr(new PrintStream(errBuffer));

            tex = new TeX(properties, null);
            int status = tex.run(args);

            result = errBuffer.toString();
            if (expect != null) {
                assertEquals(expect, result);
            }
            assertEquals("", outBuffer.toString());
            assertEquals(exit, status);

        } finally {
            outBuffer.close();
            System.setOut(stdout);
            errBuffer.close();
            System.setErr(stderr);
        }
        return result;
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testUndefinedProperty() throws Exception {

        runTest(new String[]{"--undefined"}, System.getProperties(), BANNER
                + "Missing argument on command line", -1);
    }

    /**
     * <testcase>
     *  This test case validates that <tt>-version</tt> prints the version
     *  number and exists with code 0.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVersion() throws Exception {

        runTest(new String[]{"-version"}, makeProperties(), BANNER, 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHelp() throws Exception {

        String s = runTest(new String[]{"-help"}, makeProperties(), null, 0);
        assertTrue(s + "\ndoes  not match", s
                .startsWith("Usage: extex <options> file\n"));
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCopying() throws Exception {

        String s = runTest(new String[]{"-copying"}, makeProperties(), null, 0);
        assertTrue(s + "\ndoes  not match", s
                .startsWith("\nGNU LIBRARY GENERAL PUBLIC LICENSE"));
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCopyright() throws Exception {

        runTest(
                new String[]{"-copyright"},
                makeProperties(),
                "Copyright (C) 2003-"
                        + Calendar.getInstance().get(Calendar.YEAR)
                        + " DANTE e.V. (mailto:extex@dante.de).\n"
                        + "There is NO warranty.  Redistribution of this software is\n"
                        + "covered by the terms of the GNU Library General Public License.\n"
                        + "For more information about these matters, use the command line\n"
                        + "switch -copying.\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHelp2() throws Exception {

        String s = runTest(new String[]{"-prog=abc", "-help"},
                makeProperties(), null, 0);
        assertTrue(s + "\ndoes  not match", s
                .startsWith("Usage: abc <options> file\n"));
    }

    /**
     * <testcase>
     *  This test case validates that <tt>-ver</tt> prints the version
     *  number and exists with code 0.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVer() throws Exception {

        runTest(new String[]{"-ver"}, makeProperties(), BANNER, 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInteraction1() throws Exception {

        runTest(new String[]{"-interaction"}, makeProperties(), BANNER
                + "Missing argument on command line", -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInteraction2() throws Exception {

        runTest(new String[]{"-interaction=xxx"}, makeProperties(), BANNER
                + "Interaction xxx is unknown\n", -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInteraction3() throws Exception {

        runTest(new String[]{"-interaction="}, makeProperties(), BANNER
                + "Interaction  is unknown\n", -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInteraction4() throws Exception {

        System.setIn(new ByteArrayInputStream("".getBytes()));
        runTest(new String[]{"-ini", "-interaction=batchmode"},
                makeProperties(), "", -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInteraction5() throws Exception {

        System.setIn(new ByteArrayInputStream("".getBytes()));
        runTest(new String[]{"-ini", "-interaction=b"}, makeProperties(), "",
                -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInteraction6() throws Exception {

        System.setIn(new ByteArrayInputStream("".getBytes()));
        runTest(new String[]{"-ini", "-int=0"}, makeProperties(), "", -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testProgname1() throws Exception {

        runTest(new String[]{"-progname", "abc", "-version"}, makeProperties(),
                "This is ExTeX, Version " + ExTeX.getVersion() + " ("
                        + System.getProperty("java.version") + ")\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testProgname2() throws Exception {

        runTest(new String[]{"-prog", "abc", "-version"}, makeProperties(),
                "This is ExTeX, Version " + ExTeX.getVersion() + " ("
                        + System.getProperty("java.version") + ")\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testProgname3() throws Exception {

        runTest(new String[]{"-progname=abc", "-version"}, makeProperties(),
                "This is ExTeX, Version " + ExTeX.getVersion() + " ("
                        + System.getProperty("java.version") + ")\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testProgname4() throws Exception {

        runTest(new String[]{"-prog=abc", "-version"}, makeProperties(),
                "This is ExTeX, Version " + ExTeX.getVersion() + " ("
                        + System.getProperty("java.version") + ")\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPropertyName1() throws Exception {

        runTest(new String[]{"--extex.name", "abc", "-version"},
                makeProperties(), "This is abc, Version " + ExTeX.getVersion()
                        + " (" + System.getProperty("java.version") + ")\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPropertyName2() throws Exception {

        runTest(new String[]{"--extex.name=abc", "-version"}, makeProperties(),
                "This is abc, Version " + ExTeX.getVersion() + " ("
                        + System.getProperty("java.version") + ")\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        System.setIn(new ByteArrayInputStream("\\relax\n\\end\\n".getBytes()));
        runTest(new String[]{"-ini"}, new Properties(),
                "This is ExTeX, Version " + ExTeX.getVersion()
                        + " (ExTeX mode)\n"
                        + "**\n*\nNo pages of output.\nTranscript written on "
                        + (new File(".", "texput.log")).toString() + ".\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testNobanner1() throws Exception {

        System.setIn(new ByteArrayInputStream("\\relax\n\\end\\n".getBytes()));
        runTest(new String[]{"-ini", "--extex.nobanner=true"},
                new Properties(),
                "**\n*Transcript written on "
                        + (new File(".", "texput.log")).toString() + ".\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testExTeXBanner1() throws Exception {

        System.setIn(new ByteArrayInputStream("\\relax\n\\end\\n".getBytes()));
        runTest(new String[]{"--extex.banner=xyz", "-version"},
                new Properties(), "This is ExTeX, Version "
                        + ExTeX.getVersion() + " (xyz)\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFmt() throws Exception {

        System.setIn(new ByteArrayInputStream("\\relax\n\\end\n".getBytes()));
        runTest(
                new String[]{"&xyzzy"},
                new Properties(),
                "This is ExTeX, Version "
                        + ExTeX.getVersion()
                        + " (ExTeX mode)\n"
                        + "**\nSorry, I can't find the format `xyzzy.fmt'; will try `tex.fmt'.\n"
                        + "Sorry, I can't find the format `tex.fmt'!\n"
                        + "Transcript written on "
                        + (new File(".", "texput.log")).toString() + ".\n", -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testStarStar1() throws Exception {

        System.setIn(new ByteArrayInputStream("xyzzy\n".getBytes()));
        runTest(
                new String[]{"-ini"},
                new Properties(),
                "This is ExTeX, Version "
                        + ExTeX.getVersion()
                        + " (ExTeX mode)\n"
                        + "**\n! I can't find file `xyzzy.tex'.\n"
                        + "Please type another input file name: I can't find file `xyzzy'\n"
                        + "*\n" + "No pages of output.\n"
                        + "Transcript written on "
                        + (new File(".", "xyzzy.log")).toString() + ".\n", 0);
    }

    //TODO add more test cases
}
