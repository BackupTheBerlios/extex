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

import junit.framework.TestCase;

/*
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ControlSequenceTokenTest extends TestCase {

    /*
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ControlSequenceTokenTest.class);
    }

    /*
     */
    public void testGetCatcode() {
        Token t = new ControlSequenceToken("x");
        assertEquals(Catcode.ESCAPE,t.getCatcode());
    }

    /*
     */
    public void testToString() {
        Token t = new ControlSequenceToken("x");
        assertEquals("the control sequence \\x",t.toString());
    }

    /*
     */
    public void testToText() {
        Token t = new ControlSequenceToken("x");
        assertEquals("x",t.toText());
    }

    /*
     */
    public void testGetValue() {
        Token t = new ControlSequenceToken("x");
        assertEquals("x",t.getValue());
    }

    /*
     */
    public void testEqualsToken0() {
        Token t = new ControlSequenceToken(" ");
        assertTrue(t.equals(t));
     }

    /*
     */
    public void testEqualsToken1() {
        Token t = new ControlSequenceToken(" ");
        Token t2 = new OtherToken(" ");
        assertFalse(t.equals(t2));
    }

    /*
     */
    public void testEqualsCatcodeString0() {
        Token t = new ControlSequenceToken(" ");
        assertTrue(t.equals(Catcode.ESCAPE," "));
    }

    /*
     */
    public void testEqualsCatcodeString1() {
        Token t = new ControlSequenceToken("");
        assertFalse(t.equals(Catcode.OTHER," "));
    }

    /*
     */
    public void testEqualsCatcodechar0() {
        Token t = new ControlSequenceToken(" ");
        assertTrue(t.equals(Catcode.ESCAPE,' '));
    }

    /*
     */
    public void testEqualsCatcodechar1() {
        Token t = new ControlSequenceToken("");
        assertFalse(t.equals(Catcode.OTHER,' '));
    }

    /*
     */
    public void testEqualschar0() {
        Token t = new ControlSequenceToken(" ");
        assertTrue(t.equals(' '));
    }

    /*
     */
    public void testEqualschar1() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.equals('.'));
    }

    /*
     */
    public void testIsa0() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.SPACE));
    }

    /*
     */
    public void testIsa1() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.ACTIVE));
    }

    /*
     */
    public void testIsa2() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.COMMENT));
    }

    /*
     */
    public void testIsa3() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.CR));
    }

    /*
     */
    public void testIsa4() {
        Token t = new ControlSequenceToken(" ");
        assertTrue(t.isa(Catcode.ESCAPE));
    }

    /*
     */
    public void testIsa5() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.IGNORE));
    }

    /*
     */
    public void testIsa6() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.INVALID));
    }

    /*
     */
    public void testIsa7() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.LEFTBRACE));
    }

    /*
     */
    public void testIsa8() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.LETTER));
    }

    /*
     */
    public void testIsa9() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.MACPARAM));
    }

    /*
     */
    public void testIsa10() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.MATHSHIFT));
    }

    /*
     */
    public void testIsa11() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.OTHER));
    }

    /*
     */
    public void testIsa12() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.RIGTHBRACE));
    }

    /*
     */
    public void testIsa13() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.SUBMARK));
    }

    /*
     */
    public void testIsa14() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.SUPMARK));
    }

    /*
     */
    public void testIsa15() {
        Token t = new ControlSequenceToken(" ");
        assertFalse(t.isa(Catcode.TABMARK));
    }
    
}
