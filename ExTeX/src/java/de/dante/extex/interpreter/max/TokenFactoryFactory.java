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
package de.dante.extex.interpreter.max;

import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;


/**
 * This class provides a factory for token factories.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
class TokenFactoryFactory extends AbstractFactory {

    /**
     * The field <tt>configuration</tt> contains the configuration for
     * this factory.
     */
    private Configuration configuration;

    /**
     * Creates a new object.
     *
     * @param config the configuration to use
     */
    public TokenFactoryFactory(final Configuration config) {

        super();
        this.configuration = config;
    }

    /**
     * Instance delivering method.
     *
     * @return an appropriate instance
     *
     * @throws ConfigurationException in case of an error in the
     *  configuration
     */
    public TokenFactory createInstance() throws ConfigurationException {

        return (TokenFactory) createInstanceForConfiguration(configuration,
                TokenFactory.class);
    }
}
