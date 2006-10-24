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

package de.dante.extex.interpreter.max;

import de.dante.extex.scanner.type.Catcode;
import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the interpreter Max.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class MaxTest extends ExTeXLauncher {

    /**
     * <testcase>
     *  Test case checking that the prefix leads to the expected error message
     * </testcase>
     *
     * @param prefix the prefix code
     * @param tag the character to be assigned
     * @param catcode the category
     * @param longName the name of tag
     *
     * @throws Exception in case of an error
     */
    private void tryFlag(final String prefix, final String tag,
            final Catcode catcode, final String longName) throws Exception {

        assertFailure(//--- input code ---
                "\\catcode`\\" + tag + "=" + catcode.getCode() + " \\" + prefix
                        + " " + tag + "\\end",
                //--- log message ---
                "You can\'t use the prefix `\\" + prefix + "\' with "
                        + longName);
    }

    /**
     * Constructor for MaxTest.
     *
     * @param arg the name
     */
    public MaxTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that a sole tab mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testTabMark() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode`&=4\\relax" + "&" + "\\end ",
                //--- log message ---
                "Misplaced alignment tab character &");
    }

    /**
     * Test case checking that a sole sub mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testSupMark() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode`^=7\\relax" + "^" + "\\end ",
                //--- log message ---
                "Missing $ inserted");
    }

    /**
     * Test case checking that a sole super mark leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testSubMark() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode`_=8\\relax" + "_" + "\\end ",
                //--- log message ---
                "Missing $ inserted");
    }

    /**
     * Test case checking that a sole macro parameter leads to an error message.
     *
     * @throws Exception in case of an error
     */
    public void testMacroParam() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode`#=6\\relax" + "#" + "\\end ",
                //--- log message ---
                "You can't use `macro parameter character #' in vertical mode");
    }

    /**
     * Test case checking that an undefined active character leads to an
     * error message.
     *
     * @throws Exception in case of an error
     */
    public void testUndefinedAcive() throws Exception {

        assertFailure(//--- input code ---
                "\\catcode`~=13\\relax" + "~" + "\\end ",
                //--- log message ---
                "Undefined control sequence ~");
    }

    /**
     * Test case checking that an undefined control sequence leads to an
     * error message.
     *
     * @throws Exception in case of an error
     */
    public void testUndefinedCs() throws Exception {

        assertFailure(//--- input code ---
                "\\UNDEF" + "\\end ",
                //--- log message ---
                "Undefined control sequence \\UNDEF");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTabNoGlobalFlag() throws Exception {

        tryFlag("global", "&", Catcode.TABMARK, "alignment tab character &");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTabNoImmediateFlag() throws Exception {

        tryFlag("immediate", "&", Catcode.TABMARK, "alignment tab character &");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTabNoLongFlag() throws Exception {

        tryFlag("long", "&", Catcode.TABMARK, "alignment tab character &");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTabNoOuterFlag() throws Exception {

        tryFlag("outer", "&", Catcode.TABMARK, "alignment tab character &");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTabNoProtectedFlag() throws Exception {

        tryFlag("protected", "&", Catcode.TABMARK, "alignment tab character &");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSubNoGlobalFlag() throws Exception {

        tryFlag("global", "_", Catcode.SUBMARK, "subscript character _");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSubNoImmediateFlag() throws Exception {

        tryFlag("immediate", "_", Catcode.SUBMARK, "subscript character _");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSubNoLongFlag() throws Exception {

        tryFlag("long", "_", Catcode.SUBMARK, "subscript character _");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSubNoOuterFlag() throws Exception {

        tryFlag("outer", "_", Catcode.SUBMARK, "subscript character _");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSubNoProtectedFlag() throws Exception {

        tryFlag("protected", "_", Catcode.SUBMARK, "subscript character _");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSuperNoGlobalFlag() throws Exception {

        tryFlag("global", "^", Catcode.SUPMARK, "superscript character ^");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSuperNoImmediateFlag() throws Exception {

        tryFlag("immediate", "^", Catcode.SUPMARK, "superscript character ^");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSuperNoLongFlag() throws Exception {

        tryFlag("long", "^", Catcode.SUPMARK, "superscript character ^");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSuperNoOuterFlag() throws Exception {

        tryFlag("outer", "^", Catcode.SUPMARK, "superscript character ^");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSuperNoProtectedFlag() throws Exception {

        tryFlag("protected", "^", Catcode.SUPMARK, "superscript character ^");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetterNoGlobalFlag() throws Exception {

        tryFlag("global", "A", Catcode.LETTER, "the letter A");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetterNoImmediateFlag() throws Exception {

        tryFlag("immediate", "A", Catcode.LETTER, "the letter A");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetterNoLongFlag() throws Exception {

        tryFlag("long", "A", Catcode.LETTER, "the letter A");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetterNoOuterFlag() throws Exception {

        tryFlag("outer", "A", Catcode.LETTER, "the letter A");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetterNoProtectedFlag() throws Exception {

        tryFlag("protected", "A", Catcode.LETTER, "the letter A");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOtherNoGlobalFlag() throws Exception {

        tryFlag("global", "1", Catcode.OTHER, "the character 1");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOtherNoImmediateFlag() throws Exception {

        tryFlag("immediate", "1", Catcode.OTHER, "the character 1");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOtherNoLongFlag() throws Exception {

        tryFlag("long", "1", Catcode.OTHER, "the character 1");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOtherNoOuterFlag() throws Exception {

        tryFlag("outer", "1", Catcode.OTHER, "the character 1");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testOtherNoProtectedFlag() throws Exception {

        tryFlag("protected", "1", Catcode.OTHER, "the character 1");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathshiftNoGlobalFlag() throws Exception {

        tryFlag("global", "$", Catcode.MATHSHIFT, "math shift character $");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathshiftNoImmediateFlag() throws Exception {

        tryFlag("immediate", "$", Catcode.MATHSHIFT, "math shift character $");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathshiftNoLongFlag() throws Exception {

        tryFlag("long", "$", Catcode.MATHSHIFT, "math shift character $");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathshiftNoOuterFlag() throws Exception {

        tryFlag("outer", "$", Catcode.MATHSHIFT, "math shift character $");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathshiftNoProtectedFlag() throws Exception {

        tryFlag("protected", "$", Catcode.MATHSHIFT, "math shift character $");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBegingroupNoGlobalFlag() throws Exception {

        tryFlag("global", "{", Catcode.LEFTBRACE, "begin-group character {");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBegingroupNoImmediateFlag() throws Exception {

        tryFlag("immediate", "{", Catcode.LEFTBRACE, "begin-group character {");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBegingroupNoLongFlag() throws Exception {

        tryFlag("long", "{", Catcode.LEFTBRACE, "begin-group character {");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBegingroupNoOuterFlag() throws Exception {

        tryFlag("outer", "{", Catcode.LEFTBRACE, "begin-group character {");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBegingroupNoProtectedFlag() throws Exception {

        tryFlag("protected", "{", Catcode.LEFTBRACE, "begin-group character {");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEndgroupNoGlobalFlag() throws Exception {

        tryFlag("global", "}", Catcode.RIGHTBRACE, "end-group character }");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEndgroupNoImmediateFlag() throws Exception {

        tryFlag("immediate", "}", Catcode.RIGHTBRACE, "end-group character }");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEndgroupNoLongFlag() throws Exception {

        tryFlag("long", "}", Catcode.RIGHTBRACE, "end-group character }");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEndgroupNoOuterFlag() throws Exception {

        tryFlag("outer", "}", Catcode.RIGHTBRACE, "end-group character }");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEndgroupNoProtectedFlag() throws Exception {

        tryFlag("protected", "}", Catcode.RIGHTBRACE, "end-group character }");
    }

    /**
     * <testcase>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEndgroup1() throws Exception {

        assertFailure( //
                DEFINE_BRACES + "}",
                //
                "Too many }'s");
    }

}
