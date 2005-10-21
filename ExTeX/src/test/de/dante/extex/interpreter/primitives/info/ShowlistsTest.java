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

package de.dante.extex.interpreter.primitives.info;

import java.util.Properties;

import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\showlists</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ShowlistsTest extends NoFlagsPrimitiveTester {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public ShowlistsTest(final String arg) {

        super(arg, "showlists", "");
    }

    /**
     * <testcase primitive="\showlists">
     *  Test case checking that the <tt>\showlists</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        Properties p = prepare();

        assertFailure(p, //--- input code ---
                "\\showlists\\end ",
                //--- error channel ---
                "### vertical mode entered at line 0\n" + "prevdepth ignored\n");
    }

    /**
     * <testcase primitive="\showlists">
     *  Test case checking that the <tt>\showlists</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        Properties p = prepare();

        assertOutput(p, //--- input code ---
                DEFINE_BRACES + "\\hbox{\\showlists}\\end ",
                //--- error channel ---
                "### restricted horizontal mode entered at line 1\n"
                        + "spacefactor 1000\n"
                        + "### vertical mode entered at line 0\n"
                        + "prevdepth ignored\n",
                //
                TERM);
    }

    /**
     * Prepar the properties to use a fine log level.
     *
     * @return te properties to use
     */
    private Properties prepare() {

        Properties p = getProps();
        p.setProperty("extex.launcher.loglevel", "fine");
        return p;
    }

}
