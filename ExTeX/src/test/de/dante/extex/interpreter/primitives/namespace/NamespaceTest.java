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

package de.dante.extex.interpreter.primitives.namespace;

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\namespace</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class NamespaceTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(NamespaceTest.class);
    }

    /**
     * Constructor for NamespaceTest.
     *
     * @param arg the name
     */
    public NamespaceTest(final String arg) {

        super(arg, "namespace", "{a}", DEFINE_BRACES);
    }

    /**
     * Getter for the configuration name.
     *
     * @return the name of the configuration
     */
    protected String getConfig() {

        return "nextex.xml";
    }

    /**
     * <testcase primitive="\namespace">
     *  Test case checking that <tt>\namespace</tt> is initially empty.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertSuccess(//--- input code ---
                ":\\the\\namespace:"
                + "\\end ",
                //--- output channel ---
                "::" + TERM);
    }

    /**
     * <testcase primitive="\namespace">
     *  Test case checking that <tt>\namespace</tt> can be set and read.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\namespace{TeX}"
                + ":\\the\\namespace:"
                + "\\end ",
                //--- output channel ---
                ":TeX:" + TERM);
    }

    /**
     * <testcase primitive="\namespace">
     *  Test case checking that <tt>\namespace</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test10() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\let\\x a"
                + "\\namespace{TeX}"
                + "\\let\\x b"
                + ".\\x."
                + "\\namespace{abc}"
                + "\\the\\namespace:"
                + ".\\x."
                + "\\end ",
                //--- output channel ---
                ".b.abc:.a." + TERM);
    }

    /**
     * <testcase primitive="\namespace">
     *  Test case checking that <tt>\namespace</tt> interacts with groups.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test20() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_CATCODES
                + "\\namespace{A}"
                + "\\begingroup\\global\\namespace{TeX}\\endgroup"
                + ":\\the\\namespace:"
                + "\\end ",
                //--- output channel ---
                ":TeX:" + TERM);
    }

    //TODO implement the primitive specific test cases

}