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

package de.dante.extex.interpreter.primitives.typesetter.spacing;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\vskip</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class VskipTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(VskipTest.class);
    }

    /**
     * Constructor for VskipTest.
     *
     * @param arg the name
     */
    public VskipTest(final String arg) {

        super(arg, "vskip", "12pt");
    }

    /**
     * <testcase primitive="\vskip">
     *  Test case checking that <tt>\vskip</tt> switches to vertical mode and
     *  inserts a glue node with the given value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\vskip 12pt\\end ",
                //--- output channel ---
                "\\vbox(12.0pt+0.0pt)x0.0pt\n" + //
                ".\\glue12.0pt\n");
    }

    /**
     * <testcase primitive="\vskip">
     *  Test case checking that <tt>\vskip</tt> switches to vertical mode and
     *  inserts a glue node with the given value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\vskip 12pt plus 3pt minus 4pt\\end ",
                //--- output channel ---
                "\\vbox(12.0pt+0.0pt)x0.0pt\n" + //
                ".\\glue12.0pt plus 3.0pt minus 4.0pt\n");
    }

    //TODO implement primitive specific test cases

}
