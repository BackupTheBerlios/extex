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

package de.dante.extex.interpreter.primitives.arithmetic;

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\multiply</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class MultiplyTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(MultiplyTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public MultiplyTest(final String arg) {

        super(arg, "multiply", "\\count1 1 ");
    }

    /**
     * <testcase primitive="\multiply">
     *  Test case checking that <tt>\multiply</tt> on a letter leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        runFailureCode(//--- input code ---
                "\\multiply a",
                //--- log message ---
                "You can\'t use `a\' after \\multiply");
    }

    /**
     * <testcase primitive="\multiply">
     *  Test case checking that <tt>\multiply</tt> on a other token leads to an
     *  error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOther1() throws Exception {

        runFailureCode(//--- input code ---
                "\\multiply 12 ",
                //--- log message ---
                "You can\'t use `1\' after \\multiply");
    }

    /**
     * <testcase primitive="\multiply">
     *  Test case checking that <tt>\multiply</tt> on a macro parameter token
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro1() throws Exception {

        runFailureCode(//--- input code ---
                "\\catcode`#=6 "
                + "\\multiply #2 ",
                //--- log message ---
                "You can\'t use `#\' after \\multiply");
    }

    /**
     * <testcase primitive="\multiply">
     *  Test case checking that <tt>\multiply</tt> on a non-multipliable
     *  primitive (\\relax) leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRelax1() throws Exception {

        runFailureCode(//--- input code ---
                "\\multiply \\relax ",
                //--- log message ---
                "You can\'t use `\\relax\' after \\multiply");
    }

    /**
     * <testcase primitive="\multiply">
     *  Test case checking that <tt>\multiply</tt> on a count register name
     *  works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount1() throws Exception {

        runCode(//--- input code ---
                "\\count1 8 "
                +"\\multiply \\count1 16 "
                + "\\the\\count1 \\end",
                //--- output channel ---
                "128" + TERM);
    }

    /**
     * <testcase primitive="\multiply">
     *  Test case checking that <tt>\multiply</tt> on a count register name
     *  works with the global flag.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount2() throws Exception {

        runCode(//--- input code ---
                "\\count1 8 "
                +"\\begingroup\\global\\multiply \\count1 16 \\endgroup "
                + "\\the\\count1 \\end",
                //--- output channel ---
                "128" + TERM);
    }

    /**
     * <testcase primitive="\multiply">
     *  Test case checking that <tt>\multiply</tt> on a dimen register name
     *  works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimen1() throws Exception {

        runCode(//--- input code ---
                "\\dimen1 8pt "
                + "\\multiply \\dimen1 16 "
                + "\\the\\dimen1 \\end",
                //--- output channel ---
                "128.0pt" + TERM);
    }

    /**
     * <testcase primitive="\multiply">
     *  Test case checking that <tt>\multiply</tt> on a dimen register name
     *  works with the global flag.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimen2() throws Exception {

        runCode(//--- input code ---
                "\\dimen1 8pt "
                + "\\begingroup\\global\\multiply \\dimen1 16 \\endgroup "
                + "\\the\\dimen1 \\end",
                //--- output channel ---
                "128.0pt" + TERM);
    }

}
