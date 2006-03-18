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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class AllTests {

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(AllTests.suite());
    }

    /**
     * Create the test suite for all tests
     *
     * @return the test suite
     */
    public static Test suite() {

        TestSuite suite = new TestSuite("Test for de.dante.extex.interpreter");
        //$JUnit-BEGIN$
        suite.addTestSuite(KerningTest.class);
        suite.addTestSuite(TypesetterTest.class);
        suite.addTestSuite(InterpreterTest.class);
        suite.addTestSuite(Max1.class);
        suite.addTestSuite(FlagsImplTest.class);
        //$JUnit-END$
        return suite;
    }

}
