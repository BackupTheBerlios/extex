/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

import java.util.Iterator;
import java.util.Properties;

import de.dante.util.StringList;
import de.dante.util.framework.configuration.Configuration;

/**
 * This class provides an implementation for a configuration which is based on
 * properties.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ConfigurationPropertyImpl implements Configuration {

    /**
     * The field <tt>base</tt> contains the ...
     */
    private String base;

    /**
     * The field <tt>properties</tt> contains the ...
     */
    private Properties properties;

    /**
     * Creates a new object.
     *
     * @param properties ...
     */
    public ConfigurationPropertyImpl(final Properties properties) {

        super();
        this.properties = (Properties) properties.clone();
        this.base = "";
    }

    /**
     * Creates a new object.
     *
     * @param properties
     * @param base
     */
    private ConfigurationPropertyImpl(final Properties properties,
            final String base) {

        super();
        this.properties = properties;
        this.base = base;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#findConfiguration(java.lang.String)
     */
    public Configuration findConfiguration(final String key)
            throws ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#findConfiguration(
     *      java.lang.String,
     *      java.lang.String)
     */
    public Configuration findConfiguration(final String key,
            final String attribute) throws ConfigurationException {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getAttribute(
     *     java.lang.String)
     */
    public String getAttribute(final String name) {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getConfiguration(
     *      java.lang.String)
     */
    public Configuration getConfiguration(final String key)
            throws ConfigurationException {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getConfiguration(
     *      java.lang.String,
     *      java.lang.String)
     */
    public Configuration getConfiguration(final String key,
            final String attribute) throws ConfigurationException {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getValue()
     */
    public String getValue() throws ConfigurationException {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getValue(java.lang.String)
     */
    public String getValue(final String key) throws ConfigurationException {

        return properties.getProperty(key, null);
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getValueAsInteger(
     *      java.lang.String, int)
     */
    public int getValueAsInteger(final String key, final int defaultValue)
            throws ConfigurationException {

        String s = getValue(key);

        if (s != null && s.matches("[0-9]+")) {
            return Integer.parseInt(s);
        }

        return defaultValue;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getValues(
     *      java.lang.String)
     */
    public StringList getValues(final String key) {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#getValues(
     *      de.dante.util.StringList,
     *      java.lang.String)
     */
    public void getValues(final StringList list, final String key) {

    }

    /**
     * @see de.dante.util.framework.configuration.Configuration#iterator(
     *      java.lang.String)
     */
    public Iterator iterator(final String key) throws ConfigurationException {

        return new Iterator() {

            public void remove() {

            }

            public boolean hasNext() {

                return false;
            }

            public Object next() {

                return null;
            }
        };
    }

}
