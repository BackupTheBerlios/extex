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
package de.dante.extex.interpreter.primitives;

import de.dante.extex.ExTeXLauncher;
import junit.framework.TestCase;

/*
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class RelaxTest extends TestCase {

    /**
     * Main program. Just in case.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(RelaxTest.class);
    }

    /**
     * Constructor for RelaxTest.
     * 
     * @param arg0
     */
    public RelaxTest(String arg0) {
        super(arg0);
    }
    
    /**
     * Test case checking that a pure \relax has no effect.
     */
    public void test0() throws Exception {
        String result = new ExTeXLauncher("pi*").run("abc\\relax def");
        System.err.println(result);
        assertEquals("", result);
    }
    
    /**
     * Test case checking that a pure \relax has no effect.
     */
    public void test1() throws Exception {
        String result = new ExTeXLauncher("pi").run("\\relax");
        assertEquals("", result);
    }
    
    /**
     * Test case checking that a whitespace after a \relax is ignored.
     */
    public void test2() throws Exception {
        String result = new ExTeXLauncher("pi").run("\\relax ");
        assertEquals("", result);
    }

    /**
     * Test case checking that more whitespace after a \relax is ignored.
     */
    public void test3() throws Exception {
        String result = new ExTeXLauncher("pi").run("\\relax               ");
        assertEquals("", result);
    }

    /**
     * Test case checking that a comment after a \relax is ignored.
     */
    public void test4() throws Exception {
        String result = new ExTeXLauncher("pi").run("\\relax %1234 ");
        assertEquals("", result);
    }
}
