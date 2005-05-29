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
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import de.dante.util.StringList;
import de.dante.util.StringListIterator;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This file finder searches for the file in different directories and with
 * several extensions.
 *
 * <h2>Configuration</h2>
 * The file finder can be configured to influence its actions.
 * The following example shows a configuration for a file finder:
 *
 * <pre>
 * &lt;Finder class="de.dante.util.resource.FileFinder"
 *         trace="false"
 *         default="default"&gt;
 *   &lt;tex&gt;
 *     &lt;path property="extex.texinputs"/&gt;
 *     &lt;path property="texinputs"/&gt;
 *     &lt;path&gt;.&lt;/path&gt;
 *     &lt;extension&gt;&lt;/extension&gt;
 *     &lt;extension&gt;.tex&lt;/extension&gt;
 *   &lt;/tex&gt;
 *   &lt;fmt&gt;
 *     &lt;path property="extex.texinputs"/&gt;
 *     &lt;path property="texinputs"/&gt;
 *     &lt;path&gt;.&lt;/path&gt;
 *     &lt;extension&gt;&lt;/extension&gt;
 *     &lt;extension&gt;.fmt&lt;/extension&gt;
 *   &lt;/fmt&gt;
 *   &lt;default&gt;
 *     &lt;path property="extex.texinputs"/&gt;
 *     &lt;path property="texinputs"/&gt;
 *     &lt;path&gt;.&lt;/path&gt;
 *     &lt;extension&gt;&lt;/extension&gt;
 *   &lt;/default&gt;
 * &lt;/Finder&gt;
 * </pre>
 *
 * <p>
 *  Whenever a resource is sought its type is used to find the appropriate
 *  parameters for the search. If the sub-configuration with the name of the
 *  type exists then this subconfiguration is used. For instance if the
 *  resource <tt>tex</tt> with the type <tt>fmt</tt> is sought then the
 *  sub-configuration <tt>fmt</tt> determines how to find this file.
 * </p>
 * <p>
 *  If no sub-configuration of the given type is present then the attribute
 *  <tt>default</tt> is used to find the default sub-configuration. In the
 *  example given above this default configuration is called <tt>default</tt>.
 *  Nevertheless it would also be possible to point the default configuration
 *  to another existing configuration. The attribute <tt>default</tt> is
 *  mandatory.
 * </p>
 * <p>
 *  Each sub-configuration takes the tags <tt>path</tt> and <tt>extension</tt>
 *  in arbitrary number.
 *  <tt>path</tt> contains the path prepended before the resource name.
 *  <tt>extension</tt> contains the extension appended after the resource name.
 * </p>
 * <p>
 *  <tt>path</tt> can carry the attribute <tt>property</tt>. In this case the
 *  value is ignored and the value is taken from the property named in the
 *  attribute. Otherwise the value of the tag is taken as path. The value taken
 *  from the property can contain several paths. They are separated by the
 *  separator specified for the platform. For instance on windows the separator
 *  <tt>;</tt> is used and on Unix the seprator <tt>:</tt> is used.
 * </p>
 * <p>
 *  All combinations of path, resource name and extension are tried in turn.
 *  If one combination leads to a readable file then an input stream to this
 *  file is used.
 * </p>
 * <p>
 *  The attribute <tt>trace</tt> can be used to force a tracing of the actions
 *  in the log file. The tracing is performed only if a logger is present when
 *  needed. The tracing flag can be overwritten at run-time.
 *  The attribute <tt>trace</tt> is optional.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class FileFinder
        implements
            ResourceFinder,
            LogEnabled,
            PropertyConfigurable {

    /**
     * The constant <tt>EXTENSION_TAG</tt> contains the name of the tag to get
     * the possible extensions.
     */
    private static final String EXTENSION_TAG = "extension";

    /**
     * The constant <tt>PATH_TAG</tt> contains the name of the tag to get the
     * path information.
     */
    private static final String PATH_TAG = "path";

    /**
     * The field <tt>bundle</tt> contains the resource bundle for messages.
     */
    private transient ResourceBundle bundle = null;

    /**
     * The field <tt>config</tt> contains the configuration object on which this
     * file finder is based.
     */
    private Configuration config;

    /**
     * The field <tt>logger</tt> contains the logger to be used for tracing.
     */
    private Logger logger = null;

    /**
     * The field <tt>properties</tt> contains the properties instance to use.
     */
    private Properties properties = null;

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
    public FileFinder(final Configuration configuration) {

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
     * Try to find a file by adding extensions.
     *
     * @param name the name of the file to find
     * @param path the path of the file to find
     * @param cfg the configuration
     *
     * @return the input stream for the file or <code>null</code> if none was
     *  found.
     */
    private InputStream find(final String name, final String path,
            final Configuration cfg) {

        InputStream stream;
        StringListIterator extIt = cfg.getValues(EXTENSION_TAG).getIterator();
        boolean verbose = (trace && logger != null);

        while (extIt.hasNext()) {
            String ext = extIt.next();
            File file = new File(path, name + ext);

            if (verbose) {
                trace("Try", file.toString(), null);
            }
            if (file.canRead()) {
                try {
                    stream = new FileInputStream(file);
                    if (verbose) {
                        trace("Found", file.toString(), null);
                    }
                    return stream;
                } catch (FileNotFoundException e) {
                    // Ignore unreadable files.
                    // This should not happen since it has already been
                    // tested before.
                    trace("NotFound", file.toString(), null);
                }
            }
        }
        return null;
    }

    /**
     * Try to find a file on some paths by adding extensions.
     *
     * @param name the name of the file to find
     * @param paths a list of paths to explore
     * @param cfg the configuration
     *
     * @return the input stream for the file or <code>null</code> if none was
     *  found.
     */
    private InputStream find(final String name, final StringList paths,
            final Configuration cfg) {

        InputStream stream = null;
        StringListIterator iterator = paths.getIterator();

        while (stream == null && iterator.hasNext()) {
            String p = iterator.next();
            stream = find(name, p, cfg);
        }

        return stream;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        boolean verbose = (trace && logger != null);
        if (verbose) {
            trace("Searching", name, type);
        }

        InputStream stream = null;
        Configuration cfg = config.findConfiguration(type);
        if (cfg == null) {
            String t = config.getAttribute("default");
            if (t == null) {
                throw new ConfigurationMissingAttributeException("default",
                        config);
            }
            cfg = config.getConfiguration(t);

            if (verbose) {
                trace("ConfigurationNotFound", type, t);
            }
        }

        Iterator iterator = cfg.iterator(PATH_TAG);
        while (stream == null && iterator.hasNext()) {
            Configuration c = (Configuration) iterator.next();
            String prop = c.getAttribute("property");
            if (prop != null) {
                String path = properties.getProperty(prop, null);
                if (path != null) {
                    stream = find(name, new StringList(path, System
                            .getProperty("path.separator", ":")), cfg);
                } else if (verbose) {
                    trace("UndefinedProperty", prop, null);
                }
            } else {
                stream = find(name, c.getValue(), cfg);
            }
        }

        if (stream == null && verbose) {
            trace("Failed", name, null);
        }

        return stream;
    }

    /**
     * Getter for configuration.
     *
     * @return the configuratio.
     */
    protected Configuration getConfiguration() {

        return this.config;
    }

    /**
     * Getter for logger.
     *
     * @return the logger.
     */
    public Logger getLogger() {

        return this.logger;
    }

    /**
     * @see de.dante.util.resource.PropertyConfigurable#setProperties(
     *      java.util.Properties)
     */
    public void setProperties(final Properties properties) {

        this.properties = properties;
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
            bundle = ResourceBundle.getBundle(FileFinder.class.getName());
        }

        logger.fine(MessageFormat.format(bundle.getString(key), //
                new Object[]{arg, arg2}));
    }
}