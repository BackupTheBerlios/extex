/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.scanner;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\scantokens</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ScantokensTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ScantokensTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ScantokensTest(final String arg) {

        super(arg, "scantokens", "{}");
    }

    /**
     * <testcase primitive="\scantokens">
     *  Test case checking that <tt>\scantokens</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\scantokens{a}\\end",
                //--- output channel ---
                "a" + TERM);
    }

    /**
     * <testcase primitive="\scantokens">
     *  Test case checking that <tt>\scantokens</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testEscapechar1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\escapechar=65 \\scantokens{\\xxx}\\end",
                //--- output channel ---
                "Axxx" + TERM);
    }


    /**
     * <testcase primitive="\scantokens">
     *  Test case checking that <tt>\scantokens</tt> inserts the tokens from
     *  <tt>\everyeof</tt>.
     * </testcase>
     *
     * Note: The white-space inserted by \scantokens is correct
     * (checked with e-TeX)
     *
     * @throws Exception in case of an error
     */
    public void testEveryeof1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\everyeof{x}\\scantokens{a}b\\end",
                //--- output channel ---
                "a xb" + TERM);
    }

    //TODO implement more primitive specific test cases

}
