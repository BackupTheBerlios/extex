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
 * This is a test suite for the primitive <tt>\fontcharht</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FontcharhtTest extends ExTeXLauncher {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public FontcharhtTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\fontcharht">
     *  Test case checking that <tt>\fontcharht</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVertical1() throws Exception {

        runFailureCode(//--- input code ---
                "\\fontcharht ",
                //--- log message ---
                "You can't use `\\fontcharht' in vertical mode");
    }

    /**
     * <testcase primitive="\fontcharht">
     *  Test case checking that <tt>\fontcharht</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runFailureCode(//--- input code ---
                "\\dimen0=\\fontcharht ",
                //--- log message ---
                "Unexpected end of file while processing \\fontcharht");
    }

    /**
     * <testcase primitive="\fontcharht">
     *  Test case checking that <tt>\fontcharht</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        runFailureCode(//--- input code ---
                "\\dimen0=\\fontcharht\\nullfont ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\fontcharht" checked="etex">
     *  Test case checking that <tt>\fontcharht</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrPoint() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\the\\fontcharht\\cmr `.\\end",
                //--- output message ---
                "1.05554pt\n\n");
    }

    /**
     * <testcase primitive="\fontcharht" checked="etex">
     *  Test case checking that <tt>\fontcharht</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrG() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\the\\fontcharht\\cmr `g\\end",
                //--- output message ---
                "4.30554pt\n\n");
    }

    /**
     * <testcase primitive="\fontcharht" checked="etex">
     *  Test case checking that <tt>\fontcharht</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrPlus() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\the\\fontcharht\\cmr `+\\end",
                //--- output message ---
                "5.83333pt\n\n");
    }

    /**
     * <testcase primitive="\fontcharht" checked="etex">
     *  Test case checking that <tt>\fontcharht</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrPlus2() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\dimen0=\\fontcharht\\cmr `+\\the\\dimen0\\end",
                //--- output message ---
                "5.83333pt\n\n");
    }

    /**
     * <testcase primitive="\fontcharht">
     *  Test case checking that <tt>\fontcharht</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrPlus3() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\count0=\\fontcharht\\cmr `+\\the\\count0\\end",
                //--- output message ---
                "382293\n\n");
    }

}
