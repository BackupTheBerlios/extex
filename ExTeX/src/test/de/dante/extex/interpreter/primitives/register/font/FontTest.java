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

package de.dante.extex.interpreter.primitives.register.font;

import java.util.Properties;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\font</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FontTest extends ExTeXLauncher {

    /**
     * Constructor for the test.
     *
     * @param arg the name
     */
    public FontTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that \fontdimen on unset keys returns 0 pt.
     * @throws Exception in case of an error
     */
    public void testNoMacro1() throws Exception {

        assertFailure(//--- input code ---
                "\\font noFont at 10pt " + "\\end",
                //--- log message ---
                "Missing control sequence inserted");
    }

    /**
     * Test case checking that \font throws an error when the font is not found.
     * @throws Exception in case of an error
     */
    public void testUndefined1() throws Exception {

        assertFailure(//--- input code ---
                "\\font\\abc=noFont at 10pt " + "\\end",
                //--- log message ---
                "Font \\abc=noFont not loadable: Metric (TFM) file not found");
    }

    /**
     * Test case checking that \font can load a font (cmtt12) without
     * additional attributes.
     * @throws Exception in case of an error
     */
    public void testFont1() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.output", "text");

        assertSuccess(properties,
                //--- input code ---
                "\\font\\abc=cmtt12 " + "\\abc x" + "\\end",
                //--- output channel ---
                "\nx\n");
    }

    /**
     * Test case checking that \font can load a font (cmtt12) with a given
     * size attribute.
     * @throws Exception in case of an error
     */
    public void testFont2() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\abc=cmtt12 at 10pt " + "\\end",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that \font can load a font (cmtt12) with a given
     * positive scaled attribute.
     * @throws Exception in case of an error
     */
    public void testFont3() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\abc=cmtt12 scaled 2000 " + "\\end",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that \font can load a font (cmtt12) to an active
     * character.
     * @throws Exception in case of an error
     */
    public void testFont4() throws Exception {

        assertSuccess(//--- input code ---
                "\\catcode`/=13 " + "\\font/=cmtt12 " + "\\end",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that \font produces an error message when the scaled
     * attribute is negative.
     * @throws Exception in case of an error
     */
    public void testNegativeScale1() throws Exception {

        assertFailure(//--- input code ---
                "\\font\\abc=cmtt12 scaled -2000 " + "\\end",
                //--- log message ---
                "Illegal magnification has been changed to 1000");
    }

    /**
     * Test case checking that \font can load a non emf font.
     * @throws Exception in case of an error
     */
    public void testNonEmf1() throws Exception {

        assertSuccess(//--- input code ---
                "\\font\\abc=cmr10\\relax " + "\\end",
                //--- output channel ---
                "");
    }

}