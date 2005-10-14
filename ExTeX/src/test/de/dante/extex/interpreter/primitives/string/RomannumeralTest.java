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

package de.dante.extex.interpreter.primitives.string;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\romannumeral</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class RomannumeralTest extends NoFlagsPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(RomannumeralTest.class);
    }

    /**
     * Constructor for RomannumeralTest.
     *
     * @param arg the name
     */
    public RomannumeralTest(final String arg) {

        super(arg, "romannumeral", "1");
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on a count with the value
     *  2 gives <tt>ii</tt>.
     * </testcase>
     */
    public void testCount1() throws Exception {

        assertSuccess("\\count0=2 \\romannumeral\\count0 \\end", "ii" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on -1 gives
     *  the empty token list.
     * </testcase>
     */
    public void test_1() throws Exception {

        assertSuccess("\\romannumeral -1 \\end", "");
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 0 gives
     *  the empty token list.
     * </testcase>
     */
    public void test0() throws Exception {

        assertSuccess("\\romannumeral 0 \\end", "");
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 1 gives
     *  <tt>i</tt>.
     * </testcase>
     */
    public void test1() throws Exception {

        assertSuccess("\\romannumeral 1 \\end", "i" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 2 gives
     *  <tt>ii</tt>.
     * </testcase>
     */
    public void test2() throws Exception {

        assertSuccess("\\romannumeral 2 \\end", "ii" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 3 gives
     *  <tt>iii</tt>.
     * </testcase>
     */
    public void test3() throws Exception {

        assertSuccess("\\romannumeral 3 \\end", "iii" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 4 gives
     *  <tt>iv</tt>.
     * </testcase>
     */
    public void test4() throws Exception {

        assertSuccess("\\romannumeral 4 \\end", "iv" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 5 gives
     *  <tt>v</tt>.
     * </testcase>
     */
    public void test5() throws Exception {

        assertSuccess("\\romannumeral 5 \\end", "v" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 6 gives
     *  <tt>vi</tt>.
     * </testcase>
     */
    public void test6() throws Exception {

        assertSuccess("\\romannumeral 6 \\end", "vi" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 7 gives
     *  <tt>vii</tt>.
     * </testcase>
     */
    public void test7() throws Exception {

        assertSuccess("\\romannumeral 7 \\end", "vii" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 8 gives
     *  <tt>viii</tt>.
     * </testcase>
     */
    public void test8() throws Exception {

        assertSuccess("\\romannumeral 8 \\end", "viii" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 9 gives
     *  <tt>ix</tt>.
     * </testcase>
     */
    public void test9() throws Exception {

        assertSuccess("\\romannumeral 9 \\end", "ix" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 10 gives
     *  <tt>x</tt>.
     * </testcase>
     */
    public void test10() throws Exception {

        assertSuccess("\\romannumeral 10 \\end", "x" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 11 gives
     *  <tt>xi</tt>.
     * </testcase>
     */
    public void test11() throws Exception {

        assertSuccess("\\romannumeral 11 \\end", "xi" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 12 gives
     *  <tt>xii</tt>.
     * </testcase>
     */
    public void test12() throws Exception {

        assertSuccess("\\romannumeral 12 \\end", "xii" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 13 gives
     *  <tt>xiii</tt>.
     * </testcase>
     */
    public void test13() throws Exception {

        assertSuccess("\\romannumeral 13 \\end", "xiii" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 14 gives
     *  <tt>xiv</tt>.
     * </testcase>
     */
    public void test14() throws Exception {

        assertSuccess("\\romannumeral 14 \\end", "xiv" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 15 gives
     *  <tt>xv</tt>.
     * </testcase>
     */
    public void test15() throws Exception {

        assertSuccess("\\romannumeral 15 \\end", "xv" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 49 gives
     *  <tt>xlix</tt>.
     * </testcase>
     */
    public void test49() throws Exception {

        assertSuccess("\\romannumeral 49 \\end", "xlix" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 50 gives
     *  <tt>l</tt>.
     * </testcase>
     */
    public void test50() throws Exception {

        assertSuccess("\\romannumeral 50 \\end", "l" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 51 gives
     *  <tt>li</tt>.
     * </testcase>
     */
    public void test51() throws Exception {

        assertSuccess("\\romannumeral 51 \\end", "li" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 99 gives
     *  <tt>xcix</tt>.
     * </testcase>
     */
    public void test99() throws Exception {

        assertSuccess("\\romannumeral 99 \\end", "xcix" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 100 gives
     *  <tt>c</tt>.
     * </testcase>
     */
    public void test100() throws Exception {

        assertSuccess("\\romannumeral 100 \\end", "c" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 101 gives
     *  <tt>ci</tt>.
     * </testcase>
     */
    public void test101() throws Exception {

        assertSuccess("\\romannumeral 101 \\end", "ci" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 499 gives
     *  <tt>cdxcix</tt>.
     * </testcase>
     */
    public void test499() throws Exception {

        assertSuccess("\\romannumeral 499 \\end", "cdxcix" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 500 gives
     *  <tt>d</tt>.
     * </testcase>
     */
    public void test500() throws Exception {

        assertSuccess("\\romannumeral 500 \\end", "d" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 501 gives
     *  <tt>di</tt>.
     * </testcase>
     */
    public void test501() throws Exception {

        assertSuccess("\\romannumeral 501 \\end", "di" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 999 gives
     *  <tt>cmxcix</tt>.
     * </testcase>
     */
    public void test999() throws Exception {

        assertSuccess("\\romannumeral 999 \\end", "cmxcix" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 1000 gives
     *  <tt>m</tt>.
     * </testcase>
     */
    public void test1000() throws Exception {

        assertSuccess("\\romannumeral 1000 \\end", "m" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 1001 gives
     *  <tt>mi</tt>.
     * </testcase>
     */
    public void test1001() throws Exception {

        assertSuccess("\\romannumeral 1001 \\end", "mi" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 1999 gives
     *  <tt>mcmxcix</tt>.
     * </testcase>
     */
    public void test1999() throws Exception {

        assertSuccess("\\romannumeral 1999 \\end", "mcmxcix" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 2000 gives
     *  <tt>mm</tt>.
     * </testcase>
     */
    public void test2000() throws Exception {

        assertSuccess("\\romannumeral 2000 \\end", "mm" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 3001 gives
     *  <tt>mmmi</tt>.
     * </testcase>
     */
    public void test3001() throws Exception {

        assertSuccess("\\romannumeral 3001 \\end", "mmmi" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 4001 gives
     *  <tt>mmmmi</tt>.
     * </testcase>
     */
    public void test4001() throws Exception {

        assertSuccess("\\romannumeral 4001 \\end", "mmmmi" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 5001 gives
     *  <tt>mmmmmi</tt>.
     * </testcase>
     */
    public void test5001() throws Exception {

        assertSuccess("\\romannumeral 5001 \\end", "mmmmmi" + TERM);
    }

    /**
     * <testcase primitive="\romannumeral">
     *  Test case checking that <tt>\romannumeral</tt> on 6001 gives
     *  <tt>mmmmmmi</tt>.
     * </testcase>
     */
    public void test6001() throws Exception {

        assertSuccess("\\romannumeral 6001 \\end", "mmmmmmi" + TERM);
    }
}
