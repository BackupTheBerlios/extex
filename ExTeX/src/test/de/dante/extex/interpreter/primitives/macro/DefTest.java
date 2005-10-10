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
 * @version $Revision: 1.7 $
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
                + "--\\aaa--",
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
    public void testGloal1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa{AAA}"
                + "{\\global\\def\\aaa{BBB}}"
                + "--\\aaa--",
                //--- output message ---
                "--BBB--" + TERM);
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLong1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\long\\def\\aaa{AAA}"
                + "--\\aaa--",
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
    public void testA1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa#1{A#1A}"
                + "--\\aaa 1--",
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
    public void testPattern1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\def\\aaa#1.{--#1--}"
                + "\\aaa 2."
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "--2--" + TERM);
    }


}