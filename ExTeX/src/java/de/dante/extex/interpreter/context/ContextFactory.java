/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

import java.util.logging.Logger;

import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a factory for a
 * {@link de.dante.extex.interpreter.context.Context Context}.
 *
 *
 * <pre>
 *  &lt;Context class="the.package.TheClass"&gt;
 *  &lt;/Context&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class ContextFactory extends AbstractFactory {

    /**
     * Creates a new object.
     *
     * @param configuration the configuration for this factory
     * @param logger the logger
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    public ContextFactory(final Configuration configuration, final Logger logger)
            throws ConfigurationException {

        super();
        enableLogging(logger);
        configure(configuration);
    }

    /**
     * Get an instance of a context.
     * This method selects one of the entries in the configuration. The
     * selection is done with the help of a type String. If the type is
     * <code>null</code> or the empty string then the default from the
     * configuration is used.
     *
     * @param type the type to use
     *
     * @return a new context
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public Context newInstance(final String type) throws ConfigurationException {

        return (Context) createInstance(type, Context.class);
    }

}
