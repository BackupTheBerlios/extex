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

package de.dante.extex.interpreter.primitives.group;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\currentgrouplevel</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class CurrentgrouplevelTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(CurrentgrouplevelTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public CurrentgrouplevelTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\currentgrouplevel">
     *  Test case checking that a lonely <tt>\currentgrouplevel</tt> leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1 "
                + "\\catcode`}=2 "
                +"\\currentgrouplevel",
                //--- log message ---
                "\\the\\currentgrouplevel",
                //--- output channel ---
                "");
    }

}