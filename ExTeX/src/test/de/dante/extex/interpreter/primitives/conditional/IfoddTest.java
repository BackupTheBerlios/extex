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
 * This is a test suite for the primitive <tt>\ifodd</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class IfoddTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfoddTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfoddTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\ifodd">
     *  Test case checking that <tt>\ifodd</tt> with a constant 1
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\ifodd 1 a\\else b\\fi",
                //--- log message ---
                "",
                //--- output channel ---
                "a\n\n");
    }

    /**
     * <testcase primitive="\ifodd">
     *  Test case checking that <tt>\ifodd</tt> with a constant 1
     *  selects nothing if the else branch is missing.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        runCode(//--- input code ---
                "x\\ifodd 1 a\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax \n\n");
    }

    /**
     * <testcase primitive="\ifodd">
     *  Test case checking that <tt>\ifodd</tt> with a constant -1
     *  selects the then branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        runCode(//--- input code ---
                "\\ifodd -1 a\\else b\\fi",
                //--- log message ---
                "",
                //--- output channel ---
                "a\n\n");
    }

    /**
     * <testcase primitive="\ifodd">
     *  Test case checking that <tt>\ifodd</tt> with a constant 1
     *  selects nothing if the else branch is missing.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        runCode(//--- input code ---
                "x\\ifodd -1 a\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xax \n\n");
    }

    /**
     * <testcase primitive="\ifodd">
     *  Test case checking that <tt>\ifodd</tt> with a constant 0
     *  selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        runCode(//--- input code ---
                "\\ifodd 0 a\\else b\\fi",
                //--- log message ---
                "",
                //--- output channel ---
                "b\n\n");
    }

    /**
     * <testcase primitive="\ifodd">
     *  Test case checking that <tt>\ifodd</tt> with a constant 0
     *  selects nothing if the else branch is missing.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test6() throws Exception {

        runCode(//--- input code ---
                "x\\ifodd 0 a\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xx \n\n");
    }

}