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
 * This exception is thrown when a dynamicaly loaded class has signaled an
 * illegal access.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ConfigurationIllegalAccessException extends ConfigurationException {

    /**
     * Creates a new object.
     *
     * @param cause the next Throwable in the list
     */
    public ConfigurationIllegalAccessException(final Throwable cause) {

        super(null, cause);
    }

    /**
     * Getter for the text prefix of this ConfigException.
     * The text is taken from the resource bundle <tt>ConfigurationEception</tt>
     * under the key <tt>ConfigurationIllegalAccessException.Text</tt>. The
     * argument {0} is replaced by the message of the embedded cause as passed
     * to the constructor.
     *
     * @return the text
     */
    protected String getMesssage() {

        return getLocalizer().format(
                "ConfigurationIllegalAccessException.Text",
                getCause().getLocalizedMessage());
    }

}