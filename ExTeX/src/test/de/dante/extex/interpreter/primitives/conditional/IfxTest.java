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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\ifx</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class IfxTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(IfxTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public IfxTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifx aa true\\else false\\fi",
                //--- output channel ---
                "true" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifx ab true\\else false\\fi",
                //--- output channel ---
                "false" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> classifies different undefined
     *  control sequences as different.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro1() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifx \\xx\\yy true\\else false\\fi",
                //--- output channel ---
                "false" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> classifies an undefined
     *  control sequences as identical to itself.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro2() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifx \\xx\\xx true\\else false\\fi",
                //--- output channel ---
                "true" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> classifies an undefined
     *  control sequences as different to <tt>\relax</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro3() throws Exception {

        assertSuccess(//--- input code ---
                "\\ifx \\relax\\yy true\\else false\\fi",
                //--- output channel ---
                "false" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> reproduces the example from the
     *  TeXbook.
     * </testcase>
     *
     * <p>
     *  For example, after `<tt>\def\a{\c}</tt> <tt>\def\b{\d}</tt>
     *  <tt>\def\c{\e}</tt> <tt>\def\d{\e}</tt> <tt>\def\e{A}</tt>', an
     *  <tt>\ifx</tt> test will find <tt>\c</tt> and <tt>\d</tt> equal,
     *  but not <tt>\a</tt> and <tt>\b</tt>, nor <tt>\d</tt> and <tt>\e</tt>,
     *  nor any other combinations of <tt>\a</tt>, <tt>\b</tt>, <tt>\c</tt>,
     *  <tt>\d</tt>, <tt>\e</tt>.
     * <p>
     *
     * @throws Exception in case of an error
     */
    public void testTeXbook() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\def\\a{\\c}"
                + "\\def\\b{\\d}"
                + "\\def\\c{\\e}"
                + "\\def\\d{\\e}"
                + "\\def\\e{A}"
                + "\\ifx\\c\\d t \\else f \\fi "
                + "\\ifx\\a\\b t \\else f \\fi "
                + "\\ifx\\a\\c t \\else f \\fi "
                + "\\ifx\\a\\d t \\else f \\fi "
                + "\\ifx\\a\\e t \\else f \\fi "
                + "\\ifx\\b\\c t \\else f \\fi "
                + "\\ifx\\b\\d t \\else f \\fi "
                + "\\ifx\\b\\e t \\else f \\fi "
                + "\\ifx\\c\\e t \\else f \\fi "
                + "\\ifx\\d\\e t \\else f \\fi "
                + "\\end",
                //--- output channel ---
                "t f f f f f f f f f" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFont1() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\f cmr10 "
                + "\\ifx\\f\\f t\\else f\\fi"
                + "\\end",
                //--- output channel ---
                "t" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFont2() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\f cmr10 "
                + "\\ifx\\f\\g t\\else f\\fi"
                + "\\end",
                //--- output channel ---
                "f" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFont3() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\f cmr10 "
                + "\\font\\g cmr10 "
                + "\\ifx\\f\\g t\\else f\\fi"
                + "\\end",
                //--- output channel ---
                "f" + TERM);
    }

    /**
     * <testcase primitive="\ifx">
     *  Test case checking that <tt>\ifx</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFont4() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\f cmr10 "
                + "\\let\\g\\f "
                + "\\ifx\\f\\g t\\else f\\fi"
                + "\\end",
                //--- output channel ---
                "t" + TERM);
    }

    //TODO implement more primitive specific test cases

}