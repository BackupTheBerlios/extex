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

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\wd</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class WdTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(WdTest.class);
    }

    /**
     * Constructor for WdTest.
     *
     * @param arg the name
     */
    public WdTest(final String arg) {

        super(arg, "wd", "1=1pt");
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that <tt>\wd</tt> throws an error if an eof is
     *  encountered.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        assertFailure(//--- input code ---
                "\\wd ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that an eof leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        assertFailure(//--- input code ---
                "\\wd 12 ",
                //--- log message ---
                "Illegal unit of measure (pt inserted)");
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that a void box has zero width.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVoid1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\count0=\\wd123 "
                + "\\the\\count0 "
                + "\\end",
                //--- output channel ---
                "0" + TERM);
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that a hbox containing "x" in font cmtt12 has the
     *  width 6.175pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testWd1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\font\\fnt cmtt12 \\relax"
                + "\\setbox123=\\hbox{\\fnt x} "
                + "\\the\\wd123 "
                + "\\end",
                //--- output channel ---
                "6.175pt" + TERM); // checked with TeX
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that a hbox containing "abc" in font cmtt12 has the
     *  width 18.52501pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testWd2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\font\\fnt cmtt12 \\relax"
                + "\\setbox123=\\hbox{\\fnt abc} "
                + "\\the\\wd123 "
                + "\\end",
                //--- output channel ---
                "18.52501pt" + TERM); // checked with TeX
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testWd3() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\font\\fnt cmmi10 "
                + "\\setbox123=\\hbox{\\fnt a} "
                + "\\the\\wd3 "
                + "\\the\\wd2 "
                + "\\the\\wd1 "
                + "\\end",
                //--- output channel ---
                "0.0pt" + "0.0pt" + "0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that a hbox containing "abc" in font cmmi10 has the
     *  width 5.28589pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testWd11() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\font\\fnt cmmi10 "
                + "\\setbox123=\\hbox{\\fnt a} "
                + "\\the\\wd123 "
                + "\\end",
                //--- output channel ---
                "5.28589pt" + TERM); // checked with TeX
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that a void box has the width 0pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testWd21() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\count1=\\wd123 "
                + "\\the\\count1"
                + "\\end",
                //--- output channel ---
                "0" + TERM); // checked with TeX
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testWd22() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\font\\fnt cmmi10 "
                + "\\setbox123=\\hbox{\\fnt a} "
                + "\\dimen1=\\wd123 "
                + "\\the\\dimen1"
                + "\\end",
                //--- output channel ---
                "5.28589pt" + TERM); // checked with TeX
    }

    /**
     * <testcase primitive="\wd">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testWd31() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\font\\fnt cmmi10 "
                + "\\setbox123=\\hbox{\\fnt a} "
                + "\\wd123=12pt"
                + "\\dimen1=\\wd123 "
                + "\\the\\dimen1"
                + "\\end",
                //--- output channel ---
                "12.0pt" + TERM); // checked with TeX
    }

}