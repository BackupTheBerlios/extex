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

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextExtension;
import de.dante.extex.interpreter.context.impl.ContextImpl;
import de.dante.extex.interpreter.type.bool.Bool;
import de.dante.extex.interpreter.type.hash.toks.HashToks;
import de.dante.extex.interpreter.type.pair.Pair;
import de.dante.extex.interpreter.type.real.Real;
import de.dante.extex.interpreter.type.transform.Transform;
import de.dante.extex.main.exception.MainExTeXExtensionException;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * This is a reference implementation for an interpreter context with
 * ExTeX functions.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class ContextExtensionImpl extends ContextImpl
        implements
            Context,
            ContextExtension,
            Serializable {

    /**
     * Creates a new object.
     * @param   config  the configuration
     * @throws ConfigurationException ...
     * @throws GeneralException ...
     */
    public ContextExtensionImpl(final Configuration config)
            throws ConfigurationException, GeneralException {

        super(config);
        if (!(getGroup() instanceof GroupExtension)) {
            throw new MainExTeXExtensionException();
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#getReal(
     *      java.lang.String)
     */
    public Real getReal(final String name) {

        return ((GroupExtension) getGroup()).getReal(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setReal(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.real.Real,
     *      boolean)
     */
    public void setReal(final String name, final Real value,
            final boolean global) {

        ((GroupExtension) getGroup()).setReal(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setReal(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.real.Real)
     */
    public void setReal(final String name, final Real value) {

        ((GroupExtension) getGroup()).setReal(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#getBool(java.lang.String)
     */
    public Bool getBool(final String name) {

        return ((GroupExtension) getGroup()).getBool(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setBool(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.bool.Bool, boolean)
     */
    public void setBool(final String name, final Bool value,
            final boolean global) {

        ((GroupExtension) getGroup()).setBool(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setBool(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.bool.Bool)
     */
    public void setBool(final String name, final Bool value) {

        ((GroupExtension) getGroup()).setBool(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#getPair(
     *      java.lang.String)
     */
    public Pair getPair(final String name) {

        return ((GroupExtension) getGroup()).getPair(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setPair(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.pair.Pair,
     *      boolean)
     */
    public void setPair(final String name, final Pair value,
            final boolean global) {

        ((GroupExtension) getGroup()).setPair(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setPair(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.pair.Pair)
     */
    public void setPair(final String name, final Pair value) {

        ((GroupExtension) getGroup()).setPair(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#getTransform(
     *      java.lang.String)
     */
    public Transform getTransform(final String name) {

        return ((GroupExtension) getGroup()).getTransform(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setTransform(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.transform.Transform,
     *      boolean)
     */
    public void setTransform(final String name, final Transform value,
            final boolean global) {

        ((GroupExtension) getGroup()).setTransform(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setTransform(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.transform.Transform)
     */
    public void setTransform(final String name, final Transform value) {

        ((GroupExtension) getGroup()).setTransform(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#getHashToks(
     *      java.lang.String)
     */
    public HashToks getHashToks(final String name) {

        return ((GroupExtension) getGroup()).getHashToks(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setHashToks(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.hash.toks.HashToks,
     *      boolean)
     */
    public void setHashToks(final String name, final HashToks value,
            final boolean global) {

        ((GroupExtension) getGroup()).setHashToks(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExtension#setHashToks(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.hash.toks.HashToks)
     */
    public void setHashToks(final String name, final HashToks value) {

        ((GroupExtension) getGroup()).setHashToks(name, value);
    }
}