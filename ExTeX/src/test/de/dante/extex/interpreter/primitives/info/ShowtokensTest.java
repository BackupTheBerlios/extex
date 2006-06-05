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

package de.dante.extex.interpreter.primitives.info;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\showtokens</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ShowtokensTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public ShowtokensTest(final String arg) {

        super(arg, "showtokens", "{abc}", "", "> abc.\n");
    }

    /**
     * <testcase primitive="\showtokens">
     *  Test case checking that <tt>\showtokens</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertOutput(//--- input code ---
                DEFINE_BRACES
                + "\\showtokens{\\relax}"
                + "\\end",
                //--- output channel ---
                "> \\relax.\n",
                "");
    }

    /**
     * <testcase primitive="\showtokens">
     *  Test case checking that <tt>\showtokens</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertOutput(//--- input code ---
                DEFINE_BRACES
                + "\\showtokens{\\jobname}"
                + "\\end",
                //--- output channel ---
                "> \\jobname.\n",
                "");
    }

    /**
     * <testcase primitive="\showtokens">
     *  Test case checking that <tt>\showtokens</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test12() throws Exception {

        assertOutput(//--- input code ---
                DEFINE_BRACES
                + "\\showtokens\\expandafter{\\jobname}"
                + "\\end",
                //--- output channel ---
                "> texput.\n",
                "");
    }

    //TODO implement the primitive specific test cases

}
