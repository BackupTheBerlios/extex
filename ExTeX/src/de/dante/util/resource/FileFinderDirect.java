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
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FileFinderDirect implements ResourceFinder {

    /**
     * The field <tt>extensionList</tt> contains the list of extensions to use.
     */
    private StringList extensionList;

    /**
     * Creates a new object.
     *
     * @param extensions the list of extensions to use
     */
    public FileFinderDirect(final StringList extensions) {

        super();
        extensionList = extensions;
    }

    /**
     * Setter for the extensions. The given string is splitted at the separator
     * <tt>:</tt>.
     * </p>
     *
     * @param extensions the extensions to set.
     */
    public void setExtension(final String extensions) {

        extensionList = new StringList(extensions, ":");
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        File file;

        StringListIterator extIt = extensionList.getIterator();
        while (extIt.hasNext()) {
            file = new File(name + extIt.next());
            if (file.canRead()) {
                try {
                    InputStream stream = new FileInputStream(file);
                    return stream;
                } catch (FileNotFoundException e) {
                    // ignore unreadable files
                }
            }
        }

        return null;
    }

}