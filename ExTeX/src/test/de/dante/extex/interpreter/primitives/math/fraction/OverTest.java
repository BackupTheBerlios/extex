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

package de.dante.extex.interpreter.primitives.math.fraction;

import de.dante.extex.interpreter.primitives.math.AbstractMathTester;

/**
 * This is a test suite for the primitive <tt>\over</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class OverTest extends AbstractMathTester {

    /**
     * Constructor for OverTest.
     *
     * @param arg the name
     */
    public OverTest(final String arg) {

        super(arg, "over", " b$", "a ");
    }

    /**
     * <testcase primitive="\over">
     *  ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH + "$a\\over b \\over c$ \\end",
                //--- error stream ---
                "Ambiguous; you need another { and }");
    }

    /**
     * <testcase primitive="\over">
     *  ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathMode1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$a\\over b$ \\end",
                //--- output stream ---
                "???"); //TODO gene: check
    }

    /**
     * Test case checking that \mathaccent needs the math mode.
     * @throws Exception in case of an error
     */
    public void testMath2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH + DEFINE_BRACES
                + "${a \\over b}$ \\end",
                //--- output channel ---
                "???"); //TODO gene: check
    }

}
