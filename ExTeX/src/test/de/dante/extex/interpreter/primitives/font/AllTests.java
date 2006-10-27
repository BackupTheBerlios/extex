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

package de.dante.extex.interpreter.primitives.font;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for the font primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class AllTests {

    /**
     * Define the test suite
     *
     * @return the tests
     */
    public static Test suite() {

        TestSuite suite = new TestSuite(
                "Test for de.dante.extex.interpreter.primitives.font");
        //$JUnit-BEGIN$
        suite.addTestSuite(FontcharwdTest.class);
        suite.addTestSuite(FontdimenTest.class);
        suite.addTestSuite(FontcharicTest.class);
        suite.addTestSuite(HyphencharTest.class);
        suite.addTestSuite(SkewcharTest.class);
        suite.addTestSuite(FontnameTest.class);
        suite.addTestSuite(FontchardpTest.class);
        suite.addTestSuite(FontcharhtTest.class);
        //$JUnit-END$
        return suite;
    }

}
