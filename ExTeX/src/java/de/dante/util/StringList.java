/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class provides a {@link java.util.List List} of
 * {@link java.lang.String String}s. This class is meant to be used as a
 * type-save replacement for the generic classes implementing the
 * {@link java.util.List List} interface.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class StringList extends ArrayList implements List {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new object.
     * Initially the StringList is empty.
     */
    public StringList() {

        super();
    }

    /**
     * Creates a new object and initiates it with a single String value.
     *
     * @param s the initial String
     */
    public StringList(final String s) {

        super();
        add(s);
    }

    /**
     * Creates a new object and initiates it with some String value.
     *
     * @param s the initial String
     * @param sepRegex the seprator regular expression
     */
    public StringList(final String s, final String sepRegex) {

        super();
        String[] sl = s.split(sepRegex);
        for (int i = 0; i < sl.length; i++) {
            add(sl[i]);
        }
    }

    /**
     * Creates a new object and fills it with values from the given Collection.
     *
     * @param c a collection of initial elements
     */
    public StringList(final Collection c) {

        super(c);
    }

    /**
     * Return a {@link StringListIterator StringListIterator} to traverse the
     * elements of this instance.
     *
     * @return the iterator
     */
    public StringListIterator getIterator() {

        return new StringListIterator(this);
    }

    /**
     * Getter for the i<sup>th</sup> element of the object.
     *
     * @param i index of the element to retrieve
     *
     * @return the i<sup>th</sup> element or <code>null</code> if none is
     * found
     */
    public String getString(final int i) {

        return (String) super.get(i);
    }

    /**
     * Add a {@link String String} to the end of the list.
     *
     * @param s the string to add
     */
    public void add(final String s) {

        super.add(s);
    }
}