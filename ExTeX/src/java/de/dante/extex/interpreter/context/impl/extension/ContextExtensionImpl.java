/*
 * Copyright (C) 2004 The ExTeX Group
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
import de.dante.extex.interpreter.type.Real;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationMissingException;

/**
 * This is a reference implementation for an interpreter context with
 * ExTeX functions.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
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
            throw new ConfigurationMissingException("illegal group found");
            //TODO i18n
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExTeX#getReal(java.lang.String)
     */
    public Real getReal(final String name) {
        return ((GroupExtension) getGroup()).getReal(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExTeX#setReal(java.lang.String,
     *      de.dante.extex.interpreter.type.Real, boolean)
     */
    public void setReal(final String name, final Real value, final boolean global) {
        ((GroupExtension) getGroup()).setReal(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextExTeX#setReal(java.lang.String,
     *      de.dante.extex.interpreter.type.Real)
     */
    public void setReal(final String name, final Real value) {
        ((GroupExtension) getGroup()).setReal(name, value);
    }
}
