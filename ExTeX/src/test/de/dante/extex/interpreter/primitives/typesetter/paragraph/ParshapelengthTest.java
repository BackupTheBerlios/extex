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

package de.dante.extex.interpreter.primitives.typesetter.paragraph;

import de.dante.test.NonExecuteTester;

/**
 * This is a test suite for the primitive <tt>\parshapelength</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ParshapelengthTest extends NonExecuteTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ParshapelengthTest.class);
    }

    /**
     * Constructor for ParshapelengthTest.
     *
     * @param arg the name
     */
    public ParshapelengthTest(final String arg) {

        super(arg, "parshapelength", "");
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that <tt>\parshapelength</tt> is applicable to
     *  <tt>\the</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\parshapelength0 \\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that <tt>\parshapelength</tt> on a negative index
     *  returns 0pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\parshapelength-1 \\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertSuccess(//--- input code ---
                "\\the\\parshapelength2 \\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertSuccess(//--- input code ---
                "\\parshape1 12pt 24pt \\the\\parshapelength0 \\end",
                //--- output channel ---
                "24.0pt" + TERM);
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        assertSuccess(//--- input code ---
                "\\parshape2 12pt 24pt 36pt 48pt \\the\\parshapelength0 \\end",
                //--- output channel ---
                "24.0pt" + TERM);
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test6() throws Exception {

        assertSuccess(//--- input code ---
                "\\parshape2 12pt 24pt 36pt 48pt \\the\\parshapelength1 \\end",
                //--- output channel ---
                "48.0pt" + TERM);
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that the last value is repeated.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test7() throws Exception {

        assertSuccess(//--- input code ---
                "\\parshape2 12pt 24pt 36pt 48pt \\the\\parshapelength2 \\end",
                //--- output channel ---
                "48.0pt" + TERM);
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that the last value is repeated.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test8() throws Exception {

        assertSuccess(//--- input code ---
                "\\parshape2 12pt 24pt 36pt 48pt \\the\\parshapelength222 \\end",
                //--- output channel ---
                "48.0pt" + TERM);
    }

    /**
     * <testcase primitive="\parshapelength">
     *  Test case showing that <tt>\parshapelength</tt> is count-convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testConvertible1() throws Exception {

        assertSuccess(//--- input code ---
                "\\count1=\\parshapelength0 \\the\\count1\\end",
                //--- output channel ---
                "0" + TERM);
    }

    //TODO implement primitive specific test cases

}
