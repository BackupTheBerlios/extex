/*
 * Copyright (C) 2004 Gerd Neugebauer
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.primitives.font;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\fontdimen</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FontdimenTest extends ExTeXLauncher {

    /**
     * Constructor for FontdimenTest.
     *
     * @param arg the name
     */
    public FontdimenTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that \fontdimen on unset keys returns 0 pt.
     * @throws Exception in case of an error
     */
    public void testUnset1() throws Exception {

        runCode(//--- input code ---
                "\\the\\fontdimen0\\nullfont "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "\n\\nullfont 0\\nullfont .\\nullfont 0\\nullfont p\\nullfont t\n");
    }

    /**
     * Test case checking that \fontdimen on unset keys returns 0 pt.
     * @throws Exception in case of an error
     */
    public void testUnset2() throws Exception {

        runCode(//--- input code ---
                "\\the\\fontdimen65000\\nullfont "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "\n\\nullfont 0\\nullfont .\\nullfont 0\\nullfont p\\nullfont t\n");
    }

    /**
     * Test case checking that \fontdimen can be set and read back for \nullfont.
     * @throws Exception in case of an error
     */
    public void testSet1() throws Exception {

        runCode(//--- input code ---
                "\\fontdimen0\\nullfont=42pt "
                + "\\the\\fontdimen65000\\nullfont "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "\n\\nullfont 4\\nullfont 2\\nullfont .\\nullfont 0\\nullfont p\\nullfont t\n");
    }

    /**
     * Test case checking that \fontdimen can be set and read back for cmtt12.
     * @throws Exception in case of an error
     */
    public void testSet2() throws Exception {

        runCode(//--- input code ---
                "\\font\\fnt=cmtt12\\relax "
                + "\\fontdimen0\\fnt=42pt "
                + "\\the\\fontdimen65000\\fnt "
                + "\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "\n\\nullfont 4\\nullfont 2\\nullfont .\\nullfont 0\\nullfont p\\nullfont t\n");
    }

}
