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
 * This is a test suite for the primitive <tt>\skewchar</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class SkewcharTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for SkewcharTest.
     *
     * @param arg the name
     */
    public SkewcharTest(final String arg) {

        super(arg, "skewchar", "\\nullfont=123 ");
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that an end of file leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        assertFailure(//--- input code ---
                "\\skewchar ",
                //--- log message ---
                "Unexpected end of file while processing \\skewchar");
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that an end of file leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        assertFailure(//--- input code ---
                "\\skewchar\\nullfont ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that a missing font identifier leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMissing1() throws Exception {

        assertFailure(//--- input code ---
                "\\skewchar x",
                //--- log message ---
                "Missing font identifier");
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that a missing font identifier leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMissing2() throws Exception {

        assertFailure(//--- input code ---
                "\\skewchar \\x",
                //--- log message ---
                "Undefined control sequence \\x");
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that a correct value is produced when the skew char
     *  is not preset for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewcharNullfont0() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\skewchar\\nullfont"
                + "\\end ",
                //--- output channel ---
                "-1" + TERM);
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that a correct value is produced when the skew char
     *  is set to 123 for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewcharNullfont1() throws Exception {

        assertSuccess(//--- input code ---
                "\\skewchar\\nullfont =123 \\relax"
                + "\\the\\skewchar\\nullfont"
                + "\\end ",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that a correct value is produced when the skew char
     *  is set to undefined for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewcharNullfont2() throws Exception {

        assertSuccess(//--- input code ---
                "\\skewchar\\nullfont =-1 \\relax"
                + "\\the\\skewchar\\nullfont"
                + "\\end ",
                //--- output channel ---
                "-1" + TERM);
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that \skewchar is countconvertible for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewcharNullfont3() throws Exception {

        assertSuccess(//--- input code ---
                "\\skewchar\\nullfont =123 \\relax"
                + "\\count1=\\skewchar\\nullfont"
                + "\\the\\count 1"
                + "\\end ",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that a correct value is produced when the skew char
     *  is not preset for a loaded \font.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewchar0() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\the\\skewchar\\x"
                + "\\end ",
                //--- output channel ---
                "-1" + TERM);
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that a correct value is produced when the skew char
     *  is set to 123 for a loaded \font.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewchar1() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\skewchar\\x =123 \\relax"
                + "\\the\\skewchar\\x"
                + "\\end ",
                //--- output channel ---
                "123" + TERM);
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that a correct value is produced when the skew char
     *  is set to undefined for a loaded \font.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewchar2() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\skewchar\\x =-1 \\relax"
                + "\\the\\skewchar\\x"
                + "\\end ",
                //--- output channel ---
                "-1" + TERM);
    }

    /**
     * <testcase primitive="\skewchar">
     *  Test case checking that \skewchar is countconvertible for a loaded \font.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewchar3() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\skewchar\\x =123 \\relax"
                + "\\count1=\\skewchar\\x"
                + "\\the\\count 1"
                + "\\end ",
                //--- output channel ---
                "123" + TERM);
    }

}
