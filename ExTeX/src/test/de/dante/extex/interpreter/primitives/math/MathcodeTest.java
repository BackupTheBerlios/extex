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

package de.dante.extex.interpreter.primitives.math;

import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.type.math.MathClass;
import de.dante.extex.interpreter.type.math.MathCode;
import de.dante.test.NoFlagsButGlobalPrimitiveTester;
import de.dante.util.UnicodeChar;

/**
 * This is a test suite for the primitive <tt>\mathcode</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class MathcodeTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Constructor for MathcodeTest.
     *
     * @param arg the name
     */
    public MathcodeTest(final String arg) {

        super(arg, "mathcode", "12=32 ");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(
        //--- input code ---
                "\\mathcode`. \"10041" + " \\end",
                //--- output message ---
                "Bad mathchar (65601)");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr2() throws Exception {

        assertFailure(
        //--- input code ---
                "\\mathcode`. 32769" + " \\end",
                //--- output message ---
                "Bad mathchar (32769)");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRange0() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\mathcode`. 0" + " \\end",
                //--- output message ---
                "");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRange1() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\mathcode`. 32767" + " \\end",
                //--- output message ---
                "");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRange2() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\mathcode`. 32768" + " \\end",
                //--- output message ---
                "");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr3() throws Exception {

        assertFailure(
        //--- input code ---
                "\\mathcode`. -1" + " \\end",
                //--- output message ---
                "Bad mathchar (-1)");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOrdinary1() throws Exception {

        Interpreter interpreter = assertSuccess(
        //--- input code ---
                "\\mathcode`. \"41" + "\\end",
                //--- output message ---
                "");
        MathCode mc = interpreter.getContext()
                .getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.ORDINARY, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals('A', mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLarge1() throws Exception {

        Interpreter interpreter = assertSuccess(
        //--- input code ---
                "\\mathcode`. \"1041" + "\\end",
                //--- output message ---
                "");
        MathCode mc = interpreter.getContext()
                .getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.LARGE, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals('A', mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBin1() throws Exception {

        Interpreter interpreter = assertSuccess(
        //--- input code ---
                "\\mathcode`. \"2041" + "\\end",
                //--- output message ---
                "");
        MathCode mc = interpreter.getContext()
                .getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.BINARY, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals('A', mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRel1() throws Exception {

        Interpreter interpreter = assertSuccess(
        //--- input code ---
                "\\mathcode`. \"3041" + "\\end",
                //--- output message ---
                "");
        MathCode mc = interpreter.getContext()
                .getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.RELATION, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals('A', mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOpen1() throws Exception {

        Interpreter interpreter = assertSuccess(
        //--- input code ---
                "\\mathcode`. \"4041" + "\\end",
                //--- output message ---
                "");
        MathCode mc = interpreter.getContext()
                .getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.OPENING, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals('A', mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testClose1() throws Exception {

        Interpreter interpreter = assertSuccess(
        //--- input code ---
                "\\mathcode`. \"5041" + "\\end",
                //--- output message ---
                "");
        MathCode mc = interpreter.getContext()
                .getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.CLOSING, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals('A', mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPunc1() throws Exception {

        Interpreter interpreter = assertSuccess(
        //--- input code ---
                "\\mathcode`. \"6041" + "\\end",
                //--- output message ---
                "");
        MathCode mc = interpreter.getContext()
                .getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.PUNCTUATION, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals('A', mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVar1() throws Exception {

        Interpreter interpreter = assertSuccess(
        //--- input code ---
                "\\mathcode`. \"7041" + "\\end",
                //--- output message ---
                "");
        MathCode mc = interpreter.getContext()
                .getMathcode(UnicodeChar.get('.'));
        assertEquals(MathClass.VARIABLE, mc.getMathClass());
        assertEquals(0, mc.getMathGlyph().getFamily());
        assertEquals('A', mc.getMathGlyph().getCharacter().getCodePoint());
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that <tt>\mathcode</tt> is convertible into a count.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount1() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\mathcode`.=1234 " + "\\count0=\\mathcode`.\\the\\count0\\end",
                //--- output message ---
                "1234" + TERM);
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that <tt>\mathcode</tt> is theable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testThe1() throws Exception {

        assertSuccess(
        //--- input code ---
                "\\mathcode`.=1234 " + "\\the\\mathcode`.\\end",
                //--- output message ---
                "1234" + TERM);
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test100() throws Exception {

        assertSuccess(
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                        + "\\mathcode`. \"41" + "$a.b$\\end",
                //--- output message ---
                "aAb" + TERM);
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testActiveErr1() throws Exception {

        assertFailure(
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH + DEFINE_BRACES
                + "\\mathcode`. \"8000" + "$a.b$\\end",
                //--- output message ---
                "Undefined control sequence .");
    }

    /**
     * <testcase primitive="\mathcode">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testActive1() throws Exception {

        assertSuccess(
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH + DEFINE_BRACES
                + "\\catcode`\\.=13 \\def.{xxx}"
                + "\\catcode`\\.=12 "
                + "\\mathcode`. \"8000" + "$a.b$\\end",
                //--- output message ---
                "axxxb" + TERM);
    }

    //TODO implement more primitive specific test cases

}
