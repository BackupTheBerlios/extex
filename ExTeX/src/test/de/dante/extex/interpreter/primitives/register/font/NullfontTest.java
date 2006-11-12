/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.font;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\nullfont</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class NullfontTest extends ExTeXLauncher {

    /**
     * Constructor for the test.
     *
     * @param arg the name
     */
    public NullfontTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> does not contain certain
     *  characters.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\nullfont " + "abcABC012,.-" + "\\end",
                //--- output channel ---
                "");
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> has empty font dimens.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFontdimen1() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\the\\fontdimen0\\nullfont "
                + "\\the\\fontdimen1\\nullfont "
                + "\\the\\fontdimen2\\nullfont "
                + "\\the\\fontdimen3\\nullfont "
                + "\\the\\fontdimen4\\nullfont "
                + "\\end",
                //--- output channel ---
                "0.0pt0.0pt0.0pt0.0pt0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> can change font dimens.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFontdimen2() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\fontdimen0\\nullfont = 123pt"
                + "\\the\\fontdimen0\\nullfont "
                + "\\end",
                //--- output channel ---
                "123.0pt" + TERM);
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> can change font dimens
     *  with a negative index.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFontdimen3() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\fontdimen-1\\nullfont = 123pt"
                + "\\the\\fontdimen-1\\nullfont "
                + "\\end",
                //--- output channel ---
                "123.0pt" + TERM);
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> can change font dimens
     *  with a string index.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testFontdimen4() throws Exception {

        assertSuccess(
        //--- input code ---
                DEFINE_BRACES
                + "\\fontdimen{abc}\\nullfont = 123pt"
                + "\\the\\fontdimen{abc}\\nullfont "
                + "\\end",
                //--- output channel ---
                "123.0pt" + TERM);
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> has no skew char set.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewchar1() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\the\\skewchar\\nullfont "
                + "\\end",
                //--- output channel ---
                "-1" + TERM);
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> can set skew char.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSkewchar2() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\skewchar\\nullfont =`A"
                + "\\the\\skewchar\\nullfont "
                + "\\end",
                //--- output channel ---
                "65" + TERM);
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> has a hyphen char set.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphenchar1() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\the\\hyphenchar\\nullfont "
                + "\\end",
                //--- output channel ---
                "45" + TERM);
    }

    /**
     * <testcase primitive="\nullfont">
     *  Test case checking that <tt>\nullfont</tt> can set hyphen char.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHyphenchar2() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\hyphenchar\\nullfont =`A"
                + "\\the\\hyphenchar\\nullfont "
                + "\\end",
                //--- output channel ---
                "65" + TERM);
    }

}
