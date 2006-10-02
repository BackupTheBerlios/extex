/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.main.logging.LogFormatter;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Abstract class for all font utilities.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */

public abstract class AbstractFontUtil {

    /**
     * the configuration.
     */
    private Configuration config;

    /**
     * the properties.
     */
    private Properties prop;

    /**
     * the resource finder.
     */
    private ResourceFinder finder;

    /**
     * The field <tt>logger</tt> contains the logger currently in use.
     */
    private Logger logger;

    /**
     * The field <tt>localizer</tt> contains the localizer. It is initiated
     * with a localizer for the name of this class.
     */
    private Localizer localizer = LocalizerFactory
            .getLocalizer(AbstractFontUtil.class.getName());

    /**
     * The console handler.
     */
    private Handler consoleHandler;

    /**
     * Create a new object.
     *
     * @param loggerclass The class for the logger
     * @throws ConfigurationException if a config-error occurs.
     */
    public AbstractFontUtil(final Class loggerclass)
            throws ConfigurationException {

        super();

        // logger
        logger = Logger.getLogger(loggerclass.getName());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LogFormatter());
        consoleHandler.setLevel(Level.WARNING);
        logger.addHandler(consoleHandler);

        localizer = LocalizerFactory.getLocalizer(loggerclass.getName());

        // configuration
        config = new ConfigurationFactory().newInstance("config/extex.xml");

        prop = new Properties();
        try {
            InputStream in = new FileInputStream(".extex");
            prop.load(in);
        } catch (Exception e) {
            prop.setProperty("extex.fonts", "src/font");
        }

        try {
            InputStream in = new FileInputStream(System
                    .getProperty("user.home")
                    + "/.extex");
            Properties prop2 = new Properties();
            prop2.load(in);
            prop.putAll(prop2);
        } catch (Exception e) {
            // do nothing
            e.printStackTrace();
        }

        finder = (new ResourceFinderFactory()).createResourceFinder(config
                .getConfiguration("Resource"), null, prop, null);

    }

    /**
     * Create a new object.
     *
     * @throws ConfigurationException if a config-error occurs.
     */
    public AbstractFontUtil() throws ConfigurationException {

        this(AbstractFontUtil.class);
    }

    /**
     * Returns the logger.
     *
     * @return Returns the logger.
     */
    public Logger getLogger() {

        return logger;
    }

    /**
     * Returns the localizer.
     *
     * @return Returns the localizer.
     */
    public Localizer getLocalizer() {

        return localizer;
    }

    /**
     * Returns the configuration.
     * @return Returns the configuration.
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

    /**
     * Returns the consoleHandler.
     * @return Returns the consoleHandler.
     */
    public Handler getConsoleHandler() {

        return consoleHandler;
    }

}
