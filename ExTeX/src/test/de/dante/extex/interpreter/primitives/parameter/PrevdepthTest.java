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

package de.dante.extex.interpreter.primitives.parameter;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\prevdepth</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class PrevdepthTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(PrevdepthTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public PrevdepthTest(final String arg) {

        super(arg, "prevdepth", "2pt");
    }

    /**
     * <testcase primitive="\prevdepth">
     *  Test case checking that <tt>\prevdepth</tt> in horizontal mode
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHorizontalMode1() throws Exception {

        assertFailure(//--- input code ---
                "a\\prevdepth=12pt ",
                //--- error channel ---
                "You can't use `\\prevdepth' in horizontal mode");
    }

    /**
     * <testcase primitive="\prevdepth">
     *  Test case checking that <tt>\prevdepth</tt> in horizontal mode
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHorizontalMode2() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "\\hbox{\\prevdepth=12pt} ",
                //--- error channel ---
                "You can't use `\\prevdepth' in restricted horizontal mode");
    }

    /**
     * <testcase primitive="\prevdepth">
     *  Test case checking that <tt>\prevdepth</tt> in math mode
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathMode1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "$\\prevdepth=12pt $",
                //--- error channel ---
                "You can't use `\\prevdepth' in math mode");
    }

    /**
     * <testcase primitive="\prevdepth">
     *  Test case checking that <tt>\prevdepth</tt> in display math mode
     *  leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathMode2() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "$$\\prevdepth=12pt $$",
                //--- error channel ---
                "You can't use `\\prevdepth' in displaymath mode");
    }

    /**
     * <testcase primitive="\prevdepth">
     *  Test case checking that <tt>\prevdepth</tt> has a default value of
     *  -1000pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\prevdepth\\end",
                //--- error channel ---
                "-1000.0pt" + TERM);
    }

    /**
     * <testcase primitive="\prevdepth">
     *  Test case checking that <tt>\prevdepth</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\prevdepth=12pt \\the\\prevdepth\\end",
                //--- error channel ---
                "12.0pt" + TERM);
    }

    /**
     * <testcase primitive="\prevdepth">
     *  Test case checking that <tt>\prevdepth</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\prevdepth=12pt \\dimen1=\\prevdepth \\the\\dimen1\\end",
                //--- error channel ---
                "12.0pt" + TERM);
    }

}