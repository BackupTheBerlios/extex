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
 * This Exception is thrown when a configuration contains a syntax error.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ConfigurationSyntaxException extends ConfigurationException {
    /**
     * Create a new object.
     *
     * @param message the message string
     * @param source the source of the exception
     */
    public ConfigurationSyntaxException(final String message,
        final String source) {
        super(message, source);
    }

    /**
     * Create a new object.
     *
     * @param message message the message string
     * @param cause the next Throwable in the list
     */
    public ConfigurationSyntaxException(final String message,
        final Throwable cause) {
        super(message, cause);
    }

    /**
     * Getter for the text prefix of this ConfigException.
     *
     * @return the text
     */
    protected String getText() {
        return Messages.format("ConfigSyntaxException.Text");
    }
}
