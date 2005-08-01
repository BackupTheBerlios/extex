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

package de.dante.util.font;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationFactory;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Abstract class for all font utilities.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public abstract class AbstractFontUtil {

    /**
     * teh configuration
     */
    private Configuration config;

    /**
     * the properties
     */
    private Properties prop;

    /**
     * the resource finder
     */
    private ResourceFinder finder;

    /**
     * Create a new object.
     * @throws ConfigurationException if a config-error occurs.
     */
    public AbstractFontUtil() throws ConfigurationException {

        super();

        config = new ConfigurationFactory().newInstance("config/extex.xml");

        prop = new Properties();
        try {
            InputStream in = new FileInputStream(".extex");
            prop.load(in);
        } catch (Exception e) {
            prop.setProperty("extex.fonts", "src/font");
        }

        try {
            InputStream in = new FileInputStream(System.getProperty("user.home") + "/.extex");
            Properties prop2 = new Properties();
            prop2.load(in);
            prop.putAll(prop2);
        } catch (Exception e) {
            // do ntohing
            e.printStackTrace();
        }

        finder = (new ResourceFinderFactory()).createResourceFinder(config
                .getConfiguration("Resource"), null, prop);

    }

    /**
     * Returns the config.
     * @return Returns the config.
     */
    public Configuration getConfig() {

        return config;
    }

    /**
     * Returns the finder.
     * @return Returns the finder.
     */
    public ResourceFinder getFinder() {

        return finder;
    }

    /**
     * Returns the prop.
     * @return Returns the prop.
     */
    public Properties getProp() {

        return prop;
    }

}
