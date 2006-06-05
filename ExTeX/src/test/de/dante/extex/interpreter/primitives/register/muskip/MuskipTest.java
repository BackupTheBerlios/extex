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

package de.dante.extex.interpreter.primitives.register.muskip;

/**
 * This is a test suite for the primitive <tt>\muskip</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class MuskipTest extends AbstractMuskipRegisterTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(MuskipTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public MuskipTest(final String arg) {

        super(arg, "muskip", "42", "0.0mu");
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> needs mu as unit.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        assertFailure(//--- input code ---
                "\\muskip0=1pt",
                //--- log message ---
                "Illegal unit of measure (mu inserted)");
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can parse a constant with
     *  integer number.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                "\\muskip0=1mu"
                + "\\the\\muskip0\\end",
                //--- log message ---
                "1.0mu" + TERM);
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can parse a constant with
     *  fraction number.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                "\\muskip0=1.23mu"
                + "\\the\\muskip0\\end",
                //--- log message ---
                "1.23mu" + TERM);
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can parse a constant with
     *  negative fraction number.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertSuccess(//--- input code ---
                "\\muskip0=-1.23mu"
                + "\\the\\muskip0\\end",
                //--- log message ---
                "-1.23mu" + TERM);
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can parse a constant with
     *  negative fraction number.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertOutput(//--- input code ---
                "\\muskip0=1mu plus 1fil"
                + "\\showthe\\muskip0\\end",
                //--- log message ---
                "> 1.0mu plus 1.0fil.\n", "");
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can parse a variable.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertOutput(//--- input code ---
                "\\muskip0=1.23mu"
                + "\\muskip1=\\muskip0"
                + "\\showthe\\muskip1\\end",
                //--- log message ---
                "> 1.23mu.\n", "");
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can parse a ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test11() throws Exception {

        assertSuccess(//--- input code ---
                "\\count0=2 "
                + "\\muskip0=\\count0mu"
                + "\\the\\muskip0\\end",
                //--- log message ---
                "2.0mu" + TERM);
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can advance its value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAdvance1() throws Exception {

        assertSuccess(//--- input code ---
                "\\muskip1=2mu"
                + "\\advance\\muskip1 1mu"
                + "\\the\\muskip1\\end",
                //--- log message ---
                "3.0mu" + TERM);
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can advance its value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAdvance2() throws Exception {

        assertSuccess(//--- input code ---
                "\\muskip0=1mu"
                + "\\muskip1=2mu"
                + "\\advance\\muskip1\\muskip0"
                + "\\the\\muskip1\\end",
                //--- log message ---
                "3.0mu" + TERM);
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can divide its value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDivide1() throws Exception {

        assertSuccess(//--- input code ---
                "\\muskip1=2mu"
                + "\\divide\\muskip1 2"
                + "\\the\\muskip1\\end",
                //--- log message ---
                "1.0mu" + TERM);
    }

    /**
     * <testcase primitive="\muskip">
     *  Test case checking that <tt>\muskip</tt> can multiply its value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMultiply1() throws Exception {

        assertSuccess(//--- input code ---
                "\\muskip1=2mu"
                + "\\multiply\\muskip1 2"
                + "\\the\\muskip1\\end",
                //--- log message ---
                "4.0mu" + TERM);
    }

    //TODO implement more primitive specific test cases

}
