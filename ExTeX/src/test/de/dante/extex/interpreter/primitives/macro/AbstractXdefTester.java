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

package de.dante.extex.interpreter.primitives.macro;

/**
 * This is a test suite for xdef primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractXdefTester extends AbstractDefTester {

    /**
     * Creates a new object.
     *
     * @param name the name
     * @param def the name of the primitive
     */
    public AbstractXdefTester(final String name, final String def) {

        super(name, def);
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testExpand1() throws Exception {

        assertSuccess(//--- input code ---
                DEFINE_BRACES 
                + "\\def\\a{A}"
                + "\\" + getDef() + "\\aaa{X\\a X}"
                + "\\def\\a{B}"
                + "--\\aaa--\\end",
                //--- output message ---
                "--XAX--" + TERM);
    }


}
