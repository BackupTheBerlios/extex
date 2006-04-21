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

package de.dante.extex.interpreter.primitives.macro;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\expandafter</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ExpandafterTest extends ExTeXLauncher {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ExpandafterTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase primitive="\expandafter">
     *  Test case checking that <tt>\expandafter</tt> exchanges two letters.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testExpandafterLetterLetter1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\expandafter ab" + "\\end",
                //--- output message ---
                "ba" + TERM);
    }

    /**
     * <testcase primitive="\expandafter">
     *  Test case checking that <tt>\expandafter</tt> passes on <tt>\global</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testGlobal1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\global\\expandafter A\\count0=123 B"
                        + "\\end",
                //--- output message ---
                "AB" + TERM);
    }

    /**
     * <testcase primitive="\expandafter">
     *  Test case checking that <tt>\expandafter</tt> can expand the second
     *  token.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES + "\\let\\x=X \\let\\y=Y"
                        + "\\expandafter\\x\\y A" + "\\end",
                //--- output message ---
                "XYA" + TERM);
    }

}