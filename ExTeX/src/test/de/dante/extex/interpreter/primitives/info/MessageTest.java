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
 * @version $Revision: 1.1 $
 */
public class MessageTest extends ExTeXLauncher {

    /**
     * Constructor for RelaxTest.
     *
     * @param arg the name
     */
    public MessageTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that \message results in an error message, if the
     * following token is not a left brace.
     *
     * @throws Exception in case of an error
     */
    public void testMissingBrace1() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.config", "extex");

        runCode(properties,
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\message }"
                + "\\end ",
                "Missing `{' inserted",
                null);
    }

    /**
     * Test case checking that \message results in an error message, if the
     * following token is not a left brace.
     *
     * @throws Exception in case of an error
     */
    public void testMissingBrace2() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.config", "extex");

        runCode(properties,
                "\\message {"
                + "\\end ",
                "Missing `{' inserted",
                null);
    }

}