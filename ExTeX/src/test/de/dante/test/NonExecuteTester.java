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

package de.dante.test;

/**
 * This is a test suite for the primitive <tt>\parshapelength</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class NonExecuteTester extends ExTeXLauncher {

    /**
     * The field <tt>primitive</tt> contains the name of the primitive.
     */
    private String primitive;

    /**
     * The field <tt>args</tt> contains the additional arguments for the
     * flag test.
     */
    private String args;

    /**
     * The field <tt>prepare</tt> contains the preparation code.
     */
    private String prepare = DEFINE_CATCODES;

    /**
     * Creates a new object.
     *
     * @param arg the name of the test case
     * @param primitive the name of the primitive
     * @param args additional arguments for the flag test
     */
    public NonExecuteTester(final String name, final String primitive,
            final String args) {

        super(name);
        this.primitive = primitive;
        this.args = args;
    }

    /**
     * Creates a new object.
     *
     * @param arg the name of the test suite
     * @param primitive the name of the integer register to test
     * @param args the parameters for the invocation
     * @param prepare the preparation code
     */
    public NonExecuteTester(final String arg, final String primitive,
            final String args, final String prepare) {

        this(arg, primitive, args);
        this.prepare = DEFINE_CATCODES + prepare;
    }

    /**
     * <testcase>
     *  Test case showing that the primitive can not be used in vertical mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVerticalMode1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\" + primitive + args,
                //--- error channel ---
                "You can't use `\\" + primitive + "' in vertical mode");
    }

    /**
     * <testcase>
     *  Test case showing that the primitive can not be used in inner
     *  vertical mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInnerVerticalMode1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\vbox{\\" + primitive + args + "}\\end",
                //--- error channel ---
                "You can't use `\\" + primitive + "' in inner vertical mode");
    }

    /**
     * <testcase>
     *  Test case showing that the primitive can not be used in vertical mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHorizontalMode1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "x\\" + primitive + args + "\\end",
                //--- error channel ---
                "You can't use `\\" + primitive + "' in horizontal mode");
    }

    /**
     * <testcase>
     *  Test case showing that the primitive can not be used in restricted
     *  horizontal mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testRestrictedHorizontalMode1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "\\hbox{\\" + primitive + args + "}\\end",
                //--- error channel ---
                "You can't use `\\" + primitive
                        + "' in restricted horizontal mode");
    }

    /**
     * <testcase>
     *  Test case showing that the primitive can not be used in math mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMathMode1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "$\\" + primitive + args + "$\\end",
                //--- error channel ---
                "You can't use `\\" + primitive + "' in math mode");
    }

    /**
     * <testcase>
     *  Test case showing that the primitive can not be used in display math
     *  mode.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDisplayMathMode1() throws Exception {

        assertFailure(//--- input code ---
                prepare + "$$\\" + primitive + args + "$$\\end",
                //--- error channel ---
                "You can't use `\\" + primitive + "' in displaymath mode");
    }

}
