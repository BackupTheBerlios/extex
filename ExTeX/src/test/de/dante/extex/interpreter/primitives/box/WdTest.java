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

package de.dante.extex.interpreter.primitives.box;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\wd</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class WdTest extends ExTeXLauncher {

    /**
     * Constructor for HboxTest.
     *
     * @param arg the name
     */
    public WdTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that an eof leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\relax "
                + "\\wd ",
                //--- log message ---
                "Missing number, treated as zero",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that an eof leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\relax "
                + "\\wd 12 ",
                //--- log message ---
                "Illegal unit of measure (pt inserted)",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that a void box has zero width.
     *
     * @throws Exception in case of an error
     */
    public void testVoid1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\relax"
                + "\\count0=\\wd123 "
                + "\\the\\count0 "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "0 \n");
    }

    /**
     * Test case checking that a hbox containing "x" in font cmtt12 has the
     * width 6.175pt.
     *
     * @throws Exception in case of an error
     */
    public void testWd1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\relax"
                + "\\font\\fnt cmtt12 \\relax"
                + "\\setbox123=\\hbox{\\fnt x} "
                + "\\the\\wd123 "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "6.175pt \n"); // checked wih TeX
    }

    /**
     * Test case checking that a hbox containing "abc" in font cmtt12 has the
     * width 18.52501pt.
     *
     * @throws Exception in case of an error
     */
    public void testWd2() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\relax"
                + "\\font\\fnt cmtt12 \\relax"
                + "\\setbox123=\\hbox{\\fnt abc} "
                + "\\the\\wd123 "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "18.52501pt \n"); // checked wih TeX
    }

    /**
     * Test case checking that a hbox containing "abc" in font cmmi10 has the
     * width 5.28589pt.
     *
     * @throws Exception in case of an error
     */
    public void testWd11() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\relax"
                + "\\font\\fnt cmmi10 "
                + "\\setbox123=\\hbox{\\fnt a} "
                + "\\the\\wd123 "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "5.28589pt \n"); // checked wih TeX
    }
}