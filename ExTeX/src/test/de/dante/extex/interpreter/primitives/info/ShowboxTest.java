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

package de.dante.extex.interpreter.primitives.info;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\showbox</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ShowboxTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public ShowboxTest(final String arg) {

        super(arg, "showbox", "1 ", "", "\\box1=void\nOK\n");
    }

    /**
     * <testcase primitive="\showbox">
     *  Test case checking that <tt>\showbox</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(//--- input code ---
                "\\showbox -1",
                //--- error channel ---
                "Bad register code (-1)");
    }

    /**
     * <testcase primitive="\showbox">
     *  Test case checking that <tt>\showbox</tt> of a void register works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVoid1() throws Exception {

        assertFailure(//--- input code ---
                "\\showbox 1 \\end",
                //--- error channel ---
                "\\box1=void\nOK\n");
    }

    /**
     * <testcase primitive="\showbox">
     *  Test case checking that <tt>\showbox</tt> of a hbox works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHbox1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES
                + "\\setbox1=\\hbox{}\\showbox 1 \\end",
                //--- error channel ---
                "\\box1=\n\\hbox(0.0pt+0.0pt)x0.0pt\nOK\n");
    }

    /**
     * <testcase primitive="\showbox">
     *  Test case checking that <tt>\showbox</tt> of a vbox works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVbox1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES
                + "\\setbox1=\\vbox{}\\showbox 1 \\end",
                //--- error channel ---
                "\\box1=\n\\vbox(0.0pt+0.0pt)x0.0pt\nOK\n");
    }

    //TODO implement more primitive specific test cases

}
