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

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is an abstract base class for testing math primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class AbstractMathTester extends NoFlagsPrimitiveTester {

    /**
     * The field <tt>DEFINE_MATH_FONTS</tt> contains the definition for the
     * required fonts.
     */
    public static final String DEFINE_MATH_FONTS =
        "\\font\\f cmsy10 \\textfont2=\\f"
        + "\\font\\f cmsy7 \\scriptfont2=\\f"
        + "\\font\\f cmsy5 \\scriptscriptfont2=\\f"
        + "\\font\\f cmex10 \\textfont3=\\f"
        + "\\scriptfont3=\\f"
        + "\\scriptscriptfont3=\\f"
        + "\\font\\f cmmi10 \\textfont1=\\f "
        + "\\font\\f cmmi7 \\scriptfont1=\\f "
        + "\\font\\f cmmi5 \\scriptscriptfont1=\\f ";

    /**
     * The field <tt>primitive</tt> contains the name of the primitive to test.
     */
    private String primitive;

    /**
     * The field <tt>arguments</tt> contains the arguments for the primitive
     * invocation.
     */
    private String arguments;

    /**
     * Creates a new object.
     *
     * @param arg the name
     * @param primitive the name of the primitive to test
     * @param arguments the arguments for the invocation
     */
    public AbstractMathTester(final String arg, final String primitive,
            final String arguments) {

        this(arg, primitive, arguments, "");
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     * @param primitive the name of the primitive to test
     * @param arguments the arguments for the invocation
     * @param prepare the code to insert before the invocation
     */
    public AbstractMathTester(final String arg, final String primitive,
            final String arguments, final String prepare) {

        super(arg, primitive, arguments + "$", "\\catcode`\\$=3 $" + prepare);
        this.primitive = primitive;
        this.arguments = arguments;
    }

    /**
     * <testcase>
     *  Test case checking that the primitive needs the math mode.
     *  Vertical mode is not enough.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testNonMathMode1() throws Exception {

        assertFailure(//--- input code ---
                "\\" + primitive + arguments + " ",
                //--- log message ---
                "Missing $ inserted");
    }

    /**
     * <testcase>
     *  Test case checking that the primitive needs the math mode.
     *  Horizontal mode is not enough.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testNonMathMode2() throws Exception {

        assertFailure(//--- input code ---
                "a\\" + primitive + arguments + " ",
                //--- log message ---
                "Missing $ inserted");
    }

}
