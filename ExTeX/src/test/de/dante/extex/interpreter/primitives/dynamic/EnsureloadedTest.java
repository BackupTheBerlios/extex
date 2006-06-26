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

package de.dante.extex.interpreter.primitives.dynamic;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\ensureloaded</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class EnsureloadedTest extends NoFlagsPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public EnsureloadedTest(final String arg) {

        super(arg, "ensureloaded", "{tex}");
    }

    /**
     * <testcase primitive="\ensureloaded">
     *  Test case checking that <tt>\ensureloaded</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(
        //--- input code ---
                DEFINE_BRACES + "\\ensureloaded{etex}"
                        + "\\the\\TeXXeTstate" + " \\end",
                //--- log message ---
                "0" + TERM);
    }

    /**
     * <testcase primitive="\ensureloaded">
     *  Test case checking that <tt>\ensureloaded</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(
        //--- input code ---
                DEFINE_BRACES + "\\ensureloaded{jx}"
                        + "\\ifdefined\\javadef ok\\fi" + " \\end",
                //--- log message ---
                "ok" + TERM);
    }

}
