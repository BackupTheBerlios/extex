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

package de.dante.extex.interpreter.primitives.register.box;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\copy</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class CopyTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(CopyTest.class);
    }

    /**
     * Constructor for CopyTest.
     *
     * @param arg the name
     */
    public CopyTest(final String arg) {

        super(arg, "copy", "1", DEFINE_BRACES);
    }

    /**
     * <testcase primitive="\copy">
     *  Test case checking that <tt>\copy</tt> throws an error if a number is
     *  missing.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        assertFailure(//--- input code ---
                "\\copy a",
                //--- log message ---
                "Missing number, treated as zero");
    }

    /**
     * <testcase primitive="\copy">
     *  Test case checking that <tt>\copy</tt>
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\setbox1\\hbox{A}\\copy1\\end",
                //--- output channel ---
                "A" + TERM);
    }

    /**
     * <testcase primitive="\copy">
     *  Test case checking that <tt>\copy</tt> preserves the contents of the copy
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\setbox1\\hbox{A}\\copy1\\copy1\\end",
                //--- output channel ---
                "A\nA" + TERM);
    }

}