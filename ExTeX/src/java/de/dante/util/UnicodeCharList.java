/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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
 */

package de.dante.util;

import java.util.ArrayList;

/**
 * This class provides a list of <code>UnicodeChar</code>s.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class UnicodeCharList {

    /**
     * The field <tt>list</tt> contains the list.
     */
    private ArrayList list;

    /**
     * Create a new object.
     * This list is initially empty.
     */
    public UnicodeCharList() {

        super();
        list = new ArrayList();
    }

    /**
     * Create a new object.
     * This list is initially empty.
     *
     * @param initsize initsize for the <code>ArrayList</code>
     */
    public UnicodeCharList(final int initsize) {

        super();
        list = new ArrayList(initsize);
    }

    /**
     * Add a new element.
     *
     * @param uc the <code>UnicodeChar</code> to add
     */
    public void add(final UnicodeChar uc) {

        list.add(uc);
    }

    /**
     * @see java.util.ArrayList#clear()
     */
    public void clear() {

        this.list.clear();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {

        if (!(obj instanceof UnicodeCharList)) {
            return false;
        }

        UnicodeCharList ucl = (UnicodeCharList) obj;
        int size = list.size();
        if (size != ucl.list.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!list.get(i).equals(ucl.list.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return the <code>UnicodeChar</code> at index.
     *
     * @param index the index
     *
     * @return  the <code>UnicodeChar</code> at index.
     */
    public UnicodeChar get(final int index) {

        return (UnicodeChar) list.get(index);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        int size = list.size();
        int hash = 0;

        for (int i = 0; i < size; i++) {
            hash += list.get(i).hashCode();
        }
        return hash;
    }

    /**
     * Return the size of the list.
     *
     * @return the size of the list
     */
    public int size() {

        return list.size();
    }

    /**
     * Return the <code>UnicodeCharList</code> as <code>String</code>.
     *
     * @return the string of the list
     */
    public String toString() {

        StringBuffer buf = new StringBuffer(list.size());
        for (int i = 0; i < list.size(); i++) {
            buf.append(get(i).toString());
        }
        return buf.toString();
    }

}
