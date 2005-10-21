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

package de.dante.extex.interpreter.primitives.dynamic;

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\nativedef</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class NativedefTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Constructor for SkewcharTest.
     *
     * @param arg the name
     */
    public NativedefTest(final String arg) {

        super(arg, "nativedef",
                "{java}\\t{de.dante.extex.interpreter.primitives.Relax}");
    }

    /**
     * @see de.dante.test.ExTeXLauncher#getConfig()
     */
    protected String getConfig() {

        return "extex-native.xml";
    }

    /**
     * <testcase primitive="\nativedef">
     *  Test case checking that <tt>\nativedef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
        //--- input code ---
                DEFINE_BRACES + "\\nativedef{java}"
                        + "\\t{de.dante.extex.interpreter.primitives.info.The}"
                        + "\\t\\count42" + " \\end",
                //--- log message ---
                "0" + TERM);
    }

    /**
     * <testcase primitive="\nativedef">
     *  Test case checking that <tt>\nativedef</tt> respects the global keyword.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal1() throws Exception {

        assertSuccess(
        //--- input code ---
                DEFINE_BRACES + "\\begingroup" + "\\global\\nativedef{java}"
                        + "\\t{de.dante.extex.interpreter.primitives.info.The}"
                        + "\\endgroup" + "\\t\\count42" + " \\end",
                //--- log message ---
                "0" + TERM);
    }

    /**
     * <testcase primitive="\nativedef">
     *  Test case checking that <tt>\nativedef</tt> respects the \global keyword.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal2() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_BRACES + "\\begingroup" + "\\nativedef{java}"
                        + "\\t{de.dante.extex.interpreter.primitives.info.The}"
                        + "\\endgroup" + "\\t\\count42" + " \\end",
                //--- log message ---
                "Undefined control sequence \\t");
    }

    /**
     * <testcase primitive="\nativedef">
     *  Test case checking that <tt>\nativedef</tt> respects \globaldefs.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal3() throws Exception {

        assertSuccess(
        //--- input code ---
                DEFINE_BRACES + "\\globaldefs=1 " + "\\begingroup"
                        + "\\nativedef{java}"
                        + "\\t{de.dante.extex.interpreter.primitives.info.The}"
                        + "\\endgroup" + "\\t\\count42" + " \\end",
                //--- log message ---
                "0" + TERM);
    }

    /**
     * <testcase primitive="\nativedef">
     *  Test case checking that <tt>\nativedef</tt> respects \afterassignment.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAfterassignment1() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_BRACES + "\\afterassignment\\x " + "\\begingroup"
                        + "\\nativedef{java}"
                        + "\\t{de.dante.extex.interpreter.primitives.info.The}"
                        + "\\endgroup",
                //--- log message ---
                "Undefined control sequence \\x");
    }

}
