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

import junit.framework.TestCase;

/**
 * Test for a fixed point format.
 * <p>
 * The Fixed point format consists of a signed,
 * 2 s complement mantissa and an
 * unsigned fraction. To compute the actual value,
 * take the mantissa and add the fraction.
 * <p>
 * <p>Examples:</p>
 * <table>
 *   <thead><tr>td>Decimal Value</td><td>Hex Value</td>
 *          <td>Mantissa</td><td>Fraction</td></tr>
 *   </thead>
 *   <tr><td>1.999939 </td><td>0x7fff</td><td>1  </td><td>16383/16384</td></tr>
 *   <tr><td>1.75     </td><td>0x7000</td><td>1  </td><td>0/16384</td></tr>
 *   <tr><td>0.000061 </td><td>0x0001</td><td>0  </td><td>1/16384</td></tr>
 *   <tr><td>0.0      </td><td>0x0000</td><td>0  </td><td>0/16384</td></tr>
 *   <tr><td>-0.000061</td><td>0xffff</td><td>-1 </td><td>16383/16384</td></tr>
 *   <tr><td>-2.0     </td><td>0x8000</td><td>-2 </td><td>0/16384</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FixedPointTest extends TestCase {

    /**
     * mains
     * @param args  the commandline
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(FixedPointTest.class);
    }

    /**
     * test 0x7fff
     */
    public void testA1() {

        FixedPoint fp = new FixedPoint(0x7fff);
        assertEquals(1.999939, fp.getDoubleValue(), 0.000001);
    }

    /**
     * test 0x7000
     */
    public void testA2() {

        FixedPoint fp = new FixedPoint(0x7000);
        assertEquals(1.75, fp.getDoubleValue(), 0.0);
    }

    /**
     * test 0x0001
     */
    public void testA3() {

        FixedPoint fp = new FixedPoint(0x0001);
        assertEquals(0.000061, fp.getDoubleValue(), 0.0000001);
    }

    /**
     * test 0x0000
     */
    public void testA4() {

        FixedPoint fp = new FixedPoint(0x0000);
        assertEquals(0.0, fp.getDoubleValue(), 0.0);
    }

    /**
     * test 0xffff
     */
    public void testA5() {

        FixedPoint fp = new FixedPoint(0xffff);
        assertEquals(-0.000061, fp.getDoubleValue(), 0.0000001);
    }

    /**
     * test 0x8000
     */
    public void testA6() {

        FixedPoint fp = new FixedPoint(0x8000);
        assertEquals(-2.0, fp.getDoubleValue(), 0.0);
    }

    /**
     * test 1.999939
     */
    public void testB1() {

        FixedPoint fp = new FixedPoint(1.999939);
        assertEquals(0x7fff, fp.getFixedPoint());
    }

    /**
     * test 1.75
     */
    public void testB2() {

        FixedPoint fp = new FixedPoint(1.75);
        assertEquals(0x7000, fp.getFixedPoint());
    }

    /**
     * test 0.000061
     */
    public void testB3() {

        FixedPoint fp = new FixedPoint(0.000061);
        assertEquals(0x0001, fp.getFixedPoint());
    }

    /**
     * test 0.0
     */
    public void testB4() {

        FixedPoint fp = new FixedPoint(0.0);
        assertEquals(0x0000, fp.getFixedPoint());
    }

    /**
     * test -0.000061
     */
    public void testB5() {

        FixedPoint fp = new FixedPoint(-0.000061);
        assertEquals(0xffff, fp.getFixedPoint());
    }

    /**
     * test -2.0
     */
    public void testB6() {

        FixedPoint fp = new FixedPoint(-2.0);
        assertEquals(0x8000, fp.getFixedPoint());
    }

    /**
     * test 0x21998
     */
    public void testA7() {

        FixedPoint fp = new FixedPoint(0x21998);
        assertEquals(2.09997558594, fp.getDoubleValue(), 0.0);
    }

    /**
     * test 2.09997558594
     */
    public void testB7() {

        FixedPoint fp = new FixedPoint(2.09997558594);
        assertEquals(0x21998, fp.getFixedPoint());
    }

}