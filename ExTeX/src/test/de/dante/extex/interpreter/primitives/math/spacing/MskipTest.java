/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.math.spacing;

import de.dante.extex.interpreter.primitives.math.AbstractMathTester;

/**
 * This is a test suite for the primitive <tt>\mskip</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class MskipTest extends AbstractMathTester {

    /**
     * Constructor for MskipTest.
     *
     * @param arg the name
     */
    public MskipTest(final String arg) {

        super(arg, "mskip", " 1mu");
    }

    /**
     * <testcase primitive="\mskip">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_MATH
                        + "$a\\mskip12mu b$\\end",
                //--- output message ---
                "\\vbox(6.94444pt+0.0pt)x3000.0pt\n"
                        + ".\\hbox(6.94444pt+0.0pt)x3000.0pt\n" //
                        + "..a\n" //
                        + "..\\glue12.0pt\n" // TODO gene: convert mu to sp
                        + "..b\n");
    }

    //TODO implement more primitive specific test cases
}
