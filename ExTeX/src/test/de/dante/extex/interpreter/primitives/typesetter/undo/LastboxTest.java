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

package de.dante.extex.interpreter.primitives.typesetter.undo;

import de.dante.extex.interpreter.primitives.math.AbstractMathTester;
import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\lastbox</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class LastboxTest extends ExTeXLauncher {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(LastboxTest.class);
    }

    /**
     * Constructor for LastboxTest.
     *
     * @param arg the name
     */
    public LastboxTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\lastbox">
     *  Test case showing that the primitive can not be used in vertical mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVerticalMode1() throws Exception {

        assertFailure(//--- input code ---
                "\\lastbox",
                //--- error channel ---
                "You can't use `\\lastbox' in vertical mode");
    }

    /**
     * <testcase primitive="\lastbox">
     *  Test case showing that the primitive can not be used in math mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathMode1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "$\\lastbox$\\end",
                //--- error channel ---
                "You can't use `\\lastbox' in math mode");
    }

    /**
     * <testcase primitive="\lastbox">
     *  Test case showing that the primitive can not be used in display math
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDisplayMathMode1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "$$\\lastbox$$\\end",
                //--- error channel ---
                "You can't use `\\lastbox' in displaymath mode");
    }

    /**
     * <testcase primitive="\lastbox">
     *  Test case checking that <tt>\lastbox</tt> can not be used in math mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_MATH + AbstractMathTester.DEFINE_MATH_FONTS
                        + "$\\setbox0=\\lastbox $",
                //--- error channel ---
                "You can't use `\\lastbox' in math mode");
    }

    /**
     * <testcase primitive="\lastbox">
     *  Test case checking that <tt>\lastbox</tt> can not be used in math mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr2() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_MATH + AbstractMathTester.DEFINE_MATH_FONTS
                        + "$\\lastbox $",
                //--- error channel ---
                "You can't use `\\lastbox' in math mode");
    }

    /**
     * <testcase primitive="\lastbox">
     *  Test case checking that <tt>\lastbox</tt> can not be used in display
     *  math mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr11() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_MATH + AbstractMathTester.DEFINE_MATH_FONTS
                        + "$$\\setbox0=\\lastbox $$",
                //--- error channel ---
                "You can't use `\\lastbox' in displaymath mode");
    }

    /**
     * <testcase primitive="\lastbox">
     *  Test case checking that <tt>\lastbox</tt> can not be used in display
     *  math mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr12() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_MATH + AbstractMathTester.DEFINE_MATH_FONTS
                        + "$$\\lastbox $$",
                //--- error channel ---
                "You can't use `\\lastbox' in displaymath mode");
    }

    //TODO implement more primitive specific test cases

}
