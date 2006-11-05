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

package de.dante.test;

/**
 * This abstract test suite contains some tests to check that all flags but the
 * global flag lead to an error.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public abstract class NoFlagsButGlobalPrimitiveTester extends ExTeXLauncher {

    /**
     * The field <tt>primitive</tt> contains the name of the primitive.
     */
    private String primitive;

    /**
     * The field <tt>arguments</tt> contains the additional arguments for the
     * flag test.
     */
    private String arguments;

    /**
     * The field <tt>prepare</tt> contains the preparation code.
     */
    private String prepare = DEFINE_BRACES;

    /**
     * The field <tt>out</tt> contains the prefix of the output message.
     */
    private String out = "";

    /**
     * Creates a new object.
     *
     * @param arg the name of the test case
     * @param primitive the name of the primitive
     * @param args the arguments for assignment
     */
    public NoFlagsButGlobalPrimitiveTester(final String arg,
            final String primitive, final String args) {

        super(arg);
        this.primitive = primitive;
        this.arguments = args;
    }

    /**
     * Creates a new object.
     *
     * @param arg the name of the test case
     * @param primitive the name of the primitive
     * @param args the arguments for assignment
     * @param prepare the preparation code
     */
    public NoFlagsButGlobalPrimitiveTester(final String arg,
            final String primitive, final String args, final String prepare) {

        this(arg, primitive, args);
        this.prepare = DEFINE_BRACES + prepare;
    }

    /**
     * Creates a new object.
     *
     * @param arg the name of the test case
     * @param primitive the name of the primitive
     * @param args the arguments for assignment
     * @param prepare the preparation code
     * @param out prefix of the output message
     */
    public NoFlagsButGlobalPrimitiveTester(final String arg,
            final String primitive, final String args, final String prepare,
            final String out) {

        this(arg, primitive, args, prepare);
        this.out = out;
    }

    /**
     * <testcase>
     *  Test case checking that the <tt>\immediate</tt> flag leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testNoImmediateFlag() throws Exception {

        assertFailure(
        //--- input code ---
                prepare + "\\immediate\\" + primitive + arguments,
                //--- log message ---
                out + "You can\'t use the prefix `\\immediate\' "
                        + "with the control sequence"
                        + (primitive.length() < 16 ? " " : "\n") + "\\"
                        + primitive);
    }

    /**
     * <testcase>
     *  Test case checking that the <tt>\long</tt> flag leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testNoLongFlag() throws Exception {

        assertFailure(
        //--- input code ---
                prepare + "\\long\\" + primitive + arguments,
                //--- log message ---
                out + "You can\'t use the prefix `\\long\' "
                        + "with the control sequence"
                        + (primitive.length() > 16 ? "\n" : " ") + "\\"
                        + primitive);
    }

    /**
     * <testcase>
     *  Test case checking that the <tt>\outer</tt> flag leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testNoOuterFlag() throws Exception {

        assertFailure(
        //--- input code ---
                prepare + "\\outer\\" + primitive + arguments,
                //--- log message ---
                out + "You can\'t use the prefix `\\outer\' "
                + "with the control sequence"
                + (primitive.length() > 16 ? "\n" : " ") + "\\"
                + primitive);
    }

    /**
     * <testcase>
     *  Test case checking that the <tt>\protected</tt> flag leads to an error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testNoProtectedFlag() throws Exception {

        assertFailure(
        //--- input code ---
                prepare + "\\protected\\" + primitive + arguments,
                //--- log message ---
                out + "You can\'t use the prefix `\\protected\' "
                        + "with the control sequence"
                        + (primitive.length() < 16 ? " " : "\n") + "\\"
                        + primitive);
    }

}
