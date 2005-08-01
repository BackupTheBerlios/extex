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

package de.dante.util.resource;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import de.dante.util.StringListIterator;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ClasspathFinder implements LogEnabled, ResourceFinder {

    /**
     * The field <tt>bundle</tt> contains the resource bundle for messages.
     */
    private transient ResourceBundle bundle = null;

    /**
     * The field <tt>config</tt> contains the configuration object on which this
     * resource finder is based.
     */
    private Configuration config;

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
     * Creates a new object.
     *
     * @param configuration the encapsulated configuration object
     */
    public ClasspathFinder(final Configuration configuration) {

        super();
        this.config = configuration;
        String t = configuration.getAttribute("trace");
        if (t != null && Boolean.valueOf(t).booleanValue()) {
            trace = true;
        }
    }

    /**
     * Setter for the logger.
     *
     * @param theLogger the logger to set.
     *
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTracing(boolean)
     */
    public void enableTracing(final boolean flag) {

        trace = flag;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(
     *      java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        ClassLoader classLoader = this.getClass().getClassLoader();

        Configuration cfg = config.findConfiguration(type);
        if (cfg == null) {
            String t = config.getAttribute("default");
            if (t == null) {
                throw new ConfigurationMissingAttributeException("default",
                        config);
            }
            cfg = config.getConfiguration(t);
            if (cfg == null) {
                return null;
            }
        }

        StringListIterator extIt = cfg.getValues("extension").getIterator();

        while (extIt.hasNext()) {
            String ext = extIt.next();
            String fullName = name + ext;
            InputStream stream = classLoader.getResourceAsStream(fullName);

            if (stream != null) {
                if (trace) {
                    trace("Found", fullName, null);
                }
                return stream;
            }
            trace("NotFound", fullName, null);
        }
        return null;
    }

    /**
     * Produce an internationalized trace message.
     *
     * @param key the resource key for the message format
     * @param arg the first argument to insert
     * @param arg2 the second argument to insert
     */
    private void trace(final String key, final String arg, final String arg2) {

        if (bundle == null) {
            bundle = ResourceBundle.getBundle(ClasspathFinder.class.getName());
        }

        logger.fine(MessageFormat.format(bundle.getString(key), //
                new Object[]{arg, arg2}));
    }
}
