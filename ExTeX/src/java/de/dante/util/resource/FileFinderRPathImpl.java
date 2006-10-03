/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.util.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import de.dante.util.StringListIterator;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This file finder search recursively in a directory.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class FileFinderRPathImpl
        implements
            ResourceFinder,
            LogEnabled,
            PropertyConfigurable {

    /**
     * The field <tt>logger</tt> contains the logger to be used for tracing.
     */
    private Logger logger = null;

    /**
     * The field <tt>trace</tt> contains the indicator that tracing is required.
     * This field is set to <code>true</code> according to the configuration.
     */
    private boolean trace = false;

    /**
     * The field <tt>config</tt> contains the configuration object on which this
     * file finder is based.
     */
    private Configuration config;

    /**
     * The field <tt>properties</tt> contains the properties.
     */
    private Properties properties = null;

    /**
     * Creates a new object.
     *
     * @param configuration the encapsulated configuration object
     */
    public FileFinderRPathImpl(final Configuration configuration) {

        super();
        config = configuration;
        String t = configuration.getAttribute("trace");
        if (t != null && Boolean.valueOf(t).booleanValue()) {
            trace = true;
        }
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        // config
        Configuration cfg = config.findConfiguration(type);
        if (cfg == null) {
            String t = config.getAttribute("default");
            if (t == null) {
                throw new ConfigurationMissingAttributeException("default",
                        config);
            }
            cfg = config.getConfiguration(t);

            if (trace && logger != null) {
                logger.fine("FileFinder: " + type + " not found; Using default"
                        + t + ".\n");
            }
        }

        if (trace && logger != null) {
            logger.fine("FileFinder: Searching " + name + " [" + type + "]\n");
        }

        Iterator iterator = cfg.iterator("path");
        while (iterator.hasNext()) {
            Configuration c = (Configuration) iterator.next();
            String prop = c.getAttribute("property");
            String path = null;
            if (prop != null) {
                path = properties.getProperty(prop);
            } else {
                path = c.getValue();
            }

            // only for directories
            File fpath = new File(path);
            if (!fpath.isDirectory()) {
                if (trace && logger != null) {
                    logger.fine("FileFinder: no path : " + fpath.toString()
                            + "\n");
                }
                return null;
            }

            InputStream in = findFile(fpath, name, cfg);
            if (in != null) {
                return in;
            }
        }
        return null;
    }

    /**
     * Find a file recursively.
     *
     * @param fpath path for searching
     * @param name  the file name
     * @param cfg   the configuration
     * @return Returns the input stream
     */
    private InputStream findFile(final File fpath, final String name,
            final Configuration cfg) {

        File file;
        StringListIterator extIt = cfg.getValues("extension").getIterator();
        while (extIt.hasNext()) {
            String ext = extIt.next();
            file = new File(fpath, name + (ext.equals("") ? "" : ".") + ext);
            if (trace && logger != null) {
                logger.fine("FileFinder: Try " + file + "\n");
            }
            if (file.canRead()) {
                try {
                    InputStream stream = new FileInputStream(file);
                    if (trace && logger != null) {
                        logger.fine("FileFinder: Found " + file.toString()
                                + "\n");
                    }
                    return stream;
                } catch (FileNotFoundException e) {
                    // ignore unreadable files
                    continue;
                }
            }
        }

        // call findFile recursively
        File[] files = fpath.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                InputStream in = findFile(files[i], name, cfg);
                // found ??
                if (in != null) {
                    return in;
                }
            }
        }
        // nothing found!
        return null;

    }

    /**
     * Getter for logger.
     *
     * @return the logger.
     */
    public Logger getLogger() {

        return logger;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTracing(boolean)
     */
    public void enableTracing(final boolean flag) {

        trace = flag;
    }

    /**
     * @see de.dante.util.resource.PropertyConfigurable#setProperties(java.util.Properties)
     */
    public void setProperties(final Properties prop) {

        properties = prop;
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(java.util.logging.Logger)
     */
    public void enableLogging(final Logger alogger) {

        logger = alogger;
    }
}