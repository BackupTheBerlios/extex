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
 * This is a test suite for the primitive <tt>\hskip</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class HskipTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(HskipTest.class);
    }

    /**
     * Constructor for HskipTest.
     *
     * @param arg the name
     */
    public HskipTest(final String arg) {

        super(arg, "hskip", "12pt", "A");
    }

    /**
     * <testcase primitive="\hskip">
     *  Test case checking that <tt>\hskip</tt> switches to vertical mode and
     *  inserts a glue node with the appropriate value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hskip 123pt\\end ",
                //--- output channel ---
                "\\vbox(0.0pt+0.0pt)x123.0pt\n" + //
                ".\\hbox(0.0pt+0.0pt)x123.0pt\n" + //
                "..\\glue123.0pt\n");
    }

    /**
     * <testcase primitive="\hskip">
     *  Test case checking that <tt>\hskip</tt> switches to vertical mode and
     *  inserts a glue node with the appropriate value.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hskip 123pt plus 1.2fil\\end ",
                //--- output channel ---
                "\\vbox(0.0pt+0.0pt)x123.0pt\n" + //
                ".\\hbox(0.0pt+0.0pt)x123.0pt\n" + //
                "..\\glue123.0pt plus 1.2fil\n");
    }

}
