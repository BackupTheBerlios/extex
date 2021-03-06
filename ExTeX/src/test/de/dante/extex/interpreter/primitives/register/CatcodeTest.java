/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\catcode</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class CatcodeTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(CatcodeTest.class);
    }

    /**
     * Constructor for CatcodeTest.
     *
     * @param arg the name
     */
    public CatcodeTest(final String arg) {

        super(arg, "catcode", "1=2 ");
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> throws an error on eof.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEOF1() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> throws an error on eof.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEOF2() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode 65 ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> throws an error on eof.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEOF3() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode 65 =",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> throws an error when an
     *  illegal catcode is passed in.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testIllegal1() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode 65 = -1",
                //--- log message ---
                "Invalid code (-1), should be in the range 0..15");
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> throws an error when an
     *  illegal catcode is passed in.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testIllegal2() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode 65 = 16",
                //--- log message ---
                "Invalid code (16), should be in the range 0..15");
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> is theable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`A=10 "
                + "\\the\\catcode`A\\end",
                //--- output channel ---
                "10" + TERM);
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> is an array value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`A=10 "
                + "\\catcode`B=12 "
                + "\\the\\catcode`A:\\the\\catcode`B\\end",
                //--- output channel ---
                "10:12" + TERM);
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> is count convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`A=10 "
                + "\\count1=\\catcode`A \\the\\count1\\end",
                //--- output channel ---
                "10" + TERM);
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> is interacts with groups.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test20() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`A=10 "
                + "\\begingroup\\catcode`A=12 \\endgroup"
                + "\\the\\catcode`A\\end",
                //--- output channel ---
                "10" + TERM);
    }

    /**
     * <testcase primitive="\catcode">
     *  Test case checking that <tt>\catcode</tt> is an assignment.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test30() throws Exception {

        assertSuccess(//--- input code ---
                "\\afterassignment abc"
                + "\\catcode`A=10 "
                + "\\the\\catcode`A\\end",
                //--- output channel ---
                "bca10" + TERM);
    }

}
