/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.macro;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\csname</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class CsnameTest extends ExTeXLauncher {

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public CsnameTest(final String arg) {

        super(arg);
    }

    /**
     * Test case checking that the normal operation is performed on letter
     * inputs only.
     *
     * @throws Exception in case of an error
     */
    public void testLetters1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\abc{-a-b-c-}" +
                "\\csname abc\\endcsname\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "-a-b-c-\n\n-a-b-c-\n\n");
    }

    /**
     * Test case checking that the normal operation is performed on letter
     * inputs only and whitespace is ignored.
     *
     * @throws Exception in case of an error
     */
    public void testLetters2() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\abc{-a-b-c-}" +
                "\\csname a b  c\\endcsname\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "-a-b-c-\n\n-a-b-c-\n\n");
    }

    /**
     * Test case checking that the normal operation is performed on letter
     * inputs only and whitespace and \relax is ignored.
     *
     * @throws Exception in case of an error
     */
    public void testLetters3() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\abc{-a-b-c-}" +
                "\\csname a b \\relax c\\endcsname\\end",
                //--- log message ---
                "",
                //--- output channel ---
                "-a-b-c-\n\n-a-b-c-\n\n");
    }

    /**
     * Test case checking that the normal operation is performed on letter
     * and digit inputs only.
     *
     * @throws Exception in case of an error
     */
    public void testMixed1() throws Exception {

        runCode(//--- input code ---
                "\\catcode`{=1"
                + "\\catcode`}=2"
                + "\\def\\abc{-a-b-c-}" +
                "\\csname abc 123\\endcsname\\end",
                //--- log message ---
                "Undefined control sequence",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that the normal operation is performed on letter
     * and digit inputs only.
     *
     * @throws Exception in case of an error
     */
    public void testError1() throws Exception {

        runCode(//--- input code ---
                "\\csname abc \\par\\endcsname\\end",
                //--- log message ---
                "Missing \\endcsname inserted",
                //--- output channel ---
                "");
    }

    /**
     * Test case checking that eof is recognized.
     *
     * @throws Exception in case of an error
     */
    public void testEOF1() throws Exception {

        runCode(//--- input code ---
                "\\expandafter\\meaning\\csname ",
                //--- log message ---
                "",
                //--- output channel ---
                "\\=undefined\n\n\\=undefined\n\n");
    }

}