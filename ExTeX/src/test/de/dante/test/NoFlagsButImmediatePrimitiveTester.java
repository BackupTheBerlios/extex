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
 * This class is an abstract base class for test suited containing some test
 * cases to verify that the prefixes <tt>\global</tt>, <tt>\long</tt>,
 * <tt>\outer</tt>, and <tt>\protected</tt> lead to an error.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public abstract class NoFlagsButImmediatePrimitiveTester extends ExTeXLauncher {

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
     * Creates a new object.
     *
     * @param name the name of the test case
     * @param primitive the name of the primitive
     * @param args additional arguments for the flag test
     */
    public NoFlagsButImmediatePrimitiveTester(final String name,
            final String primitive, final String args) {

        super(name);
        this.primitive = primitive;
        this.args = args;
    }

    /**
     *
     * @throws Exception in case of an error
     */
    public void testNoGlobalFlag() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "\\global\\" + primitive + args,
                //--- log message ---
                "You can\'t use the prefix `\\global\' with the control sequence \\"
                        + primitive);
    }

    /**
     *
     * @throws Exception in case of an error
     */
    public void testNoLongFlag() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "\\long\\" + primitive + args,
                //--- log message ---
                "You can\'t use the prefix `\\long\' with the control sequence \\"
                        + primitive);
    }

    /**
     *
     * @throws Exception in case of an error
     */
    public void testNoOuterFlag() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "\\outer\\" + primitive + args,
                //--- log message ---
                "You can\'t use the prefix `\\outer\' with the control sequence \\"
                        + primitive);
    }

    /**
     *
     * @throws Exception in case of an error
     */
    public void testNoProtectedFlag() throws Exception {

        assertFailure(//--- input code ---
                DEFINE_CATCODES + "\\protected\\" + primitive + args,
                //--- log message ---
                "You can\'t use the prefix `\\protected\' with the control sequence \\"
                        + primitive);
    }

}
