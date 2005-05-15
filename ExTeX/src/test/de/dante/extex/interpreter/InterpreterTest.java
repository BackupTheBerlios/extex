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

package de.dante.extex.interpreter;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the the interpreter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class InterpreterTest extends ExTeXLauncher {

    /**
     * Constructor for MaxTest.
     *
     * @param arg the name
     */
    public InterpreterTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that an open block leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testOpenBlock() throws Exception {

        runCode(//--- input code ---
                "\\begingroup"
                + "\\end ",
                //--- log message ---
                "(\\end occurred inside a group at level 1)",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that two open blocks leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testOpenBlock2() throws Exception {

        runCode(//--- input code ---
                "\\begingroup"
                + "\\begingroup"
                + "\\end ",
                //--- log message ---
                "(\\end occurred inside a group at level 2)",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that two open blocks leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testOpenIf() throws Exception {

        runCode(//--- input code ---
                "\\iftrue"
                + "\\end ",
                //--- log message ---
                "(\\end occurred when \\iftrue was incomplete)",
                //--- output channel ---
                "");
    }

}