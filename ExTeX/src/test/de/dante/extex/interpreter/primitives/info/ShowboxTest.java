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

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\showbox</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ShowboxTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public ShowboxTest(final String arg) {

        super(arg, "showbox", "1 ", "", "\\box#1=void\nOK\n");
    }

    /**
     * <testcase primitive="\showbox">
     *  Test case checking that the <tt>\showbox</tt> of a void register works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void _____________testVoid1() throws Exception {

        //TODO other test runner needed
        assertFailure(//--- input code ---
                "\\showbox 1 ",
                //--- error channel ---
                "");
    }

    //TODO implement the primitive specific test cases

}
