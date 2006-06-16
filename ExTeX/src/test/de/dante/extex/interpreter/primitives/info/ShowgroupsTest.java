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

package de.dante.extex.interpreter.primitives.info;

import java.util.Properties;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\showgroups</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ShowgroupsTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public ShowgroupsTest(final String arg) {

        super(arg, "showgroups", "", "", "### bottom level group\n");
    }

    /**
     * Prepare the properties to use a fine log level and suppress the banner.
     * @return the properties to use
     */
    private Properties prepare() {

        Properties p = getProps();
        p.setProperty("extex.launcher.loglevel", "info");
        p.setProperty("extex.nobanner", "true");
        return p;
    }

    /**
     * <testcase primitive="\showgroups">
     *  Test case checking that <tt>\showgroups</tt> produces the correct
     *  message for the top-level group.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertOutput(prepare(),
                //--- input code ---
                "\\showgroups\\end ",
                //--- error channel ---
                "### bottom level group\n", "");
    }

    /**
     * <testcase primitive="\showgroups">
     *  Test case checking that <tt>\showgroups</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertOutput(prepare(),
                //--- input code ---
                "\\begingroup\\showgroups\\endgroup\\end ",
                //--- error channel ---
                "### semi simple group (level 1) entered at line 1 (\\begingroup)\n"
                + "### bottom level group\n", "");
    }

    /**
     * <testcase primitive="\showgroups">
     *  Test case checking that <tt>\showgroups</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        assertOutput(prepare(),
                //--- input code ---
                DEFINE_BRACES
                + "{\\showgroups}\\end ",
                //--- error channel ---
                "### simple group (level 1) entered at line 1 ({)\n"
                + "### bottom level group\n", "");
    }

    /**
     * <testcase primitive="\showgroups">
     *  Test case checking that <tt>\showgroups</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        assertOutput(prepare(),
                //--- input code ---
                DEFINE_BRACES
                + "\\hbox{\\showgroups}\\end ",
                //--- error channel ---
                "### hbox group (level 1) entered at line 1 (\\hbox)\n"
                + "### bottom level group\n",
                null);
    }

    /**
     * <testcase primitive="\showgroups">
     *  Test case checking that <tt>\showgroups</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        assertOutput(prepare(),
                //--- input code ---
                DEFINE_BRACES
                + "\\hbox to 12pt{\\showgroups}\\end ",
                //--- error channel ---
                "### adjusted hbox group (level 1) entered at line 1 (\\hbox)\n"
                + "### bottom level group\n",
                null);
    }

    /**
     * <testcase primitive="\showgroups">
     *  Test case checking that <tt>\showgroups</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test6() throws Exception {

        assertOutput(prepare(),
                //--- input code ---
                DEFINE_BRACES
                + "\\vbox{\\showgroups}\\end ",
                //--- error channel ---
                "### vbox group (level 1) entered at line 1 (\\vbox)\n"
                + "### bottom level group\n",
                null);
    }

    /**
     * <testcase primitive="\showgroups">
     *  Test case checking that <tt>\showgroups</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test7() throws Exception {

        assertOutput(prepare(),
                //--- input code ---
                DEFINE_BRACES
                + "\\vtop{\\showgroups}\\end ",
                //--- error channel ---
                "### vtop group (level 1) entered at line 1 (\\vtop)\n"
                + "### bottom level group\n",
                null);
    }

    /**
     * <testcase primitive="\showgroups">
     *  Test case checking that <tt>\showgroups</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMulti1() throws Exception {

        assertOutput(prepare(),
                //--- input code ---
                DEFINE_BRACES
                + "\\begingroup{\\showgroups}\\endgroup\\end ",
                //--- error channel ---
                "### simple group (level 2) entered at line 1 ({)\n"
                + "### semi simple group (level 1) entered at line 1 (\\begingroup)\n"
                + "### bottom level group\n", "");
    }

    //TODO implement the primitive specific test cases

}
