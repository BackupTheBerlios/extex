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
package de.dante.extex.scanner.stream;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for all tests in this package.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public final class AllTests {

    /**
     * Creates a new object.
     */
    private AllTests() {
    }

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * The test suite.
     *
     * @return the suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for de.dante.extex.scanner.stream");
        //$JUnit-BEGIN$

        //$JUnit-END$
        return suite;
    }
}
