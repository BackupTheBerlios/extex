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
 * This file finder search recuriv in a directory.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class FileFinderRPathImpl implements ResourceFinder {

    /**
     * The field <tt>path</tt> contains the directory to consider.
     */
    private File path;

    /**
     * The field <tt>ext</tt> the list of extensions to consider.
     */
    private StringList extensionList;

    /**
     * Creates a new object.
     *
     * @param apath          directory to consider
     * @param extensions    list of extensions to consider
     */
    public FileFinderRPathImpl(final String apath, final StringList extensions) {

        super();
        path = new File(apath);
        extensionList = extensions;
    }

    /**
     * Setter for the extensions. The given string is splitted at the separator
     * <tt>:</tt>.
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

        return findFile(path, name, type);
    }

    /**
     * Find the File in a directory or call the method itself with a new directory.
     * 
     * @param fpath     the path to search
     * @param name      the name of the file
     * @param type      the extensuin of the file
     * @return  Returns the <code>InputStream</code> of the file or 
     *          <code>null</code>, if not found.
     */
    private InputStream findFile(final File fpath, final String name,
            final String type) {

        // only for diretories
        if (!fpath.isDirectory()) {
            return null;
        }

        // find file in the directory
        File file;

        StringListIterator extIt = extensionList.getIterator();
        while (extIt.hasNext()) {
            String ext = extIt.next();
            file = new File(fpath, name + (ext.equals("") ? "" : ".") + ext);
            if (file.canRead()) {
                try {
                    InputStream stream = new FileInputStream(file);
                    return stream;
                } catch (FileNotFoundException e) {
                    // ignore unreadable files
                    continue;
                }
            }
        }

        // call findFile recursiv
        File[] files = fpath.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                InputStream in = findFile(files[i], name, type);
                // found ??
                if (in != null) {
                    return in;
                }
            }
        }
        // nothing found!
        return null;
    }
}