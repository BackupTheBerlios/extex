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

package de.dante.extex.interpreter.primitives.box;

import java.util.Properties;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\hrule</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class HruleTest extends ExTeXLauncher {

    /**
     * Constructor for HruleTest.
     *
     * @param arg the name
     */
    public HruleTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that a \hrule can stand by itself.
     *
     * @throws Exception in case of an error
     */
    public void testMissingBrace1() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.output", "dump");

        runCode(properties,
                //--- input code ---
                "\\hsize=123pt "
                + "\\hrule"
                + "\\end ",
                //--- log message ---
                "",
                //--- output chanel ---
                "\\vbox(0.4pt+0.0pt)x0.0pt\n.\\rule(0.4pt+0.0pt)x0.0pt\n");
        //TODO is this correct?
    }

}