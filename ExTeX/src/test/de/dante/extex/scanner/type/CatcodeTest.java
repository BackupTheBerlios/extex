/*
 * Copyright (C) 2004  Gerd Neugebauer
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.scanner.type;

import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.CatcodeVisitor;
import junit.framework.TestCase;

/**
 * Test cases for Catcodes
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class CatcodeTest extends TestCase implements CatcodeVisitor {

    /**
     * The field <tt>CAT_INVALID</tt> contains the numerical value for invalid
     * characters.
     */
    private static final int CAT_INVALID = 15;

    /**
     * The field <tt>CAT_COMMENT</tt> contains the numerical value for comment
     * characters.
     */
    private static final int CAT_COMMENT = 14;

    /**
     * The field <tt>CAT_ACTIVE</tt> contains the numerical value for active
     * characters.
     */
    private static final int CAT_ACTIVE = 13;

    /**
     * The field <tt>CAT_OTHER</tt> contains the numerical value for other
     * characters.
     */
    private static final int CAT_OTHER = 12;

    /**
     * The field <tt>CAT_LETTER</tt> contains the numerical value for letter
     * characters.
     */
    private static final int CAT_LETTER = 11;

    /**
     * The field <tt>CAT_SPACE</tt> contains the numerical value for space
     * characters.
     */
    private static final int CAT_SPACE = 10;

    /**
     * The field <tt>CAT_IGNORE</tt> contains the numerical value for ignored
     * characters.
     */
    private static final int CAT_IGNORE = 9;

    /**
     * The field <tt>CAT_SUB</tt> contains the numerical value for subscript
     * mark characters.
     */
    private static final int CAT_SUB = 8;

    /**
     * The field <tt>CAT_SUP</tt> contains the numerical value for superscript
     * mark characters.
     */
    private static final int CAT_SUP = 7;

    /**
     * The field <tt>CAT_HASH</tt> contains the numerical value for hash
     * mark characters &ndash; used for macro parameters.
     */
    private static final int CAT_HASH = 6;

    /**
     * The field <tt>CAT_CR</tt> contains the numerical value for carridge
     * return characters.
     */
    private static final int CAT_CR = 5;

    /**
     * The field <tt>CAT_TAB</tt> contains the numerical value for TAB
     * mark characters.
     */
    private static final int CAT_TAB = 4;

    /**
     * The field <tt>CAT_MATH</tt> contains the numerical value for math shift
     * characters.
     */
    private static final int CAT_MATH = 3;

    /**
     * The field <tt>CAT_RIGHT</tt> contains the numerical value for right brace
     * characters.
     */
    private static final int CAT_RIGHT = 2;

    /**
     * The field <tt>CAT_LEFT</tt> contains the numerical value for left brace
     * characters.
     */
    private static final int CAT_LEFT = 1;

    /**
     * The field <tt>CAT_ESC</tt> contains the numerical value for escape
     * characters.
     */
    private static final int CAT_ESC = 0;

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(CatcodeTest.class);
    }

    /**
     * Test that escape tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode0() throws Exception {

        assertEquals(Catcode.ESCAPE, Catcode.toCatcode(CAT_ESC));
        assertEquals(CAT_ESC, Catcode.ESCAPE.getCode());
        assertEquals("escape", Catcode.ESCAPE.getName());
        assertEquals("escape", Catcode.ESCAPE.toString());
    }

    /**
     * Test that left brace tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode1() throws Exception {

        assertEquals(Catcode.LEFTBRACE, Catcode.toCatcode(CAT_LEFT));
        assertEquals(CAT_LEFT, Catcode.LEFTBRACE.getCode());
        assertEquals("leftbrace", Catcode.LEFTBRACE.getName());
        assertEquals("leftbrace", Catcode.LEFTBRACE.toString());
    }

    /**
     * Test that right brace tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode2() throws Exception {

        assertEquals(Catcode.RIGHTBRACE, Catcode.toCatcode(CAT_RIGHT));
        assertEquals(CAT_RIGHT, Catcode.RIGHTBRACE.getCode());
        assertEquals("rightbrace", Catcode.RIGHTBRACE.getName());
        assertEquals("rightbrace", Catcode.RIGHTBRACE.toString());
    }

    /**
     * Test that math shift tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode3() throws Exception {

        assertEquals(Catcode.MATHSHIFT, Catcode.toCatcode(CAT_MATH));
        assertEquals(CAT_MATH, Catcode.MATHSHIFT.getCode());
        assertEquals("mathshift", Catcode.MATHSHIFT.getName());
        assertEquals("mathshift", Catcode.MATHSHIFT.toString());
    }

    /**
     * Test that tab mark tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode4() throws Exception {

        assertEquals(Catcode.TABMARK, Catcode.toCatcode(CAT_TAB));
        assertEquals(CAT_TAB, Catcode.TABMARK.getCode());
        assertEquals("tabmark", Catcode.TABMARK.getName());
        assertEquals("tabmark", Catcode.TABMARK.toString());
    }

    /**
     * Test that CR tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode5() throws Exception {

        assertEquals(Catcode.CR, Catcode.toCatcode(CAT_CR));
        assertEquals(CAT_CR, Catcode.CR.getCode());
        assertEquals("cr", Catcode.CR.getName());
        assertEquals("cr", Catcode.CR.toString());
    }

    /**
     * Test that macro parameter tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode6() throws Exception {

        assertEquals(Catcode.MACROPARAM, Catcode.toCatcode(CAT_HASH));
        assertEquals(CAT_HASH, Catcode.MACROPARAM.getCode());
        assertEquals("macroparam", Catcode.MACROPARAM.getName());
        assertEquals("macroparam", Catcode.MACROPARAM.toString());
    }

    /**
     * Test that superscript mark tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode7() throws Exception {

        assertEquals(Catcode.SUPMARK, Catcode.toCatcode(CAT_SUP));
        assertEquals(CAT_SUP, Catcode.SUPMARK.getCode());
        assertEquals("supmark", Catcode.SUPMARK.getName());
        assertEquals("supmark", Catcode.SUPMARK.toString());
    }

    /**
     * Test that subscript mark tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode8() throws Exception {

        assertEquals(Catcode.SUBMARK, Catcode.toCatcode(CAT_SUB));
        assertEquals(CAT_SUB, Catcode.SUBMARK.getCode());
        assertEquals("submark", Catcode.SUBMARK.getName());
        assertEquals("submark", Catcode.SUBMARK.toString());
    }

    /**
     * Test that ignore tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode9() throws Exception {

        assertEquals(Catcode.IGNORE, Catcode.toCatcode(CAT_IGNORE));
        assertEquals(CAT_IGNORE, Catcode.IGNORE.getCode());
        assertEquals("ignore", Catcode.IGNORE.getName());
        assertEquals("ignore", Catcode.IGNORE.toString());
    }

    /**
     * Test that space tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode10() throws Exception {

        assertEquals(Catcode.SPACE, Catcode.toCatcode(CAT_SPACE));
        assertEquals(CAT_SPACE, Catcode.SPACE.getCode());
        assertEquals("space", Catcode.SPACE.getName());
        assertEquals("space", Catcode.SPACE.toString());
    }

    /**
     * Test that letter tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode11() throws Exception {

        assertEquals(Catcode.LETTER, Catcode.toCatcode(CAT_LETTER));
        assertEquals(CAT_LETTER, Catcode.LETTER.getCode());
        assertEquals("letter", Catcode.LETTER.getName());
        assertEquals("letter", Catcode.LETTER.toString());
    }

    /**
     * Test that other tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode12() throws Exception {

        assertEquals(Catcode.OTHER, Catcode.toCatcode(CAT_OTHER));
        assertEquals(CAT_OTHER, Catcode.OTHER.getCode());
        assertEquals("other", Catcode.OTHER.getName());
        assertEquals("other", Catcode.OTHER.toString());
    }

    /**
     * Test that active tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode13() throws Exception {

        assertEquals(Catcode.ACTIVE, Catcode.toCatcode(CAT_ACTIVE));
        assertEquals(CAT_ACTIVE, Catcode.ACTIVE.getCode());
        assertEquals("active", Catcode.ACTIVE.getName());
        assertEquals("active", Catcode.ACTIVE.toString());
    }

    /**
     * Test that comment tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode14() throws Exception {

        assertEquals(Catcode.COMMENT, Catcode.toCatcode(CAT_COMMENT));
        assertEquals(CAT_COMMENT, Catcode.COMMENT.getCode());
        assertEquals("comment", Catcode.COMMENT.getName());
        assertEquals("comment", Catcode.COMMENT.toString());
    }

    /**
     * Test that invalid tokens have correct code, name, and string
     * representation.
     *
     * @throws Exception in case of an error
     */
    public void testCatcode15() throws Exception {

        assertEquals(Catcode.INVALID, Catcode.toCatcode(CAT_INVALID));
        assertEquals(CAT_INVALID, Catcode.INVALID.getCode());
        assertEquals("invalid", Catcode.INVALID.getName());
        assertEquals("invalid", Catcode.INVALID.toString());
    }

    /**
     * Test that the static method toCatcode() throws an exception when invoked
     * with a negative number.
     *
     * @throws Exception in case of an error
     */
    public void testToCatcodeFail1() throws Exception {

        try {
            Catcode.toCatcode(-1);
            assertFalse("Test succeeded unexpectedly", true);
        } catch (CatcodeException e) {
            assertTrue(true);
        }
    }

    /**
     * Test that the static method toCatcode() throws an exception when invoked
     * with a to large number.
     *
     * @throws Exception in case of an error
     */
    public void testToCatcodeFail2() throws Exception {

        try {
            Catcode.toCatcode(16);
            assertFalse("Test succeeded unexpectedly", true);
        } catch (CatcodeException e) {
            assertTrue(true);
        }
    }

    /**
     * The field <tt>visited</tt> contains the indicator that a visitor has been
     * invoked. It will be set by the visit* methods to the catcode encountered.
     */
    private static int visited = -1;

    /**
     * Test that the catcode visitor works for escape tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit0() throws Exception {

        assertEquals("esc", Catcode.ESCAPE.visit(this, "1", "2", null));
        assertEquals(CAT_ESC, visited);
    }

    /**
     * Test that the catcode visitor works for left brace tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit1() throws Exception {

        assertEquals("{", Catcode.LEFTBRACE.visit(this, "1", "2", null));
        assertEquals(CAT_LEFT, visited);
    }

    /**
     * Test that the catcode visitor works for right brace tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit2() throws Exception {

        assertEquals("}", Catcode.RIGHTBRACE.visit(this, "1", "2", null));
        assertEquals(CAT_RIGHT, visited);
    }

    /**
     * Test that the catcode visitor works for math shift tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit3() throws Exception {

        assertEquals("$", Catcode.MATHSHIFT.visit(this, "1", "2", null));
        assertEquals(CAT_MATH, visited);
    }

    /**
     * Test that the catcode visitor works for tab mark tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit4() throws Exception {

        assertEquals("&", Catcode.TABMARK.visit(this, "1", "2", null));
        assertEquals(CAT_TAB, visited);
    }

    /**
     * Test that the catcode visitor works for CR tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit5() throws Exception {

        assertEquals("cr", Catcode.CR.visit(this, "1", "2", null));
        assertEquals(CAT_CR, visited);
    }

    /**
     * Test that the catcode visitor works for macro parameter tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit6() throws Exception {

        assertEquals("#", Catcode.MACROPARAM.visit(this, "1", "2", null));
        assertEquals(CAT_HASH, visited);
    }

    /**
     * Test that the catcode visitor works for superscript mark tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit7() throws Exception {

        assertEquals("^", Catcode.SUPMARK.visit(this, "1", "2", null));
        assertEquals(CAT_SUP, visited);
    }

    /**
     * Test that the catcode visitor works for subscript mark tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit8() throws Exception {

        assertEquals("_", Catcode.SUBMARK.visit(this, "1", "2", null));
        assertEquals(CAT_SUB, visited);
    }

    /**
     * Test that the catcode visitor works for ignore tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit9() throws Exception {

        assertEquals("ignore", Catcode.IGNORE.visit(this, "1", "2", null));
        assertEquals(CAT_IGNORE, visited);
    }

    /**
     * Test that the catcode visitor works for space tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit10() throws Exception {

        assertEquals(" ", Catcode.SPACE.visit(this, "1", "2", null));
        assertEquals(CAT_SPACE, visited);
    }

    /**
     * Test that the catcode visitor works for letter tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit11() throws Exception {

        assertEquals("letter", Catcode.LETTER.visit(this, "1", "2", null));
        assertEquals(CAT_LETTER, visited);
    }

    /**
     * Test that the catcode visitor works for other tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit12() throws Exception {

        assertEquals(".", Catcode.OTHER.visit(this, "1", "2", null));
        assertEquals(CAT_OTHER, visited);
    }

    /**
     * Test that the catcode visitor works for active tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit13() throws Exception {

        assertEquals("active", Catcode.ACTIVE.visit(this, "1", "2", null));
        assertEquals(CAT_ACTIVE, visited);
    }

    /**
     * Test that the catcode visitor works for comment tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit14() throws Exception {

        assertEquals("%", Catcode.COMMENT.visit(this, "1", "2", null));
        assertEquals(CAT_COMMENT, visited);
    }

    /**
     * Test that the catcode visitor works for invalid tokens.
     *
     * @throws Exception in case of an error
     */
    public void testVisit15() throws Exception {

        assertEquals("invalid", Catcode.INVALID.visit(this, "1", "2", null));
        assertEquals(CAT_INVALID, visited);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitActive(
     *      java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitActive(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_ACTIVE;
        return "active";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitComment(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_COMMENT;
        return "%";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitCr(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_CR;
        return "cr";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitEscape(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_ESC;
        return "esc";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitIgnore(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_IGNORE;
        return "ignore";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitInvalid(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_INVALID;
        return "invalid";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitLeftBrace(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_LEFT;
        return "{";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitLetter(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_LETTER;
        return "letter";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitMacroParam(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_HASH;
        return "#";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitMathShift(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_MATH;
        return "$";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitOther(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_OTHER;
        return ".";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRightBrace(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitRightBrace(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_RIGHT;
        return "}";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitSpace(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_SPACE;
        return " ";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitSubMark(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_SUB;
        return "_";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitSupMark(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_SUP;
        return "^";
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(
     *      java.lang.final Object,
     *      java.lang.Object, java.lang.final Object)
     */
    public final Object visitTabMark(final Object arg1, final Object arg2,
            final Object uc) {

        assertEquals("1", arg1);
        assertEquals("2", arg2);
        visited = CAT_TAB;
        return "&";
    }
}