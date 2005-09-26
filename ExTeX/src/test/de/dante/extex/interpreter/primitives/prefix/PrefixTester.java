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

package de.dante.extex.interpreter.primitives.prefix;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the prefix primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class PrefixTester extends ExTeXLauncher {

    /**
     * The field <tt>primitive</tt> contains the ...
     */
    private String primitive;

    /**
     * Constructor for PrefixTester.
     *
     * @param name the name of the test suite
     */
    public PrefixTester(final String name, final String primitive) {

        super(name);
        this.primitive = primitive;
    }

    /**
     * <testcase>
     *  Test case checking that the prefix before a letter leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPrefixLetter1() throws Exception {

        runFailureCode(//--- input code ---
                "\\" + primitive + " a",
                //--- log message ---
                "You can\'t use the prefix `\\" + primitive
                        + "' with the letter a");
    }

    /**
     * <testcase>
     *  Test case checking that the prefix before a digit leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPrefixOther1() throws Exception {

        runFailureCode(//--- input code ---
                "\\" + primitive + " 1",
                //--- log message ---
                "You can\'t use the prefix `\\" + primitive
                        + "' with the character 1");
    }

    /**
     * <testcase>
     *  Test case checking that the prefix before a left brace
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPrefixBeginGroup1() throws Exception {

        runFailureCode(//--- input code ---
                DEFINE_CATCODES + "\\" + primitive + " {",
                //--- log message ---
                "You can\'t use the prefix `\\" + primitive
                        + "' with begin-group character {");
    }

    /**
     * <testcase>
     *  Test case checking that the prefix before a right brace
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPrefixEndGroup1() throws Exception {

        runFailureCode(//--- input code ---
                DEFINE_CATCODES + "{\\" + primitive + " }",
                //--- log message ---
                "You can\'t use the prefix `\\" + primitive
                        + "' with end-group character }");
    }

    /**
     * <testcase>
     *  Test case checking that the prefix before a math shift
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPrefixMathShift1() throws Exception {

        runFailureCode(//--- input code ---
                DEFINE_CATCODES + "\\" + primitive + " $ ",
                //--- log message ---
                "You can\'t use the prefix `\\" + primitive
                        + "' with math shift character $");
    }

    /**
     * <testcase>
     *  Test case checking that the prefix before an alignment tab
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPrefixTab1() throws Exception {

        runFailureCode(//--- input code ---
                DEFINE_CATCODES + "\\" + primitive + " &",
                //--- log message ---
                "You can\'t use the prefix `\\" + primitive
                        + "' with alignment tab character &");
    }

    /**
     * <testcase>
     *  Test case checking that the prefix before a subscript mark
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPrefixSub1() throws Exception {

        runFailureCode(//--- input code ---
                DEFINE_CATCODES + "$\\" + primitive + " _",
                //--- log message ---
                "You can\'t use the prefix `\\" + primitive
                        + "' with subscript character _");
    }

    /**
     * <testcase>
     *  Test case checking that the prefix before a superscript mark
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPrefixSuper1() throws Exception {

        runFailureCode(//--- input code ---
                DEFINE_CATCODES + "$\\" + primitive + " ^",
                //--- log message ---
                "You can\'t use the prefix `\\" + primitive
                        + "' with superscript character ^");
    }

}
