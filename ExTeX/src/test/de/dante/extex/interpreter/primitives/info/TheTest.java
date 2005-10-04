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
 * This is a test suite for the primitive <tt>\the</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class TheTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public TheTest(final String arg) {

        super(arg, "the", "\\count1 ");
    }

    /**
     * <testcase primitive="\the">
     *  Test case checking that the <tt>\the</tt> of a letter leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErrorLetter() throws Exception {

        runFailureCode(//--- input code ---
                "\\the a ",
                //--- error channel ---
                "You can't use `the letter a' after \\the");
    }

    /**
     * <testcase primitive="\the">
     *  Test case checking that the <tt>\the</tt> of a digit leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErrorOther() throws Exception {

        runFailureCode(//--- input code ---
                "\\the 1 ",
                //--- error channel ---
                "You can't use `the character 1' after \\the");
    }

    /**
     * <testcase primitive="\the">
     *  Test case checking that the <tt>\the</tt> of a count register works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount1() throws Exception {

        runCode(//--- input code ---
                "\\the \\count1 ",
                //--- output channel ---
                "0" + TERM);
    }

    /**
     * <testcase primitive="\the">
     *  Test case checking that the <tt>\the</tt> of a count register works.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount2() throws Exception {

        runCode(//--- input code ---
                "\\count1=123 \\the \\count1 ",
                //--- output channel ---
                "123" + TERM);
    }



}
