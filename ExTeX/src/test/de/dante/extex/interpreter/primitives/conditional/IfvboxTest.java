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
 * This is a test suite for the primitive <tt>\ifvbox</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class IfvboxTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfvboxTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfvboxTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\ifvbox">
     *  Test case checking that <tt>\ifvbox</tt> on an undefined box
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\ifvbox42 a\\else b\\fi",
                //--- log message ---
                "",
                //--- output channel ---
                "b\n\n");
    }

    /**
     * <testcase primitive="\ifvbox">
     *  Test case checking that <tt>\ifvbox</tt> on an undefined box
     *  selects nothing if the else branch is missing.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        runCode(//--- input code ---
                "x\\ifvbox42 a\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xx \n\n");
    }

    /**
     * <testcase primitive="\ifvbox">
     *  Test case checking that <tt>\ifvbox</tt> on an empty vbox
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1 "
                + "\\catcode`}=2 "
                + "\\setbox42\\vbox{}"
                + "\\ifvbox42 a\\else b\\fi",
                //--- log message ---
                "",
                //--- output channel ---
                "a\n\n");
    }

    /**
     * <testcase primitive="\ifvbox">
     *  Test case checking that <tt>\ifvbox</tt> on an empty hbox
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1 "
                + "\\catcode`}=2 "
                + "\\setbox42\\hbox{}"
                + "\\ifvbox42 a\\else b\\fi",
                //--- log message ---
                "",
                //--- output channel ---
                "b\n\n");
    }


}