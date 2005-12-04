/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\relax</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public class RelaxTest extends NoFlagsPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(RelaxTest.class);
    }

    /**
     * Constructor for RelaxTest.
     *
     * @param name the name
     */
    public RelaxTest(final String name) {

        super(name, "relax", "");
    }

    /**
     * <testcase primitive="\relax">
     *  Test case checking that a pure <tt>\relax</tt> has no effect.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\relax\\end",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\relax">
     *  Test case checking that a pure <tt>\relax</tt> has no effect in the
     *  middle of a word.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertSuccess(//--- input code ---
                "abc\\relax def\\end",
                //--- output channel ---
                "abcdef" + TERM);
    }

    /**
     * <testcase primitive="\relax">
     *  Test case checking that a white-space after a <tt>\relax</tt> is
     *  ignored.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\relax \\end",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\relax">
     *  Test case checking that more white-space after a <tt>\relax</tt> is
     *  ignored.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertSuccess(//--- input code ---
                "\\relax         \\end",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\relax">
     *  Test case checking that a comment after a <tt>\relax</tt> is ignored.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        assertSuccess(//--- input code ---
                "\\relax %1234 \n\r\\end",
                //--- output channel ---
                "");
    }

}
