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

package de.dante.extex.font;

import de.dante.extex.ExTeXRunner;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * Test for the tfm class.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */

public class TfmTest extends ExTeXRunner {

    /**
     * size 12
     */
    private static final int SIZE12 = 12;

    /**
     * the font factory
     */
    private FontFactory fontFactory;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {

        super.setUp();
        fontFactory = extex.getFontFactory();
    }

    /**
     * test: design size from tfm.
     * @throws Exception if an error occurs
     */
    public void testdesignsize() throws Exception {

        TFMFont font = fontFactory.readTFMFont("cmr12");
        assertNotNull("font not found", font);
        assertEquals(font.getDesignSizeAsDouble(), SIZE12, 0);

    }

    /**
     * test: design / actual size from cmr12.
     *
     * @throws Exception if an error occurs
     */
    public void test01() throws Exception {

        Dimen pt12 = new Dimen(SIZE12 * Dimen.ONE_PT.getValue());

        FountKey key = new FountKey("cmr12", null, null, new Glue(0), true,
                true);

        Font cmr12 = fontFactory.getInstance(key);

        assertEquals(pt12.toString(), cmr12.getDesignSize().toString());
        assertEquals(pt12.toString(), cmr12.getActualSize().toString());

        assertEquals("3.91663pt", cmr12.getFontDimen("SPACE").toString());
    }

    /**
     * test: ligature
     *
     * @throws Exception if an error occurs
     */
    public void test02() throws Exception {

        FountKey key = new FountKey("cmr12", null, null, new Glue(0), true,
                true);

        Font cmr12 = fontFactory.getInstance(key);

        // A - V

        FixedDimen k = cmr12.getKerning(UnicodeChar.get('A'), UnicodeChar
                .get('V'));

        assertEquals("-1.30554pt", k.toString());
    }

    // -----------------------------------------

    /**
     * main
     * @param args  the command line
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(TfmTest.class);
    }

}
