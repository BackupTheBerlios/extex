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

package de.dante.extex.interpreter.primitives.typesetter.displace;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\moveright</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MoverightTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(MoverightTest.class);
    }

    /**
     * Constructor for MoverightTest.
     *
     * @param arg the name
     */
    public MoverightTest(final String arg) {

        super(arg, "moveright", "1pt\\hbox{}");
    }

    /**
     * <testcase primitive="\moveright">
     *  Test case checking that <tt>\moveright</tt> on a void box works.
     * </testcase>
     */
    public void testVoid1() throws Exception {

        assertSuccess(DEFINE_BRACES + "\\moveright1pt\\box0 \\end",
        //
                "");
    }

    //TODO implement primitive specific test cases

}
