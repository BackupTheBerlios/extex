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

package de.dante.extex.interpreter.primitives.info;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\meaning</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MeaningTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public MeaningTest(final String arg) {

        super(arg, "meaning", "\\count1 ");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a letter is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        runCode(//--- input code ---
                "\\meaning a",
                //--- output channel ---
                "the letter a\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a digit is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOther1() throws Exception {

        runCode(//--- input code ---
                "\\meaning 1",
                //--- output channel ---
                "the character 1\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a open-group character
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLeft1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1" + "\\meaning {",
                //--- output channel ---
                "begin-group character {\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a close group character
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRight1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`}=2" + "\\meaning }",
                //--- output channel ---
                "end-group character }\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a begin-mat character
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMath1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`$=3" + "\\meaning $",
                //--- output channel ---
                "math shift character $\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a alignment tab
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTab1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`&=4" + "\\meaning &",
                //--- output channel ---
                "alignment tab character &\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a macro parameter
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHash1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`#=6" + "\\meaning #",
                //--- output channel ---
                "macro parameter character #\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a superscript
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSuper1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`^=7" + "\\meaning ^",
                //--- output channel ---
                "superscript character ^\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a subscript
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSub1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`_=8" + "\\meaning _",
                //--- output channel ---
                "subscript character _\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of an undefined active
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testActive1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`~=13" + "\\meaning ~",
                //--- output channel ---
                "~=undefined\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined active
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testActive2() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES + "\\catcode`~=13" + "\\def~{}" + "\\meaning ~",
                //--- output channel ---
                "~=macro:\n ->\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined active
     *  character is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testActive3() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES + "\\catcode`~=13" + "\\def~{abc}" + "\\meaning ~",
                //--- output channel ---
                "~=macro:\n ->abc\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of an undefined macro
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        runCode(//--- input code ---
                "\\meaning \\undef ",
                //--- output channel ---
                "\\undef=undefined\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of \relax is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\meaning \\relax ",
                //--- output channel ---
                "\\relax=\\relax\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of \meaning is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        runCode(//--- input code ---
                "\\meaning \\meaning ",
                //--- output channel ---
                "\\meaning=\\meaning\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined macro
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro0() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES + "\\def\\x{abc}" + "\\meaning \\x ",
                //--- output channel ---
                "\\x=macro:\n ->abc\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined macro with
     *  one parameter is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro1() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES + "\\def\\x#1{ab#1cd}" + "\\meaning \\x ",
                //--- output channel ---
                "\\x=macro:\n#1 ->ab#1cd\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of a defined macro with
     *  a complex parameter is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMacro2() throws Exception {

        runCode(//--- input code ---
                DEFINE_CATCODES + "\\def\\x A#1B#2{ab#1cd}" + "\\meaning \\x ",
                //--- output channel ---
                "\\x=macro:\nA#1B#2 ->ab#1cd\n\n");
    }

    /**
     * <testcase primitive="\meaning">
     *  Test case checking that the <tt>\meaning</tt> of an integer parameter
     *  is reported.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRegister1() throws Exception {

        runCode(//--- input code ---
                "\\meaning \\day ",
                //--- output channel ---
                "\\day=\\day\n\n");
    }


}
