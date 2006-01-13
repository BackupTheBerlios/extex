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

package de.dante;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.dante.extex.scanner.stream.impl.TokenStreamStringImplTest;
import de.dante.extex.scanner.stream.impl32.TokenStreamStringImpl32Test;
import de.dante.extex.unicodeFont.EncFactoryTest;
import de.dante.extex.unicodeFont.FontFactoryTest;
import de.dante.extex.unicodeFont.PsFontsMapReaderTest;
import de.dante.util.Fixed32Test;
import de.dante.util.xml.XMLStreamWriterTest;
import de.dante.util.xslt.XsltTransformXhtmlTest;

/**
 * Tests for mgn.
 *
 * <ul>
 *  <li>XsltTransformXhtmlTest</li>
 *  <li>TokenStreamStringImplTest</li>
 *  <li>TokenStreamStringImpl32Test</li>
 *  <li>XMLStreamWriterTest</li>
 *  <li>Fixed32Test</li>
 *  <li>PsFontsMapReaderTest</li>
 *  <li>EncFactoryTest</li>
 *  <li>FontFactoryTest</li>
 * </ul>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class MGNTests {

    /**
     * The test suite.
     * @return The Test
     */
    public static Test suite() {

        TestSuite suite = new TestSuite("Test for mgn");

        //$JUnit-BEGIN$
        suite.addTestSuite(XsltTransformXhtmlTest.class);
        suite.addTestSuite(TokenStreamStringImplTest.class);
        suite.addTestSuite(TokenStreamStringImpl32Test.class);
        suite.addTestSuite(XMLStreamWriterTest.class);
        suite.addTestSuite(Fixed32Test.class);
        suite.addTestSuite(PsFontsMapReaderTest.class);
        suite.addTestSuite(EncFactoryTest.class);
        suite.addTestSuite(FontFactoryTest.class);
        //$JUnit-END$

        return suite;
    }

}
