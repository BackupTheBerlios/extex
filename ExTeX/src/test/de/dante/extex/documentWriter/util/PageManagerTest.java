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

package de.dante.extex.documentWriter.util;

import junit.framework.TestCase;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class PageManagerTest extends TestCase {

    /**
     * Command line interface for the tests.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(PageManagerTest.class);
    }

    /**
     * Run a test case.
     *
     * @param reference the string containing the indicator of the expeced
     *  result
     * @param spec the specification to test
     */
    private void run(final String reference, final String spec) {

        PageManager pm = new PageManager();
        if (spec != null) {
            pm.addPages(spec);
        }

        for (int i = 0; i < reference.length(); i++) {
            if (reference.charAt(i) == '_') {
                assertTrue("true " + Integer.toString(i), pm.isSelected(i));
            } else {
                assertFalse("false " + Integer.toString(i), pm.isSelected(i));
            }
        }
    }

    /**
     * <testcase>
     *  Check that initially all pages are accepted.
     * </testcase>
     */
    public void testInit() {

        run("____________________", null);
    }

    /**
     * <testcase>
     *  Check that no pages are accepted for the empty specification.
     * </testcase>
     */
    public void testEmpty() {

        run("01234567890123456789", "");
    }

    /**
     * <testcase>
     *  Check that no pages are accepted for the empty specification.
     * </testcase>
     */
    public void testSpace() {

        run("01234567890123456789", " ");
    }

    /**
     * <testcase>
     * </testcase>
     */
    public void test1() {

        run("0_234567890123456789", "1");
    }

    /**
     * <testcase>
     * </testcase>
     */
    public void test2() {

        run("01_34567890123456789", "2");
    }

    /**
     * <testcase>
     * </testcase>
     */
    public void test3() {

        run("0_2_4567890123456789", "1,3");
    }

    /**
     * <testcase>
     * </testcase>
     */
    public void test4() {

        run("___34567890123456789", "-2");
    }

    /**
     * <testcase>
     * </testcase>
     */
    public void test5() {

        run("012345678901________", "12-");
    }

    /**
     * <testcase>
     * </testcase>
     */
    public void test10() {

        run("0_2_4567_____3456789", "1,3,8-12");
    }

    /**
     * <testcase>
     * </testcase>
     */
    public void test11() {

        run("0____________3456789", "1-9,8-12");
    }

}
