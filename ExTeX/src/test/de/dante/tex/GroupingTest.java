/*
 * Copyright (C) 2004  Michael Niedermair
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
package de.dante.tex;

import junit.framework.TestCase;

/*
 * Test for grouping.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class GroupingTest extends TestCase {

    /**
     * Main entry function for running alone.
     * @param  args commandlineargs
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(GroupingTest.class);
    }


    /**
     * Test the primitive <tt>\u005cunless</tt>.
     * @exception Exception iff test failed
     */
    public void testGroup()
        throws Exception {

        TestTeX.test("jugrouping", "src/test/data/jugrouping.testtxt");
    }

}
