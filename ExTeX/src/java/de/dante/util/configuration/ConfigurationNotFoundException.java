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
 * This Exception is thrown when a configuration could not be found.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ConfigurationNotFoundException extends ConfigurationException {
    /**
     * The field <tt>configName</tt> contains the name of the missing
     * configuration.
     */
    private String configName;

    /**
     * Create a new object.
     *
     * @param configName the name of the missing configuration
     * @param source the the name of the file for which this exception occurred
     */
    public ConfigurationNotFoundException(final String configName,
        final String source) {
        super(null, source);
        this.configName = configName;
    }

    /**
     * Getter for the text prefix of this
     * {@link de.dante.util.ConfigurationException ConfigurationException}.
     *
     * @return the text
     */
    protected String getText() {
        return Messages.format("ConfigurationNotFoundException.Text",
                               configName);
    }
}
