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
 * This is a test suite for the primitive <tt>\iffalse</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class IffalseTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IffalseTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IffalseTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\iffalse">
     *  Test case checking that <tt>\iffalse</tt> selects the else branch.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\iffalse a\\else b\\fi",
                //--- log message ---
                "",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\iffalse">
     *  Test case checking that <tt>\iffalse</tt> selects nothing if the else
     *  branch is missing.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        runCode(//--- input code ---
                "x\\iffalse a\\fi x",
                //--- log message ---
                "",
                //--- output channel ---
                "xx" + TERM);
    }

}