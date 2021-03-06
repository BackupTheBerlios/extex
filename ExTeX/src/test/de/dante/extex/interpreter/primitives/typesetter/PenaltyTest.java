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

package de.dante.extex.interpreter.primitives.typesetter;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\penalty</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class PenaltyTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(PenaltyTest.class);
    }

    /**
     * Constructor for PenaltyTest.
     *
     * @param arg the name
     */
    public PenaltyTest(final String arg) {

        super(arg, "penalty", "12");
    }

    /**
     * <testcase primitive="\penalty">
     *  Test case checking that <tt>\penalty</tt> inserts a penalty node.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "x\\penalty -12 x\\end ",
                //--- output channel ---
                "\\vbox(8.0pt+0.0pt)x3000.0pt\n" + //
                ".\\hbox(8.0pt+0.0pt)x3000.0pt\n" + //
                "..x\n" + //
                "..\\penalty -12\n" + //
                "..x\n");
    }

}
