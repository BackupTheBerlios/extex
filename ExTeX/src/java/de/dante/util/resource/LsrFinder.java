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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import de.dante.util.StringList;
import de.dante.util.StringListIterator;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationIOException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingException;
import de.dante.util.framework.configuration.exception.ConfigurationWrapperException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This resource finder searches a file in a <tt>ls-R</tt> file database as
 * present in a texmf tree. For this purpose the <tt>ls-R</tt> file databases
 * found are read and stored internally.
 *
 * <h2>Configuration</h2>
 * The lsr finder can be configured to influence its actions.
 * The following example shows a configuration for a lsr finder:
 *
 * <pre>
 * &lt;Finder class="de.dante.util.resource.LsrFinder"
 *          default="default"
 *          capacity="1234567"
 *          trace="false"&gt;
 *   &lt;path property="extex.font.path"&gt;&lt;/path&gt;
 *   &lt;path property="texmf.path"&gt;&lt;/path&gt;
 *
 *   &lt;tfm&gt;&lt;extension&gt;.tfm&lt;/extension&gt;&lt;/tfm&gt;
 *   &lt;efm&gt;&lt;extension&gt;.efm&lt;/extension&gt;&lt;/efm&gt;
 *   &lt;pfb&gt;&lt;extension&gt;.pfb&lt;/extension&gt;&lt;/pfb&gt;
 *   &lt;ttf&gt;&lt;extension&gt;.ttf&lt;/extension&gt;&lt;/ttf&gt;
 *   &lt;default&gt;&lt;extension/&gt;&lt;/default&gt;
 * &lt;/Finder&gt;
 * </pre>
 *
 * <p>
 *  Whenever a resource is sought the first step is to ensure that the file
 *  databases are read in. For this purpose the <tt>path</tt> tag is used.
 *  The <tt>path</tt> tags name directories which may contain file databases.
 *  The file databases have a fixed name <tt>ls-R</tt>.
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
 *  To find a resource its type is used to find the appropriate
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
 *  Each sub-configuration takes the <tt>extension</tt> in arbitrary number.
 *  <tt>extension</tt> contains the extension appended after the resource name.
 * </p>
 * <p>
 *  All combinations of resource name and extension are tried in turn to be
 *  found in the file database.
 *  If one combination leads to a readable file then an input stream to this
 *  file is used.
 * </p>
 * <p>
 *  The attribute <tt>trace</tt> can be used to force a tracing of the actions
 *  in the log file. The tracing is performed only if a logger is present when
 *  needed. The tracing flag can be overwritten at run-time.
 *  The attribute <tt>trace</tt> is optional.
 * </p>
 * <p>
 *  The attribute <tt>capacity</tt> can be used to configure the initial
 *  capacity of the internal cache for the fle database. If this number is less
 *  than one than an internal default is used. This value should be larger than
 *  the number of files expected for best performance.
 *  The attribute <tt>capacity</tt> is optional.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class LsrFinder
        implements
            ResourceFinder,
            LogEnabled,
            PropertyConfigurable {

    /**
     * The field <tt>ATTR_DEFAULT</tt> contains the attribute name for the
     * default type.
     */
    private static final String ATTR_DEFAULT = "default";

    /**
     * The field <tt>ATTR_PROPERTY</tt> contains the attribute name for the
     * property access.
     */
    private static final String ATTR_PROPERTY = "property";

    /**
     * The constant <tt>EXTENSION_TAG</tt> contains the name of the tag to get
     * the possible extensions.
     */
    private static final String EXTENSION_TAG = "extension";

    /**
     * The field <tt>INITIAL_LIST_SIZE</tt> contains the initial size of the
     * list items in the cache.
     */
    private static final int INITIAL_LIST_SIZE = 2;

    /**
     * The field <tt>LSR_FILE_NAME</tt> contains the name of the ls-R file.
     */
    private static final String LSR_FILE_NAME = "ls-R";

    /**
     * The field <tt>TAG_PATH</tt> contains the name of the tag to identify
     * paths.
     */
    private static final String TAG_PATH = "path";

    /**
     * The field <tt>bundle</tt> contains the resource bundle for messages.
     */
    private ResourceBundle bundle = null;

    /**
     * The field <tt>cache</tt> contains the map for the ls-R entries.
     */
    private Map cache = null;

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
     * The field <tt>properties</tt> contains the properties provided for this
     * finder.
     */
    private Properties properties = System.getProperties();

    /**
     * The field <tt>trace</tt> contains the indicator that tracing is required.
     * This field is set to <code>true</code> according to the configuration.
     */
    private boolean trace = false;

    /**
     * The field <tt>initialCapacity</tt> contains the initial capacity of the
     * cache. If the value is less than 1 then the default of the unerling
     * implementation is used.
     */
    private int initialCapacity = -1;

    /**
     * Creates a new object.
     *
     * @param configuration the encapsulated configuration object
     */
    public LsrFinder(final Configuration configuration) {

        super();
        this.config = configuration;
        String a = configuration.getAttribute("trace");
        this.trace = (a != null && Boolean.valueOf(a).booleanValue());

        a = configuration.getAttribute("capacity");
        if (a != null && !"".equals(a)) {
            try {
                this.initialCapacity = Integer.parseInt(a);
            } catch (NumberFormatException e) {
                this.initialCapacity = -1;
            }
        }
    }

    /**
     * Setter for the logger.
     *
     * @param logger the new logger
     *
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger logger) {

        this.logger = logger;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTracing(boolean)
     */
    public void enableTracing(final boolean flag) {

        this.trace = flag;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        boolean verbose = (trace && logger != null);

        if (verbose) {
            trace("Searching", name, type, null);
        }

        if (cache == null) {
            initialize();
        }

        Configuration cfg = config.findConfiguration(type);
        if (cfg == null) {
            String t = config.getAttribute(ATTR_DEFAULT);
            if (t == null) {
                throw new ConfigurationMissingAttributeException(ATTR_DEFAULT,
                        config);
            }
            cfg = config.getConfiguration(t);
            if (cfg == null) {
                return null;
            }

            if (verbose) {
                trace("ConfigurationNotFound", type, t, null);
            }
        }

        StringListIterator it = cfg.getValues(EXTENSION_TAG).getIterator();

        while (it.hasNext()) {
            List l = (List) cache.get(name + it.next());
            if (l == null) {
                continue;
            }
            for (int i = 0; i < l.size(); i++) {
                File file = (File) l.get(i);
                if (verbose) {
                    trace("Try", file.toString(), null, null);
                }
                if (file != null && file.canRead()) {
                    try {
                        InputStream stream = new FileInputStream(file);
                        if (verbose) {
                            trace("Found", file.toString(), null, null);
                        }
                        return stream;
                    } catch (FileNotFoundException e) {
                        // ignore unreadable files
                        if (verbose) {
                            trace("FoundUnreadable", file.toString(), null,
                                    null);
                        }
                        continue;
                    }
                }
            }
        }
        if (verbose) {
            trace("Failed", name, null, null);
        }
        return null;
    }

    /**
     * Load the external cache file into memory.
     *
     * @throws ConfigurationException in case of an error
     */
    private void initialize() throws ConfigurationException {

        boolean verbose = (trace && logger != null);
        Iterator it = config.iterator(TAG_PATH);
        if (!it.hasNext()) {
            throw new ConfigurationMissingException(TAG_PATH, config.toString());
        }

        cache = (initialCapacity > 0
                ? new HashMap(initialCapacity)
                : new HashMap());

        while (it.hasNext()) {
            Configuration cfg = (Configuration) it.next();
            String pathProperty = cfg.getAttribute(ATTR_PROPERTY);
            String name;
            if (pathProperty != null) {
                name = properties.getProperty(pathProperty);
                if (name == null) {
                    if (verbose) {
                        trace("UndefinedProperty", pathProperty, null, null);
                    }
                } else {
                    StringListIterator sit = new StringList(name, System
                            .getProperty("path.separator", ":")).getIterator();
                    while (sit.hasNext()) {
                        String file = sit.next();
                        load(file);
                    }
                }
            } else {
                name = cfg.getValue();
                if (name != null && !name.equals("")) {
                    load(name);
                }
            }
        }
    }

    /**
     * Load the ls-R file.
     *
     * @param path  the path for the ls-R file
     *
     * @throws ConfigurationException if an error occurred
     */
    private void load(final String path) throws ConfigurationException {

        long start = System.currentTimeMillis();
        File file = new File(path, LSR_FILE_NAME);
        if (!file.canRead()) {
            if (logger != null) {
                trace("UnreadableLsr", file.toString(), null, null);
            }
            return;
        }

        File directory = new File(path);
        List list;

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            int len;

            for (String line = in.readLine(); line != null; line = in
                    .readLine()) {
                len = line.length();
                if (len == 0 || line.charAt(0) == '%') {
                    continue;
                } else if (line.charAt(len - 1) == ':') {
                    directory = new File(path, //
                            line.substring((line.startsWith("./") ? 2 : 0),
                                    len - 1));
                } else {
                    list = (List) cache.get(line);
                    if (list == null) {
                        list = new ArrayList(INITIAL_LIST_SIZE);
                        cache.put(line, list);
                    }
                    list.add(new File(directory, line));
                }
            }
            in.close();

        } catch (FileNotFoundException e) {
            throw new ConfigurationWrapperException(e);
        } catch (IOException e) {
            throw new ConfigurationIOException(null, e);
        }

        if (trace && logger != null) {
            trace("DatabaseLoaded", file.toString(), //
                    Long.toString(System.currentTimeMillis() - start), //
                    Integer.toString(cache.size()));
        }
    }

    /**
     * Setter for the properties.
     *
     * @param prop the new properties
     *
     * @see de.dante.util.resource.PropertyConfigurable#setProperties(
     *      java.util.Properties)
     */
    public void setProperties(final Properties prop) {

        properties = prop;
    }

    /**
     * Produce an internationalized trace message.
     *
     * @param key the resource key for the message format
     * @param arg the first argument to insert
     * @param arg2 the second argument to insert
     * @param arg3 the third argument to insert
     */
    private void trace(final String key, final String arg, final String arg2,
            final String arg3) {

        if (bundle == null) {
            bundle = ResourceBundle.getBundle(LsrFinder.class.getName());
        }

        logger.fine(MessageFormat.format(bundle.getString(key), //
                new Object[]{arg, arg2, arg3}));
    }
}