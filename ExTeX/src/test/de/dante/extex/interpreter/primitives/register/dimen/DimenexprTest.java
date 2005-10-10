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

package de.dante.extex.interpreter.primitives.register.dimen;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\dimenexpr</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class DimenexprTest extends ExTeXLauncher {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DimenexprTest.class);
    }

    /**
     * Constructor for NumexprTest.
     *
     * @param arg the name
     */
    public DimenexprTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> without any term produces an
     *  error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEOF1() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr \\relax",
                //--- log message ---
                "Missing number, treated as zero",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> with a non-balanced
     *  parenthesis produces an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError2() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr ( 123 \\relax",
                //--- log message ---
                "Missing ) inserted for expression",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> with a non-balanced
     *  parenthesis produces an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError4() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr ( 123pt \\relax",
                //--- log message ---
                "Missing ) inserted for expression",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> with a non-balanced
     *  parenthesis produces an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr ( \\relax",
                //--- log message ---
                "Missing number, treated as zero",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> produces an error for
     *  division by zero.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError3() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 6pt/0\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "Arithmetic overflow",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> can add two numbers.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 1pt + 2pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "3.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> can add multiply numbers.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 2*3pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "6.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> can subtract two numbers.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 5pt-2pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "3.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> can divide two numbers.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 6pt/2\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "3.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> * binds more than +.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 1pt + 2*3pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "7.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> * binds more than +.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test11() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 2pt*3 + 1pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "7.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> / binds more than +.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test12() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 4pt/2 + 10pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "12.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> / binds more than +.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test13() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 10pt + 4pt/2\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "12.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> * binds more than -.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test14() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 1pt - 2pt*3\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "-5.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> * binds more than -.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test15() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 2pt*3 - 1pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "5.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> / binds more than -.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test16() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 4pt/2 - 10pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "-8.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> / binds more than -.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test17() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 10pt - 4pt/2\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "8.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> parentheses work.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test21() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 2*(1pt + 2pt)\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "6.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> the unary minus is
     *  treated correctly.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test31() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 2*-3pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "-6.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> the unary minus is
     *  treated correctly &ndash; even if repeated twice.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test32() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr 2*--3pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "6.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> the unary minus is
     *  treated correctly.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test33() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr -2pt + 3pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "1.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that in <tt>\dimenexpr</tt> the unary minus is
     *  treated correctly &ndash; even if repeated twice.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test34() throws Exception {

        runCode(//--- input code ---
                "\\dimen1=\\dimenexpr --2pt+3pt\\relax" + "\\the\\dimen1 ",
                //--- log message ---
                "",
                //--- output channel ---
                "5.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimenexpr">
     *  Test case checking that <tt>\dimenexpr</tt> can be used after
     *  <tt>\the</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testThe0() throws Exception {

        runCode(//--- input code ---
                "\\the\\dimenexpr 2*3pt\\relax",
                //--- log message ---
                "",
                //--- output channel ---
                "6.0pt" + TERM);
    }

}
