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

package de.dante.extex.unicodeFont;

import junit.framework.TestCase;
import de.dante.extex.unicodeFont.glyphname.GlyphName;

/**
 * Test the class <code>GlyphName</code>.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class GlyphNameTest extends TestCase {

    /**
     * test 01. initial okay
     *
     * @throws Exception if an error occurred.
     */
    public void test01() throws Exception {

        assertNotNull(glyphname);
    }

    /**
     * test 02. right key value
     * @throws Exception if an error occurred.
     */
    public void test02() throws Exception {

        assertEquals(hex2int("05D3"), glyphname.getUnicode("dalet")
                .getCodePoint());
        assertEquals(hex2int("0041"), glyphname.getUnicode("A").getCodePoint());
        assertEquals(hex2int("0061"), glyphname.getUnicode("a").getCodePoint());
        assertEquals(hex2int("30BA"), glyphname.getUnicode("zukatakana")
                .getCodePoint());
        assertEquals(hex2int("2790"), glyphname.getUnicode(
                "sevencircleinversesansserif").getCodePoint());
        assertEquals(hex2int("FF65"), glyphname.getUnicode(
                "middledotkatakanahalfwidth").getCodePoint());

    }

    /**
     * test 03. no existing glyph
     * @throws Exception if an error occurred.
     */
    public void test03() throws Exception {

        assertNull(glyphname.getUnicode("not exists"));
    }

    /**
     * Convert a hex string to a int value.
     * @param hex   the hex string.
     * @return Convert a hex string to a int value.
     */
    private int hex2int(final String hex) {

        return Integer.parseInt(hex, 16);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        if (glyphname == null) {
            glyphname = GlyphName.getInstance();
        }
    }

    /**
     *  The instance of the class.
     */
    private GlyphName glyphname;

    /**
     * main.
     * @param args  The command line.
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(GlyphNameTest.class);
    }

}
