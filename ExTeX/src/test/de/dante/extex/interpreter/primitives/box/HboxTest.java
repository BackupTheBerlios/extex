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

package de.dante.extex.interpreter.primitives.box;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\hbox</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class HboxTest extends ExTeXLauncher {

    /**
     * Constructor for HboxTest.
     *
     * @param arg the name
     */
    public HboxTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that a missing left brace directly after the macro
     * token leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testMissingBrace1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + ""
                + "\\hbox a"
                + "\\end ",
                //--- log message ---
                "Missing `{' inserted",
                //--- output channel ---
                null);
    }

    /**
     * Test case checking that a missing left brace after a "to" specification
     * leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testMissingBrace2() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + ""
                + "\\hbox to 2pt a"
                + "\\end ",
                //--- log message ---
                "Missing `{' inserted",
                //--- output channel ---
                null);
    }

    /**
     * Test case checking that a missing left brace after a "spread"
     * specification leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testMissingBrace3() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + ""
                + "\\hbox spread 2pt a"
                + "\\end ",
                //--- log message ---
                "Missing `{' inserted",
                //--- output channel ---
                null);
    }

    /**
     * Test case checking that an outer macro in the preamble leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + ""
                + "\\hbox ",
                //--- log message ---
                "Unexpected end of file while processing \\hbox",
                //--- output channel ---
                null);
    }

}