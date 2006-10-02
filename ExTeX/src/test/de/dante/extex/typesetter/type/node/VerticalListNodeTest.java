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

package de.dante.extex.typesetter.type.node;

import junit.framework.TestCase;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;

/**
 * This file contains test cases for the vertical list node.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class VerticalListNodeTest extends TestCase {

    /**
     * Command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(VerticalListNodeTest.class);
    }

    /**
     * Test that splitting an empty list works for a positive target height.
     */
    public void testSplitEmpty1() {

        VerticalListNode vlist = new VerticalListNode();
        VerticalListNode vl = vlist.split(Dimen.ONE_INCH, null, null);
        assertTrue(vlist.empty());
        assertTrue(vl.empty());
    }

    /**
     * Test that splitting an empty list works for a target height of 0pt.
     */
    public void testSplitEmpty2() {

        VerticalListNode vlist = new VerticalListNode();
        VerticalListNode vl = vlist.split(Dimen.ZERO_PT, null, null);
        assertTrue(vlist.empty());
        assertTrue(vl.empty());
    }

    /**
     * Test that splitting an empty list works for a negative target height.
     */
    public void testSplitEmpty3() {

        VerticalListNode vlist = new VerticalListNode();
        VerticalListNode vl = vlist.split(new Dimen(-42), null, null);
        assertTrue(vlist.empty());
        assertTrue(vl.empty());
    }

    /**
     * Test that splitting an empty list works for a positive target height.
     */
    public void testVpack1() {

        VerticalListNode vlist = new VerticalListNode();
        vlist.add(new RuleNode(Dimen.ONE_INCH, Dimen.ONE_INCH, Dimen.ONE_INCH,
                null, true));
        Dimen h = new Dimen(Dimen.ONE_INCH);
        h.multiply(2);
        vlist.vpack(h);
        assertFalse(vlist.empty());
        assertEquals("\\vbox(144.53998pt+72.26999pt)x72.26999pt\n"
                + ".\\rule72.26999pt+72.26999ptx72.26999pt", vlist.toString());
    }

    /**
     * Test that splitting an empty list works for a positive target height.
     */
    public void testVpack2() {

        VerticalListNode vlist = new VerticalListNode();
        vlist.addSkip(Glue.S_S);
        vlist.add(new RuleNode(Dimen.ONE_INCH, Dimen.ONE_INCH, Dimen.ONE_INCH,
                null, true));
        vlist.addSkip(Glue.S_S);
        Dimen h = new Dimen(Dimen.ONE_INCH);
        h.multiply(2);
        assertEquals(0, vlist.vpack(h));
        assertFalse(vlist.empty());
        assertEquals("\\vbox(144.53998pt+0.0pt)x72.26999pt\n"
                + ".\\glue0.0pt plus 1.0fil minus 1.0fil\n"
                + ".\\rule72.26999pt+72.26999ptx72.26999pt\n"
                + ".\\glue0.0pt plus 1.0fil minus 1.0fil", vlist.toString());
    }

}
