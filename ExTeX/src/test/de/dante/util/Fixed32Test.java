/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.util;

import de.dante.extex.font.type.Fixed32;
import junit.framework.TestCase;

/**
 * Test for a fixed point format.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Fixed32Test extends TestCase {

    /**
     * mains
     * @param args  the commandline
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(Fixed32Test.class);
    }

    /**
     * test 0x21998
     */
    public void testA1() {

        Fixed32 fp = new Fixed32(0x21998);
        assertEquals(2.09997558594, fp.getDoubleValue(), 0.00000000001);
    }

    /**
     * test 2.09997558594
     */
    public void testB1() {

        Fixed32 fp = new Fixed32(2.09997558594);
        assertEquals(0x21998, fp.getFixedPoint());
    }

}