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

package de.dante.extex.interpreter.primitives.file;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\read</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ReadTest extends ExTeXLauncher {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ReadTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ReadTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\read">
     *  Test case checking that a <tt>\read</tt> works on unopened file
     *  handles.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        runFailureCode(//--- input code ---
                "\\read 1",
                //--- error channel ---
                "Missing `to' inserted");
    }

    /**
     * <testcase primitive="\read">
     *  Test case checking that a <tt>\read</tt> works on unopened file
     *  handles.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runFailureCode(//--- input code ---
                "\\read 1 to ",
                //--- error channel ---
                "Missing control sequence inserted");
    }

}