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

package de.dante.extex.interpreter.primitives.arithmetic;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This is a test suite for arithmetic primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class AllTests {

    /**
     * Define the test suite
     *
     * @return the tests
     */
    public static Test suite() {

        TestSuite suite = new TestSuite(
                "Test for de.dante.extex.interpreter.primitives.arithmetic");
        //$JUnit-BEGIN$
        suite.addTestSuite(AdvanceTest.class);
        suite.addTestSuite(DivideTest.class);
        suite.addTestSuite(MultiplyTest.class);
        //$JUnit-END$
        return suite;
    }

}
