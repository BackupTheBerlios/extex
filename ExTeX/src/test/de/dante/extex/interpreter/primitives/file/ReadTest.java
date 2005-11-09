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

package de.dante.extex.interpreter.primitives.file;

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\read</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ReadTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ReadTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ReadTest(final String arg) {

        super(arg, "read", "1 to \\x", "\\openin1 src/test/data/read_data.tex ");
    }

    /**
     * <testcase primitive="\read">
     *  Test case checking that a <tt>\read</tt> works on unopened file
     *  handles.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTerm0() throws Exception {

        assertFailure(//--- input code ---
                "\\read 1 to \\x ",
                //--- error channel ---
                "*** (cannot \\read from terminal in nonstop modes)");
    }

    /**
     * <testcase primitive="\read">
     *  Test case checking that a <tt>\read</tt> works on unopened file
     *  handles.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr0() throws Exception {

        assertFailure(//--- input code ---
                "\\read 1",
                //--- error channel ---
                "Missing `to' inserted");
    }

    /**
     * <testcase primitive="\read">
     *  Test case checking that a <tt>\read</tt> works on unopened file
     *  handles.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(//--- input code ---
                "\\read 1 to ",
                //--- error channel ---
                "Missing control sequence inserted");
    }

    /**
     * <testcase primitive="\read">
     *  Test case checking that a <tt>\read</tt> can read the first line of the
     *  test file.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\openin1 src/test/data/read_data.tex " +
                "\\read 1 to \\x " + "\\x" + "\\end",
                //--- output channel ---
                "123xyz" + TERM);
    }

    /**
     * <testcase primitive="\read">
     *  Test case checking that a <tt>\read</tt> honors the <tt>\global</tt>
     *  prefix b defining the macro globally.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\openin1 src/test/data/read_data.tex " +
                "{\\global\\read 1 to \\x}" + "\\x" + "\\end",
                //--- output channel ---
                "123xyz" + TERM);
    }

    /**
     * <testcase primitive="\read">
     *  Test case checking that a <tt>\read</tt> makes its definition locally
     *  to the current group per default.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "\\openin1 src/test/data/read_data.tex " +
                "{\\read 1 to \\x}" + "\\x" + "\\end",
                //--- output channel ---
                "Undefined control sequence \\x");
    }

}
