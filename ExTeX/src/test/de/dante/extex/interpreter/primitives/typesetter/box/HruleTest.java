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

package de.dante.extex.interpreter.primitives.typesetter.box;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\hrule</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class HruleTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(HruleTest.class);
    }

    /**
     * Constructor for HruleTest.
     *
     * @param name the name
     */
    public HruleTest(final String name) {

        super(name, "hrule", "");
    }

    /**
     * <testcase primitive="\hrule">
     *  Test case checking that <tt>\hrule</tt> switches to vertical mode and
     *  inserts a rule node. The default height is 0.4pt.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hrule\\end ",
                //--- output channel ---
                "\\vbox(0.4pt+0.0pt)x0.0pt\n" + //
                ".\\rule0.4pt+0.0ptx0.0pt\n");
    }

    /**
     * <testcase primitive="\hrule">
     *  Test case checking that <tt>\hrule</tt> switches to vertical mode and
     *  inserts a rule node of given height.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hrule height 1pt\\end ",
                //--- output channel ---
                "\\vbox(1.0pt+0.0pt)x0.0pt\n" + //
                ".\\rule1.0pt+0.0ptx0.0pt\n");
    }

    /**
     * <testcase primitive="\hrule">
     *  Test case checking that <tt>\hrule</tt> switches to vertical mode and
     *  inserts a rule node of given height, width, and depth.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hrule height 2pt depth 1pt width 42pt\\end ",
                //--- output channel ---
                "\\vbox(2.0pt+1.0pt)x42.0pt\n" + //
                ".\\rule2.0pt+1.0ptx42.0pt\n");
    }

}
