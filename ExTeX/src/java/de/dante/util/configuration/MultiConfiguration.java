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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.dante.util.StringList;

/**
 * Container for several {@link de.dante.util.configuration.Configuration Configuration} objects.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class MultiConfiguration implements Configuration {
    /** The internal array of configs */
    private Configuration[] configs;
    
    /**
     * Creates a new object.
     *
     * @param parts the configs to treat jointly
     *
     * @throws ConfigException in case that the configuration is invalid
     */
    private MultiConfiguration(Configuration[] parts) throws ConfigurationException {
        super();
        configs = parts;
    }

    /**
     * @see de.dante.util.configuration.Configuration#getConfiguration(java.lang.String)
     */
    public Configuration getConfiguration(String key) throws ConfigurationException {
        List v = new ArrayList();

        for (int i = 0; i < configs.length; i++) {
            Configuration cfg =configs[i].findConfiguration(key);
            if (cfg != null){
                v.add(cfg);
            }
        }

        switch (v.size()) {
            case 0 :
                return null;
            case 1 :
                return (Configuration) v.get(0);
            default :
                return new MultiConfiguration((Configuration[]) v.toArray());
        }
    }

/**
     * @see de.dante.util.configuration.Configuration#findConfiguration(java.lang.String)
     */
    public Configuration findConfiguration(String key)
            throws ConfigurationException {

        List v = new ArrayList();

        for (int i = 0; i < configs.length; i++) {
            Configuration cfg = configs[i].findConfiguration(key);
            if (cfg != null) {
                v.add(cfg);
            }
        }

        switch (v.size()) {
            case 0 :
                return null;
            case 1 :
                return (Configuration) v.get(0);
            default :
                return new MultiConfiguration((Configuration[]) v.toArray());
        }
    }

    /**
     * @see de.dante.util.configuration.Configuration#getConfiguration(java.lang.String, java.lang.String)
     */
    public Configuration getConfiguration(String key, String attribute)
                     throws ConfigurationException {
        List v = new ArrayList();

        for (int i = 0; i < configs.length; i++) {
            try {
                v.add(configs[i].getConfiguration(key, attribute));
            } catch (ConfigurationNotFoundException e) {
                // ignored
            }
        }

        if (v.size() == 0) {
            throw new ConfigurationNotFoundException(null,key+"["+attribute+"]");
        }

        if (v.size() == 1) {
            return (Configuration) v.get(0);
        }

        return new MultiConfiguration((Configuration[]) v.toArray());
    }

    /**
     * @see de.dante.util.configuration.Configuration#getValueAsInteger(java.lang.String, int)
     */
    public int getValueAsInteger(String key, int defaultValue)
                    throws ConfigurationException {
        for (int i = 0; i < configs.length; i++) {
            int val = configs[i].getValueAsInteger(key, defaultValue);

            if (val != defaultValue) {
                return val;
            }
        }

        return defaultValue;
    }

    /**
     * @see de.dante.util.configuration.Configuration#getValue(java.lang.String)
     */
    public String getValue(String key) throws ConfigurationException {
        for (int i = 0; i < configs.length; i++) {
            String val = configs[i].getValue(key);

            if (val != null) {
                return val;
            }
        }

        return null;
    }

    /**
     * @see de.dante.util.configuration.Configuration#getValues(java.lang.String)
     */
    public StringList getValues(String key) throws ConfigurationException {
        StringList sl = new StringList();

        for (int i = 0; i < configs.length; i++) {
            // TODO configs[i].addValues(key, sl);
            throw new ConfigurationMissingException("unimplemented");
        }

        return sl;
    }

    /**
     * @see de.dante.util.configuration.Configuration#iterator(java.lang.String)
     */
    public Iterator iterator(String key) throws ConfigurationException {
        return new MultiConfigurationIterator(configs, key);
    }
    
    /**
     * @see de.dante.util.configuration.Configuration#getAttribute(java.lang.String)
     */
    public String getAttribute(String name) throws ConfigurationException {
        // todo unimplemented because not needed
        throw new ConfigurationMissingException("unimplemented");
    }
    /**
     * @see de.dante.util.configuration.Configuration#getValue()
     */
    public String getValue() throws ConfigurationException {
        // todo unimplemented because not needed
        throw new ConfigurationMissingException("unimplemented");
    }

}
