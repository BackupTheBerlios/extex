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

package de.dante.extex.interpreter.primitives.typesetter.spacing;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for horizontal filling primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractHfillTester extends NoFlagsPrimitiveTester {

    /**
     * The field <tt>primitive</tt> contains the name of the primitive to test.
     */
    private String primitive;

    /**
     * The field <tt>invocation</tt> contains the concatenation of primitive
     * name and arguments.
     */
    private String invocation;

    /**
     * The field <tt>prepare</tt> contains the preparation code.
     */
    private String prepare = DEFINE_BRACES;


    /**
     * Constructor for HfillTest.
     *
     * @param arg the name
     */
    public AbstractHfillTester(final String arg, final String primitive,
            final String args) {

        super(arg, primitive, args);
        this.primitive = primitive;
        this.invocation = primitive + args;
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVbox1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\vbox{a\\" + invocation + " b} \\end",
                //--- error channel ---
                "..."); //TODO
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testHbox1() throws Exception {

        assertSuccess(//--- input code ---
                prepare + "\\hbox{a\\" + invocation + " b} \\end",
                //--- error channel ---
                "..."); //TODO
    }

    //TODO implement primitive specific test cases

}
