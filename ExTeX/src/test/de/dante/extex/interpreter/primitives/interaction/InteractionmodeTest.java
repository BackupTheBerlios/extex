/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.interaction;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\interactionmode</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class InteractionmodeTest extends ExTeXLauncher {
    
    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(InteractionmodeTest.class);
    }

    /**
     * Constructor for RelaxTest.
     *
     * @param arg the name
     */
    public InteractionmodeTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\interactionmode">
     *  Test case checking that batch mode is reported as 0.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        runCode(//--- input code ---
                "\\batchmode\\count0=\\interactionmode"
                + " \\the\\count0 \\end",
                //--- output channel ---
                "0" + TERM);
    }

    /**
     * <testcase primitive="\interactionmode">
     *  Test case checking that non-stop mode is reported as 1.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\nonstopmode\\count0=\\interactionmode"
                + " \\the\\count0 \\end",
                //--- output channel ---
                "1" + TERM);
    }

    /**
     * <testcase primitive="\interactionmode">
     *  Test case checking that scroll mode is reported as 2.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        runCode(//--- input code ---
                "\\scrollmode\\count0=\\interactionmode"
                + " \\the\\count0 \\end",
                //--- output channel ---
                "2" + TERM);
    }

    /**
     * <testcase primitive="\interactionmode">
     *  Test case checking that error stop mode is reported as 3.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        runCode(//--- input code ---
                "\\errorstopmode\\count0=\\interactionmode"
                + " \\the\\count0 \\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase primitive="\interactionmode">
     *  Test case checking that <tt>\interactionmode</tt> can be used to set the
     *  interaction mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSet1() throws Exception {

        runCode(//--- input code ---
                "\\interactionmode=3 \\count0=\\interactionmode"
                + " \\the\\count0 \\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase primitive="\interactionmode">
     *  Test case checking that <tt>\interactionmode</tt> can be used to set the
     *  interaction mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSet2() throws Exception {

        runCode(//--- input code ---
                "\\interactionmode 3 \\count0=\\interactionmode"
                + " \\the\\count0 \\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase primitive="\interactionmode">
     *  Test case checking that <tt>\interactionmode</tt> does not accept 4.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSetErr1() throws Exception {

        runFailureCode(//--- input code ---
                "\\interactionmode=4 ",
                //--- log message ---
                "Interaction 4 is unknown\n");
    }

    /**
     * <testcase primitive="\interactionmode">
     *  Test case checking that <tt>\interactionmode</tt> does not accept -1.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSetErr2() throws Exception {

        runFailureCode(//--- input code ---
                "\\interactionmode=-1 ",
                //--- log message ---
                "Missing number, treated as zero");
    }

}
