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

import de.dante.test.NoFlagsButImmediatePrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\write</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class WriteTest extends NoFlagsButImmediatePrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(WriteTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public WriteTest(final String arg) {

        super(arg, "write", "1 {abc}");
    }

    /**
     * <testcase primitive="\write">
     *  Test case checking that a lonely <tt>\write</tt> leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        assertFailure(//--- input code ---
                "\\write ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\write">
     *  Test case checking that a lonely <tt>\write</tt> with an index
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        assertFailure(//--- input code ---
                "\\write 2",
                //--- log message ---
                "Unexpected end of file while processing tokens" //TODO tokens should be \write
                );
    }

    /**
     * <testcase primitive="\write">
     *  Test case checking that a lonely <tt>\write</tt> leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof3() throws Exception {

        assertFailure(//--- input code ---
                "\\immediate\\write ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\write">
     *  Test case checking that a lonely <tt>\write</tt> with an index
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof4() throws Exception {

        assertFailure(//--- input code ---
                "\\immediate\\write 2",
                //--- log message ---
                "Unexpected end of file while processing write" //TODO tokens should be \write
                );
    }

    /**
     * <testcase primitive="\write">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testImmediate1() throws Exception {

        assertOutput(//--- input code ---
                DEFINE_BRACES + "\\immediate\\write 2{abc} \\end",
                //--- log message ---
                "abc", "");
    }

}
