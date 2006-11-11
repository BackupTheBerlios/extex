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
 * This is a test suite for the primitive <tt>\nonscript</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class NonscriptTest extends AbstractMathTester {

    /**
     * Constructor for NonscriptTest.
     *
     * @param arg the name
     */
    public NonscriptTest(final String arg) {

        super(arg, "nonscript", "");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$x\\nonscript\\mskip 1mux$" //
                + "\\end",
                //--- output message ---
                "\\vbox(4.30554pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(4.30554pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..\\glue1.0pt\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\displaystyle x\\nonscript\\mskip 1mux$" //
                + "\\end",
                //--- output message ---
                "\\vbox(4.30554pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(4.30554pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..\\glue1.0pt\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\scriptstyle x\\nonscript\\mskip 1mux$" //
                + "\\end",
                //--- output message ---
                "\\vbox(3.01389pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(3.01389pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\scriptscriptstyle x\\nonscript\\mskip 1mux$" //
                + "\\end",
                //--- output message ---
                "\\vbox(2.15277pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(2.15277pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMKern1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$x\\nonscript\\mkern 1mux$" //
                + "\\end",
                //--- output message ---
                "\\vbox(4.30554pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(4.30554pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..\\kern0.55554pt\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMKern2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\displaystyle x\\nonscript\\mkern 1mux$" //
                + "\\end",
                //--- output message ---
                "\\vbox(4.30554pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(4.30554pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..\\kern0.55554pt\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMKern3() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\scriptstyle x\\nonscript\\mkern 1mux$" //
                + "\\end",
                //--- output message ---
                "\\vbox(3.01389pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(3.01389pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMKern4() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\scriptscriptstyle x\\nonscript\\mkern 1mux$" //
                + "\\end",
                //--- output message ---
                "\\vbox(2.15277pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(2.15277pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..x\n");
    }


    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testKern1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$x\\nonscript\\kern 1ptx$" //
                + "\\end",
                //--- output message ---
                "\\vbox(4.30554pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(4.30554pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..\\kern1.0pt\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testKern2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\displaystyle x\\nonscript\\kern 1ptx$" //
                + "\\end",
                //--- output message ---
                "\\vbox(4.30554pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(4.30554pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..\\kern1.0pt\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testKern3() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\scriptstyle x\\nonscript\\kern 1ptx$" //
                + "\\end",
                //--- output message ---
                "\\vbox(3.01389pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(3.01389pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..x\n");
    }

    /**
     * <testcase primitive="\nonscript">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testKern4() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                DEFINE_MATH_FONTS + DEFINE_MATH
                + "$\\scriptscriptstyle x\\nonscript\\kern 1ptx$" //
                + "\\end",
                //--- output message ---
                "\\vbox(2.15277pt+0.0pt)x3000.0pt\n"
                + ".\\hbox(2.15277pt+0.0pt)x3000.0pt\n"
                + "..x\n"
                + "..x\n");
    }

    //TODO implement more primitive specific test cases (displaymath?, cramped?)

}
