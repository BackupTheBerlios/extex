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

package de.dante.extex.interpreter.primitives.macro;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for def primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractDefTester extends ExTeXLauncher {

    /**
     * The field <tt>primitive</tt> contains the name of the def.
     */
    private String def;

    /**
     * Creates a new object.
     *
     * @param name the name
     * @param def the name of the primitive
     */
    public AbstractDefTester(final String name, final String def) {

        super(name);
        this.def = def;
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testImmediate1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES
                + "\\immediate\\"+ def + "\\aaa{}",
                //--- log message ---
                "You can't use the prefix `\\immediate' with the control sequence \\"
                + def);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBasic1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\" + def + "\\aaa{AAA}"
                + "--\\aaa--\\end",
                //--- output message ---
                "--AAA--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that long is an accepted prefix.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLong1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\long\\" + def + "\\aaa{AAA}"
                + "--\\aaa--\\end",
                //--- output message ---
                "--AAA--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLong2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\long\\" + def + "\\aaa{AAA\\par BBB}"
                + "--\\aaa--\\end",
                //--- output message ---
                "--AAA\n\nBBB--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testArguments1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\" + def + "\\aaa#1{A#1A}"
                + "--\\aaa 1--\\end",
                //--- output message ---
                "--A1A--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testArguments2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\" + def + "\\aaa#1{A#1A}"
                + "--\\aaa {1}--\\end",
                //--- output message ---
                "--A1A--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testArguments3() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\" + def + "\\aaa#1{A#1A}"
                + "--\\aaa {12}--\\end",
                //--- output message ---
                "--A12A--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that two arguments are parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTwoArguments1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\" + def + "\\a#1#2{--#1--#2--}"
                + "\\a 2."
                + "\\end ",
                //--- output channel ---
                "--2--.--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPattern1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\"+ def + "\\aaa#1.{--#1--}"
                + "\\aaa 2."
                + "\\end ",
                //--- output channel ---
                "--2--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPattern2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\"+ def + "\\aaa#1.{--#1--}"
                + "\\aaa {2.1}."
                + "\\end ",
                //--- output channel ---
                "--2.1--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testBrace1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\"+ def + "\\aaa#1.{--#1--}"
                + "\\aaa }.",
                //--- output channel ---
                "Argument of \\aaa has an extra }");
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHashArgument1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\"+ def + "\\a{--##--}\\end",
                //--- output channel ---
                "");
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHashArgument2() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH
                + "\\"+ def + "\\a{--##--}\\a ",
                //--- output channel ---
                "You can't use `macro parameter character #' in horizontal mode");
    }

    
    /**
     * Getter for def.
     *
     * @return the def
     */
    public String getDef() {
    
        return this.def;
    }
}
