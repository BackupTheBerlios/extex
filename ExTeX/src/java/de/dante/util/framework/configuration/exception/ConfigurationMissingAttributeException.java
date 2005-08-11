/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.util.framework.configuration.exception;

import de.dante.util.framework.configuration.Configuration;


/**
 * This Exception is thrown when a configuration is requested with the path
 * <code>null</code>> or the empty string.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ConfigurationMissingAttributeException extends
        ConfigurationException {

    /**
     * Create a new object.
     *
     * @param message the message string
     * @param location the location where the exception has occured
     */
    public ConfigurationMissingAttributeException(final String message,
        final String location) {

        super(message, location);
    }

    /**
     * Create a new object.
     *
     * @param message the message string
     * @param origin the configuration in which the exception has occurred
     */
    public ConfigurationMissingAttributeException(final String message,
        final Configuration origin) {

        super(message, origin.toString());
    }

    /**
     * Creates a new object.
     *
     * @param message message the message string
     * @param cause the next Throwable in the list
     */
    public ConfigurationMissingAttributeException(final String message,
        final Throwable cause) {

        super(message, cause);
    }

    /**
     * Getter for the text prefix of this ConfigException.
     * The text is taken from the resource bundle <tt>ConfigurationEception</tt>
     * under the key <tt>ConfigurationMissingAttributeException.Text</tt>. The
     * argument {0} is replaced by the name of the missing attribute as passed
     * to the constructor.
     *
     * @return the text
     */
    public String getText() {

        return getLocalizer().format("ConfigurationMissingAttributeException.Text");
    }

}
