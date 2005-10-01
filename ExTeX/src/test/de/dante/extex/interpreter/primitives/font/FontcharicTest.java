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

package de.dante.extex.interpreter.primitives.font;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\fontcharic</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FontcharicTest extends ExTeXLauncher {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public FontcharicTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\fontcharic">
     *  Test case checking that <tt>\fontcharic</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVertical1() throws Exception {

        runFailureCode(//--- input code ---
                "\\fontcharic ",
                //--- log message ---
                "You can't use `\\fontcharic' in vertical mode");
    }

    /**
     * <testcase primitive="\fontcharic">
     *  Test case checking that <tt>\fontcharic</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runFailureCode(//--- input code ---
                "\\dimen0=\\fontcharic ",
                //--- log message ---
                "Unexpected end of file while processing \\fontcharic");
    }

    /**
     * <testcase primitive="\fontcharic">
     *  Test case checking that <tt>\fontcharic</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        runFailureCode(//--- input code ---
                "\\dimen0=\\fontcharic\\nullfont ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\fontcharic" checked="etex">
     *  Test case checking that <tt>\fontcharic</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmslPoint() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmsl cmsl10 "
                + "\\the\\fontcharic\\cmsl `.\\end",
                //--- output message ---
                "0.0pt\n\n");
    }

    /**
     * <testcase primitive="\fontcharic" checked="etex">
     *  Test case checking that <tt>\fontcharic</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmslL() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmsl cmsl10 "
                + "\\the\\fontcharic\\cmsl `l\\end",
                //--- output message ---
                "0.39354pt\n\n");
    }

    /**
     * <testcase primitive="\fontcharic" checked="etex">
     *  Test case checking that <tt>\fontcharic</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmslX() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmsl cmsl10 "
                + "\\the\\fontcharic\\cmsl `x\\end",
                //--- output message ---
                "0.68982pt\n\n");
    }

    /**
     * <testcase primitive="\fontcharic" checked="etex">
     *  Test case checking that <tt>\fontcharic</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmslPlus2() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmsl cmsl10 "
                + "\\dimen0=\\fontcharic\\cmsl `+\\the\\dimen0\\end",
                //--- output message ---
                "0.1389pt\n\n");
    }

    /**
     * <testcase primitive="\fontcharic">
     *  Test case checking that <tt>\fontcharic</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmslPlus3() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmsl cmsl10 "
                + "\\count0=\\fontcharic\\cmsl `+\\the\\count0\\end",
                //--- output message ---
                "9103\n\n");
    }

}
