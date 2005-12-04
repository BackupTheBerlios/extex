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

/**
 * This is a test suite for the primitive <tt>\hfi</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class HfiTest extends AbstractHfillTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(HfiTest.class);
    }

    /**
     * Constructor for HfiTest.
     *
     * @param arg the name
     */
    public HfiTest(final String arg) {

        super(arg, "hfi", "");
    }

    /**
     * <testcase primitive="\hfi">
     *  Test case checking that <tt>\hfi</tt> switches to vertical mode and
     *  inserts a glue node with 1fi.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(showNodesProperties(),
        //--- input code ---
                "\\hfi\\end ",
                //--- output channel ---
                "\\vbox(0.0pt+0.0pt)x0.0pt\n" + //
                ".\\hbox(0.0pt+0.0pt)x0.0pt\n" + //
                "..\\glue0.0pt plus 1.0fi\n");
    }

}
