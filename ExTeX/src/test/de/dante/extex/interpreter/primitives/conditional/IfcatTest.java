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
 * This is a test suite for the primitive <tt>\ifcat</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class IfcatTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfcatTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfcatTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\ifcat">
     *  Test case checking that <tt>\ifcat</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcat AA a\\else b\\fi \\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\ifcat">
     *  Test case checking that <tt>\ifcat</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter2() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcat AB a\\else b\\fi \\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\ifcat">
     *  Test case checking that <tt>\ifcat</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter3() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcat A1 a\\else b\\fi \\end",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\ifcat">
     *  Test case checking that <tt>\ifcat</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter4() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcat A\\relax a\\else b\\fi \\end",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\ifcat">
     *  Test case checking that <tt>\ifcat</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCs1() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcat \\relax\\relax a\\else b\\fi \\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\ifcat">
     *  Test case checking that <tt>\ifcat</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCs2() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifcat \\abc\\relax a\\else b\\fi \\end",
                //--- output channel ---
                "a" + TERM);
    }

    //TODO implement the primitive specific test cases

}