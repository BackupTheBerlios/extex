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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides a means to combine several file finders to be queried
 * as one.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ResourceFinderList implements ResourceFinder {

    /**
     * The field <tt>list</tt> the internal list of file finders which are
     * elements in this container.
     */
    private List list = new ArrayList();

    /**
     * Creates a new object.
     * Initially the list is empty.
     */
    public ResourceFinderList() {

        super();
    }

    /**
     * Creates a new object.
     * Initially the list is empty.
     *
     * @param configuration the configuration is ignored in this class
     */
    public ResourceFinderList(final Configuration configuration) {

        super();
    }

    /**
     * Creates a new object.
     * Initially the list contans the one file finder given as argument.
     *
     * @param finder the file finder to store in initially
     */
    public ResourceFinderList(final ResourceFinder finder) {

        super();
        add(finder);
    }

    /**
     * Creates a new object.
     * Initially the list contans the two file finders given as argument.
     *
     * @param finder1 the first file finder to store in initially
     * @param finder2 the second file finder to store in initially
     */
    public ResourceFinderList(final ResourceFinder finder1,
            final ResourceFinder finder2) {

        super();
        add(finder1);
        add(finder2);
    }

    /**
     * Creates a new object.
     * Initially the list contans the three file finders given as argument.
     *
     * @param finder1 the first file finder to store in initially
     * @param finder2 the second file finder to store in initially
     * @param finder3 the third file finder to store in initially
     */
    public ResourceFinderList(final ResourceFinder finder1,
            final ResourceFinder finder2, final ResourceFinder finder3) {

        super();
        add(finder1);
        add(finder2);
        add(finder3);
    }

    /**
     * Append an additional file finder to list of file finders contained.
     *
     * @param finder the file finder to add
     */
    public void add(final ResourceFinder finder) {

        list.add(finder);
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTrace(boolean)
     */
    public void enableTrace(final boolean flag) {

        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            ((ResourceFinder) iterator.next()).enableTrace(flag);
        }
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            InputStream stream = ((ResourceFinder) iterator.next())
                    .findResource(name, type);
            if (stream != null) {
                return stream;
            }
        }

        return null;
    }

}