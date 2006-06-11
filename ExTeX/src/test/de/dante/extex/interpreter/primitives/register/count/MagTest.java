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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.test.NoFlagsButGlobalAndImmediatePrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\mag</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class MagTest extends NoFlagsButGlobalAndImmediatePrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(MagTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public MagTest(final String arg) {

        super(arg, "mag", "=999");
    }

    /**
     * <testcase primitive="\mag">
     *  Test case checking that <tt>\mag</tt> has initially the value 1000.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDefault1() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\mag \\end",
                //--- output channel ---
                "1000" + TERM);
    }

    /**
     * <testcase primitive="\mag">
     *  Test case checking that <tt>\mag</tt> can be assigned a positive value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssign1() throws Exception {

        assertSuccess(//--- input code ---
                "\\mag=1 \\the\\mag \\end",
                //--- output channel ---
                "1" + TERM);
    }

    /**
     * <testcase primitive="\mag">
     *  Test case checking that <tt>\mag</tt> can be assigned a positive value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssign2() throws Exception {

        assertSuccess(//--- input code ---
                "\\mag=10000 \\the\\mag \\end",
                //--- output channel ---
                "10000" + TERM);
    }

    /**
     * <testcase primitive="\mag">
     *  Test case checking that <tt>\mag</tt> can not be assigned 0.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssignZero() throws Exception {

        assertFailure(//--- input code ---
                "\\mag=0 \\the\\mag \\end",
                //--- error channel ---
                "Illegal magnification has been changed to 1000 (0)");
    }

    /**
     * <testcase primitive="\mag">
     *  Test case checking that <tt>\mag</tt> can not be assigned a negative
     *  value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssignNegative() throws Exception {

        assertFailure(//--- input code ---
                "\\mag=-1 \\the\\mag \\end",
                //--- error channel ---
                "Illegal magnification has been changed to 1000 (-1)");
    }

    /**
     * <testcase primitive="\mag">
     *  Test case checking that <tt>\mag</tt> can not be assigned twice with
     *  different values.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssignTwice() throws Exception {

        assertFailure(//--- input code ---
                "\\mag=1 \\mag=2 \\end",
                //--- error channel ---
                "Incompatible magnification (2);\n"
                        + "the previous value will be retained (1)");
    }

    /**
     * <testcase primitive="\mag">
     *  Test case checking that <tt>\mag</tt> can be assigned twice if the value
     *  is the same.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssignTwiceSame() throws Exception {

        assertSuccess(//--- input code ---
                "\\mag=2 \\mag=2 \\the\\mag \\end",
                //--- output channel ---
                "2" + TERM);
    }

//TODO implement the primitive specific test cases

}
