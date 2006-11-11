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

package de.dante.extex.interpreter.primitives.math.symbol;

/**
 * This is a test suite for the primitive <tt>\mathord</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class MathordTest extends AbstractOperatorTester {

    /**
     * Constructor for MathordTest.
     *
     * @param arg the name
     */
    public MathordTest(final String arg) {

        super(arg, "mathord");
    }

    /**
     * <testcase>
     *  Test case checking that a single math character works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testLetter1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH + DEFINE_MATH_FONTS + "$\\mathord a$" + "\\end ",
                //--- output channel ---
                "\\vbox(4.30554pt+0.0pt)x3000.0pt\n"
                        + ".\\hbox(4.30554pt+0.0pt)x3000.0pt\n" + "..a\n");
    }

}
