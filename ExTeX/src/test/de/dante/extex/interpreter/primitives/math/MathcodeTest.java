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

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\mathcode</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class MathcodeTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Constructor for MathcodeTest.
     *
     * @param arg the name
     */
    public MathcodeTest(final String arg) {

        super(arg, "mathcode", "12 32 ");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
                //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                        + "\\mathcode`. \"0201" + "$a.b$\\end",
                //--- output message ---
                "a?b" + TERM); //TODO gene: check for the correct result
    }

    //TODO implement more primitive specific test cases

}
