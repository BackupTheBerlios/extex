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
 * This is a test suite for the primitive <tt>\fontdimen</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class FontdimenTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for FontdimenTest.
     *
     * @param arg the name
     */
    public FontdimenTest(final String arg) {

        super(arg, "fontdimen", "0\\nullfont=123pt ");
    }

    /**
     * <testcase primitive="\fontdimen">
     *  Test case checking that \fontdimen on unset keys returns 0 pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testUnset1() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\fontdimen0\\nullfont "
                + "\\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\fontdimen">
     *  Test case checking that \fontdimen on unset keys returns 0 pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testUnset2() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\fontdimen65000\\nullfont "
                + "\\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\fontdimen">
     *  Test case checking that \fontdimen can be set and read back for \nullfont.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSet1() throws Exception {

        assertSuccess(//--- input code ---
                "\\fontdimen65000\\nullfont=42pt "
                + "\\the\\fontdimen65000\\nullfont "
                + "\\end",
                //--- output channel ---
                "42.0pt" + TERM);
    }

    /**
     * <testcase primitive="\fontdimen">
     *  Test case checking that \fontdimen can be set and read back for cmtt12.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSet2() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\fnt=cmtt12\\relax "
                + "\\fontdimen65000\\fnt=42pt "
                + "\\the\\fontdimen65000\\fnt "
                + "\\end",
                //--- output channel ---
                "42.0pt" + TERM);
    }

    /**
     * <testcase primitive="\fontdimen">
     *  Test case checking that \fontdimen is an assignment.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAssign1() throws Exception {

        assertSuccess(//--- input code ---
                "\\afterassignment abc"
                + "\\fontdimen0\\nullfont=42pt"
                + "\\the\\fontdimen0\\nullfont "
                + "\\end",
                //--- output channel ---
                "bca42.0pt" + TERM);
    }

}
