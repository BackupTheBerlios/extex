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
 * This exception is thrown when a dynamically loaded class could not be found.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ConfigurationClassNotFoundException
    extends ConfigurationException {

    /**
     * The field <tt>classname</tt> contains the name of the class which
     * could not be found.
     */
    private String classname = null;

    /**
     * Creates a new object.
     *
     * @param aClassName the name of the class which could not be found
     * @param config the configuration in which the problem occurred or
     * <code>null</code>
     */
    public ConfigurationClassNotFoundException(final String aClassName,
            final Configuration config) {
        super(null, config.toString());
        this.classname = aClassName;
    }

    /**
     * Creates a new object.
     *
     * @param aClassName the name of the class which could not be found
     */
    public ConfigurationClassNotFoundException(final String aClassName) {
        super(null);
        this.classname = aClassName;
    }

    /**
     * Getter for the text prefix of this ConfigException.
     *
     * @return the text
     */
    protected String getText() {
        return Messages.format("ConfigurationClassNotFoundException.Text",
                               (classname != null //
                                   ? classname //
                                   : getCause() != null ? getCause()
                                       .getMessage() : ""));
    }

}
