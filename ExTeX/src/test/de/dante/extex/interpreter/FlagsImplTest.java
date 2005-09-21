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

package de.dante.extex.interpreter;

import junit.framework.TestCase;

/**
 * Test cases for the flags implementation.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FlagsImplTest extends TestCase {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(FlagsImplTest.class);
    }

    /*
     */
    public void testClear() {

    }

    /*
     */
    public void testIsExpanded1() {

        Flags f = new FlagsImpl();
        assertFalse(f.isExpanded());
    }

    /*
     */
    public void testIsGlobal() {

        Flags f = new FlagsImpl();
        assertFalse(f.isGlobal());
    }

    /*
     */
    public void testIsImmediate() {

        Flags f = new FlagsImpl();
        assertFalse(f.isImmediate());
    }

    /*
     */
    public void testIsLong() {

        Flags f = new FlagsImpl();
        assertFalse(f.isLong());
    }

    /*
     */
    public void testIsOuter() {

        Flags f = new FlagsImpl();
        assertFalse(f.isOuter());
    }

    /*
     */
    public void testIsProtected() {

        Flags f = new FlagsImpl();
        assertFalse(f.isProtected());
    }

    /*
     */
    public void testSetExpanded() {

        Flags f = new FlagsImpl();
        f.setExpanded();
        assertTrue(f.isExpanded());
    }

    /*
     */
    public void testSetGlobal() {

        Flags f = new FlagsImpl();
        f.setGlobal();
        assertFalse(f.isExpanded());
        assertTrue(f.isGlobal());
        assertFalse(f.isImmediate());
        assertFalse(f.isLong());
        assertFalse(f.isOuter());
        assertFalse(f.isProtected());
    }

    /*
     */
    public void testSetGlobalBoolean1() {

        Flags f = new FlagsImpl();
        f.setGlobal(true);
        assertFalse(f.isExpanded());
        assertTrue(f.isGlobal());
        assertFalse(f.isImmediate());
        assertFalse(f.isLong());
        assertFalse(f.isOuter());
        assertFalse(f.isProtected());
    }

    /*
     */
    public void testSetGlobalBoolean2() {

        Flags f = new FlagsImpl();
        f.setGlobal(false);
        assertFalse(f.isExpanded());
        assertFalse(f.isGlobal());
        assertFalse(f.isImmediate());
        assertFalse(f.isLong());
        assertFalse(f.isOuter());
        assertFalse(f.isProtected());
    }

    /*
     */
    public void testSetImmediate() {

        Flags f = new FlagsImpl();
        f.setImmediate();
        assertFalse(f.isExpanded());
        assertFalse(f.isGlobal());
        assertTrue(f.isImmediate());
        assertFalse(f.isLong());
        assertFalse(f.isOuter());
        assertFalse(f.isProtected());
    }

    /*
     */
    public void testSetLong() {

        Flags f = new FlagsImpl();
        f.setLong();
        assertFalse(f.isExpanded());
        assertFalse(f.isGlobal());
        assertFalse(f.isImmediate());
        assertTrue(f.isLong());
        assertFalse(f.isOuter());
        assertFalse(f.isProtected());
    }

    /*
     */
    public void testSetOuter() {

        Flags f = new FlagsImpl();
        f.setOuter();
        assertFalse(f.isExpanded());
        assertFalse(f.isGlobal());
        assertFalse(f.isImmediate());
        assertFalse(f.isLong());
        assertTrue(f.isOuter());
        assertFalse(f.isProtected());
    }

    /*
     */
    public void testSetProtected() {

        Flags f = new FlagsImpl();
        f.setProtected();
        assertFalse(f.isExpanded());
        assertFalse(f.isGlobal());
        assertFalse(f.isImmediate());
        assertFalse(f.isLong());
        assertFalse(f.isOuter());
        assertTrue(f.isProtected());
    }

}
