/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.math;

/**
 * This is a test suite for the primitive <tt>&#x5c;underline</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class UnderlineTest extends AbstractMathTester {

    /**
     * Constructor for UnderlineTest.
     *
     * @param arg the name
     */
    public UnderlineTest(final String arg) {

        super(arg, "underline", " x");
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                + "$\\underline x$ \\end",
                //--- output channel ---
                "\\vbox(4.30554pt+1.9999pt)x3000.0pt\n"
                + ".\\hbox(4.30554pt+1.9999pt)x3000.0pt\n"
                + "..\\vbox(4.30554pt+1.9999pt)x5.71527pt\n"
                + "...\\hbox(4.30554pt+0.0pt)x5.71527pt\n"
                + "....x\n"
                + "...\\kern0.0pt\n"
                + "...\\rule0.39998pt+0.0ptx5.71527pt\n"
                + "...\\kern0.0pt\n");
    }

    //TODO implement more primitive specific test cases

}
