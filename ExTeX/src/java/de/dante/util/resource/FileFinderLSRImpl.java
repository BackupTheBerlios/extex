/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.util.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import de.dante.util.StringListIterator;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationIOException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;

/**
 * This file finder search a file in a ls-R file.
 * <p>
 * All ls-R-mapfiles are stored in one map with the path as key.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class FileFinderLSRImpl
        implements
            ResourceFinder,
            LoggerTaker,
            PropertiesTaker {

    /**
     * use obj-hashmap in file
     */
    private static final boolean USEOBJ = false;

    /**
     * The constant <tt>EXTENSION_TAG</tt> contains the name of the tag to get
     * the possible extensions.
     */
    private static final String EXTENSION_TAG = "extension";

    /**
     * filename for the ls-R file
     */
    private static final String LSR = "ls-R";

    /**
     * Map for the ls-R entries
     */
    private Map lsr = null;

    /**
     * Map for all ls-R map-entries over all class instances
     */
    private static Map alllsr = new HashMap();

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
     * The field <tt>properties</tt> contains the ...
     */
    private Properties properties = null;

    /**
     * Creates a new object.
     *
     * @param configuration the encapsulated configuration object
     */
    public FileFinderLSRImpl(final Configuration configuration) {

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

        if (trace && logger != null) {
            logger.fine("FileFinder: Searching " + name + " [" + type + "]\n");
        }

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

        Configuration cfgpath = config.findConfiguration("path");
        if (cfgpath == null) {
            throw new ConfigurationMissingAttributeException("path", cfgpath);

        }

        String path = null;
        String pathproperty = cfgpath.getAttribute("property");
        if (pathproperty != null) {
            path = System.getProperty(pathproperty);
        } else {
            path = cfgpath.getValue();
        }

        lsr = (Map) alllsr.get(path);

        if (lsr == null) {
            File lsrobj = new File("ls-R.obj");
            // TODO incomplete ....

            if (USEOBJ && lsrobj.canRead()) {
                //long start = System.currentTimeMillis();
                try {
                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(lsrobj));

                    lsr = (HashMap) ois.readObject();
                    ois.close();

                } catch (Exception e) {
                    throw new ConfigurationIOException("error", e);
                }
                //System.out.println("\nload ls-R.obj "
                //        + (System.currentTimeMillis() - start) + " ms");
            } else {
                //long start = System.currentTimeMillis();
                loadLSR(path);
                //System.out.println("\nload ls-R "
                //        + (System.currentTimeMillis() - start) + " ms   size=" + lsr.size());
                if (USEOBJ) {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(
                                new FileOutputStream(lsrobj));

                        oos.writeObject(lsr);
                        oos.close();

                    } catch (Exception e) {
                        throw new ConfigurationIOException("error", e);
                    }
                }
            }
            alllsr.put(path, lsr);
        }

        File file;
        StringListIterator extIt = cfg.getValues(EXTENSION_TAG).getIterator();
        while (extIt.hasNext()) {
            String ext = extIt.next();
            file = (File) lsr.get(name + (ext.equals("") ? "" : ".") + ext);
            if (trace && logger != null) {
                logger.fine("FileFinder: Try " + file + "\n");
            }
            if (file != null && file.canRead()) {
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
        return null;
    }

    /**
     * Load the ls-R file
     *
     * @param path  the path for the ls-R file
     * @throws ConfigurationException if an error occured
     */
    private void loadLSR(final String path) throws ConfigurationException {

        lsr = new HashMap();

        try {

            BufferedReader in = new BufferedReader(new FileReader(new File(
                    path, LSR)));

            String line;
            String rpath = "";
            while ((line = in.readLine()) != null) {
                if (line.startsWith("%") || line.trim().equals("")) {
                    continue;
                }
                line = line.trim();
                if (line.endsWith(":")) {
                    // directory
                    rpath = line.substring(0, line.length() - 1);
                    if (rpath.startsWith("./")) {
                        rpath = rpath.substring(2);
                    }
                } else {
                    // file
                    File abspath = new File(path, rpath);
                    File file = new File(abspath, line);
                    // only readable files!
                    if (!file.isDirectory() /* && file.canRead()*/) {
                        lsr.put(line, file);
                    }
                }
            }
            in.close();

        } catch (FileNotFoundException e) {
            throw new ConfigurationIOException("FileNotFound", e);
        } catch (IOException e) {
            throw new ConfigurationIOException("IO", e);
        }
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
     * @see de.dante.util.resource.LoggerTaker#setLogger(java.util.logging.Logger)
     */
    public void setLogger(final Logger alogger) {

        logger = alogger;
    }

    /**
     * @see de.dante.util.resource.PropertiesTaker#setProperties(java.util.Properties)
     */
    public void setProperties(final Properties prop) {

        properties = prop;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTrace(boolean)
     */
    public void enableTrace(final boolean flag) {

        trace = flag;
    }
}