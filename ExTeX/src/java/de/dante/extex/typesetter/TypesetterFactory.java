/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.typesetter;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * This is the factory for Typesetter instances.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class TypesetterFactory {
    /** the local pointer to the configuration object */
    private Configuration config;

    /**
     * Creates a new object.
     *
     * @param config the configuration object for this factory
     */
    public TypesetterFactory(Configuration config) {
        super();
        this.config = config;
    }

    /**
     * Get an instance of a typesetter.
     *
     * @return a new typesetter
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public Typesetter newInstance() throws ConfigurationException {
        Typesetter typesetter;

        try {
            typesetter = (Typesetter) Class.forName(config.getAttribute("class"))
                                           .newInstance();
        } catch (Exception e) {
            throw new ConfigurationException("TypesetterFactory", e);
        }
        typesetter.configure(config);

        return typesetter;
    }
}
