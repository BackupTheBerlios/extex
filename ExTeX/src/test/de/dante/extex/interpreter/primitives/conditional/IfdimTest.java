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
 * This is a test suite for the primitive <tt>\ifdim</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class IfdimTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfdimTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfdimTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|0pt<1pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess1() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 0pt<1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|1pt<1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess2() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 1pt<1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|2pt<1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess3() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 2pt<1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|0pt=1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals1() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 0pt=1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|1pt=1pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals2() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 1pt=1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|2pt=1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals3() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 2pt=1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|0pt>1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater1() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 0pt>1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|1pt>1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater2() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 1pt>1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|2pt>1pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater3() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 2pt>1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|0.pt<1.pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess11() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 0.pt<1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|1.pt<1.pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess12() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 1.pt<1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|2.pt<1.pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess13() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 2.pt<1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|0.pt=1.pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals11() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 0.pt=1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|1.pt=1.pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals12() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 1.pt=1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|2.pt=1.pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals13() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 2.pt=1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|0.pt>1.pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater11() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 0.pt>1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|1.pt>1.pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater12() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 1.pt>1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|2.pt>1.pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater13() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim 2.pt>1.pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.0pt<.1pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess21() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .0pt<.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.1pt<.1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess22() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .1pt<.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.2pt<.1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLess23() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .2pt<.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.0pt=.1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals21() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .0pt=.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.1pt=.1pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals22() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .1pt=.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.2pt=.1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEquals23() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .2pt=.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.0pt>.1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater21() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .0pt>.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.1pt>.1pt|
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater22() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .1pt>.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xbx\n\n");
    }

    /**
     * <testcase primitive="\ifdim">
     *  Test case checking that <tt>\ifdim</tt> on \verb|.2pt>.1pt|
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGreater23() throws Exception {

        runCode(//--- input code ---
                "x\\ifdim .2pt>.1pt a\\else b\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax\n\n");
    }

}
