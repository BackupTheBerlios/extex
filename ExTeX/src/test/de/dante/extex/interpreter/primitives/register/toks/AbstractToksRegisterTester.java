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

package de.dante.extex.interpreter.primitives.register.toks;

import de.dante.test.ExTeXLauncher;

/**
 * This is a abstract base class for testing toks registers.
 * It provides some test cases common to all toks registers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractToksRegisterTester extends ExTeXLauncher {

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
     * The field <tt>prepare</tt> contains the ...
     */
    private String prepare = DEFINE_BRACES;

    /**
     * Creates a new object.
     *
     * @param arg the name of the test suite
     * @param primitive the name of the integer register to test
     * @param args the parameters for the invocation
     */
    public AbstractToksRegisterTester(final String arg, final String primitive,
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
     * @param primitive the name of the integer register to test
     * @param args the parameters for the invocation
     */
    public AbstractToksRegisterTester(final String arg, final String primitive,
            final String args, final String init, final String prepare) {

        this(arg, primitive, args, init);
        this.prepare = DEFINE_BRACES + prepare;
    }

    /**
     * <testcase>
     *  Test case showing that the prefix <tt>\immediate</tt> is not applicable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterImmediatePrefix1() throws Exception {

        runFailureCode(//--- input code ---
                prepare + "\\immediate\\" + invocation + "= {} ",
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
    public void testToksRegisterLongPrefix1() throws Exception {

        runFailureCode(//--- input code ---
                prepare + "\\long\\" + invocation + "= {} ",
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
    public void testToksRegisterOuterPrefix1() throws Exception {

        runFailureCode(//--- input code ---
                prepare + "\\outer\\" + invocation + "= {} ",
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
    public void testToksRegisterDefault1() throws Exception {

        runCode(//--- input code ---
                prepare + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                init + (init.length() != 0 ? TERM : ""));
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant {abc} works when
     *  using an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterAssign1() throws Exception {

        runCode(//--- input code ---
                prepare + "\\" + invocation + "={abc}\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "abc" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant {abc} works when
     *  using no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterAssign2() throws Exception {

        runCode(//--- input code ---
                prepare + "\\" + invocation + " {abc}\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "abc" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant {a{b}c} works when
     *  using no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterAssign3() throws Exception {

        runCode(//--- input code ---
                prepare + "\\" + invocation + " {a{b}c}\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "abc" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant {a#c} results in
     *  a#c when the catcode of # is OTHER.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterAssign4() throws Exception {

        runCode(//--- input code ---
                prepare + "\\" + invocation + " {a#c}\\the\\" + invocation
                        + "\\end",
                //--- output channel ---
                "a#c" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant {a#c} results in
     *  a#c when the catcode of # is OTHER.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterAssign5() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES + "\\" + invocation + " {a#c}\\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "a#c" + TERM); //TODO check: maybe double the #
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects <tt>\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterAfterassignment1() throws Exception {

        runCode(//--- input code ---
                prepare + "\\afterassignment b a" + "\\" + invocation
                        + "{xyz}c\\the\\" + invocation + "\\end",
                //--- output channel ---
                "abcxyz" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment respects grouping.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterGroup1() throws Exception {

        runCode(//--- input code ---
                prepare + "\\" + invocation + "={xyz}\\begingroup\\"
                        + invocation + "={abc}\\endgroup" + " \\the\\"
                        + invocation + "\\end",
                //--- output channel ---
                "xyz" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3mu works when using
     *  an equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterGlobalAssign1() throws Exception {

        runCode(
                //--- input code ---
                prepare + "\\begingroup\\global\\" + invocation
                        + "={abc}\\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "abc" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that an assignment of a constant 12.3mu works when using
     *  no equal sign after the primitive name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testToksRegisterGlobalAssign2() throws Exception {

        runCode(
                //--- input code ---
                prepare + "\\begingroup\\global\\" + invocation
                        + " {abc}\\endgroup" + "\\the\\" + invocation + "\\end",
                //--- output channel ---
                "abc" + TERM);
    }

    //TODO gene: add more test cases
}
