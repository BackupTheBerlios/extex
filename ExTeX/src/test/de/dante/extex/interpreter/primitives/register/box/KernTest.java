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

package de.dante.extex.interpreter.primitives.register.box;

import java.util.Properties;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\kern</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class KernTest extends ExTeXLauncher {

    /**
     * Constructor for KernTest.
     *
     * @param arg the name
     */
    public KernTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that a correct value is produced.
     *
     * @throws Exception in case of an error
     */
    public void testKern1() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.output", "dump");

        runCode(properties,
                //--- input code ---
                "x\\kern 123pt"
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "\\vbox(0.0pt+0.0pt)x123.0pt\n"
                + ".\\hbox(0.0pt+0.0pt)x123.0pt\n"
                + "..\\nullFont x\n"
                + "..[]\n"); //TODO correct?
    }

    /**
     * Test case checking that a missing dimen leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runCode(//--- input code ---
                "x\\kern ",
                //--- log message ---
                "Illegal unit of measure (pt inserted)",
                //--- output channel ---
                "");
    }

}