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

package de.dante.extex.interpreter.primitives.hyphen;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\hyphenation</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class HyphenationTest extends NoFlagsPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public HyphenationTest(final String arg) {

        super(arg, "hyphenation", "{}", DEFINE_BRACES);
    }

    /**
     * <testcase primitive="\hyphenation">
     *  Test case checking that <tt>\hyphenation</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\hsize=15pt "
                + "\\font\\f cmr10 \\f"
                + "\\hyphenation{abc-def ghi-jkl}"
                + "x abcdef\\end ",
                //--- output channel ---
                "x\n abc-\n--def" + TERM);
    }

    /**
     * <testcase primitive="\hyphenation">
     *  Test case checking that <tt>\hyphenation</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES
                + "\\hsize=15pt "
                + "\\font\\f cmr10 \\f"
                + "\\hyphenation{abc-def auf-flug}"
                + "x aufflug\\end ",
                //--- output channel ---
                "x\n auf-\n--flug" + TERM);
    }

}