/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
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

package de.dante.util.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import de.dante.extex.documentWriter.OutputStreamFactory;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationMissingException;

/**
 * This factory creates an output stream from a specification in the
 * configuration.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class OutputFactory implements OutputStreamFactory {
    /**
     * The constant <tt>ENCODING_ATTRIBUTE</tt> contains the name of the
     * attribute to get the encoding from.
     */
    private static final String ENCODING_ATTRIBUTE = "encoding";

    /**
     * The constant <tt>PATH_TAG</tt> contains the name of the tag to specify
     * the path.
     */
    private static final String PATH_TAG = "path";

    /**
     * The field <tt>config</tt> contains the configuration for this factory.
     */
    private Configuration config;

    /**
     * The field <tt>outputDirectories</tt> contaoins the list of output
     * directories. The list is tried first to last.
     */
    private String[] outputDirectories;

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
            final String[] outdirs)
            throws ConfigurationMissingException,
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
     * Create an output stream to a given file.
     * The creation is tried in a number of directories. The first succeeding
     * addtempt is returned.
     *
     * @param name the name of the file to open
     * @param type the type of the file
     *
     * @return a stream for the output or <code>null</code> if none could be
     * opened.
     *
     * @throws FileNotFoundException in case that the file could not be opened.
     * @throws ConfigurationException in case of a problem with the
     *  configuration
     */
    public OutputStream createOutputStream(final String name, final String type)
            throws FileNotFoundException,
                ConfigurationException {

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
     * @see de.dante.extex.documentWriter.OutputStreamFactory#getOutputStream()
     */
    public OutputStream getOutputStream() {

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * Open a writer to a given output file with the default encoding.
     * @param file the file to open
     *
     *
     * @return a writer for the output file
     */
    private OutputStream openFile(final File file) {

        OutputStream os = null;

        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            return null; // ignored on purpose
        }

        return os;
    }

}