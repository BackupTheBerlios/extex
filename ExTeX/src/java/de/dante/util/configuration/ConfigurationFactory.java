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
package de.dante.util.configuration;

import de.dante.extex.i18n.Messages;

/**
 * This is the factory for configurations.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ConfigurationFactory {
    /**
     * Creates a new object.
     *
     * @throws ConfigException in case that something went wrong
     */
    public ConfigurationFactory() throws ConfigurationException {
        super();
    }

    /**
     * Determine a new Configuration object which is initialized from a
     * file. 
     *
     * @param source the source of the confugration
     *
     * @return a new Configuration object
     *
     * @throws ConfigurationNoTypesetterException in case that the source is
     * <code>null</code>
     * @throws ConfigurationException in case that the creation of the
     * Configuration fails
     */
    public Configuration newInstance(String source)
                              throws ConfigurationException {
        if (source == null) {
            throw new ConfigurationInvalidNameException(Messages.format("ConfigurationFactory.EmptySourceMessage"));
        }

        return new ConfigurationXMLImpl(source);
    }
}
