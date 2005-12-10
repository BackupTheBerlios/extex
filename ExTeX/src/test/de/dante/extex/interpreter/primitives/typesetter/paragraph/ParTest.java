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

package de.dante.extex.interpreter.primitives.typesetter.paragraph;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\par</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ParTest extends NoFlagsPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ParTest.class);
    }

    /**
     * Constructor for ParTest.
     *
     * @param arg the name
     */
    public ParTest(final String arg) {

        super(arg, "par", "");
    }

    /**
     * <testcase primitive="\par">
     *  Test case checking that <tt>\par</tt> works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hsize=123pt abc\\par def\\end ",
                //--- output channel ---
                "\\vbox(0.0pt+0.0pt)x0.0pt\n" + //
                ".\\hbox(0.0pt+0.0pt)x0.0pt\n" + //
                "..a\n" + //
                "..b\n" + //
                "..c\n" + //
                "\\vbox(0.0pt+0.0pt)x0.0pt\n" + //
                ".\\hbox(0.0pt+0.0pt)x0.0pt\n" + //
                "..d\n" + //
                "..e\n" + //
                "..f\n");
    }


    //TODO implement primitive specific test cases
}
