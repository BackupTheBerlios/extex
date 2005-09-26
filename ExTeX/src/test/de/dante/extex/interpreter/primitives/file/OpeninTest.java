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

import de.dante.test.NoFlagsButGlobalAndImmediatePrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\openin</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class OpeninTest extends NoFlagsButGlobalAndImmediatePrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(OpeninTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public OpeninTest(final String arg) {

        super(arg, "openin", "1 src/test/data/empty.tex");
    }

    /**
     * <testcase primitive="\openin">
     *  Test case checking that a lonely <tt>\openin</tt> leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runCode(//--- input code ---
                "\\openin ",
                //--- log message ---
                "Missing number, treated as zero",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\openin">
     *  Test case checking that a lonely <tt>\openin</tt> with an index
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        runCode(//--- input code ---
                "\\openin 2",
                //--- log message ---
                "Unexpected end of file while processing \\openin",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\openin">
     *  Test case checking that a <tt>\openin</tt> on a missing file
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void ________________________test0() throws Exception {

        runCode(//--- input code ---
                "\\openin 2 src/test/data/VeryUnlikelyToExist",
                //--- log message ---
                "Unexpected end of file while processing \\openin",
                //--- output channel ---
                "");
    }

}