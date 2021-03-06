/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a means to combine several resource finders to be queried
 * as one.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class ResourceFinderList implements ResourceFinder, RecursiveFinder {

    /**
     * The field <tt>list</tt> the internal list of file finders which are
     * elements in this container.
     */
    private List list = new ArrayList();

    /**
     * The field <tt>parent</tt> contains the parent resource finder.
     */
    private ResourceFinder parent = null;

    /**
     * Creates a new object.
     * Initially the list is empty.
     */
    public ResourceFinderList() {

        super();
        this.parent = this;
    }

    /**
     * Append an additional file finder to list of file finders contained.
     *
     * @param finder the file finder to add
     */
    public void add(final ResourceFinder finder) {

        list.add(finder);

        if (finder instanceof RecursiveFinder) {
            ((RecursiveFinder) finder).setParent(parent);
        }
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTracing(boolean)
     */
    public void enableTracing(final boolean flag) {

        for (int i = 0; i < list.size(); i++) {
            ((ResourceFinder) list.get(i)).enableTracing(flag);
        }
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        for (int i = 0; i < list.size(); i++) {
            InputStream stream = ((ResourceFinder) list.get(i)).findResource(
                    name, type);
            if (stream != null) {
                return stream;
            }

        }

        return null;
    }

    /**
     * Setter for the parent resource finder.
     *
     * @param theParent the parent finder for recursive invocation
     *
     * @see de.dante.util.resource.RecursiveFinder#setParent(
     *      de.dante.util.resource.ResourceFinder)
     */
    public void setParent(final ResourceFinder theParent) {

        for (int i = 0; i < list.size(); i++) {
            ResourceFinder finder = (ResourceFinder) list.get(i);
            if (finder instanceof RecursiveFinder) {
                ((RecursiveFinder) finder).setParent(theParent);
            }
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("(resource");
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            sb.append(' ');
            ResourceFinder finder = (ResourceFinder) iterator.next();
            sb.append(finder.toString());
        }
        sb.append(')');
        return sb.toString();
    }

}
