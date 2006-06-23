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

package de.dante.extex.interpreter.primitives.pdftex;

import de.dante.test.NoFlagsButProtectedPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\pdfstartlink</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class PdfstartlinkTest extends NoFlagsButProtectedPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public PdfstartlinkTest(final String arg) {

        super(arg, "pdfstartlink", " user{u}", "a ");
        setConfig("pdftex-112");
    }

    /**
     * <testcase primitive="\pdfstartlink">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "a \\pdfstartlink A",
                //--- output message ---
                "pdfTeX error (ext1): action type missing");
    }

    /**
     * <testcase primitive="\pdfstartlink">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError2() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "a \\pdfstartlink thread A",
                //--- output message ---
                "pdfTeX error (ext1): identifier type missing");
    }

    /**
     * <testcase primitive="\pdfstartlink">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError3() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "a \\pdfstartlink goto A",
                //--- output message ---
                "pdfTeX error (ext1): identifier type missing");
    }

    /**
     * <testcase primitive="\pdfstartlink">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError4() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES + "a \\pdfstartlink file A",
                //--- output message ---
                "pdfTeX error (ext1): action type missing");
    }

}
