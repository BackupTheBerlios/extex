/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\integer</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class IntegerTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IntegerTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IntegerTest(final String arg) {

        super(arg, "integer", "\\x=123", "123");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertOutput(//--- input code ---
                "\\integer\\x=123 \\showthe\\x" + "\\end",
                //--- output channel ---
                "> 123.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertOutput(//--- input code ---
                "\\integer\\x=123 \\count0=\\x \\showthe\\count0" + "\\end",
                //--- output channel ---
                "> 123.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssign1() throws Exception {

        assertOutput(//--- input code ---
                "\\integer\\x=123 \\x=456 \\showthe\\x" + "\\end",
                //--- output channel ---
                "> 456.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssign2() throws Exception {

        assertOutput(//--- input code ---
                "\\count0=987 \\integer\\x=123 \\x=\\count0 \\showthe\\x"
                        + "\\end",
                //--- output channel ---
                "> 987.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAdvance1() throws Exception {

        assertOutput(//--- input code ---
                "\\integer\\x=123 \\advance\\x5 \\showthe\\x" + "\\end",
                //--- output channel ---
                "> 128.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMultiply1() throws Exception {

        assertOutput(//--- input code ---
                "\\integer\\x=123 \\multiply\\x2 \\showthe\\x" + "\\end",
                //--- output channel ---
                "> 246.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDivide1() throws Exception {

        assertOutput(//--- input code ---
                "\\integer\\x=128 \\divide\\x 2 \\showthe\\x" + "\\end",
                //--- output channel ---
                "> 64.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertOutput(//--- input code ---
                DEFINE_BRACES + "{\\integer\\x=123 {\\x=987} \\showthe\\x}"
                        + "\\end",
                //--- output channel ---
                "> 987.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test11() throws Exception {

        assertOutput(//--- input code ---
                DEFINE_BRACES + "{\\global\\integer\\x=123 }\\showthe\\x"
                        + "\\end",
                //--- output channel ---
                "> 123.\n", "");
    }

    /**
     * <testcase primitive="\integer">
     *  Test case showing that the ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test12() throws Exception {

        assertOutput(//--- input code ---
                DEFINE_BRACES + "{\\integer\\x=123 }\\showthe\\x" + "\\end",
                //--- output channel ---
                "You can't use `the control sequence \\x' after \\showthe", "");
    }

    //TODO implement the primitive specific test cases

}
