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
package de.dante.extex.scanner;

import de.dante.util.UnicodeChar;
import junit.framework.TestCase;

/*
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class OtherTokenTest extends TestCase {

    /*
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(OtherTokenTest.class);
    }

    private static Token t = new OtherToken(new UnicodeChar(';'));

    /*
     */
    public void testGetCatcode() {
        assertEquals(Catcode.OTHER,t.getCatcode());
    }

    /*
     */
    public void testToString() {
        assertEquals("the character ;",t.toString());
    }

    /*
     */
    public void testToText() {
        assertEquals(";",t.toText());
    }

    /*
     */
    public void testGetValue() {
        assertEquals(";",t.getValue());
    }

    /*
     */
    public void testEqualsToken0() {
        assertTrue(t.equals(t));
     }

    /*
     */
    public void testEqualsToken1() {
        Token t1 = new OtherToken(new UnicodeChar(' '));
        Token t2 = new SpaceToken(" ");
        assertFalse(t1.equals(t2));
    }

    /*
     */
    public void testEqualsCatcodeString0() {
        assertTrue(t.equals(Catcode.OTHER,";"));
    }

    /*
     */
    public void testEqualsCatcodeString1() {
        assertFalse(t.equals(Catcode.LETTER,";"));
    }

    /*
     */
    public void testEqualsCatcodechar0() {
        assertTrue(t.equals(Catcode.OTHER,';'));
    }

    /*
     */
    public void testEqualsCatcodechar1() {
        assertFalse(t.equals(Catcode.LETTER,';'));
    }

    /*
     */
    public void testEqualschar0() {
        assertTrue(t.equals(';'));
    }

    /*
     */
    public void testEqualschar1() {
        assertFalse(t.equals('.'));
    }

    /*
     */
    public void testIsa0() {
        assertFalse(t.isa(Catcode.SPACE));
    }

    /*
     */
    public void testIsa1() {
        assertFalse(t.isa(Catcode.ACTIVE));
    }

    /*
     */
    public void testIsa2() {
        assertFalse(t.isa(Catcode.COMMENT));
    }

    /*
     */
    public void testIsa3() {
        assertFalse(t.isa(Catcode.CR));
    }

    /*
     */
    public void testIsa4() {
        assertFalse(t.isa(Catcode.ESCAPE));
    }

    /*
     */
    public void testIsa5() {
        assertFalse(t.isa(Catcode.IGNORE));
    }

    /*
     */
    public void testIsa6() {
        assertFalse(t.isa(Catcode.INVALID));
    }

    /*
     */
    public void testIsa7() {
        assertFalse(t.isa(Catcode.LEFTBRACE));
    }

    /*
     */
    public void testIsa8() {
        assertFalse(t.isa(Catcode.LETTER));
    }

    /*
     */
    public void testIsa9() {
        assertFalse(t.isa(Catcode.MACROPARAM));
    }

    /*
     */
    public void testIsa10() {
        assertFalse(t.isa(Catcode.MATHSHIFT));
    }

    /*
     */
    public void testIsa11() {
        assertTrue(t.isa(Catcode.OTHER));
    }

    /*
     */
    public void testIsa12() {
        assertFalse(t.isa(Catcode.RIGHTBRACE));
    }

    /*
     */
    public void testIsa13() {
        assertFalse(t.isa(Catcode.SUBMARK));
    }

    /*
     */
    public void testIsa14() {
        assertFalse(t.isa(Catcode.SUPMARK));
    }

    /*
     */
    public void testIsa15() {
        assertFalse(t.isa(Catcode.TABMARK));
    }
    
}
