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

package de.dante.extex.interpreter;

import java.util.Properties;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the ligature.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class LigatureTest extends ExTeXLauncher {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(LigatureTest.class);
    }

    /**
     * Constructor for KerningTest.
     *
     * @param arg the name
     */
    public LigatureTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testXyz() throws Exception {

        Properties properties = getProps();
        properties.setProperty("extex.jobname", "job");
        properties.setProperty("extex.output", "dump");

        assertSuccess(properties,
        //--- input code ---
                "\\font\\f=cmr10 \\f " + "Affe",
                //--- output channel ---
                "\\vbox(6.94444pt+0.0pt)x3000.0pt\n" //
                + ".\\hbox(6.94444pt+0.0pt)x3000.0pt\n" //
                + "..A\n" //
                + "..\n" //
                + "..e\n");
    }

}
