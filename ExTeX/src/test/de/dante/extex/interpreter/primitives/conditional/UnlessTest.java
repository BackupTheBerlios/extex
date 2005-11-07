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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\u005cnless</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class UnlessTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(UnlessTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public UnlessTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(
                //--- input code ---
                "\\unless\\relax",
                //--- error channel ---
                "You can't use `\\relax' after \\unless");
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr2() throws Exception {

        assertFailure(
                //--- input code ---
                "\\unless\\ifcase",
                //--- error channel ---
                "You can't use `\\ifcase' after \\unless");
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
                //--- input code ---
                "\\unless\\ifnum1=1 a\\else b\\fi \\end",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(
                //--- input code ---
                "\\unless\\ifnum1=3 a\\else b\\fi \\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA1() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\if AA a\\else b\\fi\\end",
                //--- log message ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA2() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifcat AA a\\else b\\fi\\end",
                //--- log message ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA3() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifnum 1=2 a\\else b\\fi\\end",
                //--- log message ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA4() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifdim 1sp=2sp a\\else b\\fi\\end",
                //--- log message ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA5() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifodd2 a\\else b\\fi\\end",
                //--- log message ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA6() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifvmode a\\else b\\fi\\end",
                //--- log message ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA8() throws Exception {

        assertSuccess(
                //--- input code ---
                DEFINE_CATCODES
                        + "$\\unless\\ifmmode a\\else b\\fi$\\end",
                //--- log message ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA10() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifvoid0 a\\else b\\fi\\end",
                //--- log message ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA11() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifhbox1 a\\else b\\fi\\end",
                //--- log message ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA12() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifvbox1 a\\else b\\fi\\end",
                //--- log message ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA13() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifx AA a\\else b\\fi\\end",
                //--- log message ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA14() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\ifeof1 a\\else b\\fi\\end",
                //--- log message ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA15() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\iftrue a\\else b\\fi\\end",
                //--- log message ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA16() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\iffalse a\\else b\\fi\\end",
                //--- log message ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA19() throws Exception {

        assertSuccess(
                //--- input code ---
                "\\unless\\ifcsname aaa\\endcsname a\\else b\\fi\\end",
                //--- log message ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\u005cnless">
     *  Test case checking that <tt>\u005cnless</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA20() throws Exception {

        assertSuccess(//--- input code ---
                "\\unless\\iffontchar\\nullfont`\\a a\\else b\\fi\\end",
                //--- log message ---
                "a" + TERM);
    }

}