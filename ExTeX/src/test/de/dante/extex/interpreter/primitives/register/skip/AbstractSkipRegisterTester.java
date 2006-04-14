/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.skip;

import de.dante.test.ExTeXLauncher;

/**
 * This is a abstract base class for testing skip registers.
 * It provides some test cases common to all skip registers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public abstract class AbstractSkipRegisterTester extends ExTeXLauncher {

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
     * The field <tt>prepare</tt> contains the the preparation code inserted
     * before each test.
     */
    private String prepare = "\\hsize=200pt";

    /**
     * Creates a new object.
     *
     * @param arg the name of the test suite
     * @param primitive the name of the skip register to test
     * @param args the parameters for the invocation
     * @param init the default value
     */
    public AbstractSkipRegisterTester(final String arg, final String primitive,
            final String args, final String init) {

        super(arg);
        this.primitive = primitive;
        this.invocation = primitive + args;
        this.init = init;
    }

    /**
     * Creates a new object.
     *
     * @param arg the name of the test suite
     * @param primitive the name of the skip register to test
     * @param args the arguments for the invocation
     * @param init the default value
     * @param prepare the preparation code inserted before each test
     */
    public AbstractSkipRegisterTester(final String arg, final String primitive,
            final String args, final String init, final String prepare) {

        this(arg, primitive, args, init);
        this.prepare = this.prepare + prepare;
    }

    /**
     * <testcase>
     *  Test case showing that the prefix <tt>\immediate</tt> is not applicable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterImmediatePrefix1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\immediate\\" + invocation + "= 2pt ",
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
    public void testSkipRegisterLongPrefix1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\long\\" + invocation + "= 2pt ",
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
    public void testSkipRegisterOuterPrefix1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\outer\\" + invocation + "= 2pt ",
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
    public void testSkipRegisterDefault1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                init + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3pt works when
     *  using an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAssign1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation
                        + "=12.3pt plus 1pt minus 2pt\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3pt works when
     *  using no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAssign2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation
                        + " 12.3pt plus 1pt minus 2pt\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -12.3pt works when
     *  using an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAssign3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation
                        + "=-12.3pt plus 1pt minus 2pt \\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -12.3pt works when
     *  using no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAssign4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation
                        + "-12.3pt plus 1pt minus 2pt \\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -12.3pt works when
     *  using <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAssign5() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 " + "\\begingroup\\" + invocation
                        + "-12.3pt plus 1.0pt minus 2.0pt\\endgroup"
                        + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "-12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAfterassignment1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\afterassignment b a" + "\\" + invocation
                        + "-12.3ptc\\the\\" + invocation + "\\end",
                //--- output channel ---
                "abc-12.3pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that the value is dimen convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void ___testSkipRegisterConvertible1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "-12.3pt \\dimen0=\\"
                        + invocation + " \\the\\dimen0 \\end",
                //--- output channel ---
                "-12.3pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that the value is count convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void ___testSkipRegisterConvertible2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "-12.3pt \\count0=\\"
                        + invocation + " \\the\\count0 \\end",
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
    public void testSkipRegisterGroup1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\begingroup\\" + invocation
                        + "=12.3pt plus 1pt minus 2pt\\endgroup" + " \\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                init + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3pt works when using
     *  an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterGlobalAssign1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\begingroup\\global\\" + invocation
                        + "=12.3pt plus 1pt minus 2pt \\endgroup" + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3pt works when using
     *  no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterGlobalAssign2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\begingroup\\global\\" + invocation
                        + " 12.3pt plus 1pt minus 2pt \\endgroup" + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant 12pt works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAdvance1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=23pt plus 1.0pt minus 2.0pt "
                        + "\\advance\\" + invocation + " 12pt " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "35.0pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant 12pt works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAdvance2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=23pt plus 1pt minus 2pt "
                        + "\\advance\\" + invocation + " by 12pt " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "35.0pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -12pt works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAdvance3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=23pt  plus 1.0pt minus 2.0pt"
                        + "\\advance\\" + invocation + "-12pt " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "11.0pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -12pt works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAdvance4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=23pt plus 1pt minus 2pt"
                        + "\\advance\\" + invocation + " by -12pt " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "11.0pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -12.3pt works when using
     *  <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAdvance5() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 " + "\\begingroup\\" + invocation
                        + "-12.3pt plus 1pt minus 2pt \\endgroup" + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAfterassignment2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=0pt" + "\\afterassignment b a"
                        + "\\advance\\" + invocation
                        + "-12.3pt plus 1pt minus 2ptc\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "abc-12.3pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancing respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterGroup2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\begingroup\\advance\\" + invocation
                        + " 12.3pt plus 1pt minus 2pt \\endgroup" + " \\the\\"
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
    public void testSkipRegisterMultiply0() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3pt plus 1.0pt minus 2.0pt "
                        + "\\multiply\\" + invocation + " 0 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication with the constant 12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterMultiply1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3pt plus 1pt minus 2pt "
                        + "\\multiply\\" + invocation + " 12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "36.0pt plus 12.0pt minus 24.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication with the constant 12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterMultiply2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3pt plus 1pt minus 2pt "
                        + "\\multiply\\" + invocation + " by 12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "36.0pt plus 12.0pt minus 24.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterMultiply3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3pt plus 1pt minus 2pt "
                        + "\\multiply\\" + invocation + "-12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-36.0pt plus -12.0pt minus -24.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterMultiply4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3pt plus 1pt minus 2pt "
                        + "\\multiply\\" + invocation + " by -12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-36.0pt plus -12.0pt minus -24.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a multiplication by a constant -12.3pt works when
     *  using <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterMultiply5() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 " + "\\" + invocation
                        + "=12pt plus 1pt minus 2pt "
                        + "\\begingroup\\multiply\\" + invocation
                        + " 3 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "36.0pt plus 3.0pt minus 6.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAfterassignment3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=0pt " + "\\afterassignment b a"
                        + "\\multiply\\" + invocation + "-12 c\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "abc0.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that multiplication respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterGroup3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3pt plus 1pt minus 2pt "
                        + "\\begingroup\\multiply\\" + invocation
                        + " 12 \\endgroup" + " \\the\\" + invocation + "\\end",
                //--- output channel ---
                "3.0pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant 12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterDivide0() throws Exception {

        assertFailure(
                //--- input code ---
                prepare + "\\" + invocation + "=3.6pt " + "\\divide\\"
                        + invocation + " 0 " + "\\the\\" + invocation + "\\end",
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
    public void testSkipRegisterDivide1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3.6pt plus 12pt minus 24pt "
                        + "\\divide\\" + invocation + " 12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "0.29999pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant 12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterDivide2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3.6pt plus 12pt minus 24pt "
                        + "\\divide\\" + invocation + " by 12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "0.29999pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterDivide3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3.6pt plus 12pt minus 24pt "
                        + "\\divide\\" + invocation + "-12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-0.29999pt plus -1.0pt minus -2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterDivide4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3.6pt plus 12pt minus 24pt "
                        + "\\divide\\" + invocation + " by -12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "-0.29999pt plus -1.0pt minus -2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a division by a constant -12.3pt works when
     *  using <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterDivide5() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 " + "\\" + invocation + "=-246pt "
                        + "\\begingroup\\divide\\" + invocation
                        + "-123 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterAfterassignment4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\afterassignment b a" + "\\divide\\" + invocation
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
    public void testSkipRegisterDivide6() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=-3.6pt plus -12pt minus -24pt "
                        + "\\divide\\" + invocation + " by -12 " + "\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "0.29999pt plus 1.0pt minus 2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that division respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkipRegisterGroup4() throws Exception {

        assertSuccess(
                //--- input code ---
                prepare + "\\" + invocation + "=3pt plus 1pt minus 2pt "
                        + "\\begingroup\\divide\\" + invocation
                        + " 123 \\endgroup" + " \\the\\" + invocation + "\\end",
                //--- output channel ---
                "3.0pt plus 1.0pt minus 2.0pt" + TERM);
    }

}
