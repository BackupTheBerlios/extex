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

package de.dante.extex.interpreter.primitives.register.box;

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
     * <testcase primitive="hbox">
     *   Test case checking that a missing left brace directly after the macro
     *   token leads to an error.
     * </testcase>
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
                "");
    }

    /**
     * <testcase primitive="hbox">
     *   Test case checking that a missing left brace after a "to" specification
     *   leads to an error.
     * </testcase>
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
                "");
    }

    /**
     * <testcase primitive="hbox">
     *   Test case checking that an outer macro in the preamble leads to an
     *   error.
     * </testcase>
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
                "");
    }

    /**
     * <testcase primitive="hbox">
     *   Test case checking that a correct hbox passes its contents to the
     *   typesetter.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHbox1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2 "
                + "\\font\\fnt cmtt12 \\fnt"
                + "\\hbox{abc}"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "abc\n\n");
    }

    /**
     * <testcase primitive="hbox">
     *   Test case checking that an outer macro in the preamble leads to an
     *   error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHbox2() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + ""
                + "\\hbox{}",
                //--- log message ---
                "",
                //--- output channel ---
                "\n\n");
    }

    /**
     * <testcase primitive="hbox">
     *   Test case checking that a hbox containing "abc" in font cmtt12 has the
     *   width 18.52501pt. This value has been computed with <logo>TeX</logo>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHbox3() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\relax"
                + "\\font\\fnt cmtt12 \\fnt"
                + "\\setbox1=\\hbox{abc} "
                + "\\the\\wd1 "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "18.52501pt \n\n"); // checked wih TeX
    }


}