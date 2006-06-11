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

package de.dante.extex.interpreter.primitives.color;

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\colordef</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ColordefTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ColordefTest.class);
    }

    /**
     * @see de.dante.test.ExTeXLauncher#getConfig()
     */
    protected String getConfig() {

        return "colorextex";
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ColordefTest(final String arg) {

        super(arg, "colordef", "\\x{.1 .2 .3}", "");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef rgb",
                //--- log message ---
                "Missing control sequence inserted");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError2() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x undef ",
                //--- log message ---
                "Missing left brace for color value");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError3() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x {1} ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError4() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x {1 2 3} ",
                //--- log message ---
                "Illegal color value");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x {.1 .2 .3} " +
                "\\showthe\\x \\end",
                //--- log message ---
                "> rgb {0.09999237 0.19998474 0.29999238}.\n");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test20() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x {.1 .2 .3} " +
                "\\colordef\\y\\x" +
                "\\showthe\\y \\end",
                //--- log message ---
                "> rgb {0.09999237 0.19998474 0.29999238}.\n");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test30() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x {1 1 1} " +
                "\\showthe\\x \\end",
                //--- log message ---
                "> rgb {1.0 1.0 1.0}.\n");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test31() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x rgb {1 1 1} " +
                "\\showthe\\x \\end",
                //--- log message ---
                "> rgb {1.0 1.0 1.0}.\n");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test32() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x gray {1} " +
                "\\showthe\\x \\end",
                //--- log message ---
                "> gray {1.0}.\n");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test33() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x hsv {1 1 1} " +
                "\\showthe\\x \\end",
                //--- log message ---
                "> hsv {1.0 1.0 1.0}.\n");
    }

    /**
     * <testcase primitive="colordef">
     *  Test case checking that <tt>\colordef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test34() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\colordef\\x cmyk {1 1 1 1} " +
                "\\showthe\\x \\end",
                //--- log message ---
                "> cmyk {1.0 1.0 1.0 1.0}.\n");
    }

}
