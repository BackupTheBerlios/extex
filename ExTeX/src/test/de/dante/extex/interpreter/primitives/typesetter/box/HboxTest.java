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

package de.dante.extex.interpreter.primitives.typesetter.box;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\hbox</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class HboxTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(HboxTest.class);
    }

    /**
     * Constructor for HboxTest.
     *
     * @param arg the name
     */
    public HboxTest(final String arg) {

        super(arg, "hbox", "{} ");
    }

    /**
     * <testcase primitive="\hbox">
     *  Test case checking that <tt>\hbox</tt> on the empty box works.
     * </testcase>
     */
    public void testEmpty1() throws Exception {

        assertSuccess(DEFINE_BRACES + "\\hbox{}\\end",
                //
                "" + TERM);
    }

    /**
     * <testcase primitive="\hbox">
     *  Test case checking that <tt>\hbox</tt> on a non-empty box works.
     * </testcase>
     */
    public void test1() throws Exception {

        assertSuccess(DEFINE_BRACES + "\\hbox{abc}\\end",
                //
                "abc" + TERM);
    }

    //TODO implement primitive specific test cases

}
