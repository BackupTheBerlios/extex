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
 * This is a test suite for the primitive <tt>\relax</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class IfcaseTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfcaseTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfcaseTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that the case 0 is hit if present.
     * @throws Exception in case of an error
     */
    public void testConst0() throws Exception {

        runCode(//--- input code ---
                "\\ifcase 0 a\\or b\\or c\\or d\\else e\\fi \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "a\n\n");
    }

    /**
     * Test case checking that the case 2 is hit if present.
     * @throws Exception in case of an error
     */
    public void testConst1() throws Exception {

        runCode(//--- input code ---
                "\\ifcase 1 a\\or b\\or c\\or d\\else e\\fi \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "b\n\n");
    }

    /**
     * Test case checking that the case 2 is hit if present.
     * @throws Exception in case of an error
     */
    public void testConst2() throws Exception {

        runCode(//--- input code ---
                "\\ifcase 2 a\\or b\\or c\\or d\\else e\\fi \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "c\n\n");
    }

    /**
     * Test case checking that the case 12 hits the else case if present.
     * @throws Exception in case of an error
     */
    public void testConst12() throws Exception {

        runCode(//--- input code ---
                "\\ifcase -1 a\\or b\\or c\\or d\\else e\\fi \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "e\n\n");
    }

    /**
     * Test case checking that the case 12 hits nothing.
     * @throws Exception in case of an error
     */
    public void testConst12b() throws Exception {

        runCode(//--- input code ---
                "\\ifcase -1 a\\or b\\or c\\or d\\fi \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "");
    }


    /**
     * Test case checking that the case -1 hits the else case if present.
     * @throws Exception in case of an error
     */
    public void testConst_1() throws Exception {

        runCode(//--- input code ---
                "\\ifcase -1 a\\or b\\or c\\or d\\else e\\fi \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "e\n\n");
    }

    /**
     * Test case checking that the case -1 hits the else case if present.
     * @throws Exception in case of an error
     */
    public void testConst_1b() throws Exception {

        runCode(//--- input code ---
                "\\ifcase -1 a\\or b\\or c\\or d\\fi \\end",
                //--- log message ---
                "",
                //--- output channel ---
                "");
    }

}