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

package de.dante.extex.interpreter.primitives.register;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\afterassignment</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class AfterassignmentTest extends ExTeXLauncher {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(AfterassignmentTest.class);
    }

    /**
     * Constructor for MathafterassignmentTest.
     *
     * @param arg the name
     */
    public AfterassignmentTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\afterassignment">
     *  Test case checking that <tt>\afterassignment</tt> throws an error on eof.
     * <testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEOF1() throws Exception {

        runCode(//--- input code ---
                "\\afterassignment",
                //--- log message ---
                "Unexpected end of file while processing \\afterassignment",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\afterassignment">
     *  Test case checking that <tt>\afterassignment</tt> can take a letter.
     * <testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        assertSuccess(//--- input code ---
                "\\afterassignment b a\\count0=123 \\end",
                //--- output channel ---
                "ab" + TERM);
    }

    /**
     * <testcase primitive="\afterassignment">
     *  Test case checking that <tt>\afterassignment</tt> can take a digit.
     * <testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOther1() throws Exception {

        assertSuccess(//--- input code ---
                "\\afterassignment 1 a\\count0=123 \\end",
                //--- output channel ---
                "a1" + TERM);
    }

    /**
     * <testcase primitive="\afterassignment">
     *  Test case checking that <tt>\afterassignment</tt> can take only one
     *  token.
     * <testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDouble1() throws Exception {

        assertSuccess(//--- input code ---
                "\\afterassignment x\\afterassignment b a\\count0=123 \\end",
                //--- output channel ---
                "ab" + TERM);
    }

    /**
     * <testcase primitive="\afterassignment">
     *  Test case checking that <tt>\afterassignment</tt> does not interact with
     *  groups.
     * <testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGroup1() throws Exception {

        assertSuccess(//--- input code ---
                "\\afterassignment x\\begingroup\\afterassignment ba\\endgroup"
                        + "\\count0=12 \\end",
                //--- output channel ---
                "ab" + TERM);
    }

}
