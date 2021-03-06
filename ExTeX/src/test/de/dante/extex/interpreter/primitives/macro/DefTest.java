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

package de.dante.extex.interpreter.primitives.macro;

/**
 * This is a test suite for the primitive <tt>\def</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class DefTest extends AbstractDefTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public DefTest(final String arg) {

        super(arg, "def");
    }

    /**
     * <testcase primitive="\def">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\def\\aaa{AAA}"
                        + "{\\global\\def\\aaa{BBB}}" + "--\\aaa--\\end",
                //--- output message ---
                "--BBB--" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHashArgument3() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH + "\\" + getDef()
                        + "\\a{\\def\\b##1{B}}" + "\\a \\b2\\end",
                //--- output channel ---
                "B" + TERM);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHashArgument4() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + DEFINE_HASH + "\\" + getDef()
                        + "\\a{\\def\\b##1{B##1B}}" + "\\a \\b2\\end",
                //--- output channel ---
                "B2B" + TERM);
    }

}
