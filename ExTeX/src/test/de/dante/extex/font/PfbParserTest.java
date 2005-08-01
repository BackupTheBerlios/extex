/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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
 */

package de.dante.extex.font;

import junit.framework.TestCase;
import de.dante.extex.font.type.pfb.PfbParser;

/**
 * Test the pdf parser.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class PfbParserTest extends TestCase {

    /**
     * 01
     */
    public void test01() {

        String[] enc = parser.getEncoding();

        assertEquals(enc[32], "space");
        assertEquals(enc[50], "two");
        assertEquals(enc[95], "underscore");
        assertEquals(enc[117], "u");
        assertEquals(enc[138], "Scaron");
        assertEquals(enc[255], "ydieresis");
        assertEquals(enc[0], null);
    }

    /**
     * the parser
     */
    private PfbParser parser;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {

        parser = new PfbParser("src/font/lmr12.pfb");

    }

    public static void main(String[] args) {

        junit.textui.TestRunner.run(PfbParserTest.class);
    }

}
