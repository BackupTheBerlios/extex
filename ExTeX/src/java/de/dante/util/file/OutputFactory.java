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
package de.dante.util.file;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationMissingException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class OutputFactory {

    /**
     * The constant <tt>PATH_TAG</tt> ...
     */
    private static final String PATH_TAG = "path";

    /**
     * The constant <tt>ENCODING_ATTRIBUTE</tt> ...
     */
    private static final String ENCODING_ATTRIBUTE = "encoding";

    /**
     * The field <tt>outputDirectories</tt> contaoins the list of output
     * directories. te list is tried first to last.
     */
    private String[] outputDirectories;

    /**
     * The field <tt>config</tt> contains the configuration for this factory.
     */
    private Configuration config;

    /**
     * Creates a new object.
     *
     * @param configuration the configuration object for this instance
     * @param outdirs the list of output directories
     *
     * @throws ConfigurationMissingException in case that the configuration
     * argument is <code>null</code>
     * @throws ConfigurationMissingAttributeException in case that the
     * attribute for the default encoding is missing
     */
    public OutputFactory(final Configuration configuration,
            final String[] outdirs) throws ConfigurationMissingException,
            ConfigurationMissingAttributeException {

        super();
        outputDirectories = outdirs;
        config = configuration;
        if (config == null) {
            throw new ConfigurationMissingException("OutputFactory");
        }
        if (config.getAttribute(ENCODING_ATTRIBUTE) == null) {
            throw new ConfigurationMissingAttributeException(
                ENCODING_ATTRIBUTE, config);
        }
    }

    /**
     * ...
     *
     * @param name ...
     * @param type ...
     *
     * @return ...
     *
     * @throws FileNotFoundException in case that the output file coud not be
     *             opened
     */
    public Writer createWriter(final String name, final String type)
            throws FileNotFoundException, ConfigurationException {

        return createWriter(name, type, config.getAttribute(ENCODING_ATTRIBUTE));
    }

    /**
     * ...
     *
     * @param name ...
     * @param type ...
     * @param encoding the name of the encoding to use. This overrules the
     *      default encoding in the configuration. A value of <code>null</code>
     *      signals that no encoding is requested.
     * @return ...
     *
     * @throws FileNotFoundException ...
     * @throws ConfigurationException ...
     */
    public Writer createWriter(final String name, final String type,
        final String encoding) throws FileNotFoundException,
        ConfigurationException {

        String filename = name + "." + type;
        String dir;

        if (outputDirectories != null) {
            for (int i = 0; i < outputDirectories.length; i++) {
                dir = outputDirectories[i];
                if (dir != null) {
                    Writer os = openFile(new File(dir, filename), encoding);
                    if (os != null) {
                        return os;
                    }
                }
            }
        }

        Configuration cfg = config.getConfiguration(type);
        Iterator iter = cfg.iterator(PATH_TAG);
        while (iter.hasNext()) {
            dir = (String) (iter.next());
            if (dir != null) {
                Writer os = openFile(new File(dir, filename), encoding);
                if (os != null) {
                    return os;
                }
            }
        }

        throw new FileNotFoundException(name);
    }

    /**
     * ...
     *
     * @param name
     * @param type
     *
     * @return ...
     *
     * @throws FileNotFoundException ...
     * @throws ConfigurationException ...
     */
    public OutputStream createOutputStream(final String name, final String type)
            throws FileNotFoundException, ConfigurationException {

        String filename = name + "." + type;
        String dir;

        if (outputDirectories != null) {
            for (int i = 0; i < outputDirectories.length; i++) {
                dir = outputDirectories[i];
                if (dir != null) {
                    OutputStream os = openFile(new File(dir, filename));
                    if (os != null) {
                        return os;
                    }
                }
            }
        }

        Configuration cfg = config.getConfiguration(type);
        Iterator iter = cfg.iterator(PATH_TAG);
        while (iter.hasNext()) {
            dir = (String) (iter.next());
            if (dir != null) {
                OutputStream os = openFile(new File(dir, filename));
                if (os != null) {
                    return os;
                }
            }
        }

        throw new FileNotFoundException(name);
    }

    /**
     * ...
     *
     * @param file ...
     * @param enc the name of the encoding to use
     *
     * @return ...
     */
    private Writer openFile(final File file, final String enc) {

        Writer os = null;

        try {
            if (enc != null) {
                os = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), enc));
            } else {
                os = new BufferedWriter(new FileWriter(file));
            }
        } catch (IOException e) {
            // ignored on purpose
        }

        return os;
    }

    /**
     * ...
     *
     * @param file ...
     *
     * @return ...
     */
    private OutputStream openFile(final File file) {

        OutputStream os = null;

        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            // ignored on purpose
        }

        return os;
    }

}
