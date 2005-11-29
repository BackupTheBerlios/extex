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

package de.dante.extex.interpreter.primitives.file.xslt;

import de.dante.test.NoFlagsButGlobalPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\inputXSLT</tt>.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class InputXsltTest extends NoFlagsButGlobalPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(InputXsltTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public InputXsltTest(final String arg) {

        super(arg, "javadef",
                "\\inputXSLT{de.dante.extex.interpreter.primitives.file.xslt.InputXslt}");

    }

    /**
     * @see de.dante.test.ExTeXLauncher#getConfig()
     */
    protected String getConfig() {

        return "extex-jx.xml";
    }

    /**
     * <testcase primitive="\javadef">
     *  Test case checking that <tt>\javadef</tt> ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        // TODO incomplete

        assertSuccess(
                //--- input code ---
                DEFINE_BRACES
                        + "\\javadef\\inputXSLT{"
                        + "de.dante.extex.interpreter.primitives.file.xslt.InputXslt}"
                        + "\\inputXSLT{xslt_01.xhtml}{xhtml2latex.xsl}"
                        + " \\end",
                //--- log message ---
                "");
    }
}