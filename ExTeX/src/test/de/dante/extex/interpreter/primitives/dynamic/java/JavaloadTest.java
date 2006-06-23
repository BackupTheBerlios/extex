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

package de.dante.extex.interpreter.primitives.dynamic.java;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\javaload</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class JavaloadTest extends NoFlagsPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public JavaloadTest(final String arg) {

        super(arg, "javaload",
                "{de.dante.extex.interpreter.primitives.dynamic.java.JavaloadSensor}");
        setConfig("extex-jx");
    }

    /**
     * <testcase primitive="\javaload">
     *  Test case checking that <tt>\javaload</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
                //--- input code ---
                DEFINE_BRACES
                        + "\\javaload"
                        + "{de.dante.extex.interpreter.primitives.dynamic.java.JavaloadSensor}"
                        + " \\end",
                //--- log message ---
                "");
        assertTrue(JavaloadSensor.isKilroy());
    }

}
