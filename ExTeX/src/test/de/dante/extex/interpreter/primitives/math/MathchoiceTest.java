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

/**
 * This is a test suite for the primitive <tt>\mathchoice</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class MathchoiceTest extends AbstractMathTester {

    /**
     * Constructor for MathchoiceTest.
     *
     * @param arg the name
     */
    public MathchoiceTest(final String arg) {

        super(arg, "mathchoice", "{}{}{}{}");
    }

    /**
     * <testcase>
     *  Test case checking that <tt>\mathchoice</tt> selects the correct branch
     *  in display mode.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void testDisplay1() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                + "$$\\mathchoice{a}{b}{c}{d}$$ \\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that <tt>\mathchoice</tt> selects the correct branch
     *  in text mode.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void testText1() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                + "$\\mathchoice{a}{b}{c}{d}$ \\end",
                //--- output channel ---
                "b" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that <tt>\mathchoice</tt> selects the correct branch
     *  in explicit display mode.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void testDisplay2() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                + "$\\displaystyle\\mathchoice{a}{b}{c}{d}$ \\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that <tt>\mathchoice</tt> selects the correct branch
     *  in explicit script mode.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void testScript1() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                + "$\\scriptstyle\\mathchoice{a}{b}{c}{d}$ \\end",
                //--- output channel ---
                "c" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that <tt>\mathchoice</tt> selects the correct branch
     *  in explicit scriptscript mode.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void testScriptScript1() throws Exception {

        assertSuccess(//--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                + "$\\scriptscriptstyle\\mathchoice{a}{b}{c}{d}$ \\end",
                //--- output channel ---
                "d" + TERM);
    }

    //TODO implement more primitive specific test cases

}
