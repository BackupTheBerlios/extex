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

package de.dante.extex.interpreter.primitives.math.numbering;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is an abstract base class for numbering primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class AbstractNumberingTester extends NoFlagsPrimitiveTester {

    /**
     * Creates a new object.
     *
     * @param name the name
     * @param primitive the name of the primitive
     */
    public AbstractNumberingTester(final String name, final String primitive) {

        super(name, primitive, "$$", DEFINE_MATH + "$$");
    }

    /**
     * <testcase>
     *  Test case checking that the primitive needs the display math mode.
     *  Vertical mode is not sufficient.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVerticalMode1() throws Exception {

        assertFailure(//--- input code ---
                "\\" + getPrimitive(),
                //--- log message ---
                "You can't use `\\" + getPrimitive() + "' in vertical mode");
    }

    /**
     * <testcase>
     *  Test case checking that the primitive needs the display math mode.
     *  Horizontal mode is not sufficient.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHorizontalMode1() throws Exception {

        assertFailure(//--- input code ---
                "a\\" + getPrimitive(),
                //--- log message ---
                "You can't use `\\" + getPrimitive() + "' in horizontal mode");
    }

    /**
     * <testcase>
     *  Test case checking that the primitive needs the display math mode.
     *  Inline math mode is not sufficient.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathMode1() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_MATH + "$\\" + getPrimitive(),
                //--- log message ---
                "You can't use `\\" + getPrimitive() + "' in math mode");
    }

    /**
     * <testcase>
     *  Test case checking that the primitive can't be use together with
     *  <tt>\eqno</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathMode2() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_MATH + "$$1\\" + getPrimitive() + "2\\eqno",
                //--- log message ---
                "You can't use `\\eqno' in math mode");
    }

    /**
     * <testcase>
     *  Test case checking that the primitive can't be use together with
     *  <tt>\leqno</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathMode3() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_MATH + "$$1\\" + getPrimitive() + "2\\leqno",
                //--- log message ---
                "You can't use `\\leqno' in math mode");
    }

}
