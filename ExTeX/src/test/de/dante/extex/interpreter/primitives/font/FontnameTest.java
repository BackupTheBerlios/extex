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

package de.dante.extex.interpreter.primitives.font;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\fontname</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class FontnameTest extends ExTeXLauncher {

    /**
     * Constructor for FontnameTest.
     *
     * @param arg the name
     */
    public FontnameTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that an end of file leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testEof1() throws Exception {

        runCode(//--- input code ---
                "\\fontname ",
                //--- log message ---
                "Unexpected end of file while processing \\fontname",
                //--- output channel ---
                null);
    }

    /**
     * Test case checking that a missing font identifier leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testMissing1() throws Exception {

        runCode(//--- input code ---
                "\\fontname x",
                //--- log message ---
                "Missing font identifier",
                //--- output channel ---
                null);
    }

    /**
     * Test case checking that a missing font identifier leads to an error.
     *
     * @throws Exception in case of an error
     */
    public void testMissing2() throws Exception {

        runCode(//--- input code ---
                "\\fontname \\x",
                //--- log message ---
                "Undefined control sequence",
                //--- output channel ---
                null);
    }

    /**
     * Test case checking that a correct value is produced.
     *
     * @throws Exception in case of an error
     */
    public void testFontname1() throws Exception {

        runCode(//--- input code ---
                "\\font\\x=cmtt12"
                + "\\fontname\\x "
                + "\\end ",
                //--- log message ---
                "",
                //--- output channel ---
                "cmtt12\n");
    }

}