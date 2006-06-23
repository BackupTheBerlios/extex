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

package de.dante.extex.interpreter.primitives.pdftex;

import de.dante.test.NoFlagsButImmediateAndProtectedPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\pdfthread</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class PdfthreadTest extends NoFlagsButImmediateAndProtectedPrimitiveTester {

    /**
     * Creates a new threadect.
     *
     * @param arg the name
     */
    public PdfthreadTest(final String arg) {

        super(arg, "pdfthread", " name {a}{}");
        setConfig("pdftex-112");
    }

    /**
     * <testcase primitive="\pdfthread">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "a \\pdfthread name b",
                //--- output message ---
                "Missing `{' inserted");
    }

    //TODO implement more primitive specific test cases

}
