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

package de.dante.extex.interpreter.primitives.info;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\show</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ShowTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public ShowTest(final String arg) {

        super(arg, "show", "\\count1 ", "", "> \\count=\\count.\n");
    }

    /**
     * <testcase primitive="\show">
     *  Test case checking that <tt>\show</tt> works with
     *  <tt>\relax</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertFailure(//--- input code ---
                "\\show\\relax"
                        + "\\end",
                //--- output channel ---
                "> \\relax=\\relax.\n");
    }

    /**
     * <testcase primitive="\show">
     *  Test case checking that <tt>\show</tt> works with
     *  <tt>\relax</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\def\\x{abc}\\show\\x"
                        + "\\end",
                //--- output channel ---
                "> \\x=macro:\n->abc.\n");
    }

    //TODO implement the primitive specific test cases

}
