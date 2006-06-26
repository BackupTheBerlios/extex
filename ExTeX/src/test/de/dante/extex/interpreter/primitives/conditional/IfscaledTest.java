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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\ifscaled</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class IfscaledTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfscaledTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfscaledTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|0<1|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess1() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 0<1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|1<1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess2() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 1<1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|2<1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess3() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 2<1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|0=1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals1() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 0=1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|1=1|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals2() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 1=1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|2=1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals3() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 2=1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|0>1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater1() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 0>1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|1>1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater2() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 1>1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|2>1|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater3() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 2>1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|0.<1.|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess11() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 0.<1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|1.<1.|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess12() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 1.<1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|2.<1.|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess13() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 2.<1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|0.=1.|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals11() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 0.=1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|1.=1.|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals12() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 1.=1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|2.=1.|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals13() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 2.=1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|0.>1.|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater11() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 0.>1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|1.>1.|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater12() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 1.>1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|2.>1.|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater13() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled 2.>1. a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.0<.1|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess21() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .0<.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.1<.1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess22() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .1<.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.2<.1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess23() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .2<.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.0=.1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals21() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .0=.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.1=.1|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals22() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .1=.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.2=.1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals23() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .2=.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.0>.1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater21() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .0>.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.1>.1|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater22() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .1>.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xbx" + TERM);
    }

    /**
     * <testcase primitive="\ifscaled">
     *  Test case checking that <tt>\ifscaled</tt> on \verb|.2>.1|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater23() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifscaled .2>.1 a\\else b\\fi x\\end",
                //--- output channel ---
                "xax" + TERM);
    }

}
