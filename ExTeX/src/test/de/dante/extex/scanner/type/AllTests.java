/*
 * Copyright (C) 2004  Gerd Neugebauer
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

package de.dante.extex.scanner.type;

import de.dante.extex.scanner.type.token.ActiveCharacterTokenTest;
import de.dante.extex.scanner.type.token.ControlSequenceTokenTest;
import de.dante.extex.scanner.type.token.CrTokenTest;
import de.dante.extex.scanner.type.token.LeftBraceTokenTest;
import de.dante.extex.scanner.type.token.LetterTokenTest;
import de.dante.extex.scanner.type.token.MacroParamTokenTest;
import de.dante.extex.scanner.type.token.MathShiftTokenTest;
import de.dante.extex.scanner.type.token.OtherTokenTest;
import de.dante.extex.scanner.type.token.RightBraceTokenTest;
import de.dante.extex.scanner.type.token.SpaceTokenTest;
import de.dante.extex.scanner.type.token.SubMarkTokenTest;
import de.dante.extex.scanner.type.token.SupMarkTokenTest;
import de.dante.extex.scanner.type.token.TabMarkTokenTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test cases for the scanner.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class AllTests {

    /**
     * The constructor is private to avoid that somebody uses it.
     */
    private AllTests() {

        super();
    }

    /**
     * Command line interface.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(suite());
    }

    /**
     * Combines all test in this package into one suite.
     *
     * @return the suite
     */
    public static Test suite() {

        TestSuite suite = new TestSuite("Test for de.dante.extex.scanner");
        //$JUnit-BEGIN$
        suite.addTestSuite(SpaceTokenTest.class);
        suite.addTestSuite(ActiveCharacterTokenTest.class);
        suite.addTestSuite(LetterTokenTest.class);
        suite.addTestSuite(ControlSequenceTokenTest.class);
        suite.addTestSuite(OtherTokenTest.class);
        suite.addTestSuite(CrTokenTest.class);
        suite.addTestSuite(LeftBraceTokenTest.class);
        suite.addTestSuite(RightBraceTokenTest.class);
        suite.addTestSuite(MacroParamTokenTest.class);
        suite.addTestSuite(MathShiftTokenTest.class);
        suite.addTestSuite(SubMarkTokenTest.class);
        suite.addTestSuite(SupMarkTokenTest.class);
        suite.addTestSuite(TabMarkTokenTest.class);
        suite.addTestSuite(CatcodeTest.class);
        //$JUnit-END$
        return suite;
    }
}