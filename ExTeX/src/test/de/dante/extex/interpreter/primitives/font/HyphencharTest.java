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

package de.dante.extex.interpreter.primitives.font;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\hyphenchar</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class HyphencharTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for HyphencharTest.
     *
     * @param arg the name
     */
    public HyphencharTest(final String arg) {

        super(arg, "hyphenchar", "\\nullfont=123 ");
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that an end of file leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runCode(//--- input code ---
                "\\hyphenchar ",
                //--- log message ---
                "Unexpected end of file while processing \\hyphenchar",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that an end of file leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        runCode(//--- input code ---
                "\\hyphenchar\\nullfont ",
                //--- log message ---
                "Missing number, treated as zero",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that a missing font identifier leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMissing1() throws Exception {

        runCode(//--- input code ---
                "\\hyphenchar x",
                //--- log message ---
                "Missing font identifier",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that a missing font identifier leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMissing2() throws Exception {

        runCode(//--- input code ---
                "\\hyphenchar \\x",
                //--- log message ---
                "Undefined control sequence the control sequence \\x",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that a correct value is produced when the hyphen char
     *  is not preset for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphencharNullfont0() throws Exception {

        runCode(//--- input code ---
                "\\the\\hyphenchar\\nullfont"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "45" + TERM);
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that a correct value is produced when the hyphen char
     *  is set to 123 for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphencharNullfont1() throws Exception {

        runCode(//--- input code ---
                "\\hyphenchar\\nullfont =123 \\relax"
                + "\\the\\hyphenchar\\nullfont"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that a correct value is produced when the hyphen char
     *  is set to undefined for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphencharNullfont2() throws Exception {

        runCode(//--- input code ---
                "\\hyphenchar\\nullfont =-1 \\relax"
                + "\\the\\hyphenchar\\nullfont"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "-1" + TERM);
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that \hyphenchar is countconvertible for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphencharNullfont3() throws Exception {

        runCode(//--- input code ---
                "\\hyphenchar\\nullfont =123 \\relax"
                + "\\count1=\\hyphenchar\\nullfont"
                + "\\the\\count 1"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that a correct value is produced when the hyphen char
     *  is not preset for a loaded \font.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphenchar0() throws Exception {

        runCode(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\the\\hyphenchar\\x"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "45" + TERM);
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that a correct value is produced when the hyphen char
     *  is set to 123 for a loaded \font.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphenchar1() throws Exception {

        runCode(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\hyphenchar\\x =123 \\relax"
                + "\\the\\hyphenchar\\x"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that a correct value is produced when the hyphen char
     *  is set to undefined for a loaded \font.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphenchar2() throws Exception {

        runCode(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\hyphenchar\\x =-1 \\relax"
                + "\\the\\hyphenchar\\x"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "-1" + TERM);
    }

    /**
     * <testcase primitive="\hyphenchar">
     *  Test case checking that \hyphenchar is countconvertible for a loaded \font.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphenchar3() throws Exception {

        runCode(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\hyphenchar\\x =123 \\relax"
                + "\\count1=\\hyphenchar\\x"
                + "\\the\\count 1"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "123" + TERM);
    }

}
