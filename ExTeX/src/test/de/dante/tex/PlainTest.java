/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.tex;

import de.dante.test.ExTeXLauncher;

/**
 * This is the test suite for plain.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class PlainTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(PlainTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public PlainTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase>
     *  Test case checking that a lonely <tt>\endgroup</tt> leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPlain() throws Exception {

        assertOutput(
                "\\input develop/test/data/plain-dump \\end",
                "",
                "Preloading the plain format: codes, registers, parameters, fonts, more fonts,\n"
                + "macros, math definitions, output routines, hyphenation");
    }

}
