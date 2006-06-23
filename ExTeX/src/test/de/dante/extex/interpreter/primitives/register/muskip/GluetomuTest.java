/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.muskip;

import de.dante.test.NonExecuteTester;

/**
 * This is a test suite for the primitive <tt>\gluetomu</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class GluetomuTest extends NonExecuteTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(GluetomuTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public GluetomuTest(final String arg) {

        super(arg, "gluetomu", "0pt ");
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMuskipRegisterGroup4() throws Exception {

        assertOutput(
        //--- input code ---
                "\\muskip0=\\gluetomu1.2pt \\showthe\\muskip0 \\end",
                //--- output channel ---
                "> 1.2mu.\n", "");
    }

    //TODO implement the primitive specific test cases

}
