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

package de.dante.extex.interpreter.max;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the scanner routines.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ScanNumberTest extends ExTeXLauncher {

    /**
     * Constructor for MaxTest.
     *
     * @param arg the name
     */
    public ScanNumberTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a single digit number.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber1() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=4\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "4" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a two digit number.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber2() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=45\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "45" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a three digit number.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber3() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser also takes a space as terminator.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber4() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=456 "
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser accepts hex numbers.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber5() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=\"1a "
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "26" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser accepts octal numbers.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber6() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar='17 "
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "15" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser accepts octal numbers.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumberErr1() throws Exception {

        assertFailure(//--- input code ---
                "\\escapechar=' ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a negative number.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber11() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=-456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "-456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a number with two
     *  minus signs in front.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber12() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=--456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a number with three
     *  minus signs in front.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber13() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=---456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "-456" + TERM);
    }


    /**
     * <testcase >
     *  This test case checks that the parser can decode a positive number with
     *  a single <tt>+</tt> sign.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber21() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=+456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a number with two
     *  plus signs in front.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber22() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=++456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a number with three
     *  plus signs in front.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber23() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=+++456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }


    /**
     * <testcase >
     *  This test case checks that the parser can decode a negative number with
     *  mixed signs.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber31() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=-+456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "-456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a negative number with
     *  mixed signs.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber32() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=+-456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "-456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a number with two
     *  minus signs and a plus sign in front.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber33() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=-+-456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a number with two
     *  minus signs and two plus signs in front.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber34() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=-+-+456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser can decode a number with three
     *  minus signs and two plus signsin front.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber35() throws Exception {

        assertSuccess(//--- input code ---
                "\\escapechar=-+-+-456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "-456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser expands macros on the way.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber40() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\mac{}"
                + "\\escapechar=\\mac-456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "-456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser expands macros on the way.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber41() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\mac{-}"
                + "\\escapechar=\\mac-456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser expands macros on the way.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber42() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\mac{}"
                + "\\escapechar=-\\mac456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "-456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser expands macros on the way.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber43() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\mac{-}"
                + "\\escapechar=-\\mac456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser expands macros on the way.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber44() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\mac{-1}"
                + "\\escapechar=-\\mac456\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "1456" + TERM);
    }

    /**
     * <testcase >
     *  This test case checks that the parser expands macros on the way.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testscanNumber45() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\mac{1}"
                + "\\escapechar=-\\mac45\\mac6\\relax"
                + "\\the\\escapechar"
                + "\\end",
                //--- output channel ---
                "-14516" + TERM);
    }

}
