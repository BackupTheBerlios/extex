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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.dante.util.configuration.ConfigurationException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class FileFinderList implements FileFinder {

    /**
     * The field <tt>list</tt> ...
     */
    private List list = new ArrayList();

    /**
     * Creates a new object.
     * Initially the list is empty.
     */
    public FileFinderList() {
        super();
    }

    /**
     * Creates a new object.
     * Initially the list contans the one file finder given as argument.
     *
     * @param finder ...
     */
    public FileFinderList(final FileFinder finder) {
        super();
        add(finder);
    }

    /**
     * Creates a new object.
     * Initially the list contans the two file finders given as argument.
     *
     * @param finder1 ...
     * @param finder2 ...
     */
    public FileFinderList(final FileFinder finder1, final FileFinder finder2) {
        super();
        add(finder1);
        add(finder2);
    }

    /**
     * Creates a new object.
     * Initially the list contans the three file finders given as argument.
     *
     * @param finder1 ...
     * @param finder2 ...
     * @param finder3 ...
     */
    public FileFinderList(final FileFinder finder1, final FileFinder finder2,
            final FileFinder finder3) {
        super();
        add(finder1);
        add(finder2);
        add(finder3);
    }

    /**
     * ...
     *
     * @param finder ...
     */
    public void add(final FileFinder finder) {
        list.add(finder);
    }

    /**
     * @see de.dante.util.file.FileFinder#findFile(java.lang.String,
     *      java.lang.String)
     */
    public File findFile(final String name, final String type)
            throws ConfigurationException {

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            File f = ((FileFinder) iter.next()).findFile(name, type);
            if (f != null) {
                return f;
            }
        }

        return null;
    }

}
