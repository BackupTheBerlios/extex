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

/**
 * This Exception is thrown when a configuration is requested with the path
 * <code>null</code>> or the empty string. Alternatively it can be used when
 * some other kind of configuration information is missing.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ConfigurationMissingException extends ConfigurationException {

    /**
     * The field <tt>item</tt> contains the name of the missing item.
     */
    private String item = "";

    /**
     * Create a new object.
     *
     * @param message the message string
     */
    public ConfigurationMissingException(final String message) {

        super(message, (String) null);
    }

    /**
     * Create a new object.
     *
     * @param message the message string
     * @param location the location of the missing configuration item
     */
    public ConfigurationMissingException(final String message,
            final String location) {

        super(null, location);
        item = message;
    }

    /**
     * Creates a new object.
     *
     * @param message message the message string
     * @param cause the next Throwable in the list
     */
    public ConfigurationMissingException(final String message,
            final Throwable cause) {

        super(message, cause);
    }

    /**
     * Getter for the text prefix of this ConfigException.
     * The text is taken from the resource bundle <tt>ConfigurationEception</tt>
     * under the key <tt>ConfigurationMissingException.Text</tt>.
     *
     * @return the text
     */
    protected String getText() {

        return getLocalizer()
                .format("ConfigurationMissingException.Text", item);
    }
}