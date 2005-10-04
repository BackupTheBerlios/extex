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

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\let</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class LetTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public LetTest(final String arg) {

        super(arg, "let", "\\relax\\relax");
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a letter to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetLetter1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\a A"
                + "--\\a--",
                //--- output message ---
                "--A--" + TERM);
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a letter to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetLetterLocal1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\a A"
                + "\\begingroup \\let\\a B\\endgroup"
                + "--\\a--",
                //--- output message ---
                "--A--" + TERM);
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a letter to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetLetterGlobal1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\a A"
                + "\\begingroup \\global\\let\\a B\\endgroup"
                + "--\\a--",
                //--- output message ---
                "--B--" + TERM);
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a digit to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetOther1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\a 1"
                + "--\\a--",
                //--- output message ---
                "--1--" + TERM);
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a digit to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLet1() throws Exception {

        runFailureCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\a\\a"
                + "\\a",
                //--- err message ---
                "Undefined control sequence");
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a letter to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetOtherLocal1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\a 1"
                + "\\begingroup \\let\\a 2\\endgroup"
                + "--\\a--",
                //--- output message ---
                "--1--" + TERM);
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a letter to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetOtherGlobal1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\a 1"
                + "\\begingroup \\global\\let\\a 2\\endgroup"
                + "--\\a--",
                //--- output message ---
                "--2--" + TERM);
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a digit to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetCs1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\x 1"
                + "\\let\\a \\x"
                + "--\\a--",
                //--- output message ---
                "--1--" + TERM);
    }


    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a letter to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetCsLocal1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\x 1"
                + "\\let\\a \\x"
                + "\\begingroup \\let\\a \\a\\endgroup"
                + "--\\a--",
                //--- output message ---
                "--1--" + TERM);
    }

    /**
     * <testcase primitive="let">
     *  Test case checking that let can assign a letter to a control sequence.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetCsGlobal1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\x 2"
                + "\\let\\a A"
                + "\\begingroup \\global\\let\\a \\x\\endgroup"
                + "--\\a--",
                //--- output message ---
                "--2--" + TERM);
    }

}