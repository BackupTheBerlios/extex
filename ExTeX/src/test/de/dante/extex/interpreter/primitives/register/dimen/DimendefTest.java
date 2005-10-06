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

package de.dante.extex.interpreter.primitives.register.dimen;

/**
 * This is a test suite for the primitive <tt>\dimendef</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class DimendefTest extends AbstractDimenRegisterTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DimendefTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public DimendefTest(final String arg) {

        super(arg, "x", "", "0.0pt", "\\dimendef\\x=42 ");
    }

    /**
     * <testcase primitive="\dimendef">
     *  Test case checking that <tt>\dimendef</tt> creates a dimen assignable
     *  control sequence which is equivalent to the <tt>\dimen</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\dimendef\\x=42 " + "\\dimen42=123pt "
                        + "\\the\\dimen42 \\end",
                //--- output channel ---
                "123.0pt" + TERM);
    }

    /**
     * <testcase primitive="\dimendef">
     *  Test case checking that <tt>\dimendef</tt> respects a group.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal1() throws Exception {

        runFailureCode(//--- input code ---
                "\\begingroup\\dimendef\\x=42 \\endgroup" + "\\the\\x \\end",
                //--- error channel ---
                "You can't use `the control sequence \\x' after \\the");
    }

    /**
     * <testcase primitive="\dimendef">
     *  Test case checking that <tt>\dimendef</tt> respects a group.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal2() throws Exception {

        runCode(//--- input code ---
                "\\begingroup\\global\\dimendef\\x=42 \\endgroup"
                        + "\\the\\x \\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

}
