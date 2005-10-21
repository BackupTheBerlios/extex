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

import de.dante.test.ExTeXLauncher;

/**
 * This is a base test class containing tests for array of counts.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractCharMapTester extends ExTeXLauncher {

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
     * The field <tt>max</tt> contains the ...
     */
    private String max = "32767";

    /**
     * Creates a new object.
     *
     * @param arg the name of the test suite
     * @param primitive the name of the integer register to test
     * @param args the parameters for the invocation
     * @param init the default value
     */
    public AbstractCharMapTester(final String arg, final String primitive,
            final String args, final String init, final String max) {

        super(arg);
        this.primitive = primitive;
        this.invocation = primitive + args;
        this.init = init;
        this.max = max;
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
     *  is correct.
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

        assertFailure(//--- input code ---
                prepare + "\\" + invocation + "=-123 \\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "Invalid code (-123), should be in the range 0.." + max);
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

        assertFailure(//--- input code ---
                prepare + "\\" + invocation + "-123 \\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "Invalid code (-123), should be in the range 0.." + max);
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
                        + " 123 c\\the\\" + invocation + "\\end",
                //--- output channel ---
                "abc123" + TERM);
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
                prepare + "\\" + invocation + " 123 \\count0=\\" + invocation
                        + " \\the\\count0 \\end",
                //--- output channel ---
                "123" + TERM);
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
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountRegisterGlobalAssign3() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\globaldefs=1 \\begingroup\\" + invocation
                        + " 123 \\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that the value is dimen convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDimenRegisterConvertible1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\" + invocation + " 32767 \\dimen0=\\" + invocation
                        + " \\the\\dimen0 \\end",
                //--- output channel ---
                "0.49998pt" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that assignment and retrieval works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testArrayAssign1() throws Exception {

        assertSuccess(//--- input code ---
                "\\" + primitive + "`A=`a " + "\\the\\" + primitive + "65 "
                        + "\\end",
                //--- output channel ---
                "97" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that several values are stored.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testArrayAssign2() throws Exception {

        assertSuccess(//--- input code ---
                "\\" + primitive + "`A=`a " + "\\" + primitive + "`b=`b "
                        + "\\the\\" + primitive + "65 " + "\\end",
                //--- output channel ---
                "97" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment triggers \afterassignment.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAfterassignment1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\afterassignment\\xx\\" + primitive
                        + "`A=`a \\end",
                //--- output channel ---
                "Undefined control sequence \\xx");
    }

}
