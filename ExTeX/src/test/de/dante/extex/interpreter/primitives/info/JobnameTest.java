/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\jobname</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class JobnameTest extends ExTeXLauncher {

    /**
     * Constructor for JobnameTest.
     *
     * @param arg the name
     */
    public JobnameTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that \jobname delivers a decent default value.
     *
     * @throws Exception in case of an error
     */
    public void testJobname1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\jobname"
                + "\\end ",
                //--- log message ---
                "",
                //--- output chanel ---
                "\n\\nullFont t\\nullFont e\\nullFont x"
                + "\\nullFont p\\nullFont u\\nullFont t\n");
    }

    /**
     * Test case checking that \jobname can be set properly.
     *
     * @throws Exception in case of an error
     */
    public void testJobname2() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.jobname", "job");

        runCode(properties,
                //--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\jobname"
                + "\\end ",
                //--- log message ---
                "",
                //--- output chanel ---
                "\n\\nullFont j\\nullFont o\\nullFont b\n");
    }
}