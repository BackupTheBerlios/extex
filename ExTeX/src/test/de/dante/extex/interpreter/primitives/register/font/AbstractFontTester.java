/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\font</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class AbstractFontTester extends NoFlagsButGlobalPrimitiveTester {

    /**
     * The field <tt>primitive</tt> contains the ...
     */
    private String primitive;

    /**
     * The field <tt>arguments</tt> contains the ...
     */
    private String arguments;

    /**
     * Constructor for the test.
     *
     * @param arg the name
     * @param primitive the primitive
     * @param arguments the arguments
     */
    public AbstractFontTester(final String arg, final String primitive,
            final String arguments) {

        super(arg, primitive, arguments + "\\f", "\\font\\f=cmr10 ");
        this.primitive = primitive;
        this.arguments = arguments;
    }

    /**
     * Test case checking that \fontdimen on unset keys returns 0 pt.
     * @throws Exception in case of an error
     */
    public void testNoMacro1() throws Exception {

        assertFailure(//--- input code ---
                "\\font\\f=cmr10 \\" + primitive + arguments + " xxx" + "\\end",
                //--- log message ---
                "Missing font identifier");
    }

    /**
     * Test case checking that ...
     * @throws Exception in case of an error
     */
    public void testFontThe1() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.output", "text");

        assertOutput(properties,
        //--- input code ---
                "\\showthe \\" + primitive + arguments
                        + "\\end",
                //--- output channel ---
                "> \\nullfont.\n",
                "");
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
                "\\font\\f=cmr10 \\" + primitive + arguments + "=\\f "
                        + "\\end",
                //--- output channel ---
                "");
    }
}
