/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.skip;

/**
 * This is a test suite for the primitive <tt>\skipdef</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class SkipdefTest extends AbstractSkipRegisterTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(SkipdefTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public SkipdefTest(final String arg) {

        super(arg, "x", "", "0.0pt", "\\skipdef\\x=42 ");
    }

    /**
     * <testcase primitive="\skipdef">
     *  Test case checking that <tt>\skipdef</tt> creates a skip assignable
     *  control sequence which is equivalent to the <tt>\skip</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\skipdef\\x=42 " + "\\skip42=123pt " + "\\the\\skip42 \\end",
                //--- output channel ---
                "123.0pt" + TERM);
    }

    /**
     * <testcase primitive="\skipdef">
     *  Test case checking that <tt>\skipdef</tt> respects a group.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal1() throws Exception {

        assertFailure(//--- input code ---
                "\\begingroup\\skipdef\\x=42 \\endgroup" + "\\the\\x \\end",
                //--- error channel ---
                "Undefined control sequence \\x");
    }

    /**
     * <testcase primitive="\skipdef">
     *  Test case checking that <tt>\skipdef</tt> respects a group.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal2() throws Exception {

        assertSuccess(//--- input code ---
                "\\begingroup\\global\\skipdef\\x=42 \\endgroup"
                        + "\\the\\x \\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\skipdef">
     *  Test case checking that <tt>\skipdef</tt> respects <tt>\globaldefs</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal10() throws Exception {

        assertSuccess(//--- input code ---
                "\\globaldefs=1\\begingroup\\skipdef\\x=42 \\x=123pt\\endgroup"
                        + "\\the\\x \\end",
                //--- output channel ---
                "123.0pt" + TERM);
    }

    /**
     * <testcase primitive="\skipdef">
     *  Test case checking that <tt>\skipdef</tt> can take a tokens name.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testExt1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\skipdef\\x={abc}" + "\\the\\x \\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase primitive="\skipdef">
     *  Test case checking that <tt>\skipdef</tt> respects
     *  <tt>\afterassignment</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAfterassign1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES + "\\afterassignment XA\\skipdef\\x={abc}"
                        + "B \\end",
                //--- output channel ---
                "AXB" + TERM);
    }

}
