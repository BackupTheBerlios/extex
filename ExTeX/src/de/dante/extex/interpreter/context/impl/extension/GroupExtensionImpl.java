/*
 * Copyright (C) 2004 Michael Niedermair
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
package de.dante.extex.interpreter.context.impl.extension;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.impl.Group;
import de.dante.extex.interpreter.context.impl.GroupImpl;
import de.dante.extex.interpreter.type.Real;

/**
 * This is a simple implementation for a group with ExTeX-functions.
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class GroupExtensionImpl extends GroupImpl implements Tokenizer,
         Group, GroupExtension, Serializable {

    /**
     * The map for the real registers
     */
    private Map realMap = new HashMap();

    /**
     * The next group in the linked list
     */
    private GroupExtension nextext = null;

    /**
     * Creates a new object.
     *
     * @param next the next group in the stack. If the value is <code>null</code>
     *            then this is the global base
     */
    public GroupExtensionImpl(final Group next) {

        super(next);
        nextext = (GroupExtension)next; // TODO test with instanceof -> throw Exception
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getReal(java.lang.String)
     */
    public Real getReal(final String name) {
        Real real = (Real) (realMap.get(name));

        if (real == null) {
            if (nextext != null) {
                real = nextext.getReal(name);
            } else {
                real = new Real(0);
                setReal(name, real);
            }
        }

        return real;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setReal(java.lang.String,
     *      de.dante.extex.interpreter.type.Real, boolean)
     */
    public void setReal(final String name, final Real value, final boolean global) {
        setReal(name, value);

        if (global && nextext != null) {
            nextext.setReal(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setReal(java.lang.String,
     *      de.dante.extex.interpreter.type.Real)
     */
    public void setReal(final String name, final Real value) {
        realMap.put(name, value);
    }
}
