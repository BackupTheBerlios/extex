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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\globaldefs</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class GlobaldefsTest extends ExTeXLauncher {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(GlobaldefsTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public GlobaldefsTest(final String arg) {

        super(arg);
    }

    /**
     * The field <tt>primitive</tt> contains the name of the primitive to test.
     */
    private String primitive = "globaldefs";

    /**
     * The field <tt>invocation</tt> contains the concatenation of primitive
     * name and arguments.
     */
    private String invocation = "globaldefs";

    /**
     * The field <tt>init</tt> contains the default value.
     */
    private String init = "0";

    /**
     * <testcase>
     *  Test case showing that the prefix <tt>\immediate</tt> is not applicable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterImmediatePrefix1() throws Exception {

        assertFailure(//--- input code ---
                "\\immediate\\" + invocation + "=92 ",
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
                "\\long\\" + invocation + "=92 ",
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
                "\\outer\\" + invocation + "=92 ",
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
                "\\the\\" + invocation + "\\end",
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
    public void testCountRegisterAssign1() throws Exception {

        assertSuccess(//--- input code ---
                "\\" + invocation + "=123 \\the\\" + invocation + "\\end",
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
                "\\" + invocation + " 123 \\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=-123 \\the\\" + invocation + "\\end",
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
                "\\" + invocation + "-123 \\the\\" + invocation + "\\end",
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
                "\\globaldefs=1 " + "\\begingroup\\" + invocation
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
                "\\afterassignment b a" + "\\" + invocation + "-123 c\\the\\"
                        + invocation + "\\end",
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
                "\\" + invocation + "-123 \\count0=\\" + invocation
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
                "\\begingroup\\" + invocation + "=123 \\endgroup" + " \\the\\"
                        + invocation + "\\end",
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
                "\\begingroup\\global\\" + invocation + "=123 \\endgroup"
                        + "\\the\\" + invocation + "\\end",
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
                "\\begingroup\\global\\" + invocation + " 123 \\endgroup"
                        + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=23 " + "\\advance\\" + invocation
                        + " 12 " + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=23 " + "\\advance\\" + invocation
                        + " by 12 " + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=23 " + "\\advance\\" + invocation
                        + "-12 " + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=23 " + "\\advance\\" + invocation
                        + " by -12 " + "\\the\\" + invocation + "\\end",
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
                "\\globaldefs=1 " + "\\begingroup\\" + invocation
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
                "\\" + invocation + "=0 " + "\\afterassignment b a"
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
                "\\begingroup\\advance\\" + invocation + " 123 \\endgroup"
                        + " \\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=3 " + "\\multiply\\" + invocation + " 0 "
                        + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=3 " + "\\multiply\\" + invocation
                        + " 12 " + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=3 " + "\\multiply\\" + invocation
                        + " by 12 " + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=3 " + "\\multiply\\" + invocation
                        + "-12 " + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=3 " + "\\multiply\\" + invocation
                        + " by -12 " + "\\the\\" + invocation + "\\end",
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
                "\\globaldefs=1 " + "\\" + invocation + "=12 "
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
                "\\" + invocation + "=0 " + "\\afterassignment b a"
                        + "\\multiply\\" + invocation + "-123 c\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "abc0" + TERM);
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
                "\\" + invocation + "=36 " + "\\divide\\" + invocation + " 0 "
                        + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=36 " + "\\divide\\" + invocation + " 12 "
                        + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=36 " + "\\divide\\" + invocation
                        + " by 12 " + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an multiplication by the constant -12 works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterDivide3() throws Exception {

        assertSuccess(//--- input code ---
                "\\" + invocation + "=36 " + "\\divide\\" + invocation + "-12 "
                        + "\\the\\" + invocation + "\\end",
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
                "\\" + invocation + "=36 " + "\\divide\\" + invocation
                        + " by -12 " + "\\the\\" + invocation + "\\end",
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
                "\\afterassignment b a" + "\\divide\\" + invocation
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
                "\\" + invocation + "=-36 " + "\\divide\\" + invocation
                        + " by -12 " + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

}
