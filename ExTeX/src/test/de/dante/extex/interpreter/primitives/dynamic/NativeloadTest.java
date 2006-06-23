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

package de.dante.extex.interpreter.primitives.dynamic;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\nativeload</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class NativeloadTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for SkewcharTest.
     *
     * @param arg the name
     */
    public NativeloadTest(final String arg) {

        super(arg, "nativeload",
                "{java}{de.dante.extex.interpreter.primitives.dynamic.NativeloadSensor}");
        setConfig("extex-native");
    }

    /**
     * <testcase primitive="\nativeload">
     *  Test case checking that <tt>\nativeload</tt> produces a proper error
     *  message if an invalid type is specified.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        assertFailure(
                //--- input code ---
                DEFINE_BRACES
                        + "\\nativeload{undefined type}"
                        + "{de.dante.extex.interpreter.primitives.dynamic.NativeloadSensor}"
                        + " \\end",
                //--- error message ---
                "I don't know how to load native type `undefined type'");
        assertFalse(NativeloadSensor.isKilroy());
    }

    /**
     * <testcase primitive="\nativeload">
     *  Test case checking that <tt>\nativeload</tt> produces a proper error
     *  message if an undefined class is specified.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError2() throws Exception {

        assertFailure(
        //--- input code ---
                DEFINE_BRACES + "\\nativeload{java}" + "{un.de.fined.Class}"
                        + " \\end",
                //--- error message ---
                "Class not found: un.de.fined.Class");
    }

    /**
     * <testcase primitive="\nativeload">
     *  Test case checking that <tt>\nativeload</tt> produces a proper error
     *  message if an invalid class is specified.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testError3() throws Exception {

        assertFailure(
                //--- input code ---
                DEFINE_BRACES + "\\nativeload{java}" + "{java.lang.String}"
                        + " \\end",
                //--- error message ---
                "The class java.lang.String does not implement\n"
                        + "the required interface de.dante.extex.interpreter.unit.Loader.");
    }

    /**
     * <testcase primitive="\nativeload">
     *  Test case checking that <tt>\nativeload</tt> properly invokes a correct
     *  loader.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
                //--- input code ---
                DEFINE_BRACES
                        + "\\nativeload{java}"
                        + "{de.dante.extex.interpreter.primitives.dynamic.NativeloadSensor}"
                        + " \\end",
                //--- log message ---
                "");
        assertTrue(NativeloadSensor.isKilroy());
    }

}
