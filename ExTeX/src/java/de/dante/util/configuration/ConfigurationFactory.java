/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
 * @version $Revision: 1.5 $
 */
public class ConfigurationFactory {
    /**
     * Creates a new object.
     */
    public ConfigurationFactory() {
        super();
    }

    /**
     * Delivers a new
     * {@link de.dante.extex.util.configuration.Configuration Configuration}
     * object which is initialized from a named source. This source is usually
     * a file name but can be anything else, like a URL or a reference to a
     * database -- depending on the underlying implemnetation.
     *
     * @param source the source of the confugration
     *
     * @return a new Configuration object
     *
     * @throws ConfigurationNoTypesetterException in case that the source is
     *             <code>null</code>
     * @throws ConfigurationException in case that the creation of the
     *             Configuration fails
     */
    public Configuration newInstance(final String source)
        throws ConfigurationException {
        if (source == null) {
            throw new ConfigurationInvalidNameException(Messages
                .format("ConfigurationFactory.EmptySourceMessage"));
        }

        return new ConfigurationXMLImpl(source);
    }
}
