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

package de.dante.extex.interpreter.primitives.register.box;

import de.dante.extex.interpreter.type.dimen.DimenRegisterTest;
import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\setbox</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class SetboxTest extends ExTeXLauncher {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DimenRegisterTest.class);
    }

    /**
     * Constructor for MathchardefTest.
     *
     * @param arg the name
     */
    public SetboxTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that \setbox throws an error on eof.
     * @throws Exception in case of an error
     */
    public void testEOF1() throws Exception {

        runCode(//--- input code ---
                "\\setbox",
                //--- log message ---
                "Missing number, treated as zero",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that \setbox throws an error on eof.
     * @throws Exception in case of an error
     */
    public void testEOF2() throws Exception {

        runCode(//--- input code ---
                "\\setbox33",
                //--- log message ---
                "A <box> was supposed to be here",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that \setbox throws an error on eof.
     * @throws Exception in case of an error
     */
    public void testEOF3() throws Exception {

        runCode(//--- input code ---
                "\\setbox33=",
                //--- log message ---
                "A <box> was supposed to be here",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that \setbox needs a box as second argument.
     * @throws Exception in case of an error
     */
    public void testNonBox1() throws Exception {

        runCode(//--- input code ---
                "\\setbox33=123",
                //--- log message ---
                "A <box> was supposed to be here",
                //--- output channel ---
                "");
    }

    /**
     * Test case ...
     * @throws Exception in case of an error
     */
    public void testOK1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1 "
                + "\\catcode`}=2 "
                + "\\setbox 12=\\hbox{abc}"
                + "\\count0=\\wd12 "
                + "\\the\\count0 "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "0 " + TERM);
    }

}
