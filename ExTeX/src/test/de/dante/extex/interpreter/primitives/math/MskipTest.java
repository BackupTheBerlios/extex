/*
 * Copyright (C) 2004 Gerd Neugebauer
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.primitives.math;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\mskip</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MskipTest extends ExTeXLauncher {

    /**
     * Constructor for MskipTest.
     *
     * @param arg the name
     */
    public MskipTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that \mskip needs the math mode.
     * @throws Exception in case of an error
     */
    public void testNonMathMode() throws Exception {

        runCode(//--- input code ---
                "\\mskip \\end",
                //--- log message ---
                "Missing $ inserted",
                //--- output channel ---
                null);
    }

}
