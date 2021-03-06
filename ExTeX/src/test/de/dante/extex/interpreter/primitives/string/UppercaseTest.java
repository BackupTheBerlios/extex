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

package de.dante.extex.interpreter.primitives.string;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>&#x5c;uppercase</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class UppercaseTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(UppercaseTest.class);
    }

    /**
     * Constructor for UppercaseTest.
     *
     * @param arg the name
     */
    public UppercaseTest(final String arg) {

        super(arg, "uppercase", "{abc}");
    }

    /**
     * <testcase primitive="&#x5c;uppercase">
     *  Test case checking that <tt>&#x5c;uppercase</tt> throws an error on eof.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEOF1() throws Exception {

        assertFailure(//--- input code ---
                "\\uppercase",
                //--- log message ---
                "Unexpected end of file while processing \\uppercase");
    }

    /**
     * <testcase primitive="&#x5c;uppercase">
     *  Test case checking that <tt>&#x5c;uppercase</tt> is invariant on
     *  uppercase letters.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\uppercase{ABC}\\end",
                //--- output channel ---
                "ABC" + TERM);
    }

    /**
     * <testcase primitive="&#x5c;uppercase">
     *  Test case checking that <tt>&#x5c;uppercase</tt> translates
     *  lowercase letters.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\uppercase{abc}\\end",
                //--- output channel ---
                "ABC" + TERM);
    }

    /**
     * <testcase primitive="&#x5c;uppercase">
     *  Test case checking that <tt>&#x5c;uppercase</tt> translates
     *  mixed letters.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\uppercase{aBc}\\end",
                //--- output channel ---
                "ABC" + TERM);
    }

    /**
     * <testcase primitive="&#x5c;uppercase">
     *  Test case checking that <tt>&#x5c;uppercase</tt> respects uccode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\uccode`b=`1 " + "\\uppercase{abc}\\end",
                //--- output channel ---
                "A1C" + TERM);
    }

}
