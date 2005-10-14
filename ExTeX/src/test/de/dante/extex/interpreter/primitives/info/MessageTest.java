/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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
 * This is a test suite for the primitive <tt>\message</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class MessageTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public MessageTest(final String arg) {

        super(arg, "message", "{}");
    }

    /**
     * <testcase primitive="\message">
     *  Test case checking that <tt>\message</tt> results in an error message,
     *  if the  following token is not a left brace.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMissingBrace1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_BRACES
                + "\\message }"
                + "\\end ",
                //--- log message ---
                "Missing `{' inserted");
    }

    /**
     * <testcase primitive="\message">
     *  Test case checking that <tt>\message</tt> results in an error message,
     *  if the following token is not a left brace.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMissingBrace2() throws Exception {

        assertFailure(//--- input code ---
                "\\message {"
                + "\\end ",
                //--- log message ---
                "Missing `{' inserted");
    }

    /**
     * <testcase primitive="\message">
     *  Test case checking that \message prints its plain argument.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMessage1() throws Exception {

        assertFailure(//--- input code ---
                "\\errorstopmode"
                + DEFINE_BRACES
                + "\\message{Hello world!}"
                + "\\end ",
                //--- log message ---
                "Hello world!");
    }

    /**
     * <testcase primitive="\message">
     *  Test case checking that \message prints its plain argument.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMessage2() throws Exception {

        assertFailure(//--- input code ---
                "\\batchmode"
                + DEFINE_BRACES
                + "\\message{Hello world!}"
                + "\\end ",
                //--- log message ---
                "Hello world!");
    }
    /**
     * <testcase primitive="\message">
     *  Test case checking that \message prints its plain argument.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMessage3() throws Exception {

        assertFailure(//--- input code ---
                "\\nonstopmode"
                + DEFINE_BRACES
                + "\\message{Hello world!}"
                + "\\end ",
                //--- log message ---
                "Hello world!");
    }

    /**
     * <testcase primitive="\message">
     *  Test case checking that \message prints its plain argument.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMessage4() throws Exception {

        assertFailure(//--- input code ---
                "\\scrollmode"
                + DEFINE_BRACES
                + "\\message{Hello world!}"
                + "\\end ",
                //--- log message ---
                "Hello world!");
    }

}