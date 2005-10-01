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
 * This is a test suite for the primitive <tt>\fontchardp</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class FontchardpTest extends ExTeXLauncher {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public FontchardpTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\fontchardp">
     *  Test case checking that <tt>\fontchardp</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVertical1() throws Exception {

        runFailureCode(//--- input code ---
                "\\fontchardp ",
                //--- log message ---
                "You can't use `\\fontchardp' in vertical mode");
    }

    /**
     * <testcase primitive="\fontchardp">
     *  Test case checking that <tt>\fontchardp</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runFailureCode(//--- input code ---
                "\\dimen0=\\fontchardp ",
                //--- log message ---
                "Unexpected end of file while processing \\fontchardp");
    }

    /**
     * <testcase primitive="\fontchardp">
     *  Test case checking that <tt>\fontchardp</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEof2() throws Exception {

        runFailureCode(//--- input code ---
                "\\dimen0=\\fontchardp\\nullfont ",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\fontchardp">
     *  Test case checking that <tt>\fontchardp</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrA() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\the\\fontchardp\\cmr `a\\end",
                //--- output message ---
                "0.0pt\n\n");
    }

    /**
     * <testcase primitive="\fontchardp" checked="etex">
     *  Test case checking that <tt>\fontchardp</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrG() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\the\\fontchardp\\cmr `g\\end",
                //--- output message ---
                "1.94444pt\n\n");
    }

    /**
     * <testcase primitive="\fontchardp" checked="etex">
     *  Test case checking that <tt>\fontchardp</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrPlus() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\the\\fontchardp\\cmr `+\\end",
                //--- output message ---
                "0.83333pt\n\n");
    }

    /**
     * <testcase primitive="\fontchardp" checked="etex">
     *  Test case checking that <tt>\fontchardp</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrPlus2() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\dimen0\\fontchardp\\cmr `+\\the\\dimen0\\end",
                //--- output message ---
                "0.83333pt\n\n");
    }

    /**
     * <testcase primitive="\fontchardp">
     *  Test case checking that <tt>\fontchardp</tt> can not be used in vertical
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCmrPlus3() throws Exception {

        runCode(//--- input code ---
                "\\font\\cmr cmr10 "
                + "\\count0=\\fontchardp\\cmr `+\\the\\count0\\end",
                //--- output message ---
                "54613\n\n");
    }

}
