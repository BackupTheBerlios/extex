/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.pair;

import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.type.pair.Pair;
import de.dante.extex.scanner.stream.impl32.TokenStreamStringImpl;
import de.dante.tex.TestTeX;
import junit.framework.TestCase;

/**
 * A test for the pair-regsiter.
 *
 * <p>
 * use extex-extension
 * </p>
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class PairRegisterTest extends TestCase {

    /**
     * Creates a new object.
     * @param arg0 ...
     */
    public PairRegisterTest(final String arg0) {

        super(arg0);
    }

    /**
     * ...
     * @param args ...
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(PairRegisterTest.class);
    }


    /**
     * ...
     * @throws Exception ...
     */
    public void testSp1() throws Exception {

        Interpreter source = TestTeX.makeInterpreter();

        source.addStream(new TokenStreamStringImpl("1234 5678  ende"));
        Pair val = new Pair(null, source);

        assertEquals("1234.0", val.getX().toString());
        assertEquals("5678.0", val.getY().toString());
    }

}