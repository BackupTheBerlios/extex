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

package de.dante.extex.format.dvi;

import java.util.Stack;

/**
 * DVI stack.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviStack {

    /**
     * the stack
     */
    private Stack stack;

    /**
     * Create a new object
     */
    public DviStack() {

        stack = new Stack();

    }

    /**
     * Tests if this stack is empty.
     * @return Returns <code>true</code>, if this stack is empty.
     */
    public boolean empty() {

        return stack.empty();
    }

    /**
     * Removes the <code>DVIValues</code> at the top of this stack and
     * returns that object as the value of this function.
     * @return Returns the <code>DVIValues</code> at the top of this stack.
     */
    public DviValues pop() {

        return (DviValues) stack.pop();
    }

    /**
     * Pushes an item onto the top of this stack.
     * @param item the item to be pushed onto this stack.
     * @return Returns the item argument.
     */
    public DviValues push(final DviValues item) {

        return (DviValues) stack.push(item);
    }

    /**
     * Clear the stack.
     */
    public void clear() {

        stack.removeAllElements();
    }
}
