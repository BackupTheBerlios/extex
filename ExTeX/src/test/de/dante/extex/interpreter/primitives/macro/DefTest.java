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

package de.dante.extex.interpreter.primitives.macro;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\def</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class DefTest extends ExTeXLauncher {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public DefTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testImmediate1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES
                + "\\immediate\\def\\aaa{}",
                //--- log message ---
                "You can't use the prefix `\\immediate' with the control sequence \\def");
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa{AAA}"
                + "--\\aaa--\\end",
                //--- output message ---
                "--AAA--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa{AAA}"
                + "{\\global\\def\\aaa{BBB}}"
                + "--\\aaa--\\end",
                //--- output message ---
                "--BBB--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that long is an accepted prefix.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLong1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\long\\def\\aaa{AAA}"
                + "--\\aaa--\\end",
                //--- output message ---
                "--AAA--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLong2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\long\\def\\aaa{AAA\\par BBB}"
                + "--\\aaa--\\end",
                //--- output message ---
                "--AAA\n\nBBB--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa#1{A#1A}"
                + "--\\aaa 1--\\end",
                //--- output message ---
                "--A1A--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa#1{A#1A}"
                + "--\\aaa {1}--\\end",
                //--- output message ---
                "--A1A--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testA3() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa#1{A#1A}"
                + "--\\aaa {12}--\\end",
                //--- output message ---
                "--A12A--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that two arguments are parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTwo1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\a#1#2{--#1--#2--}"
                + "\\a 2."
                + "\\end ",
                //--- output channel ---
                "--2--.--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPattern1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa#1.{--#1--}"
                + "\\aaa 2."
                + "\\end ",
                //--- output channel ---
                "--2--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPattern2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa#1.{--#1--}"
                + "\\aaa {2.1}."
                + "\\end ",
                //--- output channel ---
                "--2.1--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBrace1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa#1.{--#1--}"
                + "\\aaa }.",
                //--- output channel ---
                "Argument of \\aaa has an extra }");
    }

}
