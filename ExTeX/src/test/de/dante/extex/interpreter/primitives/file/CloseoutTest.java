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
 * This is a test suite for the primitive <tt>\closeout</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class CloseoutTest extends NoFlagsButImmediatePrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(CloseoutTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public CloseoutTest(final String arg) {

        super(arg, "closeout", "1");
    }

    /**
     * <testcase primitive="\closeout">
     *  Test case checking that a <tt>\closeout</tt> works on unopened file
     *  handles.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        assertSuccess(//--- input code ---
                "\\closeout1",
                //--- output channel ---
                "\n");
    }

    /**
     * <testcase primitive="\closeout">
     *  Test case checking that a <tt>\closeout</tt> works on unopened file
     *  handles.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\immediate\\closeout1",
                //--- output channel ---
                "");
    }

}