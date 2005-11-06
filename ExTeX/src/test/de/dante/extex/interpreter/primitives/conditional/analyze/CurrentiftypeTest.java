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

package de.dante.extex.interpreter.primitives.conditional.analyze;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\currentiftype</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class CurrentiftypeTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(CurrentiftypeTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public CurrentiftypeTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(//--- input code ---
                "\\currentiftype ",
                //--- log message ---
                "You can't use `\\currentiftype' in vertical mode");
    }
    
    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\currentiftype \\end",
                //--- log message ---
                "0" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcat AA\\the\\currentiftype \\fi\\end",
                //--- log message ---
                "2" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifnum 1=1 \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "3" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifdim 1sp=1sp \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "4" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifodd3 \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "5" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test6() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifvmode \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "6" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test8() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "$\\ifmmode \\the\\currentiftype \\fi$\\end",
                //--- log message ---
                "8" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifvoid0 \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "10" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test11() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifhbox1\\else \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "11" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test12() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifvbox1\\else \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "12" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test14() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifeof1 \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "14" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test15() throws Exception {

        assertSuccess(//--- input code ---
                "\\iftrue\\the\\currentiftype \\fi\\end",
                //--- log message ---
                "15" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test16() throws Exception {

        assertSuccess(//--- input code ---
                "\\iffalse\\else\\the\\currentiftype \\fi\\end",
                //--- log message ---
                "16" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test17() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcase0 \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "17" + TERM);
    }

    /**
     * <testcase primitive="\currentiftype">
     *  Test case checking that <tt>\currentiftype</tt>...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test19() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcsname relax\\endcsname \\the\\currentiftype \\fi\\end",
                //--- log message ---
                "19" + TERM);
    }

}