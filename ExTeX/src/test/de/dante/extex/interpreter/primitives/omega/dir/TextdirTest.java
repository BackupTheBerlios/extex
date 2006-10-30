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

package de.dante.extex.interpreter.primitives.omega.dir;

import de.dante.extex.interpreter.Interpreter;
import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>textdir</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TextdirTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(TextdirTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public TextdirTest(final String arg) {

        super(arg, "textdir", " LRL");
        setConfig("omega");
    }

    /**
     * <testcase primitive="\textdir">
     *  Test case checking that <tt>\textdir</tt> works ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr0() throws Exception {

        assertFailure(//--- input code ---
                "\\textdir",
                //--- output channel ---
                "Unexpected end of file");
    }

    /**
     * <testcase primitive="\textdir">
     *  Test case checking that <tt>\textdir</tt> works ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        assertFailure(//--- input code ---
                "\\textdir"
                + "\\end",
                //--- output channel ---
                "Bad direction");
    }

    /**
     * <testcase primitive="\textdir">
     *  Test case checking that <tt>\textdir</tt> works ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        Interpreter in = assertSuccess(//--- input code ---
                "\\textdir LRL"
                + "\\end",
                //--- output channel ---
                "");
        assertEquals("LRL",
                     in.getContext().getTypesettingContext().getDirection().toString());
    }

}
