/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.typesetter.undo;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>&#x5c;unkern</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class UnkernTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(UnkernTest.class);
    }

    /**
     * Constructor for UnkernTest.
     *
     * @param arg the name
     */
    public UnkernTest(final String arg) {

        super(arg, "unkern", "", "\\kern1pt");
    }

    /**
     * <testcase primitive="&#x5c;unkern">
     *  Test case checking that <tt>&#x5c;unkern</tt> need some node in the
     *  current list.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(
        //--- input code ---
                "\\unkern\\end ",
                //--- error channel ---
                "You can't use `\\unkern' in vertical mode");
    }

    /**
     * <testcase primitive="&#x5c;unkern">
     *  Test case checking that <tt>&#x5c;unkern</tt> need some node in the
     *  current list.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr2() throws Exception {

        assertFailure(
        //--- input code ---
                "\\kern1pt\\unkern\\unkern\\end ",
                //--- error channel ---
                "You can't use `\\unkern' in vertical mode");
    }

    /**
     * <testcase primitive="&#x5c;unkern">
     *  Test case checking that <tt>&#x5c;unkern</tt> does not touch a char node
     *  at the end of the current list.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "a\\unkern\\end ",
                //--- output channel ---
                "" + //
                        "\\vbox(1.0pt+1.0pt)x0.0pt\n" + //
                        ".\\hbox(1.0pt+1.0pt)x0.0pt\n" + //
                        "..a\n");
    }

    /**
     * <testcase primitive="&#x5c;unkern">
     *  Test case checking that <tt>&#x5c;unkern</tt> takes the last kern from
     *  the current list.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "a\\kern1pt\\unkern\\end ",
                //--- output channel ---
                "" + //
                        "\\vbox(1.0pt+1.0pt)x0.0pt\n" + //
                        ".\\hbox(1.0pt+1.0pt)x0.0pt\n" + //
                        "..a\n");
    }

    //TODO implement more primitive specific test cases

}
