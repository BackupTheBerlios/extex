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

package de.dante.extex.interpreter.primitives.group;

import de.dante.extex.interpreter.primitives.register.count.AbstractReadonlyCountRegisterTester;

/**
 * This is a test suite for the primitive <tt>\currentgrouplevel</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class CurrentgrouplevelTest extends AbstractReadonlyCountRegisterTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(CurrentgrouplevelTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public CurrentgrouplevelTest(final String arg) {

        super(arg, "currentgrouplevel", "0");
    }

    /**
     * <testcase primitive="\currentgrouplevel">
     *  Test case checking that <tt>\currentgrouplevel</tt> inside a group
     *  returns 1.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLevel1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "{\\the\\currentgrouplevel}\\end",
                //--- log message ---
                "1" + TERM);
    }

    /**
     * <testcase primitive="\currentgrouplevel">
     *  Test case checking that <tt>\currentgrouplevel</tt> inside a group
     *  in a group returns 2.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLevel2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "{{\\the\\currentgrouplevel}}\\end",
                //--- log message ---
                "2" + TERM);
    }

    /**
     * <testcase primitive="\currentgrouplevel">
     *  Test case checking that <tt>\currentgrouplevel</tt> is count
     *  convertible.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testConvertible1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "{{\\count0=\\currentgrouplevel \\the\\count0}}\\end",
                //--- log message ---
                "2" + TERM);
    }

    //TODO implement more primitive specific test cases

}
