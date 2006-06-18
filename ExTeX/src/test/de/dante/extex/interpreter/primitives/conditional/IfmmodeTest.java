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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.interpreter.primitives.math.AbstractMathTester;
import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\ifmmode</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class IfmmodeTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfmmodeTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfmmodeTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\ifmmode">
     *  Test case checking that <tt>\ifmmode</tt> is false in vertical mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifmmode a\\else b\\fi\\end",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\ifmmode">
     *  Test case checking that <tt>\ifmmode</tt> is false in horizontal mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "x\\ifmmode a\\else b\\fi\\end",
                //--- output channel ---
                "xb" + TERM);
    }

    /**
     * <testcase primitive="\ifmmode">
     *  Test case checking that <tt>\ifmmode</tt> is false in displaymath mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                + "$$\\ifmmode a\\else b\\fi$$\\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\ifmmode">
     *  Test case checking that <tt>\ifmmode</tt> is true in inner vertical mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES  + "\\vbox{\\ifmmode a\\else b\\fi}\\end",
                //--- output channel ---
                "b\n\n" + TERM);
    }

    /**
     * <testcase primitive="\ifmmode">
     *  Test case checking that <tt>\ifmmode</tt> is true in restricted
     *  horizontal mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES  + "\\hbox{\\ifmmode a\\else b\\fi}\\end",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase primitive="\ifmmode">
     *  Test case checking that <tt>\ifmmode</tt> is true in math mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test6() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\ifmmode a\\else b\\fi$\\end",
                //--- output channel ---
                "a" + TERM);
    }

}
