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

package de.dante.extex.interpreter.primitives.register.dimen;

import de.dante.extex.interpreter.primitives.math.AbstractMathTester;

/**
 * This is a test suite for the primitive <tt>\mathsurround</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class MathsurroundTest extends AbstractDimenRegisterTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(MathsurroundTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public MathsurroundTest(final String arg) {

        super(arg, "mathsurround", "", "0.0pt");
    }

    /**
     * <testcase>
     *  Test case checking that <tt>\mathsurround</tt> is inserted before and
     *  after math.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                + "\\mathsurround=10pt"
                + "o$x$o \\end",
                //--- output channel ---
                "\\vbox(8.0pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(8.0pt+0.0pt)x3000.0pt\n"
                + "..o\n"
                + "..\\mathon, surrounded 10.0pt\n"
                + "..x\n"
                + "..\\mathoff, surrounded 10.0pt\n"
                + "..o\n");
    }

    /**
     * <testcase>
     *  Test case checking that <tt>\mathsurround</tt> is inserted before and
     *  after math &ndash; even when set within the math environment.
     * </testcase>
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                AbstractMathTester.DEFINE_MATH_FONTS + DEFINE_CATCODES
                + "o$x\\mathsurround=10pt$o \\end",
                //--- output channel ---
                "\\vbox(8.0pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(8.0pt+0.0pt)x3000.0pt\n"
                + "..o\n"
                + "..\\mathon, surrounded 10.0pt\n"
                + "..x\n"
                + "..\\mathoff, surrounded 10.0pt\n"
                + "..o\n");
    }

}
