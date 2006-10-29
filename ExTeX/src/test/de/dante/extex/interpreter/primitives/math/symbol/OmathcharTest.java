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

package de.dante.extex.interpreter.primitives.math.symbol;

import de.dante.extex.interpreter.primitives.math.AbstractMathTester;

/**
 * This is a test suite for the primitive <tt>\omathchar</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class OmathcharTest extends AbstractMathTester {

    /**
     * Constructor for MathcharTest.
     *
     * @param arg the name
     */
    public OmathcharTest(final String arg) {

        super(arg, "omathchar", "123 ");
        setConfig("omega");
    }

    /**
     * <testcase primitive="\omathchar">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_MATH + "$\\omathchar-1 " //
                        + "\\alpha\\end",
                //--- output message ---
                "Bad mathchar (-1)");
    }

    /**
     * <testcase primitive="\omathchar">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr2() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_MATH + "$\\omathchar\"8000001 ",
                //--- output message ---
                "Bad mathchar (134217729)");
    }

    /**
     * <testcase primitive="\omathchar">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                        + "$a\\omathchar\"010B b$\\end",
                //--- output message ---
                "a\013b" + TERM);
    }

//    /**
//     * <testcase primitive="\omathchar">
//     *  Test case checking that ...
//     * </testcase>
//     *
//     * @throws Exception in case of an error
//     */
//    public void testDM1() throws Exception {
//
//        assertSuccess(
//        //--- input code ---
//                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
//                        + "$$a\\omathchar\"010B b$$\\end",
//                //--- output message ---
//                "a\013b" + TERM);
//    }
//
//    /**
//     * <testcase primitive="\omathchar">
//     *  Test case checking that ...
//     * </testcase>
//     *
//     * @throws Exception in case of an error
//     */
//    public void testDMExt1() throws Exception {
//
//        assertSuccess(
//        //--- input code ---
//                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_BRACES
//                        + DEFINE_MATH + "$$\\omathchar{ord 1 `A}$$\\end ",
//                //--- output message ---
//                "A" + TERM);
//    }

    /**
     * <testcase primitive="\omathchar">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testExt1() throws Exception {

        assertSuccess(
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_BRACES
                        + DEFINE_MATH + "$\\omathchar{ord 1 `A}$\\end ",
                //--- output message ---
                "A" + TERM);
    }

}
