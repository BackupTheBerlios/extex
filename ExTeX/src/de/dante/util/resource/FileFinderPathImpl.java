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

import de.dante.util.StringList;
import de.dante.util.StringListIterator;
import de.dante.util.configuration.ConfigurationException;

/**
 * This file finder searches for the file in different directories and with
 * several extensions.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FileFinderPathImpl implements ResourceFinder {

    /**
     * The field <tt>path</tt> contains the list of directories to consider.
     */
    private StringList pathList;

    /**
     * The field <tt>ext</tt> the list of extensions to consider.
     */
    private StringList extensionList;

    /**
     * Creates a new object.
     *
     * @param path list of directories to consider
     * @param extensions list of extensions to consider
     */
    public FileFinderPathImpl(final StringList path, final StringList extensions) {

        super();
        this.pathList = path;
        this.extensionList = extensions;
    }

    /**
     * Setter for the extensions. The given string is splitted at the separator
     * <tt>:</tt>.
     * </p>
     *
     * @param extensions the extensions to set.
     */
    public void setExtension(final String extensions) {

        this.extensionList = new StringList(extensions, ":");
    }

    /**
     * Setter for path. The given string is splitted at the separator stored in
     * the system property <tt>path.separator</tt>. This is usually the
     * value <tt>:</tt> on Unix systems and <tt>;</tt> on Windows.
     * <p>
     * If this property can not be found then the value <tt>:</tt> is used.
     * </p>
     *
     * @param path the path to set.
     */
    public void setPath(final String path) {

        this.pathList = new StringList(path, System
                .getProperty("path.separator", ":"));
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        File file;

        StringListIterator pathIt = pathList.getIterator();
        while (pathIt.hasNext()) {
            String path = pathIt.next();
            StringListIterator extIt = extensionList.getIterator();
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