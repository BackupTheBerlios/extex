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

package de.dante.extex.interpreter.primitives.typesetter;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\accent</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class AccentTest extends ExTeXLauncher {

    /**
     * Constructor for MathaccentTest.
     *
     * @param arg the name
     */
    public AccentTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that \accent does not work in math mode.
     * @throws Exception in case of an error
     */
    public void testAccentInMathMode() throws Exception {

        runCode(//--- input code ---
                "\\catcode`$=3 \\relax"
                + "$\\accent $\\end",
                //--- log message ---
                "?",
                //--- output channel ---
                "?");
    }

}