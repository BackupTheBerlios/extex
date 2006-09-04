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
import java.util.Properties;

import junit.framework.TestCase;
import de.dante.extex.ExTeX;
import de.dante.extex.interpreter.exception.InterpreterException;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class TeXTest extends TestCase {

    /**
     * The field <tt>BANNER</tt> contains the ...
     */
    public static String BANNER = "This is ExTeX, Version "
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
     * TODO gene: missing JavaDoc
     *
     * @param args the array of command line arguments
     * @param properties the properties to use
     * @param result the expected result
     * @param exit the expected exit code
     *
     * @throws InterpreterException in case of an interpreter error
     * @throws IOException in case of an io error
     */
    public static void runTest(final String[] args,
            final Properties properties, final String result, final int exit)
            throws InterpreterException,
                IOException {

        TeX tex;
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        PrintStream sout = System.out;
        PrintStream serr = System.err;
        try {
            System.setOut(new PrintStream(outBuffer));
            System.setErr(new PrintStream(errBuffer));

            tex = new TeX(properties, null);
            int status = tex.run(args);

            assertEquals("", outBuffer.toString());
            assertEquals(result, errBuffer.toString());
            assertEquals(exit, status);

        } finally {
            outBuffer.close();
            System.setOut(sout);
            errBuffer.close();
            System.setErr(serr);
        }
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test__undefined() throws Exception {

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
    public void test_version() throws Exception {

        runTest(new String[]{"-version"}, makeProperties(), BANNER, 0);
    }

    /**
     * <testcase>
     *  This test case validates that <tt>-ver</tt> prints the version
     *  number and exists with code 0.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test_ver() throws Exception {

        runTest(new String[]{"-ver"}, makeProperties(), BANNER, 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test_interaction1() throws Exception {

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
    public void test_interaction2() throws Exception {

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
    public void test_interaction3() throws Exception {

        runTest(new String[]{"-interaction="}, makeProperties(), BANNER
                + "Interaction  is unknown\n", -1);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @return
     */
    private Properties makeProperties() {

        Properties properties = new Properties();
        properties.put("java.version", System.getProperty("java.version"));
        return properties;
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test_interaction4() throws Exception {

        System.setIn(new ByteArrayInputStream("".getBytes()));
        Properties properties = makeProperties();
        runTest(new String[]{"-ini", "-interaction=batchmode"}, properties, "",
                -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test_interaction5() throws Exception {

        System.setIn(new ByteArrayInputStream("".getBytes()));
        Properties properties = makeProperties();
        runTest(new String[]{"-ini", "-interaction=b"}, properties, "", -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test_interaction6() throws Exception {

        System.setIn(new ByteArrayInputStream("".getBytes()));
        Properties properties = makeProperties();
        runTest(new String[]{"-ini", "-int=0"}, properties, "", -1);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test_progname1() throws Exception {

        Properties properties = makeProperties();
        runTest(new String[]{"-progname", "abc", "-version"}, properties,
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
    public void test_progname2() throws Exception {

        Properties properties = makeProperties();
        runTest(new String[]{"-prog", "abc", "-version"}, properties,
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
    public void test_progname3() throws Exception {

        Properties properties = makeProperties();
        runTest(new String[]{"-progname=abc", "-version"}, properties,
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
    public void test_progname4() throws Exception {

        Properties properties = makeProperties();
        runTest(new String[]{"-prog=abc", "-version"}, properties,
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
    public void test__name1() throws Exception {

        Properties properties = makeProperties();
        runTest(new String[]{"--extex.name", "abc", "-version"}, properties,
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
    public void test__name2() throws Exception {

        Properties properties = makeProperties();
        runTest(new String[]{"--extex.name=abc", "-version"}, properties,
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
                "**\n*\nNo pages of output.\nTranscript written on "
                        + (new File(".", "texput.log")).toString() + ".\n", 0);
    }

    /**
     * <testcase>
     *  This test case validates that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBanner1() throws Exception {

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
                //                        + "Transcript written on "
                //                        + (new File(".", "xyzzy.log")).toString() + ".\n"
                , 0);
    }

    //TODO add more test cases
}
