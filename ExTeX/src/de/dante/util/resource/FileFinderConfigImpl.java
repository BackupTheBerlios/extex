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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.dante.util.StringListIterator;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FileFinderConfigImpl implements ResourceFinder {

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
     * The field <tt>config</tt> contains the configuration object on which this
     * file finder is based.
     */
    private Configuration config;

    /**
     * Creates a new object.
     *
     * @param configuration the encapsulated configuration object
     */
    public FileFinderConfigImpl(final Configuration configuration) {
        super();
        this.config = configuration;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        File file;

        Configuration cfg = config.getConfiguration(type);
        StringListIterator pathIt = cfg.getValues(PATH_TAG).getIterator();
        while (pathIt.hasNext()) {
            String path = pathIt.next();
            StringListIterator extIt = cfg.getValues(EXTENSION_TAG)
                    .getIterator();
            while (extIt.hasNext()) {
                String ext = extIt.next();
                file = new File(path, name + ext);
                if (file.canRead()) {
                    try {
                        InputStream stream = new FileInputStream(file);
                        return stream;
                    } catch (FileNotFoundException e) {
                        // ignore unreadable files
                    }
                }
            }
        }

        return null;
    }

}
