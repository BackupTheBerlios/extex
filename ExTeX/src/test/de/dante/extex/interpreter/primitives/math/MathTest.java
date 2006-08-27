/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.math;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for math.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class MathTest extends ExTeXLauncher {

    /**
     * Constructor for MathaccentTest.
     *
     * @param arg the name
     */
    public MathTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase>
     *  Test case checking that missing fonts leads to an error.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void _testMathError1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_MATH + "$a$ \\end",
                //--- output channel ---
                "Math formula deleted: Insufficient symbol fonts");
    }

    /**
     * <testcase>
     *  Test case checking that a simple character can be typeset in math mode.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void _testMath1() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                        + "$a$ \\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that a simple character can be typeset in math mode.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void testMath2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hsize=100pt" +
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                        + "$a_b$ \\end",
                //--- output channel ---
                "\\vbox(4.8611pt+0.0pt)x8.80255pt\n"
                        + ".\\hbox(4.8611pt+0.0pt)x8.80255pt\n" //
                        + "..a\n" //
                        + "..\\hbox(4.8611pt+0.0pt)x3.51666pt\n" //
                        + "...b\n");
    }

}
