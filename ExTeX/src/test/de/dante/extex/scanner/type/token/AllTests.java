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
package de.dante.extex.scanner.type.token;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Test suite for token types.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class AllTests {

    public static Test suite() {

        TestSuite suite = new TestSuite(
                "Test for de.dante.extex.scanner.type.token");
        //$JUnit-BEGIN$
        suite.addTestSuite(MathShiftTokenTest.class);
        suite.addTestSuite(RightBraceTokenTest.class);
        suite.addTestSuite(LeftBraceTokenTest.class);
        suite.addTestSuite(SubMarkTokenTest.class);
        suite.addTestSuite(MacroParamTokenTest.class);
        suite.addTestSuite(SupMarkTokenTest.class);
        suite.addTestSuite(CrTokenTest.class);
        suite.addTestSuite(OtherTokenTest.class);
        suite.addTestSuite(SpaceTokenTest.class);
        suite.addTestSuite(LetterTokenTest.class);
        suite.addTestSuite(TabMarkTokenTest.class);
        suite.addTestSuite(ActiveCharacterTokenTest.class);
        suite.addTestSuite(ControlSequenceTokenTest.class);
        //$JUnit-END$
        return suite;
    }

}
