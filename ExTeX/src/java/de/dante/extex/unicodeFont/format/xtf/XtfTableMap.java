/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.xtf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Map for a TTF/OTF table.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class XtfTableMap {

    /**
     * Create a new object.
     */
    public XtfTableMap() {

        super();
        data = new HashMap();
    }

    /**
     * map
     */
    private Map data;

    /**
     * @see java.util.Map#size()
     */
    public int size() {

        return data.size();
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear() {

        data.clear();
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {

        return data.isEmpty();
    }

    /**
     * @param key   the key as int
     * @return Returns, if the map has the special key
     */
    public boolean containsKey(final int key) {

        return data.containsKey(new Integer(key));
    }

    /**
     * @param key   the key as int
     * @return Returns the value for the key
     */
    public XtfTable get(final int key) {

        return (XtfTable) data.get(new Integer(key));
    }

    /**
     * @param key   the key as int
     * @return Returns the removed element
     */
    public XtfTable remove(final int key) {

        return (XtfTable) data.remove(new Integer(key));
    }

    /**
     * @param key   the key as int
     * @param val   the value for the key
     */
    public void put(final int key, final XtfTable val) {

        data.put(new Integer(key), val);
    }

    /**
     * Returns the keys in an array
     * @return Returns the keys in an array
     */
    public int[] getKeys() {

        Set set = data.keySet();
        Integer[] i = new Integer[set.size()];
        i = (Integer[]) set.toArray(i);
        int[] keys = new int[i.length];
        for (int k = 0; k < i.length; k++) {
            keys[k] = i[k].intValue();
        }

        return keys;
    }

    /**
     * Returns a TTFTable array from the map.
     * @return Returns a TTFTable array from the map.
     */
    public XtfTable[] getTables() {

        XtfTable[] tab = new XtfTable[data.size()];
        int[] keys = getKeys();
        for (int i = 0; i < keys.length; i++) {
            tab[i] = get(keys[i]);
        }
        return tab;
    }
}