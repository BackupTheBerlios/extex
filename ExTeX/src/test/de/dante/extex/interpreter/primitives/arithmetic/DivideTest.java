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
 * This is a test suite for the primitive <tt>\divide</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class DivideTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DivideTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public DivideTest(final String arg) {

        super(arg, "divide", "\\count1 1 ");
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> on a letter leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        assertFailure(//--- input code ---
                "\\divide a",
                //--- log message ---
                "You can\'t use `a\' after \\divide");
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> on a other token leads to an
     *  error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOther1() throws Exception {

        assertFailure(//--- input code ---
                "\\divide 12 ",
                //--- log message ---
                "You can\'t use `1\' after \\divide");
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> on a macro parameter token
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro1() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode`#=6 "
                + "\\divide #2 ",
                //--- log message ---
                "You can\'t use `#\' after \\divide");
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> on a non-dividable
     *  primitive (\\relax) leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRelax1() throws Exception {

        assertFailure(//--- input code ---
                "\\divide \\relax ",
                //--- log message ---
                "You can\'t use `\\relax\' after \\divide");
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> by 0 on a count register name
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount0() throws Exception {

        assertFailure(//--- input code ---
                "\\count1 16 "
                +"\\divide \\count1 0 "
                + "\\the\\count1 \\end",
                //--- log message ---
                "Arithmetic overflow");
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> on a count register name
     *  works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount1() throws Exception {

        assertSuccess(//--- input code ---
                "\\count1 16 "
                + "\\divide \\count1 8 "
                + "\\the\\count1 \\end",
                //--- output channel ---
                "2" + TERM);
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> on a count register name
     *  works with the global flag.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount2() throws Exception {

        assertSuccess(//--- input code ---
                "\\count1 16 "
                + "\\begingroup\\global\\divide \\count1 8 \\endgroup "
                + "\\the\\count1 \\end",
                //--- output channel ---
                "2" + TERM);
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> on a dimen register name
     *  works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimen1() throws Exception {

        assertSuccess(//--- input code ---
                "\\dimen1 8pt "
                + "\\divide \\dimen1 16 "
                + "\\the\\dimen1 \\end",
                //--- output channel ---
                "0.5pt" + TERM);
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> on a dimen register name
     *  works with the global flag.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimen2() throws Exception {

        assertSuccess(//--- input code ---
                "\\dimen1 8pt "
                + "\\begingroup\\global\\divide \\dimen1 16 \\endgroup "
                + "\\the\\dimen1 \\end",
                //--- output channel ---
                "0.5pt" + TERM);
    }

    /**
     * <testcase primitive="\divide">
     *  Test case checking that <tt>\divide</tt> by 0 on a dimen register name
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimen0() throws Exception {

        assertFailure(//--- input code ---
                "\\dimen1 8pt "
                +"\\divide \\dimen1 0 "
                + "\\the\\dimen1 \\end",
                //--- log message ---
                "Arithmetic overflow");
    }

}