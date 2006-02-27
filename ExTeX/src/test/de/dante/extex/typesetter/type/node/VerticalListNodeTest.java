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

import de.dante.extex.interpreter.type.dimen.Dimen;
import junit.framework.TestCase;

/**
 * This file contains test cases for the vertical list node.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
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

}
