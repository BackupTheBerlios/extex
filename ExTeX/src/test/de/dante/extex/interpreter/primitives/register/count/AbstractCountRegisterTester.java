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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.test.ExTeXLauncher;

/**
 * This is a abstract base class for testing count registers.
 * It provides some test cases common to all count registers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public abstract class AbstractCountRegisterTester extends ExTeXLauncher {

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
    private String prepare = "";

    /**
     * Creates a new object.
     *
     * @param arg the name of the test suite
     * @param primitive the name of the integer register to test
     * @param args the parameters for the invocation
     * @param init the default value
     */
    public AbstractCountRegisterTester(final String arg,
            final String primitive, final String args, final String init) {

        super(arg);
        this.primitive = primitive;
        this.invocation = primitive + args;
        this.init = init;
    }

    /**
     * Creates a new object.
     *
     * @param arg the name of the test suite
     * @param primitive the name of the integer register to test
     * @param args the arguments for the invocation
     * @param init the default value
     * @param prepare the preparation code inserted before each test
     */
    public AbstractCountRegisterTester(final String arg,
            final String primitive, final String args, final String init,
            final String prepare) {

        this(arg, primitive, args, init);
        this.prepare = prepare;
    }

    /**
     * <testcase>
     *  Test case showing that the prefix <tt>\immediate</tt> is not applicable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterImmediatePrefix1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\immediate\\" + invocation + "=92 ",
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
    public void testCountRegisterLongPrefix1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\long\\" + invocation + "=92 ",
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
    public void testCountRegisterOuterPrefix1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\outer\\" + invocation + "=92 ",
                //--- error channel ---
                "You can't use the prefix `\\outer' with the control sequence"
                        + (primitive.length() > 18 ? "\n" : " ") + "\\"
                        + primitive);
    }

    /**
     * <testcase>
     *  Test case showing that the primitive is defined and its default value
     *  is 0.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterDefault1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                init + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that the primitive is applicable fro \showthe.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterShowthe1() throws Exception {

        assertOutput(//--- input code ---
                prepare + "\\showthe\\" + invocation + "\\end",
                //--- output channel ---
                "> " + init + ".\n",
                "");
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 123 works when using
     *  an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAssign1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=123 \\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 123 works when using
     *  no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAssign2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + " 123 \\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -123 works when using
     *  an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAssign3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=-123 \\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -123 works when using
     *  no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAssign4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "-123 \\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant -123 works when using
     *  <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAssign5() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 " + "\\begingroup\\" + invocation
                        + "-123 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "-123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAfterassignment1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\afterassignment b a" + "\\" + invocation
                        + "-123 c\\the\\" + invocation + "\\end",
                //--- output channel ---
                "abc-123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that the value is count convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterConvertible1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "-123 \\count0=\\" + invocation
                        + " \\the\\count0 \\end",
                //--- output channel ---
                "-123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterGroup1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\begingroup\\" + invocation + "=123 \\endgroup"
                        + " \\the\\" + invocation + "\\end",
                //--- output channel ---
                init + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 123 works when using
     *  an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterGlobalAssign1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\begingroup\\global\\" + invocation
                        + "=123 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 123 works when using
     *  no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterGlobalAssign2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\begingroup\\global\\" + invocation
                        + " 123 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant 12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAdvance1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=23 " + "\\advance\\"
                        + invocation + " 12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "35" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant 12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAdvance2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=23 " + "\\advance\\"
                        + invocation + " by 12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "35" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAdvance3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=23 " + "\\advance\\"
                        + invocation + "-12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "11" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAdvance4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=23 " + "\\advance\\"
                        + invocation + " by -12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "11" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancement by the constant -123 works when using
     *  <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAdvance5() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 " + "\\begingroup\\" + invocation
                        + "-123 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "-123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAfterassignment2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=0 " + "\\afterassignment b a"
                        + "\\advance\\" + invocation + "-123 c\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "abc-123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an advancing respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterGroup2() throws Exception {

        assertSuccess(
                //--- input code ---
                prepare + "\\begingroup\\advance\\" + invocation
                        + " 123 \\endgroup" + " \\the\\" + invocation + "\\end",
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
    public void testCountRegisterMultiply0() throws Exception {

        assertSuccess(
                //--- input code ---
                prepare + "\\" + invocation + "=3 " + "\\multiply\\"
                        + invocation + " 0 " + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "0" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication with the constant 12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterMultiply1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3 " + "\\multiply\\"
                        + invocation + " 12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "36" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication with the constant 12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterMultiply2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3 " + "\\multiply\\"
                        + invocation + " by 12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "36" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterMultiply3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3 " + "\\multiply\\"
                        + invocation + "-12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-36" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterMultiply4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3 " + "\\multiply\\"
                        + invocation + " by -12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-36" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a multiplication by a constant -123 works when
     *  using <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterMultiply5() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 " + "\\" + invocation + "=12 "
                        + "\\begingroup\\multiply\\" + invocation
                        + " 3 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "36" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAfterassignment3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=0 " + "\\afterassignment b a"
                        + "\\multiply\\" + invocation + "-123 c\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "abc0" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that multiplication respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterGroup3() throws Exception {

        assertSuccess(
                //--- input code ---
                prepare + "\\" + invocation + "=3 "
                        + "\\begingroup\\multiply\\" + invocation
                        + " 123 \\endgroup" + " \\the\\" + invocation + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant 12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterDivide0() throws Exception {

        assertFailure(
                //--- input code ---
                prepare + "\\" + invocation + "=36 " + "\\divide\\"
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
    public void testCountRegisterDivide1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=36 " + "\\divide\\"
                        + invocation + " 12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant 12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterDivide2() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=36 " + "\\divide\\"
                        + invocation + " by 12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant -12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterDivide3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=36 " + "\\divide\\"
                        + invocation + "-12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works when
     *  using the keyword <tt>by</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterDivide4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=36 " + "\\divide\\"
                        + invocation + " by -12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a division by a constant -123 works when
     *  using <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterDivide5() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 " + "\\" + invocation + "=-246 "
                        + "\\begingroup\\divide\\" + invocation
                        + "-123 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "2" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an division by the constant 12 works rounds to
     *  an integer.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterDivide7() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=37 " + "\\divide\\"
                        + invocation + "-12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "-3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterAfterassignment4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\afterassignment b a" + "\\divide\\" + invocation
                        + "-123 c\\end",
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
    public void testCountRegisterDivide6() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=-36 " + "\\divide\\"
                        + invocation + " by -12 " + "\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that division respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterGroup4() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + "=3 " + "\\begingroup\\divide\\"
                        + invocation + " 123 \\endgroup" + " \\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

}
