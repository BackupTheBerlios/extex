/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
 * This Exception is thrown when a configuration is requested with the path
 * <code>null</code>> or the empty string. Alternatively it can be used when
 * some other kind of configuration information is missing.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ConfigurationIOException extends ConfigurationException {
    /**
     * Create a new object.
     *
     * @param message the message string
     */
    public ConfigurationIOException(final String message) {
        super(message, (String) null);
    }

    /**
     * Create a new object.
     *
     * @param message the message string
     * @param location the location of the IO configuration item
     */
    public ConfigurationIOException(final String message, final String location) {
        super(message, location);
    }

    /**
     * Creates a new object.
     *
     * @param message message the message string
     * @param cause the next Throwable in the list
     */
    public ConfigurationIOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Getter for the text prefix of this Exception.
     * The text is taken from the {@link de.dante.extex.i18n.Messages Messages}
     * under the key <tt>ConfigurationIOException.Text</tt>.
     *
     * @return the text
     */
    protected String getText() {
        return Messages.format("ConfigurationIOException.Text");
    }
}
