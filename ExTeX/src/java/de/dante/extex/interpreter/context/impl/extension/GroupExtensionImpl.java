/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.type.Bool;
import de.dante.extex.interpreter.type.Pair;
import de.dante.extex.interpreter.type.Real;
import de.dante.extex.interpreter.type.Transform;
import de.dante.extex.main.MainExTeXExtensionException;
import de.dante.util.GeneralException;

/**
 * This is a simple implementation for a group with ExTeX-functions.
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class GroupExtensionImpl extends GroupImpl
        implements
            Tokenizer,
            Group,
            GroupExtension,
            Serializable {

    /**
     * The map for the real registers
     */
    private Map realMap = new HashMap();

    /**
     * The map for the bool registers
     */
    private Map boolMap = new HashMap();

    /**
     * The map for the pair registers
     */
    private Map pairMap = new HashMap();

    /**
     * The map for the transform registers
     */
    private Map transformMap = new HashMap();

    /**
     * The next group in the linked list
     */
    private GroupExtension nextext = null;

    /**
     * Creates a new object.
     *
     * @param next the next group in the stack. If the value is <code>null</code>
     *            then this is the global base
     * @throws GeneralException if the group is not a groupextension
     */
    public GroupExtensionImpl(final Group next) throws GeneralException {

        super(next);
        if (next != null && !(next instanceof GroupExtension)) {
            throw new MainExTeXExtensionException();
        }
        nextext = (GroupExtension) next;
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
    public void setReal(final String name, final Real value,
            final boolean global) {

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

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#getBool(
     *      java.lang.String)
     */
    public Bool getBool(final String name) {

        Bool bool = (Bool) (boolMap.get(name));

        if (bool == null) {
            if (nextext != null) {
                bool = nextext.getBool(name);
            } else {
                bool = new Bool();
                setBool(name, bool);
            }
        }

        return bool;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#setBool(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.Bool, boolean)
     */
    public void setBool(final String name, final Bool value,
            final boolean global) {

        setBool(name, value);

        if (global && nextext != null) {
            nextext.setBool(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#setBool(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.Bool)
     */
    public void setBool(final String name, final Bool value) {

        boolMap.put(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#getPair(
     *      java.lang.String)
     */
    public Pair getPair(final String name) {

        Pair pair = (Pair) (pairMap.get(name));

        if (pair == null) {
            if (nextext != null) {
                pair = nextext.getPair(name);
            } else {
                pair = new Pair();
                setPair(name, pair);
            }
        }

        return pair;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#setPair(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.Pair,
     *      boolean)
     */
    public void setPair(final String name, final Pair value,
            final boolean global) {

        setPair(name, value);

        if (global && nextext != null) {
            nextext.setPair(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#setPair(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.Pair)
     */
    public void setPair(final String name, final Pair value) {

        pairMap.put(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#getTransform(
     *      java.lang.String)
     */
    public Transform getTransform(final String name) {

        Transform transform = (Transform) (transformMap.get(name));

        if (transform == null) {
            if (nextext != null) {
                transform = nextext.getTransform(name);
            } else {
                transform = new Transform();
                setTransform(name, transform);
            }
        }

        return transform;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#setTransform(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.Transform,
     *      boolean)
     */
    public void setTransform(final String name, final Transform value,
            final boolean global) {

        setTransform(name, value);

        if (global && nextext != null) {
            nextext.setTransform(name, value, global);
        }

    }

    /**
     * @see de.dante.extex.interpreter.context.impl.extension.GroupExtension#setTransform(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.Transform)
     */
    public void setTransform(final String name, final Transform value) {

        transformMap.put(name, value);
    }
}