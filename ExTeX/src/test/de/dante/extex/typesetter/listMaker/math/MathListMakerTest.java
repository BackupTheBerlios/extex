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

package de.dante.extex.typesetter.listMaker.math;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the math typesetting.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MathListMakerTest extends ExTeXLauncher {

    /**
     * The field <tt>DEFINE_MATH_FONTS</tt> contains the ...
     */
    private static final String DEFINE_MATH_FONTS =
        "\\font\\f cmsy10 \\textfont2=\\f"
        + "\\font\\f cmsy7 \\scriptfont2=\\f"
        + "\\font\\f cmsy5 \\scriptscriptfont2=\\f"
        + "\\font\\f cmex10 \\textfont3=\\f"
        + "\\scriptfont3=\\f"
        + "\\scriptscriptfont3=\\f"
        + "\\font\\f cmmi10 \\textfont1=\\f ";

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public MathListMakerTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase>
     *  Test case checking that an empty math environment works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEmpty1() throws Exception {

        assertSuccess(//showNodesProperties(),
                //--- input code ---
                DEFINE_MATH + DEFINE_MATH_FONTS
                + "\\hsize=123pt \\vsize=456pt "
                + "\\font\\f cmr10 \\f "
                + "x$ $x"
                + "\\end ",
                //--- output channel ---
                "xx" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that a single math character works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        assertSuccess(//showNodesProperties(),
                //--- input code ---
                DEFINE_MATH + DEFINE_MATH_FONTS
                + "$a$"
                + "\\end ",
                //--- output channel ---
                "a" + TERM);
    }
}