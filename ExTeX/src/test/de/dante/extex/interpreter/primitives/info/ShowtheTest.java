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

package de.dante.extex.interpreter.primitives.info;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\showthe</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class ShowtheTest extends NoFlagsPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ShowtheTest(final String arg) {

        super(arg, "showthe", "\\count1 ", "", "> 0.\n");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> produces an error message
     *  when applied to <tt>\relax</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFailure1() throws Exception {

        assertFailure(//--- input code ---
                "\\showthe\\relax",
                //--- log message ---
                "You can't use `the control sequence \\relax' after \\showthe");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> produces an error message
     *  when applied to a digit.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFailure2() throws Exception {

        assertFailure(//--- input code ---
                "\\showthe 4",
                //--- log message ---
                "You can't use `the character 4' after \\showthe");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> works on a count register
     *  with value 0.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount1() throws Exception {

        assertFailure(//--- input code ---
                "\\showthe\\count1 \\end",
                //--- log message ---
                "> 0.\n");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> works on a count register
     *   with value 42.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount2() throws Exception {

        assertFailure(//--- input code ---
                "\\count1=42 "
                + "\\showthe\\count1 \\end",
                //--- log message ---
                "> 42.\n");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> works on a dimen register
     *  with value 0pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimen1() throws Exception {

        assertFailure(//--- input code ---
                "\\showthe\\dimen1 \\end",
                //--- log message ---
                "> 0.0pt.\n");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> works on a dimen register
     *  with value 42pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimen2() throws Exception {

        assertFailure(//--- input code ---
                "\\dimen1=42pt "
                + "\\showthe\\dimen1 \\end",
                //--- log message ---
                "> 42.0pt.\n");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> works on a glue register
     *  with zero length and no stretch or shrink.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlue1() throws Exception {

        assertFailure(//--- input code ---
                "\\showthe\\skip1 \\end",
                //--- log message ---
                "> 0.0pt.\n");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> works on a glue register
     *  with length and stretch and shrink components.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlue2() throws Exception {

        assertFailure(//--- input code ---
                "\\skip1= 12pt plus 3pt minus 4pt "
                + "\\showthe\\skip1 \\end",
                //--- log message ---
                "> 12.0pt plus 3.0pt minus 4.0pt.\n");
    }

    /**
     * <testcase primitive="\showthe">
     *  Test case checking that <tt>\showthe</tt> works on a undefined tokens
     *  register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToks1() throws Exception {

        assertFailure(//--- input code ---
                "\\showthe\\toks1 \\end",
                //--- log message ---
                "> .\n");
    }

    //TODO implement more primitive specific test cases

}
