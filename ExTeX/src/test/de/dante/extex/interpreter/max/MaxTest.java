/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.max;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the the interpreter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class MaxTest extends ExTeXLauncher {

    /**
     * Constructor for MaxTest.
     *
     * @param arg the name
     */
    public MaxTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that a sole tab mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testTabMark() throws Exception {

        runCode(//--- input code ---
                "\\catcode`&=4\\relax"
                + "&"
                + "\\end ",
                //--- log message ---
                "Misplaced alignment tab character &",
                //--- output channel ---
                "\n");
    }

    /**
     * Test case checking that a sole sub mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testSupMark() throws Exception {

        runCode(//--- input code ---
                "\\catcode`^=7\\relax"
                + "^"
                + "\\end ",
                //--- log message ---
                "Missing $ inserted",
                //--- output channel ---
                "\n");
    }

    /**
     * Test case checking that a sole super mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testSubMark() throws Exception {

        runCode(//--- input code ---
                "\\catcode`_=8\\relax"
                + "_"
                + "\\end ",
                //--- log message ---
                "Missing $ inserted",
                //--- output channel ---
                "\n");
    }

    /**
     * Test case checking that a sole macro parameter leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testMacroParam() throws Exception {

        runCode(//--- input code ---
                "\\catcode`#=6\\relax"
                + "#"
                + "\\end ",
                //--- log message ---
                "You can't use `macro parameter character #' in vertical mode",
                //--- output channel ---
                "\n");
    }

    /**
     * Test case checking that an undefined active character leads to an
     * error message.
     *
     * @throws Exception in case of an error
     */
    public void testUndefinedAcive() throws Exception {

        runCode(//--- input code ---
                "\\catcode`~=13\\relax"
                + "~"
                + "\\end ",
                //--- log message ---
                "Undefined control sequence",
                //--- output channel ---
                "\n");
    }

    /**
     * Test case checking that an undefined control sequence leads to an
     * error message.
     *
     * @throws Exception in case of an error
     */
    public void testUndefinedCs() throws Exception {

        runCode(//--- input code ---
                "\\UNDEF"
                + "\\end ",
                //--- log message ---
                "Undefined control sequence",
                //--- output channel ---
                "\n");
    }

}