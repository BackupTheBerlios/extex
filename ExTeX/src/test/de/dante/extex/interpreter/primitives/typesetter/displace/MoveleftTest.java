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
 * This is a test suite for the primitive <tt>\moveleft</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MoveleftTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(MoveleftTest.class);
    }

    /**
     * Constructor for MoveleftTest.
     *
     * @param arg the name
     */
    public MoveleftTest(final String arg) {

        super(arg, "moveleft", "1pt\\hbox{}");
    }

    /**
     * <testcase primitive="\moveleft">
     *  Test case checking that <tt>\moveleft</tt> on a void box works.
     * </testcase>
     */
    public void testVoid1() throws Exception {

        assertSuccess(DEFINE_BRACES + "\\moveleft1pt\\box0 \\end",
        //
                "");
    }

    //TODO implement primitive specific test cases

}
