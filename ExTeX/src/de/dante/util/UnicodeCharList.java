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
 */

package de.dante.util;

import java.util.ArrayList;

/**
 * This class implements a <code>ArrayList</code> for <code>UnicodeChar</code>s.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class UnicodeCharList {

    /**
     * the internal <code>ArrayList</code>
     */
    private ArrayList list;

    /**
     * Create a new object.
     */
    public UnicodeCharList() {

        super();
        list = new ArrayList();
    }

    /**
     * Create a new object.
     * @param   initsize    initsize for the <code>ArrayList</code>
     */
    public UnicodeCharList(final int initsize) {

        super();
        list = new ArrayList(initsize);
    }

    /**
     * Add a new Element.
     * @param uc    the <code>UnicodeChar</code> to add
     */
    public void add(final UnicodeChar uc) {

        list.add(uc);
    }

    /**
     * Return the <code>UnicodeChar</code> at index idx.
     * @param idx   the index
     * @return  the <code>UnicodeChar</code> at index idx.
     */
    public UnicodeChar get(final int idx) {

        return (UnicodeChar) list.get(idx);
    }

    /**
     * Return the size of the list.
     * @return  the size of the list
     */
    public int size() {

        return list.size();
    }

    /**
     * Return the <code>UnicodeCharList</code> as <code>String</code>.
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
