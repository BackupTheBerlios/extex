/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import de.dante.util.StringList;
import de.dante.util.StringListIterator;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationIOException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationMissingException;
import de.dante.util.configuration.ConfigurationWrapperException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This resource finder searches a file in a ls-R file as present in a texmf
 * tree. For this purpose the ls-R files found are read and stored internally.
 *
 * <h2>Configuration</h2>
 * ...
 *
 * <pre>
 * &lt;Finder class="de.dante.util.resource.LsrFinder"
 *          default="default"
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
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class LsrFinder implements ResourceFinder, LogEnabled, PropertiesTaker {

    /**
     * The field <tt>INITIAL_LIST_SIZE</tt> contains the initial size of the
     * list items in the cache.
     */
    private static final int INITIAL_LIST_SIZE = 2;

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
     * The field <tt>LSR_FILE_NAME</tt> contains the name of the ls-R file.
     */
    private static final String LSR_FILE_NAME = "ls-R";

    /**
     * The field <tt>TAG_PATH</tt> contains the name of the tag to identify
     * paths.
     */
    private static final String TAG_PATH = "path";

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
     * Creates a new object.
     *
     * @param configuration the encapsulated configuration object
     */
    public LsrFinder(final Configuration configuration) {

        super();
        this.config = configuration;
        String t = configuration.getAttribute("trace");
        trace = (t != null && Boolean.valueOf(t).booleanValue());
    }

    /**
     * Setter for the logger.
     *
     * @param alogger the new logger
     *
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger alogger) {

        logger = alogger;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTrace(boolean)
     */
    public void enableTrace(final boolean flag) {

        trace = flag;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        if (cache == null) {
            initialize();
        }

        Logger log = (trace ? logger : null);

        if (log != null) {
            log.fine("LsrFinder: Searching " + name + " [" + type + "]\n");
        }

        Configuration cfg = config.findConfiguration(type);
        if (cfg == null) {
            String t = config.getAttribute(ATTR_DEFAULT);
            if (t == null) {
                throw new ConfigurationMissingAttributeException(ATTR_DEFAULT,
                        config);
            }
            cfg = config.getConfiguration(t);

            if (log != null) {
                log.fine("LsrFinder: Type ``" + type
                        + "' not found; Using default `" + t + "'.\n");
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
                if (log != null) {
                    log.fine("LsrFinder: Trying " + file + "\n");
                }
                if (file != null && file.canRead()) {
                    try {
                        InputStream stream = new FileInputStream(file);
                        if (log != null) {
                            log.fine("LsrFinder: Found " + file.toString()
                                    + "\n");
                        }
                        return stream;
                    } catch (FileNotFoundException e) {
                        // ignore unreadable files
                        continue;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Load the external cache file into memory.
     *
     * @throws ConfigurationException in case of an error
     */
    private void initialize() throws ConfigurationException {

        Iterator it = config.iterator(TAG_PATH);
        if (!it.hasNext()) {
            throw new ConfigurationMissingException(TAG_PATH, config.toString());
        }

        cache = new HashMap();

        while (it.hasNext()) {
            Configuration cfg = (Configuration) it.next();
            String pathProperty = cfg.getAttribute(ATTR_PROPERTY);
            String name;
            if (pathProperty != null) {
                name = properties.getProperty(pathProperty);
                if (name == null) {
                    if (logger != null) {
                        logger.fine("LsrFinder: Property " + pathProperty
                                + " is undefined.\n");
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
     * @throws ConfigurationException if an error occured
     */
    private void load(final String path) throws ConfigurationException {

        long start = System.currentTimeMillis();
        File file = new File(path, LSR_FILE_NAME);
        String directory = "";
        File absoluteDirectory = new File(path);
        List list;

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            for (String line = in.readLine(); line != null; line = in
                    .readLine()) {
                int len = line.length();
                if (len == 0 || line.charAt(0) == '%') {
                    continue;
                }
                if (line.charAt(len - 1) == ':') {
                    directory = line.substring((line.startsWith("./") ? 2 : 0),
                            len - 1);
                    absoluteDirectory = new File(path, directory);
                } else {
                    list = (List) cache.get(line);
                    if (list == null) {
                        list = new ArrayList(INITIAL_LIST_SIZE);
                        cache.put(line, list);
                    }
                    list.add(new File(absoluteDirectory, line));
                }
            }
            in.close();

        } catch (FileNotFoundException e) {
            throw new ConfigurationWrapperException(e);
        } catch (IOException e) {
            throw new ConfigurationIOException(null, e);
        }

        if (trace && logger != null) {
            logger.fine("LsrFinder: Loaded cache file `" + file.toString()
                    + "' in " + (System.currentTimeMillis() - start)
                    + " ms, cache size=" + cache.size() + "\n");
        }
    }

    /**
     * Setter for the properties.
     *
     * @param prop the new properties
     *
     * @see de.dante.util.resource.PropertiesTaker#setProperties(
     *      java.util.Properties)
     */
    public void setProperties(final Properties prop) {

        properties = prop;
    }
}