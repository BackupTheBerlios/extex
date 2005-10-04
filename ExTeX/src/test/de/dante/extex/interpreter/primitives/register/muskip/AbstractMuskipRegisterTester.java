/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * omuion) any later version.
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

package de.dante.extex.interpreter.primitives.register.muskip;

import de.dante.test.ExTeXLauncher;

/**
 * This is a abstract base class for testing muskip registers.
 * It provides some test cases common to all muskip registers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractMuskipRegisterTester extends ExTeXLauncher {

    /**
     * The field <tt>primitive</tt> contains the name of the primitive to test.
     */
    private String primitive;

    /**
     * The field <tt>invocation</tt> contains the concatenation of primitive
     * name and arguments.
     */
    private String invocation;

    /**
     * The field <tt>init</tt> contains the default value.
     */
    private String init;

    /**
     * Creates a new object.
     *
     * @param arg the name of the test suite
     * @param primitive the name of the integer register to test
     * @param args the parameters for the invocation
     */
    public AbstractMuskipRegisterTester(final String arg,
            final String primitive, final String args, final String init) {

        super(arg);
        this.primitive = primitive;
        this.invocation = primitive + args;
        this.init = init;
    }

    /**
     * <testcase>
     *  Test case showing that the prefix <tt>\immediate</tt> is not applicable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterImmediatePrefix1() throws Exception {

        runFailureCode(//--- input code ---
                "\\immediate\\" + invocation + "= 2mu ",
                //--- error channel ---
                "You can't use the prefix `\\immediate' with the control sequence"
                        + (primitive.length() > 14 ? "\n" : " ") + "\\"
                        + primitive);
    }

    /**
     * <testcase>
     *  Test case showing that the prefix <tt>\long</tt> is not applicable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterLongPrefix1() throws Exception {

        runFailureCode(//--- input code ---
                "\\long\\" + invocation + "= 2mu ",
                //--- error channel ---
                "You can't use the prefix `\\long' with the control sequence"
                        + (primitive.length() > 19 ? "\n" : " ") + "\\"
                        + primitive);
    }

    /**
     * <testcase>
     *  Test case showing that the prefix <tt>\outer</tt> is not applicable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterOuterPrefix1() throws Exception {

        runFailureCode(//--- input code ---
                "\\outer\\" + invocation + "= 2mu ",
                //--- error channel ---
                "You can't use the prefix `\\outer' with the control sequence"
                        + (primitive.length() > 18 ? "\n" : " ") + "\\"
                        + primitive);
    }

    /**
     * <testcase>
     *  Test case showing that the primitive is defined and has its default
     *  value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterDefault1() throws Exception {

        runCode(//--- input code ---
                "\\the\\" + invocation + "\\end",
                //--- output channel ---
                init + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3mu works when using
     *  an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAssign1() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=12.3mu plus 1mu minus 2mu\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3mu works when using
     *  no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAssign2() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + " 12.3mu plus 1mu minus 2mu\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -12.3mu works when using
     *  an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAssign3() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=-12.3mu plus 1mu minus 2mu \\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -12.3mu works when using
     *  no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAssign4() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "-12.3mu plus 1mu minus 2mu \\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -12.3mu works when using
     *  <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAssign5() throws Exception {

        runCode(//--- input code ---
                "\\globaldefs=1 " + "\\begingroup\\" + invocation
                        + "-12.3mu plus 1.0mu minus 2.0mu\\endgroup"
                        + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "-12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAfterassignment1() throws Exception {

        runCode(//--- input code ---
                "\\afterassignment b a" + "\\" + invocation + "-12.3muc\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "abc-12.3mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that the value is dimen convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void ___testMuskipRegisterConvertible1() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "-12.3mu \\dimen0=\\" + invocation
                        + " \\the\\dimen0 \\end",
                //--- output channel ---
                "-12.3mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that the value is count convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void ___testMuskipRegisterConvertible2() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "-12.3mu \\count0=\\" + invocation
                        + " \\the\\count0 \\end",
                //--- output channel ---
                "-806093" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterGroup1() throws Exception {

        runCode(//--- input code ---
                "\\begingroup\\" + invocation
                        + "=12.3mu plus 1mu minus 2mu\\endgroup" + " \\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                init + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3mu works when using
     *  an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterGlobalAssign1() throws Exception {

        runCode(//--- input code ---
                "\\begingroup\\global\\" + invocation
                        + "=12.3mu plus 1mu minus 2mu \\endgroup" + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3mu works when using
     *  no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterGlobalAssign2() throws Exception {

        runCode(//--- input code ---
                "\\begingroup\\global\\" + invocation
                        + " 12.3mu plus 1mu minus 2mu \\endgroup" + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant 12mu works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAdvance1() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=23mu plus 1.0mu minus 2.0mu "
                        + "\\advance\\" + invocation + " 12mu " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "35.0mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant 12mu works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAdvance2() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=23mu plus 1mu minus 2mu " + "\\advance\\"
                        + invocation + " by 12mu " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "35.0mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -12mu works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAdvance3() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=23mu  plus 1.0mu minus 2.0mu"
                        + "\\advance\\" + invocation + "-12mu " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "11.0mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -12mu works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAdvance4() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=23mu plus 1mu minus 2mu" + "\\advance\\"
                        + invocation + " by -12mu " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "11.0mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -12.3mu works when using
     *  <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAdvance5() throws Exception {

        runCode(//--- input code ---
                "\\globaldefs=1 " + "\\begingroup\\" + invocation
                        + "-12.3mu plus 1mu minus 2mu \\endgroup" + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAfterassignment2() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=0mu" + "\\afterassignment b a"
                        + "\\advance\\" + invocation
                        + "-12.3mu plus 1mu minus 2muc\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "abc-12.3mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancing respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterGroup2() throws Exception {

        runCode(//--- input code ---
                "\\begingroup\\advance\\" + invocation
                        + " 12.3mu plus 1mu minus 2mu \\endgroup" + " \\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                init + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication with the constant 0 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterMultiply0() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3mu plus 1.0mu minus 2.0mu "
                        + "\\multiply\\" + invocation + " 0 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "0.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication with the constant 12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterMultiply1() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3mu plus 1mu minus 2mu " + "\\multiply\\"
                        + invocation + " 12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "36.0mu plus 12.0mu minus 24.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication with the constant 12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterMultiply2() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3mu plus 1mu minus 2mu " + "\\multiply\\"
                        + invocation + " by 12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "36.0mu plus 12.0mu minus 24.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterMultiply3() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3mu plus 1mu minus 2mu " + "\\multiply\\"
                        + invocation + "-12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-36.0mu plus -12.0mu minus -24.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterMultiply4() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3mu plus 1mu minus 2mu " + "\\multiply\\"
                        + invocation + " by -12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-36.0mu plus -12.0mu minus -24.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a multiplication by a constant -12.3mu works when
     *  using <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterMultiply5() throws Exception {

        runCode(//--- input code ---
                "\\globaldefs=1 " + "\\" + invocation
                        + "=12mu plus 1mu minus 2mu "
                        + "\\begingroup\\multiply\\" + invocation
                        + " 3 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "36.0mu plus 3.0mu minus 6.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAfterassignment3() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=0mu " + "\\afterassignment b a"
                        + "\\multiply\\" + invocation + "-12 c\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "abc0.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that multiplication respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterGroup3() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3mu plus 1mu minus 2mu "
                        + "\\begingroup\\multiply\\" + invocation
                        + " 12 \\endgroup" + " \\the\\" + invocation + "\\end",
                //--- output channel ---
                "3.0mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant 12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterDivide0() throws Exception {

        runFailureCode(//--- input code ---
                "\\" + invocation + "=3.6mu " + "\\divide\\" + invocation
                        + " 0 " + "\\the\\" + invocation + "\\end",
                //--- error channel ---
                "Arithmetic overflow");
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant 12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterDivide1() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3.6mu plus 12mu minus 24mu "
                        + "\\divide\\" + invocation + " 12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "0.29999mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant 12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterDivide2() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3.6mu plus 12mu minus 24mu "
                        + "\\divide\\" + invocation + " by 12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "0.29999mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterDivide3() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3.6mu plus 12mu minus 24mu "
                        + "\\divide\\" + invocation + "-12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-0.29999mu plus -1.0mu minus -2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterDivide4() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=3.6mu plus 12mu minus 24mu "
                        + "\\divide\\" + invocation + " by -12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-0.29999mu plus -1.0mu minus -2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a division by a constant -12.3mu works when
     *  using <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterDivide5() throws Exception {

        runCode(//--- input code ---
                "\\globaldefs=1 " + "\\" + invocation + "=-246mu "
                        + "\\begingroup\\divide\\" + invocation
                        + "-123 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterAfterassignment4() throws Exception {

        runCode(//--- input code ---
                "\\afterassignment b a" + "\\divide\\" + invocation
                        + "-12 c\\end",
                //--- output channel ---
                "abc" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that division by the constant -12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterDivide6() throws Exception {

        runCode(//--- input code ---
                "\\" + invocation + "=-3.6mu plus -12mu minus -24mu "
                        + "\\divide\\" + invocation + " by -12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "0.29999mu plus 1.0mu minus 2.0mu" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that division respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterGroup4() throws Exception {

        runCode(
                //--- input code ---
                "\\" + invocation + "=3mu plus 1mu minus 2mu "
                        + "\\begingroup\\divide\\" + invocation
                        + " 123 \\endgroup" + " \\the\\" + invocation + "\\end",
                //--- output channel ---
                "3.0mu plus 1.0mu minus 2.0mu" + TERM);
    }

}
