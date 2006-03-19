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

package de.dante.extex.interpreter;

import java.util.Properties;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the kerning.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class KerningTest extends ExTeXLauncher {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(KerningTest.class);
    }

    /**
     * Constructor for KerningTest.
     *
     * @param arg the name
     */
    public KerningTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAVAV() throws Exception {

        Properties properties = getProps();
        properties.setProperty("extex.jobname", "job");
        properties.setProperty("extex.output", "dump");

        assertSuccess(properties,
        //--- input code ---
                "\\font\\f=cmr10 \\f " + "AVAV",
                //--- output channel ---
                "\\vbox(6.83331pt+0.0pt)x26.66667pt\n" //
                        + ".\\hbox(6.83331pt+0.0pt)x26.66667pt\n" //
                        + "..A\n" //
                        + "..\\kern -1.11113pt\n" //
                        + "..V\n" //
                        + "..\\kern -1.11113pt\n" //
                        + "..A\n" //
                        + "..\\kern -1.11113pt\n" //
                        + "..V\n");
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
                "\\font\\f=cmr10 \\f " + "xyz",
                //--- output channel ---
                "\\vbox(4.30554pt+1.94444pt)x15.00005pt\n" //
                        + ".\\hbox(4.30554pt+1.94444pt)x15.00005pt\n" //
                        + "..x\n" //
                        + "..y\n" //
                        + "..z\n");
    }

}
