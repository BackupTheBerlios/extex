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

package de.dante.extex.interpreter.context;

import java.io.Serializable;

import de.dante.extex.interpreter.type.bool.Bool;
import de.dante.extex.interpreter.type.hash.toks.HashToks;
import de.dante.extex.interpreter.type.pair.Pair;
import de.dante.extex.interpreter.type.real.Real;
import de.dante.extex.interpreter.type.transform.Transform;

/**
 * This interface describes the container for all data of an interpreter
 * context for the ExTeX-functions.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public interface ContextExtension extends Serializable {

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Real real}
     * register in the current group. Real registers are named, either with a
     * number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     */
    void setReal(String name, Real value);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Real real}
     * register in all requested groups. Real registers are named, either with
     * a number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param real the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setReal(String name, Real real, boolean global);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.Real real}
     * register. Real registers are named, either with a number or an
     * arbitrary string.
     *
     * @param name the name or number of the real register
     *
     * @return the real register or <code>null</code> if it is not defined
     */
    Real getReal(String name);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Bool bool}
     * register in the current group. Bool registers are named, either with a
     * number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     */
    void setBool(String name, Bool value);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Bool bool}
     * register in all requested groups. Bool registers are named, either with
     * a number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setBool(String name, Bool value, boolean global);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.Bool bool}
     * register. Bool registers are named, either with a number or an
     * arbitrary string.
     *
     * @param name the name or number of the register
     * @return the bool register or <code>null</code> if it is not defined
     */
    Bool getBool(String name);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Pair pair}
     * register in the current group. pair registers are named, either with a
     * number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     */
    void setPair(String name, Pair value);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Pair pair}
     * register in all requested groups. Pair registers are named, either with
     * a number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setPair(String name, Pair value, boolean global);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.Pair pair}
     * register. Pair registers are named, either with a number or an
     * arbitrary string.
     *
     * @param name the name or number of the register
     * @return the bool register or <code>null</code> if it is not defined
     */
    Pair getPair(String name);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Transform transform}
     * register in the current group. transform registers are named, either with a
     * number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     */
    void setTransform(String name, Transform value);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Transform transform}
     * register in all requested groups. Transform registers are named, either with
     * a number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setTransform(String name, Transform value, boolean global);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.Transform transform}
     * register. Transform registers are named, either with a number or an
     * arbitrary string.
     *
     * @param name the name or number of the register
     * @return the bool register or <code>null</code> if it is not defined
     */
    Transform getTransform(String name);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.HashToks hashtoks}
     * register in the current group. Hashtoks registers are named, either with a
     * number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     */
    void setHashToks(String name, HashToks value);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.HashToks hashtoks}
     * register in all requested groups. Transform registers are named, either with
     * a number or an arbitrary string.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setHashToks(String name, HashToks value, boolean global);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.HashToks hashtoks}
     * register. Transform registers are named, either with a number or an
     * arbitrary string.
     *
     * @param name the name or number of the register
     * @return the bool register or <code>null</code> if it is not defined
     */
    HashToks getHashToks(String name);

}